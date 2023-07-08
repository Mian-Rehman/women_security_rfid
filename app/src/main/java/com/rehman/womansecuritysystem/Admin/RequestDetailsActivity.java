package com.rehman.womansecuritysystem.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.R;

public class RequestDetailsActivity extends AppCompatActivity {

    ImageView back_image;
    Button btn_reject,btn_accept;
    TextView status_text,message_text;
    String requestKey,driverUsername,requestMessage,requestStatus,accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        initViews();
        getIntentValues();

        btn_reject.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AdminDriverRequest")
                    .child(driverUsername);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                       reference.child("requestStatus").setValue("reject");
                       status_text.setText("Request Rejected");
                        Toast.makeText(RequestDetailsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        btn_accept.setOnClickListener(v -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AdminDriverRequest")
                    .child(driverUsername);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        reference.child("requestStatus").setValue("accept");
                        status_text.setText("Request accepted");
                        Toast.makeText(RequestDetailsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    private void getIntentValues()
    {
        Intent intent = getIntent();
        requestKey = intent.getStringExtra("requestKey");
        driverUsername = intent.getStringExtra("driverUsername");
        requestMessage = intent.getStringExtra("requestMessage");
        requestStatus = intent.getStringExtra("requestStatus");
        accountType = intent.getStringExtra("accountType");

        message_text.setText(requestMessage);
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        btn_reject = findViewById(R.id.btn_reject);
        btn_accept = findViewById(R.id.btn_accept);
        status_text = findViewById(R.id.status_text);
        message_text = findViewById(R.id.message_text);
    }
}