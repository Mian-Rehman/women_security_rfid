package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.AccountVerificationActivity;
import com.rehman.womansecuritysystem.ConfirmActivity;
import com.rehman.womansecuritysystem.Parent.ParentMainActivity;
import com.rehman.womansecuritysystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DriverMainActivity extends AppCompatActivity {

    ImageView logout_image,profile_image;
    TextView name_text,email_text,number_text,title_text,verify_text;
    String accountCreationKey,email,name,password,phoneNumber,accountType,username;

    LinearLayout verified_layout,cardLayout1,cardLayout2,cardLayout3,underReview_Layout,reject_Layout;

    CardView checkProfile_card,addDriver_card,addStudent_card,myStudent_card,picStudent_card;

    ProgressDialog progressDialog;
    String lon;
    String lat;
    String finder_id;
    String sDesLat,sDesLong;
    //initilize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    double currentLat = 0, currentLong = 0;
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        initViews();

        verified_layout.setVisibility(View.GONE);
        verify_text.setVisibility(View.VISIBLE);
        cardLayout1.setVisibility(View.GONE);
        cardLayout2.setVisibility(View.GONE);
        cardLayout3.setVisibility(View.GONE);

        getIntentValue();
        checkVerification();



        logout_image.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this, ConfirmActivity.class));
            finish();
        });


        verified_layout.setVisibility(View.GONE);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        verify_text.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this, AccountVerificationActivity.class));
        });



        checkProfile_card.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this, AccountVerificationActivity.class));
        });

        addStudent_card.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this,AddStudentActivity.class));
        });

        myStudent_card.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this,MyStudentList.class));
        });

        addDriver_card.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this,AddDriverActivity.class));
        });

        picStudent_card.setOnClickListener(v -> {
            startActivity(new Intent(DriverMainActivity.this,StudentPickUpActivity.class));
        });




    }

    private void getLocations()
    {
        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initilize fused location
        client = LocationServices.getFusedLocationProviderClient(DriverMainActivity.this);

        //check permission
        if (ActivityCompat.checkSelfPermission(DriverMainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //when permission granted
            //Call Method
            getCurrentLocation();

        }
        else
        {
            //when permission denied
            //requst permission
            ActivityCompat.requestPermissions(DriverMainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    private void initViews()
    {
        logout_image = findViewById(R.id.logout_image);
        name_text = findViewById(R.id.name_text);
        email_text = findViewById(R.id.email_text);
        number_text = findViewById(R.id.number_text);
        title_text = findViewById(R.id.title_text);
        verify_text = findViewById(R.id.verify_text);
        verified_layout = findViewById(R.id.verified_layout);
        profile_image = findViewById(R.id.profile_image);
        checkProfile_card = findViewById(R.id.checkProfile_card);
        addDriver_card = findViewById(R.id.addDriver_card);
        addStudent_card = findViewById(R.id.addStudent_card);
        myStudent_card = findViewById(R.id.myStudent_card);
        picStudent_card = findViewById(R.id.picStudent_card);
        cardLayout1 = findViewById(R.id.cardLayout1);
        cardLayout2 = findViewById(R.id.cardLayout2);
        cardLayout3 = findViewById(R.id.cardLayout3);
        underReview_Layout = findViewById(R.id.underReview_Layout);
        reject_Layout = findViewById(R.id.reject_Layout);
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
        name_text.setText("Name: "+name);
        email_text.setText("Email: "+email);
        number_text.setText("Phone Number: "+phoneNumber);

    }

    private void checkVerification()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(accountType).child(username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("personalDetails").exists() && snapshot.child("vehicleDetails").exists()
                        && snapshot.child("identityCardVerification").exists())
                {
                    if (snapshot.child("checkVerification").exists())
                    {
                        String value = snapshot.child("checkVerification")
                                .child("accountVerification").getValue(String.class);

                        assert value != null;
                        if (value.equals("accept"))
                        {
                            getLocations();
                            verify_text.setVisibility(View.GONE);
                            underReview_Layout.setVisibility(View.GONE);
                            reject_Layout.setVisibility(View.GONE);
                            verified_layout.setVisibility(View.VISIBLE);
                            cardLayout1.setVisibility(View.VISIBLE);
                            cardLayout2.setVisibility(View.VISIBLE);
                            cardLayout3.setVisibility(View.VISIBLE);

                        }else if (value.equals("underReview"))
                        {
                            verify_text.setText("under Review");
                            verified_layout.setVisibility(View.GONE);
                            reject_Layout.setVisibility(View.GONE);
                            verify_text.setVisibility(View.VISIBLE);
                            underReview_Layout.setVisibility(View.VISIBLE);
                            cardLayout1.setVisibility(View.GONE);
                            cardLayout2.setVisibility(View.GONE);
                            cardLayout3.setVisibility(View.GONE);
                        }
                        else if (value.equals("reject"))
                        {
                            verify_text.setText("Documents Rejected");
                            verified_layout.setVisibility(View.GONE);
                            underReview_Layout.setVisibility(View.GONE);
                            verify_text.setVisibility(View.VISIBLE);
                            reject_Layout.setVisibility(View.VISIBLE);
                            cardLayout1.setVisibility(View.GONE);
                            cardLayout2.setVisibility(View.GONE);
                            cardLayout3.setVisibility(View.GONE);
                        }

                    }

                }
                else
                {
                    verified_layout.setVisibility(View.GONE);
                    verify_text.setVisibility(View.VISIBLE);
                    cardLayout1.setVisibility(View.GONE);
                    cardLayout2.setVisibility(View.GONE);
                    cardLayout3.setVisibility(View.GONE);
                }

                if (snapshot.child("profileImage").exists())
                {
                    Glide.with(DriverMainActivity.this).load(snapshot.child("profileImage").getValue().toString())
                            .into(profile_image);
                }else
                {
                    profile_image.setImageResource(R.drawable.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getCurrentLocation() {

        //initilize task location
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //when Success
                if (location != null)
                {

                    //when location in not equal to null
                    //Get current Latitude
                    currentLat = location.getLatitude();

                    //get Current Longitude
                    currentLong = location.getLongitude();


                    //synMap
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            //when map is ready

                            map = googleMap;


                            //    Just show current location
                            //initilize lat lag
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            //craete marker option
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("i am there");

                            //zoom map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong),10));

                            //add marker on map
                            googleMap.addMarker(options);

                            lon=String.valueOf(currentLong);
                            lat=String.valueOf(currentLat);

                            storeLocationToDatabase(lon,lat,username);

                        }
                    });
                }
            }
        });


    }

    private void storeLocationToDatabase(String lon, String lat, String username)
    {
        String currentTime =  getTimeWithAmPm();
        String currentDate = getCurrentdate();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DriverLastLocation");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> map = new HashMap<>();
                map.put("currentLatitude",lat);
                map.put("currentLongitude",lon);
                map.put("driverUsername",username);
                map.put("lastLocationDate",currentDate);
                map.put("lastLocationTime",currentTime);

                reference.child(username).setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }
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