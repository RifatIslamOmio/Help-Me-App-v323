package com.example.helpme.Externals;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.helpme.Extras.Constants;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class LocationsFetch {

    private Activity activity;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private Location location, bestLocation;
    private boolean isLocationAccurate;

    public LocationsFetch(Activity activity) {
        this.activity = activity;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d(Constants.LOCATION_LOG, "onLocationResult: location = null");
                    return;
                }

                Location prevLocation=null;
                for (Location location : locationResult.getLocations()) {
                    Log.d(Constants.LOCATION_LOG, "onLocationResult: new location accuracy="+location.getAccuracy());

                    if(prevLocation==null) //first iteration
                        LocationsFetch.this.bestLocation = location;
                    else if(location.getAccuracy()<prevLocation.getAccuracy())
                        LocationsFetch.this.bestLocation = location;


                    if(location.getAccuracy()<50) {
                        LocationsFetch.this.location = location;
                        LocationsFetch.this.isLocationAccurate = true;

                        Log.d(Constants.LOCATION_LOG, "onLocationResult: taken location latlong = "
                                +location.getLatitude()+", "+location.getLongitude());

                        //check this out
                        Log.d(Constants.LOCATION_LOG, "onLocationResult: stop location updates");
                        LocationsFetch.this.fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }

                    prevLocation = location;
                }
            }
        };


        this.isLocationAccurate = false;
    }

    public Location getLocation() {
        return location;
    }

    public Location getBestLocation() {
        return bestLocation;
    }

    public boolean isLocationAccurate() {
        return isLocationAccurate;
    }

    public void setLocationAccurate(boolean locationAccurate) {
        isLocationAccurate = locationAccurate;
    }

    public void checkDeviceLocationSettings() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    try{

                        ResolvableApiException resolvable = (ResolvableApiException) e;

                        //TODO: override onActivityResult() from calling Activity
                        resolvable.startResolutionForResult(LocationsFetch.this.activity,
                                Constants.LOCATION_CHECK_CODE); //runs onActivityResult() callback

                    }catch (IntentSender.SendIntentException sendEx){
                        //ignore
                    }
                }
            }
        });

    }

    public void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }


}
