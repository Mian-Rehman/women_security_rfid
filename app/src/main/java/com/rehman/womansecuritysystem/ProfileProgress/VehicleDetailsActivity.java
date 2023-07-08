package com.rehman.womansecuritysystem.ProfileProgress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.rehman.womansecuritysystem.R;

import java.util.HashMap;
import java.util.Map;

public class VehicleDetailsActivity extends AppCompatActivity {

    ImageView back_image;
    EditText ed_vehicleOwnerName,ed_vehicleNumber,ed_driverName,ed_vehicleDetails;
    TextView tv_vehicleType;
    Button submit_button;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference reference;
    String username,accountType,vehicleOwnerName,vehicleNumber,driverName,vehicleType,vehicleDetails = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        intiViews();
        progressDialog = ProgressDialog.show(this, "Please wait", "Processing", true);
        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(accountType).child(username);

        checkValueExit();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        tv_vehicleType.setOnClickListener(v -> {
            showVehicleTypeDialog();
        });

        submit_button.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(this, "Please wait", "Processing", true);
            vehicleOwnerName  = ed_vehicleOwnerName.getText().toString().trim();
            vehicleNumber = ed_vehicleNumber.getText().toString().trim();
            driverName = ed_driverName.getText().toString().trim();
            vehicleType = tv_vehicleType.getText().toString().trim();
            vehicleDetails = ed_vehicleDetails.getText().toString().trim();

            if (isValid(vehicleOwnerName,vehicleNumber,driverName,vehicleType))
            {
                Map<String, String> map = new HashMap<>();
                map.put("vehicleOwnerName",vehicleOwnerName);
                map.put("vehicleNumber",vehicleNumber);
                map.put("driverName",driverName);
                map.put("vehicleType",vehicleType);
                map.put("vehicleDetails",vehicleDetails);
                map.put("userFieldPersonal","1");


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child("vehicleDetails").setValue(map);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                saveVerifying();

            }
        });

    }

    private void showVehicleTypeDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.vehicle_list_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        TextView van_text = layout_dialog.findViewById(R.id.van_text);
        TextView miniVan_text = layout_dialog.findViewById(R.id.miniVan_text);
        TextView car_text = layout_dialog.findViewById(R.id.car_text);
        TextView bus_text = layout_dialog.findViewById(R.id.bus_text);



        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        van_text.setOnClickListener(v -> {
            vehicleType = van_text.getText().toString().trim();
            tv_vehicleType.setText(vehicleType);
            dialog.dismiss();
        });

        miniVan_text.setOnClickListener(v -> {
            vehicleType = miniVan_text.getText().toString().trim();
            tv_vehicleType.setText(vehicleType);
            dialog.dismiss();
        });

        car_text.setOnClickListener(v -> {
            vehicleType = car_text.getText().toString().trim();
            tv_vehicleType.setText(vehicleType);
            dialog.dismiss();
        });

        bus_text.setOnClickListener(v -> {
            vehicleType = bus_text.getText().toString().trim();
            tv_vehicleType.setText(vehicleType);
            dialog.dismiss();
        });


    }

    private void saveVerifying()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("checkVerification").child("vehicleDetails").setValue("complete");
                reference.child("checkVerification").child("accountVerification").setValue("underReview");
                progressDialog.dismiss();
                Toast.makeText(VehicleDetailsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isValid(String vehicleOwnerName, String vehicleNumber, String driverName, String vehicleType)
    {
        if (vehicleOwnerName.isEmpty())
        {
            ed_vehicleOwnerName.setError("field required");
            ed_vehicleOwnerName.requestFocus();
            return false;
        }

        if (vehicleNumber.isEmpty())
        {
            ed_vehicleNumber.setError("field required");
            ed_vehicleNumber.requestFocus();
            return false;
        }

        if (driverName.isEmpty())
        {
            ed_driverName.setError("field required");
            ed_driverName.requestFocus();
            return false;
        }

        if (vehicleType.isEmpty())
        {
            Toast.makeText(this, "Select vehicle type", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void intiViews()
    {
        back_image = findViewById(R.id.back_image);
        ed_vehicleOwnerName = findViewById(R.id.ed_vehicleOwnerName);
        ed_vehicleNumber = findViewById(R.id.ed_vehicleNumber);
        ed_driverName = findViewById(R.id.ed_driverName);
        tv_vehicleType = findViewById(R.id.tv_vehicleType);
        ed_vehicleDetails = findViewById(R.id.ed_vehicleDetails);
        submit_button = findViewById(R.id.submit_button);
    }

    private void checkValueExit()
    {
        DatabaseReference checkreference = FirebaseDatabase.getInstance().getReference(accountType).child(username);
        checkreference.child("vehicleDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    vehicleOwnerName = snapshot.child("vehicleOwnerName").getValue(String.class);
                    vehicleNumber = snapshot.child("vehicleNumber").getValue(String.class);
                    driverName = snapshot.child("driverName").getValue(String.class);
                    vehicleType = snapshot.child("vehicleType").getValue(String.class);
                    vehicleDetails = snapshot.child("vehicleDetails").getValue(String.class);

                    ed_vehicleOwnerName.setText(vehicleOwnerName);
                    ed_vehicleNumber.setText(vehicleNumber);
                    ed_driverName.setText(driverName);
                    tv_vehicleType.setText(vehicleType);
                    ed_vehicleDetails.setText(vehicleDetails);
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
}