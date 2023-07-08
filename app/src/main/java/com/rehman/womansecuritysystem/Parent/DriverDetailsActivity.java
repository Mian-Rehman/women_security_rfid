package com.rehman.womansecuritysystem.Parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.R;

public class DriverDetailsActivity extends AppCompatActivity {

    TextView fullNameDetail_ed,emailDetail_ed,phoneDetail_ed,fatherName_ed,address_ed
     ,ed_vehicleOwnerName,ed_vehicleNumber,ed_driverName,ed_vehicleDetails,tv_vehicleType;
    String driverUsername;
    String email,fullName,phoneNumber,fatherName,address,
            vehicleOwnerName,vehicleNumber,driverName,vehicleType,vehicleDetails;
    ImageView back_image;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        progressDialog = ProgressDialog.show(this, "", "Please wait", true);
        initViews();

        Intent intent = getIntent();
        driverUsername = intent.getStringExtra("driverUsername");
        getDriverDetails(driverUsername);
        getVehicleDetails(driverUsername);

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getVehicleDetails(String driverUsername)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver")
                .child(driverUsername).child("vehicleDetails");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    driverName = snapshot.child("driverName").getValue(String.class);
                    vehicleDetails = snapshot.child("vehicleDetails").getValue(String.class);
                    vehicleNumber = snapshot.child("vehicleNumber").getValue(String.class);
                    vehicleOwnerName = snapshot.child("vehicleOwnerName").getValue(String.class);
                    vehicleType = snapshot.child("vehicleType").getValue(String.class);

                   ed_driverName.setText(driverName);
                   ed_vehicleDetails.setText(vehicleDetails);
                   ed_vehicleNumber.setText(vehicleNumber);
                   ed_vehicleOwnerName.setText(vehicleOwnerName);
                   tv_vehicleType.setText(vehicleType);
                }else{
                    Toast.makeText(DriverDetailsActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDriverDetails(String driverUsername)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver")
                .child(driverUsername).child("personalDetails");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    fullName = snapshot.child("userFullName").getValue(String.class);
                    fatherName = snapshot.child("fatherName").getValue(String.class);
                    address = snapshot.child("userAddress").getValue(String.class);
                    email = snapshot.child("userEmail").getValue(String.class);
                    phoneNumber = snapshot.child("userPhoneNumber").getValue(String.class);

                    fullNameDetail_ed.setText(fullName);
                    fatherName_ed.setText(fatherName);
                    address_ed.setText(address);
                    emailDetail_ed.setText(email);
                    phoneDetail_ed.setText(phoneNumber);
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(DriverDetailsActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initViews()
    {
        fullNameDetail_ed = findViewById(R.id.fullNameDetail_ed);
        emailDetail_ed = findViewById(R.id.emailDetail_ed);
        phoneDetail_ed = findViewById(R.id.phoneDetail_ed);
        fatherName_ed = findViewById(R.id.fatherName_ed);
        address_ed = findViewById(R.id.address_ed);

        ed_vehicleOwnerName = findViewById(R.id.ed_vehicleOwnerName);
        ed_vehicleNumber = findViewById(R.id.ed_vehicleNumber);
        ed_driverName = findViewById(R.id.ed_driverName);
        tv_vehicleType = findViewById(R.id.tv_vehicleType);
        ed_vehicleDetails = findViewById(R.id.ed_vehicleDetails);
        back_image = findViewById(R.id.back_image);
    }
}