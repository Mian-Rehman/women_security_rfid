package com.rehman.womansecuritysystem.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AdminImageUploadActivity extends AppCompatActivity {


    ProgressDialog progressDialog;
    EditText ed_driverUsername;
    TextView text_verify,driverName_text;
    ImageView driver_image,back_image;
    Button btn_save,btn_setCard;
    LinearLayout rfid_layout,driverDetails_layout;
    EditText ed_rfidCard;
    String driverUsername,name,imageLink,driverAccountCreationKey,rfidCardNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_image_upload);

        initViews();

        driverDetails_layout.setVisibility(View.GONE);
        rfid_layout.setVisibility(View.GONE);
        ed_rfidCard.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        text_verify.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(AdminImageUploadActivity.this, "Please wait", "Processing", true);
            driverUsername = ed_driverUsername.getText().toString().trim();
            if (driverUsername.isEmpty())
            {
                ed_driverUsername.setError("username required");
                ed_driverUsername.requestFocus();
                progressDialog.dismiss();
                return;
            }else
            {
                checkDriverDetails(driverUsername);
            }
        });

        btn_setCard.setOnClickListener(v -> {
            btn_save.setVisibility(View.VISIBLE);
            rfid_layout.setVisibility(View.VISIBLE);
            ed_rfidCard.setVisibility(View.VISIBLE);

        });

        btn_save.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(AdminImageUploadActivity.this, "Please wait", "Processing", true);
            rfidCardNumber  = ed_rfidCard.getText().toString().trim();
            if (rfidCardNumber.isEmpty() && rfidCardNumber.length() != 10)
            {
                ed_rfidCard.setError("valid card number required");
                ed_rfidCard.requestFocus();
                progressDialog.dismiss();
                return;
            }else
            {
                saveDataToDatabase();
            }
        });


    }

    private void saveDataToDatabase()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RFIDDetails");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cardKey  = reference.push().getKey();
                Map<String,String> map = new HashMap<>();
                map.put("cardKey",cardKey);
                map.put("driverAccountCreationKey",driverAccountCreationKey);
                map.put("driverName",name);
                map.put("driverImage",imageLink);
                map.put("rfidNumber",rfidCardNumber);

                assert cardKey != null;
                reference.child(driverAccountCreationKey).setValue(map);
                progressDialog.dismiss();
                Toast.makeText(AdminImageUploadActivity.this, "RFID set Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkDriverDetails(String driverUsername)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver")
                .child(driverUsername);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    driverDetails_layout.setVisibility(View.VISIBLE);
                    btn_setCard.setVisibility(View.VISIBLE);
                    rfid_layout.setVisibility(View.VISIBLE);
                    name  = snapshot.child("name").getValue(String.class);
                    imageLink  = snapshot.child("profileImage").getValue(String.class);
                    driverAccountCreationKey  = snapshot.child("accountCreationKey").getValue(String.class);

                    Glide.with(AdminImageUploadActivity.this).load(imageLink).into(driver_image);
                    driverName_text.setText(name);
                    progressDialog.dismiss();

                }else

                {
                    driverDetails_layout.setVisibility(View.GONE);
                    btn_setCard.setVisibility(View.GONE);
                    rfid_layout.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    Toast.makeText(AdminImageUploadActivity.this, "Driver Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews()
    {
        ed_driverUsername = findViewById(R.id.ed_driverUsername);
        text_verify = findViewById(R.id.text_verify);
        driver_image = findViewById(R.id.driver_image);
        driverName_text = findViewById(R.id.driverName_text);
        btn_save = findViewById(R.id.btn_save);
        btn_setCard = findViewById(R.id.btn_setCard);
        rfid_layout = findViewById(R.id.rfid_layout);
        driverDetails_layout = findViewById(R.id.driverDetails_layout);
        ed_rfidCard = findViewById(R.id.ed_rfidCard);
        back_image = findViewById(R.id.back_image);
    }

}
