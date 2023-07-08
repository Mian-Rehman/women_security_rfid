package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddDriverActivity extends AppCompatActivity {

    ImageView back_image;
    EditText ed_fullName,ed_cardNumber,ed_daysHire,ed_phoneDetail,ed_address,ed_password,ed_username,ed_message;
    Button btn_next,btn_send;
    String accountType,username,fullName,cardNumber,daysHire,phoneNumber,address,driverUsername,driverPassword;
    TextView status_text;
    LinearLayout dataField_layout,request_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        initViews();

        dataField_layout.setVisibility(View.GONE);
        request_layout.setVisibility(View.GONE);
        getApplicationSubmit();


        back_image.setOnClickListener(v -> {
            Intent intent =  new Intent(AddDriverActivity.this,DriverMainActivity.class);
            startActivity(intent);
            finish();
        });

        btn_send.setOnClickListener(v -> {
            String message = ed_message.getText().toString().trim();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AdminDriverRequest");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String key = reference.push().getKey();
                    Map<String,String> map = new HashMap<>();
                    map.put("requestKey",key);
                    map.put("driverUsername",username);
                    map.put("requestMessage",message);
                    map.put("requestStatus","none");
                    map.put("accountType",accountType);
                    reference.child(username).setValue(map);
                    Toast.makeText(AddDriverActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        btn_next.setOnClickListener(v -> {
            fullName = ed_fullName.getText().toString().trim();
            cardNumber = ed_cardNumber.getText().toString().trim();
            daysHire = ed_daysHire.getText().toString().trim();
            phoneNumber = ed_phoneDetail.getText().toString().trim();
            address = ed_address.getText().toString().trim();
            driverUsername = ed_username.getText().toString().trim();
            driverPassword = ed_password.getText().toString().trim();

            if (isValid(fullName,cardNumber,daysHire,phoneNumber,address,driverUsername,driverPassword))
            {
                String currentDate = getCurrentdate();
                String currentTime = getTimeWithAmPm();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DriversHiring");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      String key = reference.push().getKey();
                        Map<String,String> map = new HashMap<>();
                        map.put("driverFullName",fullName);
                        map.put("driverCnicNumber",cardNumber);
                        map.put("driverDaysHire",daysHire);
                        map.put("driverPhoneNumber",phoneNumber);
                        map.put("driverAddress",address);
                        map.put("driverStatus","hiring");
                        map.put("driverKey",key);
                        map.put("driverUsername",driverUsername);
                        map.put("driverPassword",driverPassword);
                        map.put("hiringDate",currentDate);
                        map.put("hiringTime",currentTime);
                        map.put("mainDriverUsername",username);
                        map.put("mainDriverAccountType",accountType);

                        assert key != null;
                        reference.child(key).setValue(map);
                        Toast.makeText(AddDriverActivity.this, "Driver Added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void getApplicationSubmit()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AdminDriverRequest")
                .child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String checkStatus = snapshot.child("requestStatus").getValue(String.class);
                    assert checkStatus != null;
                    if (checkStatus.equals("none"))
                    {
                        dataField_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.VISIBLE);
                        status_text.setText("Request is under review");
                    }else if (checkStatus.equals("reject"))
                    {
                        dataField_layout.setVisibility(View.GONE);
                        request_layout.setVisibility(View.VISIBLE);
                        status_text.setText("Request is rejected by admin");
                        btn_send.setText("Re-submit Request");
                    }else if (checkStatus.equals("accept"))
                    {
                        request_layout.setVisibility(View.GONE);
                        dataField_layout.setVisibility(View.VISIBLE);
                    }
                }else
                {
                    request_layout.setVisibility(View.VISIBLE);
                    dataField_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isValid(String fullName, String cardNumber, String daysHire,
                            String phoneNumber, String address, String driverUsername, String driverPassword)
    {
        if (fullName.isEmpty())
        {
            ed_fullName.setError("field required");
            ed_fullName.requestFocus();
            return false;
        }

        if (cardNumber.isEmpty())
        {
            ed_cardNumber.setError("field required");
            ed_cardNumber.requestFocus();
            return false;
        }

        if (daysHire.isEmpty())
        {
            ed_daysHire.setError("field required");
            ed_daysHire.requestFocus();
            return false;
        }
        if (phoneNumber.isEmpty() && phoneNumber.length() < 9)
        {
            ed_fullName.setError("field required");
            ed_fullName.requestFocus();
            return false;
        }

        if (address.isEmpty())
        {
            ed_address.setError("field required");
            ed_address.requestFocus();
            return false;
        }

        if (driverUsername.isEmpty())
        {
            ed_username.setError("field required");
            ed_username.requestFocus();
            return false;
        }

        if (driverPassword.isEmpty())
        {
            ed_password.setError("field required");
            ed_password.requestFocus();
            return false;
        }

        return true;
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        request_layout = findViewById(R.id.request_layout);
        ed_fullName = findViewById(R.id.ed_fullName);
        ed_cardNumber = findViewById(R.id.ed_cardNumber);
        ed_daysHire = findViewById(R.id.ed_daysHire);
        ed_phoneDetail = findViewById(R.id.ed_phoneDetail);
        ed_address = findViewById(R.id.ed_address);
        btn_next = findViewById(R.id.btn_next);
        ed_password = findViewById(R.id.ed_password);
        ed_username = findViewById(R.id.ed_username);
        dataField_layout = findViewById(R.id.dataField_layout);
        ed_message = findViewById(R.id.ed_message);
        btn_send = findViewById(R.id.btn_send);
        status_text = findViewById(R.id.status_text);
    }

    private String getTimeWithAmPm()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate()
    {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
    }
}