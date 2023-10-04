package com.rehman.womansecuritysystem.ProfileProgress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.R;

import java.util.HashMap;
import java.util.Map;

public class PersonalDetailActivity extends AppCompatActivity {

    ImageView back_image;
    EditText fullNameDetail_ed,emailDetail_ed,phoneDetail_ed,fatherName_ed,address_ed;
    Button submit_button;

    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    String accountType,username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        progressDialog = ProgressDialog.show(this, "Please wait", "Processing", true);
        initView();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(accountType).child(username);


        checkValueExit();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        submit_button.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(this, "Please wait", "Processing", true);
            String email = emailDetail_ed.getText().toString().trim();
            String fullName = fullNameDetail_ed.getText().toString().trim();
            String phoneNumber = phoneDetail_ed.getText().toString().trim();
            String fatherName = fatherName_ed.getText().toString().trim();
            String address = address_ed.getText().toString().trim();

            if (isValid(fullName,email,phoneNumber,fatherName,address))
            {
                Map<String, String> map = new HashMap<>();
                map.put("userFullName",fullName);
                map.put("fatherName",fatherName);
                map.put("userEmail",email);
                map.put("userPhoneNumber",phoneNumber);
                map.put("userAddress",address);
                map.put("userFieldPersonal","1");

                SharedPreferences prefs = getSharedPreferences("FULL_DATA_PERSONAL",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userFullName",fullName);
                editor.putString("userEmail",email);
                editor.putString("userPhoneNumber",phoneNumber);
                editor.apply();


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child("personalDetails").setValue(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                saveVerifying();

            }

        });

    }

    private void saveVerifying()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("checkVerification").child("personalDetails").setValue("complete");
                reference.child("checkVerification").child("accountVerification").setValue("underReview");
                progressDialog.dismiss();
                Toast.makeText(PersonalDetailActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkValueExit()
    {
        DatabaseReference checkreference = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        checkreference.child("personalDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    String checkEmail = snapshot.child("userEmail").getValue(String.class);
                    String checkName = snapshot.child("userFullName").getValue(String.class);
                    String checkPhoneNumber = snapshot.child("userPhoneNumber").getValue(String.class);

                    emailDetail_ed.setText(checkEmail);
                    fullNameDetail_ed.setText(checkName);
                    phoneDetail_ed.setText(checkPhoneNumber);
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

    private boolean isValid(String fullName, String email, String phoneNumber
    ,String fatherName,String address)
    {
        if (fullName.isEmpty())
        {
            progressDialog.dismiss();
            fullNameDetail_ed.setError("field required");
            fullNameDetail_ed.requestFocus();
            return false;
        }

        if (fatherName.isEmpty())
        {
            progressDialog.dismiss();
            fatherName_ed.setError("field required");
            fatherName_ed.requestFocus();
            return false;
        }

        if (address.isEmpty())
        {
            progressDialog.dismiss();
            address_ed.setError("field required");
            address_ed.requestFocus();
            return false;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            progressDialog.dismiss();
            emailDetail_ed.setError("invalid Email");
            emailDetail_ed.requestFocus();
            return false;
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() <10)
        {
            progressDialog.dismiss();
            phoneDetail_ed.setError("invalid Number");
            phoneDetail_ed.requestFocus();
            return false;
        }

        return true;
    }

    private void initView()
    {
        back_image = findViewById(R.id.back_image);
        fullNameDetail_ed = findViewById(R.id.fullNameDetail_ed);
        emailDetail_ed = findViewById(R.id.emailDetail_ed);
        phoneDetail_ed = findViewById(R.id.phoneDetail_ed);
        submit_button = findViewById(R.id.submit_button);
        fatherName_ed = findViewById(R.id.fatherName_ed);
        address_ed = findViewById(R.id.address_ed);
    }
}