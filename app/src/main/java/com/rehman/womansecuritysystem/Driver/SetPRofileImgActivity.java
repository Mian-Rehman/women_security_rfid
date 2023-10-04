package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rehman.womansecuritysystem.R;

import java.io.IOException;
import java.io.InputStream;

public class SetPRofileImgActivity extends AppCompatActivity {

    ImageView back_image,profilePic_Image;
    private static int IMAGE_REQUEST_CODE = 100;
    Bitmap bitmap;
    Uri uri;
    String image_link;
    FirebaseStorage storage;
    StorageReference storageReference;

    Button btn_update;
    String imageID;
    String username,accountType;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_img);

        initView();
        progressDialog = ProgressDialog.show(SetPRofileImgActivity.this, "Please wait", "Processing", true);

        SharedPreferences sp=getSharedPreferences("CURRENT",MODE_PRIVATE);
        username = sp.getString("userName","");
        accountType = sp.getString("accountType","");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(username);

        checkProfileExits();

        btn_update.setVisibility(View.INVISIBLE);

        back_image.setOnClickListener(v -> {
            Intent intent =  new Intent(SetPRofileImgActivity.this,DriverMainActivity.class);
            startActivity(intent);

        });

        profilePic_Image.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE);
            btn_update.setVisibility(View.VISIBLE);

        });

        btn_update.setOnClickListener(v -> {
            saveToDatabaseStorage();
        });
    }

    private void checkProfileExits()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profileImage").exists())
                {
                    Glide.with(SetPRofileImgActivity.this).load(snapshot.child("profileImage").getValue().toString())
                            .into(profilePic_Image);
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
            uri = data.getData();
            try {

                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                byte[] bytes = stream.toByteArray();
//                image_link = Base64.encodeToString(bytes,Base64.DEFAULT);
                profilePic_Image.setImageBitmap(bitmap);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void saveToDatabaseStorage()
    {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("updated profile");
        StorageReference storage = storageReference.child("profileImage");
        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(accountType).child(username).child("profileImage");
                        ref.setValue(uri.toString());
                        Log.d("pnnn", "onSuccess: "+uri.toString());

                    }
                });

                dialog.dismiss();
                Toast.makeText(SetPRofileImgActivity.this, "Updated", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float perecent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded :"+(int)perecent + "%");
            }
        });
    }


    private void initView()
    {
        back_image = findViewById(R.id.back_image);
        profilePic_Image = findViewById(R.id.profilePic_Image);
        btn_update = findViewById(R.id.btn_update);
    }
}