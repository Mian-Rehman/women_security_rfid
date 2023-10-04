package com.rehman.womansecuritysystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.Driver.AdminRequestActivity;
import com.rehman.womansecuritysystem.R;

public class AdminRequestDetailsActivitty extends AppCompatActivity {

    ImageView back_image;
    TextView title_text,tv_fullName,tv_email,tv_phone,tv_username;
    LinearLayout userDetails_layout,personalDetails_layout,vehicleDetails_layout,idcard_layout;
    String frontSide;
    String backSide,response,message;

    TextView fullNameDetail_ed,emailDetail_ed,phoneDetail_ed,fatherName_ed,address_ed
            ,ed_vehicleOwnerName,ed_vehicleNumber,ed_driverName,ed_vehicleDetails,tv_vehicleType,link1;

    String driver_email,driver_fullName,driver_phoneNumber,driver_fatherName,driver_address,
            vehicleOwnerName,vehicleNumber,driverName,vehicleType,vehicleDetails
            ,name,email,phoneNumber,password,accountType,accountCreationKey,userName;
    ProgressDialog progressDialog;
    ImageView idFront_image,idBack_image;

    TextView underReview_text,accept_text,reject_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_details_activitty);

        initViews();
        progressDialog = ProgressDialog.show(this, "", "Please wait", true);


        link1.setVisibility(View.GONE);

        link1.setOnClickListener(v -> {
            Intent webintent=new Intent(Intent.ACTION_VIEW);
            webintent.setData(Uri.parse(frontSide));
            if (webintent.resolveActivity(getPackageManager())!=null)
            {
                startActivity(webintent);
            }
        });

        underReview_text.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(this, "", "Processing", true);

            underReview_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
            underReview_text.setTextColor(ContextCompat.getColor(this,R.color.white));

            accept_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            accept_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            accept_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            reject_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            reject_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            reject_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            response  = "underReview";
            message = "Dear " + driver_fullName + "\n"
                    + "Your Driver Account is back to under Review due to some violation. Your documents is being review with in 6 to 7 business days"
                    + "\n" +  "Team"  +"\n" + "Smart and Secure gate pass";
            sendSMS(message);
            saveResponseToDataBase(response);
        });

        accept_text.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(this, "", "Processing", true);

            underReview_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            underReview_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            underReview_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            accept_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
            accept_text.setTextColor(ContextCompat.getColor(this,R.color.white));

            reject_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            reject_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            reject_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            response  = "accept";
            message = "Dear " + driver_fullName + "\n"
                    + "Your Driver Account is approved. Now you can stated to pick and drop their school children."
                    + "\n" +  "Team"  +"\n" + "Smart and Secure gate pass";
            sendSMS(message);
            saveResponseToDataBase(response);
        });

        reject_text.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(this, "", "Processing", true);

            underReview_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            underReview_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            underReview_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            accept_text.setBackground(ContextCompat.getDrawable(this, R.drawable.border_layout));
            accept_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            accept_text.setTextColor(ContextCompat.getColor(this,R.color.black));

            reject_text.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
            reject_text.setTextColor(ContextCompat.getColor(this,R.color.white));

            response  = "reject";
            message = "Dear " + driver_fullName + "\n"
                    + "Your Driver Account is Rejected due to some fake details. PLease provide your correct details to verify your account"
                    + "\n" +  "Team"  +"\n" + "Smart and Secure gate pass";
            sendSMS(message);
            saveResponseToDataBase(response);

        });



        personalDetails_layout.setVisibility(View.GONE);
        vehicleDetails_layout.setVisibility(View.GONE);
        idcard_layout.setVisibility(View.GONE);

        back_image.setOnClickListener(v -> {
           startActivity(new Intent(AdminRequestDetailsActivitty.this, AdminRequestActivity.class));
           finish();
        });

        getIntentValue();

    }

    private void sendSMS(String message)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
            {
                sendSMS();
            }else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
            }

        }
    }
    public void sendSMS()
    {

        phoneNumber = "+92" + phoneNumber;

        try {
            SmsManager smgr=SmsManager.getDefault();
            smgr.sendTextMessage(phoneNumber,null,message,null,null);
//            Toast.makeText(AdminRequestDetailsActivitty.this, " SMS send Successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
//            Toast.makeText(AdminRequestDetailsActivitty.this, "SMS Failed to send", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveResponseToDataBase(String response)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver")
                .child(userName).child("checkVerification");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    reference.child("accountVerification").setValue(response);
                    progressDialog.dismiss();
                    Toast.makeText(AdminRequestDetailsActivitty.this, "Request Updated", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getIntentValue()
    {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userName = intent.getStringExtra("userName");
        phoneNumber =  intent.getStringExtra("phoneNumber");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        accountCreationKey = intent.getStringExtra("accountCreationKey");
        accountType = intent.getStringExtra("accountType");

        tv_fullName.setText(name);
        tv_email.setText(email);
        tv_phone.setText(phoneNumber);
        tv_username.setText(userName);

        title_text.setText(accountType + " Account Details");
        if (accountType.equals("Driver"))
        {
            idcard_layout.setVisibility(View.VISIBLE);
            getDriverDetails(accountType,userName);
            getVehicleDetails(accountType,userName);
            getIDImages(accountType,userName);
        }else
        {
            progressDialog.dismiss();
            personalDetails_layout.setVisibility(View.GONE);
            vehicleDetails_layout.setVisibility(View.GONE);
            idcard_layout.setVisibility(View.GONE);
        }
    }

    private void getIDImages(String accountType, String userName)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(accountType)
                .child(userName).child("identityCardVerification");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    link1.setVisibility(View.GONE);

                    frontSide = snapshot.child("idFrontSide").getValue(String.class);
                    backSide = snapshot.child("idBackSide").getValue(String.class);

//                    Toast.makeText(AdminRequestDetailsActivitty.this, frontSide, Toast.LENGTH_SHORT).show();

                    idFront_image.setVisibility(View.VISIBLE);
                    idBack_image.setVisibility(View.VISIBLE);
                    Glide.with(AdminRequestDetailsActivitty.this).load(frontSide).into(idFront_image);
                    Glide.with(AdminRequestDetailsActivitty.this).load(backSide).into(idBack_image);
                }
                else
                {
                    Toast.makeText(AdminRequestDetailsActivitty.this, "ID Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getVehicleDetails(String accountType, String userName)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(accountType)
                .child(userName).child("vehicleDetails");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    vehicleDetails_layout.setVisibility(View.VISIBLE);
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
                    vehicleDetails_layout.setVisibility(View.GONE);
                    Toast.makeText(AdminRequestDetailsActivitty.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDriverDetails(String accountType, String userName)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(accountType)
                .child(userName).child("personalDetails");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    personalDetails_layout.setVisibility(View.VISIBLE);

                    driver_fullName = snapshot.child("userFullName").getValue(String.class);
                    driver_fatherName = snapshot.child("fatherName").getValue(String.class);
                    driver_address = snapshot.child("userAddress").getValue(String.class);
                    driver_email = snapshot.child("userEmail").getValue(String.class);
                    driver_phoneNumber = snapshot.child("userPhoneNumber").getValue(String.class);

                    fullNameDetail_ed.setText(driver_fullName);
                    fatherName_ed.setText(driver_fatherName);
                    address_ed.setText(driver_address);
                    emailDetail_ed.setText(driver_email);
                    phoneDetail_ed.setText(driver_phoneNumber);
                    progressDialog.dismiss();
                }else{
                    personalDetails_layout.setVisibility(View.GONE);
                    Toast.makeText(AdminRequestDetailsActivitty.this, "Data Not Found", Toast.LENGTH_SHORT).show();
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
        back_image = findViewById(R.id.back_image);
        title_text = findViewById(R.id.title_text);
        tv_phone = findViewById(R.id.tv_phone);
        userDetails_layout = findViewById(R.id.userDetails_layout);
        tv_fullName = findViewById(R.id.tv_fullName);
        tv_email = findViewById(R.id.tv_email);
        tv_username = findViewById(R.id.tv_username);

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
        personalDetails_layout = findViewById(R.id.personalDetails_layout);
        vehicleDetails_layout = findViewById(R.id.vehicleDetails_layout);
        idcard_layout = findViewById(R.id.idcard_layout);
        idFront_image = findViewById(R.id.idFront_image);
        idBack_image = findViewById(R.id.idBack_image);
        underReview_text = findViewById(R.id.underReview_text);
        accept_text = findViewById(R.id.accept_text);
        reject_text = findViewById(R.id.reject_text);
        link1 = findViewById(R.id.link1);
    }
}