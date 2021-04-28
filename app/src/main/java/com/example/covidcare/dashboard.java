package com.example.covidcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FragmentContainerView fragmentContainerView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private LatLang latLang;
    boolean checkIfLocationExist = false;
    int temp= 0;
    int temp2 = 0;
    private LatLng latLng;
    ArrayList<LatLng> locationArrayList;
    boolean locationExist = false;
    boolean checkLessThanSixFitDistance = false;
    private Distance_Lat_Lang distanceBetwnTwoLocation;
    String []getCity;
//    private int designWidth =470;
//    private int designHeight = 730;
//    private int dpHeight;
//    private int dpWidth;
//    private float density;


    public double latitude, longitude;
    Toolbar toolbar;
    TextView userName;

    // Declaring Location Object for implementing user current location

    final int REQUEST_CODE = 8932;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int TEN_MINUTES =  15 * 60 * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        navigationView = findViewById(R.id.navigation_menu);
        View headerview = navigationView.getHeaderView(0);
        userName = headerview.findViewById(R.id.tv_UserName_drawer);



        fragmentContainerView = (FragmentContainerView) findViewById(R.id.frame_layout);

        toolbar = findViewById(R.id.myToolbar);


        locationArrayList = new ArrayList<>();

        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_menu);
        navigationView.bringToFront();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        currentUser = auth.getCurrentUser();


        reference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(currentUser.getUid()).getValue(User.class);
                Prevalent.currentOnlineUser = user;
                userName.setText(Prevalent.currentOnlineUser.getFullName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();

        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Google_Map()).commit();
        }

    }


    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (ContextCompat.checkSelfPermission(dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //getLocation();

                checkSettingAndGetLocation();
            } else {
                requestLocationPermission();
            }
            mHandler.postDelayed(mHandlerTask, TEN_MINUTES);
        }
    };



    public LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Toast.makeText(dashboard.this, "Couldn't Found the location! PLease restat ", Toast.LENGTH_SHORT).show();
                return;
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    String currentTime = DateFormat.getDateTimeInstance().format(calendar.getTime());
                    String key = currentUser.getUid() + currentTime;
                    for (Location location : locationResult.getLocations()) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        LocationAddress locationAddress= new LocationAddress();
                        locationAddress.getAddressFromLocation(latitude,longitude,getApplicationContext(),new GeocoderHandler());;


                        /* Stored the location of user to database if he/she wants to volunteer and tested positive*/

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("User").child(currentUser.getUid()).exists() && snapshot.child("User").child(currentUser.getUid()).child("Covid Check").exists()) {
                                    userInput userInput = snapshot.child("User").child(currentUser.getUid()).child("Covid Check").getValue(userInput.class);
                                    if (userInput.getStatus().equals("Positive") && userInput.getVolunteer().equals("Yes")) {


                                        reference.child("Exposed Area").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot exposedArea : snapshot.getChildren()) {
                                                    latLang = exposedArea.getValue(LatLang.class);

                                                    if (latitude == latLang.getLatitude() && longitude == latLang.getLongitude()) {
                                                        checkIfLocationExist = true;
                                                        if(checkIfLocationExist){
                                                            temp2 =1;
                                                            System.out.println("YES LOCATION EXIST");
                                                        }

                                                    } else {
                                                        checkIfLocationExist = false;

                                                        System.out.println("NO LOCATION EXIST");
                                                    }


                                                    //Check if user current location (latitude,longitude) is near by exposedArea (getLatitude, getLongitude)

                                                    /*******************************************************************************


                                                     Push notification if the user is near by exposed area
                                                     Testing the function of notification by setting the latitude and longitude of current user and assumed latitude and longitude of exposed area
                                                     Negative latitude represents southern hemisphere and negative longitudes represents western hemisphere
                                                     *******************************************************************************/


                                                    distanceBetwnTwoLocation = new Distance_Lat_Lang(latitude, longitude, latLang.getLatitude(), latLang.getLongitude());
                                                    double dis = distanceBetwnTwoLocation.distance_Between_LatLong();
                                                    System.out.println("The distance between two location is: " + dis);

                                                    

                                                    if (dis <= 5.0 && dis != 0.0) {
                                                        System.out.println("EXPOSED AREA: TRUE");
                                                        System.out.println("EXPOSED AREA: "+dis);
                                                        temp =1;
                                                    } else {
                                                        System.out.println("EXPOSED AREA: FALSE");

                                                    }

                                                    //checkLessThanSixFitDistance = distanceBetwnTwoLocation.check_if_in_exposed_area();
                                                    //if(checkLessThanSixFitDistance){
                                                      //  temp =1;
                                                    //}

                                                }
                                                if (temp==1) {
                                                    callNotification();
                                                }
                                                if (temp2 != 1) {
                                                    reference.child("Exposed Area").child(key).child("latitude").setValue(latitude);
                                                    reference.child("Exposed Area").child(key).child("longitude").setValue(longitude);
                                                    reference.child("Exposed Area").child(key).child("time").setValue(currentTime);
                                                    reference.child("Exposed Area").child(key).child("Uid").setValue(currentUser.getUid());
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        System.out.println("Latitude: " + latitude + "," + "Longitude: " + longitude);

                    }

                }
            }, 3000);


        }
    };


    private void requestLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setMessage("Required location permission to use our app features.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);


                            }
                        })
                        .show()
                ;
                //isHandlerRunning = true;
            } else {
                ActivityCompat.requestPermissions(dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        } else {
            //checkSettingAndGetLocation();
            Toast.makeText(this, "Location is enabled.", Toast.LENGTH_SHORT).show();
        }

    }


    private void checkSettingAndGetLocation() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdate();

            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(dashboard.this, 1000);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });

    }

    private void startLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            Toast.makeText(this, "Permission Denied. Please allow the location!", Toast.LENGTH_SHORT).show();
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    @Override
    protected void onStart() {
        mHandlerTask.run();
        super.onStart();


    }

    @Override
    protected void onPause() {

        stopLocationUpdate();
        super.onPause();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                checkSettingAndGetLocation();

            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //App denied
                    new AlertDialog.Builder(this)
                            .setMessage("Location Services has permanently denied. Please go to your phone setting to enable this permission")
                            .setPositiveButton("             Go To Setting", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    applicationSetting();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .setCancelable(false)
                            .show()
                    ;

                } else {
                    Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void applicationSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getCallingActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new bottom_home()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {

                case R.id.bottom_menu_home:
                    selectedFragment = new bottom_home();
                    break;
                case R.id.bottom_menu_map:
                    selectedFragment = new Google_Map();
                    break;
                case R.id.bottom_menu_appointment:
                    selectedFragment = new Appointment();
                    break;
                case R.id.bottom_menu_covid_status:
                    selectedFragment = new Status();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new bottom_home()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Profile()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Message()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_news:
                Intent intent1 = new Intent(dashboard.this, News.class);
                startActivity(intent1);
                // getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_news()).commit();
                break;
            case R.id.menu_vaccine_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Vaccine_Review()).commit();
                break;
            case R.id.menu_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Help()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new About()).commit();
                break;
            case R.id.menu_logout:
                auth.signOut();
                Intent intent = new Intent(dashboard.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void callNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, Notification.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }



    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            getCity = locationAddress.split("Address:");
            System.out.println("CITY NAME IS :"+ getCity[1]);

            //System.out.println("MY ADDRESS IS "+ locationAddress);
        }
    }

    public String getCityName(){


        String temp_array[] = getCity[1].trim().split(" ");

        if(temp_array.length == 4){

            return temp_array[0];
        }
        else if(temp_array.length == 5){

            return temp_array[0]+" " +temp_array[1];
        }
        else{
            return "Lubbock";
        }
    }


}