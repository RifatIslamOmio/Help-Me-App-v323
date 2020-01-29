package com.example.helpme.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.helpme.Extras.Constants;
import com.example.helpme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener
{

    private GoogleMap mMap;

    private double callerLatitude, callerLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchLatLang();

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        notifyInternetState();

    }

    private void fetchLatLang() {

        Intent intent = getIntent();
        callerLatitude = intent.getDoubleExtra(Constants.MAP_LATITUDE_KEY,-34);
        callerLongitude = intent.getDoubleExtra(Constants.MAP_LONGITUDE_KEY,151);

    }

    private void notifyInternetState(){
        if(!Constants.isIsInternetEnabled(this))
            Toast.makeText(this.getApplicationContext(),
                    "active internet required to view map",
                    Toast.LENGTH_LONG)
            .show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

        // Add a marker in caller and move the camera
        LatLng callerLatLng = new LatLng(callerLatitude, callerLongitude);
        mMap.addMarker(new MarkerOptions().position(callerLatLng).title("caller position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(callerLatLng,18.5f));
    }


    @Override
    public boolean onMyLocationButtonClick() {

        String toastText = "";
        if(!Constants.isIsWifiEnabled(this) && !Constants.IS_LOCATION_ENABLED)
            toastText = "Turn On WiFi & Location";
        else if(!Constants.IS_LOCATION_ENABLED)
            toastText = "Turn On Location";
        else if(!Constants.isIsWifiEnabled(this))
            toastText = "Turn On WiFi";

        Toast.makeText(this, toastText + " to show your location", Toast.LENGTH_LONG).show();

        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

        if(location.getAccuracy()>150)
            Toast.makeText(this, "Location Accuracy is LOW"+location, Toast.LENGTH_LONG).show();

    }
}