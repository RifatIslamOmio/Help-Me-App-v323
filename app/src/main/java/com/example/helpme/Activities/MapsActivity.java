package com.example.helpme.Activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private double latitude, longitude;

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
        latitude = intent.getDoubleExtra(Constants.MAP_LATITUDE_KEY,-34);
        longitude = intent.getDoubleExtra(Constants.MAP_LONGITUDE_KEY,151);

    }

    private void notifyInternetState(){
        if(Constants.isIsInternetEnabled(this))
            Toast.makeText(this.getApplicationContext(),
                    "active internet required to view map",
                    Toast.LENGTH_LONG)
            .show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in caller and move the camera
        LatLng callerLatLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(callerLatLng).title("caller position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(callerLatLng,18.0f));
    }
}
