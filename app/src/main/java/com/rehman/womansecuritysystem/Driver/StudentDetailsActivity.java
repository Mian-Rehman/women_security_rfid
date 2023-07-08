package com.rehman.womansecuritysystem.Driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rehman.womansecuritysystem.R;

public class StudentDetailsActivity extends AppCompatActivity {

    ImageView back_image,phone_image;
    TextView ed_fullName,ed_childName,ed_className,ed_instituteName,ed_instituteAddress,ed_phoneDetail
            ,ed_address;

    String accountType,username,fullName,childName,className,instituteName,instituteAddress,
            phoneNumber,address,studentGetDriver,addKey,userUsername,userAccountType,parentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        initviews();
        checkUserPermission();
        getIntentValues();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        phone_image.setOnClickListener(v -> {
            phoneCall(phoneNumber);
        });

    }

    private void phoneCall(String phoneNumber)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ phoneNumber));//change the number
        startActivity(callIntent);
    }

    private void getIntentValues()
    {
        Intent intent = getIntent();
        fullName = intent.getStringExtra("fullName");
        childName = intent.getStringExtra("childName");
        className = intent.getStringExtra("className");
        instituteName = intent.getStringExtra("instituteName");
        instituteAddress = intent.getStringExtra("instituteAddress");
        phoneNumber = intent.getStringExtra("phoneNumber");
        address = intent.getStringExtra("address");
        addKey = intent.getStringExtra("addKey");
        userUsername = intent.getStringExtra("userUsername");
        userAccountType = intent.getStringExtra("userAccountType");
        studentGetDriver = intent.getStringExtra("studentGetDriver");
        parentUsername = intent.getStringExtra("parentUsername");

        ed_fullName.setText(fullName);
        ed_childName.setText(childName);
        ed_className.setText(className);
        ed_instituteName.setText(instituteName);
        ed_instituteAddress.setText(instituteAddress);
        ed_phoneDetail.setText(phoneNumber);
        ed_address.setText(address);

    }

    private void initviews()
    {
        back_image = findViewById(R.id.back_image);
        ed_fullName = findViewById(R.id.ed_fullName);
        ed_childName = findViewById(R.id.ed_childName);
        ed_className = findViewById(R.id.ed_className);
        ed_instituteName = findViewById(R.id.ed_instituteName);
        ed_instituteAddress = findViewById(R.id.ed_instituteAddress);
        ed_phoneDetail = findViewById(R.id.ed_phoneDetail);
        ed_address = findViewById(R.id.ed_address);
        phone_image = findViewById(R.id.phone_image);
    }

    private void checkUserPermission()
    {
        if (ContextCompat.checkSelfPermission(StudentDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StudentDetailsActivity.this, new String[]{
                    Manifest.permission.CALL_PHONE


            }, 100);
        }
    }
}