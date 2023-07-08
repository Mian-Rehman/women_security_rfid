package com.rehman.womansecuritysystem.Parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

public class ParentAddActivity extends AppCompatActivity {

    ImageView back_image;
    EditText ed_fullName,ed_childName,ed_className,ed_instituteName,ed_instituteAddress,ed_phoneDetail
            ,ed_address,ed_rollNo;
    Button btn_save;
    TextView personl_text;
    String accountType,username,fullName,childName,className,instituteName,instituteAddress,
    phoneNumber,address,studentGetDriver = "none",rollNumber;
    String activity,addKey,userUsername,userAccountType,parentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_add);

        initViews();
        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        if (activity.equals("adapter"))
        {
            getIntentValues();
        }

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });


        btn_save.setOnClickListener(v -> {
            fullName = ed_fullName.getText().toString().trim();
            className = ed_className.getText().toString().trim();
            childName = ed_childName.getText().toString().trim();
            instituteName = ed_instituteName.getText().toString().trim();
            instituteAddress = ed_instituteAddress.getText().toString().trim();
            phoneNumber = ed_phoneDetail.getText().toString().trim();
            address = ed_address.getText().toString().trim();
            rollNumber = ed_rollNo.getText().toString().trim();

            if (isValid(fullName,className,childName,instituteName,instituteAddress,phoneNumber,address,rollNumber))
            {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       if (activity.equals("adapter"))
                       {
                           ChildModel model = new ChildModel(fullName,childName,className,instituteName,instituteAddress
                                   ,phoneNumber,address,addKey,userUsername,userAccountType,studentGetDriver,parentUsername,rollNumber);
                           assert addKey != null;
                           reference.child(addKey).setValue(model);
                           Toast.makeText(ParentAddActivity.this, "Child Updated", Toast.LENGTH_SHORT).show();
                       }else
                       {
                           String key = reference.push().getKey();
                           ChildModel model = new ChildModel(fullName,childName,className,instituteName,instituteAddress
                                   ,phoneNumber,address,key,username,accountType,studentGetDriver,username,rollNumber);
                           assert key != null;
                           reference.child(key).setValue(model);
                           Toast.makeText(ParentAddActivity.this, "Child Added", Toast.LENGTH_SHORT).show();
                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
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
        parentUsername = intent.getStringExtra("parentUsername");
        rollNumber = intent.getStringExtra("rollNumber");

        ed_fullName.setText(fullName);
        ed_childName.setText(childName);
        ed_className.setText(className);
        ed_instituteName.setText(instituteName);
        ed_instituteAddress.setText(instituteAddress);
        ed_phoneDetail.setText(phoneNumber);
        ed_address.setText(address);
        ed_rollNo.setText(rollNumber);

        btn_save.setText("Update");
        personl_text.setText("My Child Details");

    }

    private boolean isValid(String fullName, String className,
                            String childName, String instituteName,
                            String instituteAddress, String phoneNumber, String address,String rollNumber)
    {
    if (fullName.isEmpty())
    {
        ed_fullName.setError("field required");
        ed_fullName.requestFocus();
        return false;
    }
        if (className.isEmpty())
        {
            ed_className.setError("field required");
            ed_className.requestFocus();
            return false;
        }

        if (childName.isEmpty())
        {
            ed_childName.setError("field required");
            ed_childName.requestFocus();
            return false;
        }

        if (instituteName.isEmpty())
        {
            ed_instituteName.setError("field required");
            ed_instituteName.requestFocus();
            return false;
        }

        if (instituteAddress.isEmpty())
        {
            ed_instituteAddress.setError("field required");
            ed_instituteAddress.requestFocus();
            return false;
        }

        if (rollNumber.isEmpty())
        {
            ed_rollNo.setError("field required");
            ed_rollNo.requestFocus();
            return false;
        }

        if (phoneNumber.isEmpty())
        {
            ed_phoneDetail.setError("field required");
            ed_phoneDetail.requestFocus();
            return false;
        }

        if (address.isEmpty())
        {
            ed_address.setError("field required");
            ed_address.requestFocus();
            return false;
        }

        return true;
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        ed_fullName = findViewById(R.id.ed_fullName);
        ed_childName = findViewById(R.id.ed_childName);
        ed_className = findViewById(R.id.ed_className);
        ed_instituteName = findViewById(R.id.ed_instituteName);
        ed_instituteAddress = findViewById(R.id.ed_instituteAddress);
        ed_phoneDetail = findViewById(R.id.ed_phoneDetail);
        ed_address = findViewById(R.id.ed_address);
        btn_save = findViewById(R.id.btn_save);
        personl_text = findViewById(R.id.personl_text);
        ed_rollNo = findViewById(R.id.ed_rollNo);
    }
}