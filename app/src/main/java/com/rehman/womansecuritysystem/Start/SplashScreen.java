package com.rehman.womansecuritysystem.Start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.ConfirmActivity;
import com.rehman.womansecuritysystem.Driver.DriverMainActivity;
import com.rehman.womansecuritysystem.MainActivity;
import com.rehman.womansecuritysystem.R;

public class SplashScreen extends AppCompatActivity {

    ImageView sp_image;
    TextView sp_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash_screen);

        sp_image = findViewById(R.id.sp_image);
        sp_text = findViewById(R.id.sp_text);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        sp_image.startAnimation(animation);
        sp_text.startAnimation(animation1);

        startMain();
    }

    public void startMain() {
        new Handler(Looper.myLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, ConfirmActivity.class));
            finish();
        }, 3000);
    }
}