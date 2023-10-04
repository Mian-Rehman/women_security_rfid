package com.rehman.womansecuritysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.Driver.SetPRofileImgActivity;
import com.rehman.womansecuritysystem.ProfileProgress.IdentityVerificationActivity;
import com.rehman.womansecuritysystem.ProfileProgress.PersonalDetailActivity;
import com.rehman.womansecuritysystem.ProfileProgress.VehicleDetailsActivity;

public class AccountVerificationActivity extends AppCompatActivity {

    CardView personalDetails_card, shopDetails_card, profilePic_card,shopImages_card;

    ImageView personalDetails_image, shopDetails_image, profilePic_Image,shopImages_image,back_image;
    ProgressDialog progressDialog;
    DatabaseReference reference;
    Button submitInformation_button;
    String username,accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        initView();
        progressDialog = ProgressDialog.show(AccountVerificationActivity.this, "Please wait", "Processing", true);

        reference = FirebaseDatabase.getInstance().getReference("accountVerifications").child(username);

        checkProfileExits();
        checkVerification();
        submitInformation_button.setVisibility(View.GONE);

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        submitInformation_button.setOnClickListener(v -> {
            saveVerificationDocuments();
        });

        personalDetails_card.setOnClickListener(v -> {
            startActivity(new Intent(AccountVerificationActivity.this, PersonalDetailActivity.class));
        });

        shopDetails_card.setOnClickListener(v -> {
            startActivity(new Intent(AccountVerificationActivity.this, VehicleDetailsActivity.class));
        });
//
        profilePic_card.setOnClickListener(v -> {
            startActivity(new Intent(AccountVerificationActivity.this, SetPRofileImgActivity.class));
        });
//
        shopImages_card.setOnClickListener(v -> {
            startActivity(new Intent(AccountVerificationActivity.this, IdentityVerificationActivity.class));
        });
    }

    private void saveVerificationDocuments()
    {
        SharedPreferences preferences=getSharedPreferences("PREFERENCE3",MODE_PRIVATE);
        String FirstTime= preferences.getString("FirstTimeMail_15","");

        if (FirstTime.equals("Yes"))
        {
            //data Save more time
//            SharedPreferences.Editor editor=preferences.edit();
//            editor.putString("FirstTimeInstall_14","Yes");
//            editor.apply();
//            saveUserProfileData();
//            progressDialog2.dismiss();
        }
        else
        {
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("FirstTimeMail_15","Yes");
            editor.apply();

        }

        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        reference.child("checkVerification").child("verificationComplete").setValue("complete");

        Toast.makeText(this, "Thanks for submitting documents", Toast.LENGTH_SHORT).show();
    }



    private void initView() {
        personalDetails_card = findViewById(R.id.personalDetails_card);
        personalDetails_image = findViewById(R.id.personalDetails_image);
        shopDetails_card = findViewById(R.id.shopDetails_card);
        shopDetails_image = findViewById(R.id.shopDetails_image);
        profilePic_Image = findViewById(R.id.profilePic_Image);
        profilePic_card = findViewById(R.id.profilePic_card);
        shopImages_card = findViewById(R.id.shopImages_card);
        shopImages_image = findViewById(R.id.shopImages_image);
        submitInformation_button = findViewById(R.id.submitInformation_button);
        back_image = findViewById(R.id.back_image);
    }

    private void checkProfileExits() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profileImage").exists()) {
                    Glide.with(AccountVerificationActivity.this).load(snapshot.child("profileImage").getValue().toString())
                            .into(profilePic_Image);
                    checkVerification();
                    progressDialog.dismiss();
                } else {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkVerification()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(accountType).child(username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("personalDetails").exists())
                {
                    personalDetails_image.setImageResource(R.drawable.check);
                }
                if (snapshot.child("vehicleDetails").exists())
                {
                    shopDetails_image.setImageResource(R.drawable.check);
                }
                if (snapshot.child("identityCardVerification").exists())
                {
                    shopImages_image.setImageResource(R.drawable.check);
                }


                if (snapshot.child("personalDetails").exists() && snapshot.child("vehicleDetails").exists()
                        && snapshot.child("identityCardVerification").exists())
                {
                    submitInformation_button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}