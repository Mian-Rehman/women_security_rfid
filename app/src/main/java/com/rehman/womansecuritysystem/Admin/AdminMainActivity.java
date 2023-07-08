package com.rehman.womansecuritysystem.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.rehman.womansecuritysystem.ConfirmActivity;
import com.rehman.womansecuritysystem.Driver.AdminRequestActivity;
import com.rehman.womansecuritysystem.Driver.DriverMainActivity;
import com.rehman.womansecuritysystem.R;

public class AdminMainActivity extends AppCompatActivity {

    TextView name_text,email_text,number_text,title_text;
    String accountCreationKey,accountType,name,username,email,password,phoneNumber;
    CardView driver_card,user_card,driverImage_card,request_card,driverAccount_card;
    ImageView logout_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initViews();
        getIntentValue();

        logout_image.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this, ConfirmActivity.class));
            finish();
        });

        driver_card.setOnClickListener(v -> {
            Intent intent =new Intent(AdminMainActivity.this,AdminDriverListActivity.class);
            intent.putExtra("userAccountType","Driver");
            startActivity(intent);
        });

        user_card.setOnClickListener(v -> {
            Intent intent =new Intent(AdminMainActivity.this,AdminDriverListActivity.class);
            intent.putExtra("userAccountType","Parent");
            startActivity(intent);
        });

        driverImage_card.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this,AdminImageUploadActivity.class));
        });

        request_card.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this,DriverRequestActivity.class));
        });

        driverAccount_card.setOnClickListener(v -> {
            startActivity(new Intent(AdminMainActivity.this, AdminRequestActivity.class));
        });


    }

    private void initViews()
    {
        name_text = findViewById(R.id.name_text);
        email_text = findViewById(R.id.email_text);
        number_text = findViewById(R.id.number_text);
        title_text = findViewById(R.id.title_text);
        driver_card = findViewById(R.id.driver_card);
        user_card = findViewById(R.id.user_card);
        driverImage_card = findViewById(R.id.driverImage_card);
        request_card = findViewById(R.id.request_card);
        logout_image = findViewById(R.id.logout_image);
        driverAccount_card = findViewById(R.id.driverAccount_card);
    }

    @SuppressLint("SetTextI18n")
    private void getIntentValue()
    {
        SharedPreferences sp=getSharedPreferences("CURRENT",MODE_PRIVATE);
        accountCreationKey  = sp.getString("accountCreationKey","");
        email  = sp.getString("email","");
        name  = sp.getString("name","");
        password  = sp.getString("password","");
        phoneNumber  = sp.getString("phoneNumber","");
        accountType  = sp.getString("accountType","");
        username  = sp.getString("userName","");

        title_text.setText(accountType+" Main Menu");
        name_text.setText("Manage Parent Accounts");
        email_text.setText("Manage Driver Accounts");
        number_text.setText("Set RFID Details");
    }
}