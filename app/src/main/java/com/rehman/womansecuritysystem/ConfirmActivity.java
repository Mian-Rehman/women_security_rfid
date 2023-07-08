package com.rehman.womansecuritysystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.rehman.womansecuritysystem.Start.LoginActivity;

public class ConfirmActivity extends AppCompatActivity {

    CardView admin_card,driver_card,parent_card,student_card;
    String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        initViews();

        admin_card.setOnClickListener(v -> {
            accountType = "Admin";
            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
            intent.putExtra("accountType",accountType);
            startActivity(intent);
        });

        driver_card.setOnClickListener(v -> {
            accountType = "Driver";
            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
            intent.putExtra("accountType",accountType);
            startActivity(intent);
        });

        parent_card.setOnClickListener(v -> {
            accountType = "Parent";
            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
            intent.putExtra("accountType",accountType);
            startActivity(intent);
        });

        student_card.setOnClickListener(v -> {
            accountType = "Student";
            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
            intent.putExtra("accountType",accountType);
            startActivity(intent);
        });

    }

    private void initViews() {
        admin_card = findViewById(R.id.admin_card);
        driver_card = findViewById(R.id.driver_card);
        parent_card = findViewById(R.id.parent_card);
        student_card = findViewById(R.id.student_card);
    }
}