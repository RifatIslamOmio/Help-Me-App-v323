package com.example.helpme.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.helpme.External_Models.ConnectNearby;
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
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMapLongClickListener
{

    private GoogleMap mMap;

    private double callerLatitude, callerLongitude;
    private boolean markerVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchExtras();

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        notifyState();

    }

    private void fetchExtras() {

        Intent intent = getIntent();
        callerLatitude = intent.getDoubleExtra(Constants.MAP_LATITUDE_KEY,23.8103);
        callerLongitude = intent.getDoubleExtra(Constants.MAP_LONGITUDE_KEY,90.4125);
        markerVisibility = intent.getBooleanExtra(Constants.MARKER_VISIBILITY_KEY, true);

    }

    private void notifyState(){
        if(!Constants.isIsInternetEnabled(this))
            Toast.makeText(this.getApplicationContext(),
                    "active internet required to view map",
                    Toast.LENGTH_LONG)
                    .show();

        if(!markerVisibility)
            Toast.makeText(this
                    , "Tap and hold to select location"
                    , Toast.LENGTH_LONG)
                    .show();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapLongClickListener(this);


        // Add a marker in caller and move the camera
        LatLng callerLatLng = new LatLng(callerLatitude, callerLongitude);
        mMap.addMarker(new MarkerOptions().position(callerLatLng).title("caller position").visible(markerVisibility));
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

        Toast.makeText(this
                , toastText + " to show your location"
                , Toast.LENGTH_LONG)
                .show();

        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

        if(location.getAccuracy()>150)
            Toast.makeText(this, "Location Accuracy is LOW"+location, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(!markerVisibility){ //activity started by "Pick A Location" button

            Log.d(Constants.PICK_LOCATION_LOG
                    , "onMapLongClick: setting up custom location = "+latLng);

            PostActivity.customLocationTaken = true;

            PostActivity.customLocation.setLatitude(latLng.latitude);
            PostActivity.customLocation.setLongitude(latLng.longitude);

            ConnectNearby.setLatlongStringToSend(latLng.latitude+" "+latLng.longitude);

            showConfirmDialog();

        }

        else { //activity started from receiver's end
            Toast.makeText(this
                    , "location pressed = " + latLng.toString()
                    , Toast.LENGTH_SHORT)
                    .show();

            Log.d(Constants.PICK_LOCATION_LOG, "onMapLongClick: long click detected!");
        }
    }

    private void showConfirmDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_dialog_msg)
                .setPositiveButton(R.string.confirm_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(Constants.PICK_LOCATION_LOG,
                                "onClick: closing map");
                        finish();
                    }
                })
                .setNegativeButton(R.string.confirm_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(MapsActivity.this
                                , "Tap and Hold to select location"
                                , Toast.LENGTH_LONG)
                                .show();
                    }
                });

        builder.create().show();
    }
}