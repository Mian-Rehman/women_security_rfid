package com.rehman.womansecuritysystem.ProfileProgress;

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
import android.widget.TextView;
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
import com.rehman.womansecuritysystem.Driver.SetPRofileImgActivity;
import com.rehman.womansecuritysystem.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class IdentityVerificationActivity extends AppCompatActivity {

    ImageView back_image,idFront_image,idBack_image;
    Button btn_front,btn_back;
    FirebaseStorage storage;
    StorageReference storageReference;
    String username,accountType,iDFrontImageLink,iDBackImageLink;
    Uri frontUri,backUri;
    Bitmap frontBitmap,backBitmap;
    TextView upload_text;
    FirebaseDatabase database;
    DatabaseReference reference;
    private static int FRONT_IMAGE_REQUEST_CODE = 100;
    private static int BACK_IMAGE_REQUEST_CODE = 101;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verification);

        progressDialog = ProgressDialog.show(this, "Please wait", "Processing", true);
        SharedPreferences sp=getSharedPreferences("CURRENT",MODE_PRIVATE);
        username = sp.getString("userName","");
        accountType = sp.getString("accountType","");

        initViews();
        checkImageExists();



        btn_back.setVisibility(View.GONE);
        idBack_image.setVisibility(View.GONE);
        idFront_image.setVisibility(View.GONE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(username);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(accountType).child(username);

        back_image.setOnClickListener(v -> {
            onBackPressed();

        });



        btn_front.setOnClickListener(v -> {
            btn_back.setVisibility(View.VISIBLE);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), FRONT_IMAGE_REQUEST_CODE);

        });

        btn_back.setOnClickListener(v -> {

            if (frontUri!=null)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), BACK_IMAGE_REQUEST_CODE);
            }else
            {
                Toast.makeText(this, "Upload Front Side", Toast.LENGTH_SHORT).show();
            }


        });

        upload_text.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(this, "Please wait", "Processing", true);
                saveBothImageToFirebase();
        });

    }

    private void checkImageExists()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(accountType)
                .child(username).child("identityCardVerification");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    btn_back.setVisibility(View.VISIBLE);
                    idFront_image.setVisibility(View.VISIBLE);
                    idBack_image.setVisibility(View.VISIBLE);

                    iDFrontImageLink = snapshot.child("idFrontSide").getValue(String.class);
                    iDBackImageLink = snapshot.child("idBackSide").getValue(String.class);
                    Glide.with(IdentityVerificationActivity.this)
                            .load(snapshot.child("idFrontSide").getValue().toString()).into(idFront_image);

                    Glide.with(IdentityVerificationActivity.this)
                            .load(iDBackImageLink).into(idBack_image);
                    progressDialog.dismiss();
//                    Toast.makeText(IdentityVerificationActivity.this, "exits", Toast.LENGTH_SHORT).show();
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

    private void saveBothImageToFirebase()
    {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("updated profile");
        StorageReference storage = storageReference.child("IDCARD");
        storage.putFile(frontUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        iDFrontImageLink  = uri.toString();
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(accountType).child(username).child("profileImage");
//                        ref.setValue(uri.toString());
                        storeSecondImage();
                        Log.d("pnnn", "onSuccess: "+uri.toString());

                    }
                });

                dialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float perecent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                dialog.setMessage("Uploaded :"+(int)perecent + "%");
            }
        });
    }

    private void storeSecondImage() {
        StorageReference storage = storageReference.child("IDCARDBACK");
        storage.putFile(backUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        iDBackImageLink  = uri.toString();
//                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(accountType).child(username).child("profileImage");
//                        ref.setValue(uri.toString());
                       saveToRealTime();
                        Log.d("pnnn", "onSuccess: "+uri.toString());

                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float perecent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();

            }
        });
    }

    private void saveToRealTime()
    {
        Map<String, String> map = new HashMap<>();
        map.put("idFrontSide",iDFrontImageLink);
        map.put("idBackSide",iDBackImageLink);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("identityCardVerification").setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        saveVerifying();
    }

    private void saveVerifying()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("checkVerification").child("identityCardVerification").setValue("complete");
                reference.child("checkVerification").child("accountVerification").setValue("underReview");
                progressDialog.dismiss();
                Toast.makeText(IdentityVerificationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        btn_front = findViewById(R.id.btn_front);
        btn_back = findViewById(R.id.btn_back);
        idFront_image = findViewById(R.id.idFront_image);
        idBack_image = findViewById(R.id.idBack_image);
        upload_text = findViewById(R.id.upload_text);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==FRONT_IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
            frontUri = data.getData();
            try {
                idFront_image.setVisibility(View.VISIBLE);
                InputStream inputStream = getContentResolver().openInputStream(frontUri);
                frontBitmap = BitmapFactory.decodeStream(inputStream);
                idFront_image.setImageBitmap(frontBitmap);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if(requestCode==BACK_IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null) {
            backUri = data.getData();
            try {
                idBack_image.setVisibility(View.VISIBLE);
                InputStream inputStream = getContentResolver().openInputStream(backUri);
                backBitmap = BitmapFactory.decodeStream(inputStream);
                idBack_image.setImageBitmap(backBitmap);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }
}