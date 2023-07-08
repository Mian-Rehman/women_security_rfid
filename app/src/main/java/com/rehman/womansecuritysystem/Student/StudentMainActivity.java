package com.rehman.womansecuritysystem.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rehman.womansecuritysystem.ConfirmActivity;
import com.rehman.womansecuritysystem.Driver.DriverMainActivity;
import com.rehman.womansecuritysystem.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentMainActivity extends AppCompatActivity {

    String accountCreationKey,email,name,password,phoneNumber,accountType,username;
    Button btn_updateLocation;

    String userCity;
    String userState;
    String userCountry;
    String userLocation;
    String userLatitude;
    String userLongitude;
    String userUID;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    LocationManager locationManager;

    String lon;
    String lat;
    String finder_id;

    String sDesLat,sDesLong;
    ImageView logout_image;


    //initilize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    double currentLat = 0, currentLong = 0;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        btn_updateLocation = findViewById(R.id.btn_updateLocation);
        logout_image = findViewById(R.id.logout_image);
        getIntentValue();


        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initilize fused location
        client = LocationServices.getFusedLocationProviderClient(StudentMainActivity.this);

        //check permission
        if (ActivityCompat.checkSelfPermission(StudentMainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //when permission granted
            //Call Method
            getCurrentLocation();

        }
        else
        {
            //when permission denied
            //requst permission
            ActivityCompat.requestPermissions(StudentMainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationPremissionCheck();
        GooglePlayServiceCheck();
        GPSLocationServiceCheck();



        btn_updateLocation.setOnClickListener(v -> {
            DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("EmergencyLocation");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String currentTime = getTimeWithAmPm();
                    String currentDate = getCurrentdate();
                    Map<String,String> map = new HashMap<>();
                    map.put("parentUsername",username);
                    map.put("currentLatitude",lat);
                    map.put("currentLongitude",lon);
                    map.put("city",userCity);
                    map.put("place",userLocation);
                    map.put("country",userCountry);
                    map.put("currentTime",currentTime);
                    map.put("currentDate",currentDate);
                    reference.child(username).setValue(map);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        logout_image.setOnClickListener(v -> {
            startActivity(new Intent(StudentMainActivity.this, ConfirmActivity.class));
            finish();
        });


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

    }

    private void LocationPremissionCheck() {

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        String rationale = "Please provide location permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Location Permission")
                .setSettingsDialogTitle("Warning");
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                LocationRequest();

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                LocationPremissionCheck();
            }
        });
    }

    private void LocationRequest() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PermissionChecker.PERMISSION_GRANTED) {


            fusedLocationProviderClient = new FusedLocationProviderClient(this);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {


                    if (location != null) {

                        Double locationLatitude = location.getLatitude();
                        Double locationLongitude = location.getLongitude();

                        userLatitude = locationLatitude.toString();
                        userLongitude = locationLongitude.toString();

                        if (!userLatitude.equals("0.0") && !userLongitude.equals("0.0")) {

                            LocationRetreive(locationLatitude, locationLongitude);

                        } else {

                            Toast.makeText(StudentMainActivity.this,
                                    "Please turn on any GPS or location service and restart to use the app", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        Toast.makeText(StudentMainActivity.this,
                                "Please turn on any GPS or location service and restart to use the app", Toast.LENGTH_SHORT).show();
                    }

                }

            });


        } else {

            LocationPremissionCheck();

        }
    }

    public boolean GooglePlayServiceCheck() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    private void GPSLocationServiceCheck() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, enable it to use this app?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
//                            Intent intent = new Intent(RegisterActivity.this, StartActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            finish();

                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void LocationRetreive(Double locationLatitude, Double locationLongitude) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(locationLatitude, locationLongitude, 1);
            if (addresses != null && addresses.size() > 0) {
                userCity = addresses.get(0).getLocality();
                userState = addresses.get(0).getAdminArea();
                userCountry = addresses.get(0).getCountryName();
                userLocation = addresses.get(0).getAddressLine(0);


                if (userCountry == null) {
                    if (userState != null) {
                        userCountry = userState;
                    } else if (userCity != null) {
                        userCountry = userCity;
                    } else {
                        userCountry = "null";
                    }
                }
                if (userCity == null) {
                    if (userState != null) {
                        userCity = userState;
                    } else {
                        userCity = userCountry;
                    }
                }
                if (userState == null) {
                    if (userCity != null) {
                        userState = userCity;
                    } else {
                        userState = userCountry;
                    }
                }
                if (userLocation == null) {
                    userLocation = "Null";
                }




            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(StudentMainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
}