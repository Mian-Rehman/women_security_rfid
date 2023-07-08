package com.rehman.womansecuritysystem.Parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
import com.rehman.womansecuritysystem.ConfirmActivity;
import com.rehman.womansecuritysystem.R;
import com.rehman.womansecuritysystem.Student.StudentMainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParentMainActivity extends AppCompatActivity {

    TextView name_text,email_text,number_text,title_text;
    String accountCreationKey,email,name,password,phoneNumber,accountType,username;
    CardView addPerson_card,myChild_card,driver_card,myChilds_card;

    TextView studentPlace_text,studentCity_text,studentCountry_text,date_text,time_text;
    LinearLayout map_layout;
    String studentPlace,studentCity,studentCountry,date,time,currentTime,currentDate,currentLatitude,
    currentLongitude;
    Button btn_getDirection;
    ImageView logout_image;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    LocationManager locationManager;

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
        setContentView(R.layout.activity_parent_main);

        initViews();
        getIntentValue();
        checkStudentEmergency();

        currentDate = getCurrentdate();
        currentTime = getTimeWithAmPm();

        map_layout.setVisibility(View.GONE);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        addPerson_card.setOnClickListener(v -> {
            Intent intent =new Intent(ParentMainActivity.this,ParentAddActivity.class);
            intent.putExtra("activity","activity");
            startActivity(intent);
        });

        myChild_card.setOnClickListener(v -> {
            startActivity(new Intent(ParentMainActivity.this,MyChildActivity.class));
        });

        driver_card.setOnClickListener(v -> {
            startActivity(new Intent(ParentMainActivity.this,DriverListActivity.class));
        });

        btn_getDirection.setOnClickListener(v -> {
            String sSource = lat+","+lon;

            String sDestination = currentLatitude+","+currentLongitude;
            DisplayTrack(sSource,sDestination);
        });

        logout_image.setOnClickListener(v -> {
            startActivity(new Intent(ParentMainActivity.this, ConfirmActivity.class));
            finish();
        });

        myChilds_card.setOnClickListener(v -> {
            startActivity(new Intent(ParentMainActivity.this, DriverLastLocationActivity.class));
        });


    }

    private void checkStudentEmergency()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EmergencyLocation")
                .child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                        date = snapshot.child("currentDate").getValue(String.class);
                    assert date != null;
                    if (date.equals(currentDate))
                        {
                            studentCity = snapshot.child("city").getValue(String.class);
                            studentCountry = snapshot.child("country").getValue(String.class);
                            date = snapshot.child("currentDate").getValue(String.class);
                            currentLatitude = snapshot.child("currentLatitude").getValue(String.class);
                            currentLongitude = snapshot.child("currentLongitude").getValue(String.class);
                            time = snapshot.child("currentTime").getValue(String.class);
                            studentPlace = snapshot.child("place").getValue(String.class);
                            map_layout.setVisibility(View.VISIBLE);

                            studentPlace_text.setText("Place: "+studentPlace);
                            studentCity_text.setText("City: "+studentCity);
                            studentCountry_text.setText("Country: "+studentCountry);
                            date_text.setText("date: "+date);
                            time_text.setText("Time: "+time);

                            //Assign variable
                            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.google_map);

                            //initilize fused location
                            client = LocationServices.getFusedLocationProviderClient(ParentMainActivity.this);

                            //check permission
                            if (ActivityCompat.checkSelfPermission(ParentMainActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                                //when permission granted
                                //Call Method
                                getCurrentLocation();

                            }
                            else
                            {
                                //when permission denied
                                //requst permission
                                ActivityCompat.requestPermissions(ParentMainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                            }
                        }else
                    {
                        map_layout.setVisibility(View.GONE);
                    }
                }
                else
                {
                    map_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews()
    {
        name_text = findViewById(R.id.name_text);
        email_text = findViewById(R.id.email_text);
        number_text = findViewById(R.id.number_text);
        title_text = findViewById(R.id.title_text);
        addPerson_card = findViewById(R.id.addPerson_card);
        myChild_card = findViewById(R.id.myChild_card);
        driver_card = findViewById(R.id.driver_card);
        studentPlace_text = findViewById(R.id.studentPlace_text);
        studentCity_text = findViewById(R.id.studentCity_text);
        studentCountry_text = findViewById(R.id.studentCountry_text);
        date_text = findViewById(R.id.date_text);
        time_text = findViewById(R.id.time_text);
        map_layout = findViewById(R.id.map_layout);
        btn_getDirection = findViewById(R.id.btn_getDirection);
        logout_image = findViewById(R.id.logout_image);
        myChilds_card = findViewById(R.id.myChilds_card);
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

    private String getTimeWithAmPm()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate()
    {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
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

                            double latitude = Double.parseDouble(currentLatitude);
                            double longitude = Double.parseDouble(currentLongitude);

                            //    Just show current location
                            //initilize lat lag
                            LatLng latLng = new LatLng(latitude,longitude);


                            //craete marker option
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("i am there");

                            //zoom map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(latitude,longitude),10));

                            //add marker on map
                            googleMap.addMarker(options);

                            lon=String.valueOf(currentLong);
                            lat=String.valueOf(currentLat);

                        }
                    });
                }
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

    private void DisplayTrack(String sSource, String sDestination)
    {
        //if the device dost not have a map installed, thne directed

        try {
            //when google map is installed
            //initilzied uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource +"/"
                    + sDestination);

            //initilize intent action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            //set package
            intent.setPackage("com.google.android.apps.maps");

            //set Flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
        catch (ActivityNotFoundException e)
        {
            //when google map is not installed
            //initilize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            // initlize intentt
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            //set Flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    }
}