package com.example.covidcare;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Google_Map extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    ArrayList<LatLng> locationArrayList = new ArrayList<LatLng>();
    DatabaseReference reference;
    FirebaseDatabase database;
    LatLang latLang;
    LatLng latLng;
    LatLng userCurrentLocation;
    double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Exposed Area");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        latLang = new LatLang();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getContext(), "Please Enable the location first", Toast.LENGTH_SHORT).show();
        }


        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                   latitude = location.getLatitude();
                   longitude = location.getLongitude();
                    //System.out.println(latitude +"  dadada "+longitude);
                }
                userCurrentLocation = new LatLng(latitude,longitude);
                mMap.addMarker(new MarkerOptions().position(userCurrentLocation)
                        .title("Current Location"));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(userCurrentLocation)      // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });






        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                   int i = 0;
                    for (DataSnapshot exposedLocation : snapshot.getChildren()) {
                        latLang = exposedLocation.getValue(LatLang.class);
                        latLng = new LatLng(latLang.getLatitude(), latLang.getLongitude());
                        locationArrayList.add(latLng);
                        //System.out.println("+++++++"+locationArrayList.get(i));
                        //latLngArrayList.add(latLng);

                        mMap.addMarker(new MarkerOptions()
                                .position(locationArrayList.get(i))
                                .icon(BitmapFromVector(getContext(),R.drawable.covidsign))
                                .title(locationArrayList.get(i).latitude +"  ,  "+ locationArrayList.get(i).longitude)
                        );
                        i++;
                        System.out.println("SIZE OF ARRAY: "+ locationArrayList.size());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng userCurrentLocation = new LatLng(dashboard.getLatitude(),dashboard.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


    }



    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }






}