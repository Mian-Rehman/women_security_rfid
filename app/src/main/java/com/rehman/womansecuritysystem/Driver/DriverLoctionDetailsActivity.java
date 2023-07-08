package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.rehman.womansecuritysystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DriverLoctionDetailsActivity extends AppCompatActivity {

    String driverUsername;
    String lon;
    String lat;
    String finder_id;
    String sDesLat,sDesLong;
    //initilize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    String driverLatitude,driverLongitude,lastLocationDate,lastLocationTime;

    double currentLat = 0, currentLong = 0;
    GoogleMap map;

    LinearLayout direction_layout;
    TextView lastLon_text,lastlat_text,name_text;
    ImageView back_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_loction_details);

        initValues();
        Intent intent = getIntent();
        driverUsername = intent.getStringExtra("driverUsername");

        getLocationDatabase();

        direction_layout.setOnClickListener(v -> {
            String sSource = lat+","+lon;

            String sDestination = driverLatitude+","+driverLongitude;
            DisplayTrack(sSource,sDestination);
        });

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void initValues()
    {
        direction_layout = findViewById(R.id.direction_layout);
        lastLon_text = findViewById(R.id.lastLon_text);
        lastlat_text = findViewById(R.id.lastlat_text);
        name_text = findViewById(R.id.name_text);
    }

    private void getLocationDatabase()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DriverLastLocation")
                .child(driverUsername);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    driverLatitude = snapshot.child("currentLatitude").getValue(String.class);
                    driverLongitude = snapshot.child("currentLongitude").getValue(String.class);
                    lastLocationDate = snapshot.child("lastLocationDate").getValue(String.class);
                    lastLocationTime = snapshot.child("lastLocationTime").getValue(String.class);

                    name_text.setText(driverUsername);
                    lastlat_text.setText("last saved date: "+lastLocationDate);
                    lastlat_text.setText("last saved Time: "+lastLocationTime);

                    getLocations();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocations()
    {
        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initilize fused location
        client = LocationServices.getFusedLocationProviderClient(DriverLoctionDetailsActivity.this);

        //check permission
        if (ActivityCompat.checkSelfPermission(DriverLoctionDetailsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //when permission granted
            //Call Method
            getCurrentLocation();

        }
        else
        {
            //when permission denied
            //requst permission
            ActivityCompat.requestPermissions(DriverLoctionDetailsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
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


                            double lati = Double.parseDouble(driverLatitude);
                            double langi = Double.parseDouble(driverLongitude);
                            //    Just show current location
                            //initilize lat lag
                            LatLng latLng = new LatLng(lati,langi);

                            //craete marker option
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("i am there");

                            //zoom map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lati,langi),10));

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