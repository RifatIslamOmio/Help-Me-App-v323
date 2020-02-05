package com.example.helpme.External_Models;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.helpme.Extras.Constants;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationsFetch {

    private Activity activity;
    public FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private Location location, bestLocation;
    private double bestLocationAccuracy = 99999; //used only in getLastLocation()
    private boolean isLocationAccurate;
    private boolean bestLocationTaken;
    private boolean isUpdating;
    private boolean isLocationEnabledLM;
    private boolean onLocationResultWorks;

    private static final int ACCURATE_LOCATION_THRESHOLD = 30;


    /**constructor for PostActivity*/
    public LocationsFetch(Activity activity) {
        this.activity = activity;

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        this.locationRequest = LocationRequest.create();
        locationRequest.setInterval(2500);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        this.locationCallback = new LocationCallback() {

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Log.d(Constants.LOCATION_LOG, "onLocationAvailability: test");
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if(!LocationsFetch.this.onLocationResultWorks) //check if callback runs (doesn't get called on all devices -_-)
                    LocationsFetch.this.onLocationResultWorks = true;

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


                    if(location.getAccuracy() < LocationsFetch.ACCURATE_LOCATION_THRESHOLD) {
                        LocationsFetch.this.location = location;
                        LocationsFetch.this.isLocationAccurate = true;

                        Log.d(Constants.LOCATION_LOG, "onLocationResult: taken location latlong = "
                                +location.getLatitude()+", "+location.getLongitude());

                        //new
                        LocationsFetch.this.stopLocationUpdates();

                    }

                    prevLocation = location;
                }
            }
        };

        this.isLocationAccurate = false;
        this.onLocationResultWorks = false;
        this.isUpdating = false;
        this.isLocationEnabledLM = false;
        this.bestLocationTaken = false;
    }


    public Location getLocation() {

        if(onLocationResultWorks)
            return location;

        else{ //onLocationResult() callback doesn't work. repeat the process with getLastLocation() here.

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if(location!=null){

                                if(location.getAccuracy() < LocationsFetch.ACCURATE_LOCATION_THRESHOLD) {
                                    LocationsFetch.this.isLocationAccurate = true;
                                    LocationsFetch.this.location = location;

                                    Log.d(Constants.LOCATION_LOG, "getLastLocation->onSuccess: accurate location collected.");
                                }

                                else if( location.getAccuracy()<LocationsFetch.this.bestLocationAccuracy ){
                                    LocationsFetch.this.bestLocation = location;
                                    LocationsFetch.this.bestLocationAccuracy = location.getAccuracy();

                                    Log.d(Constants.LOCATION_LOG, "getLastLocation->onSuccess: "+LocationsFetch.this.bestLocationAccuracy);
                                }

                            }

                            else{
                                Log.d(Constants.LOCATION_LOG, "getLastLocation->onSuccess: location is null");
                            }

                        }
                    })
                    .addOnFailureListener(activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(Constants.LOCATION_LOG, "onFailure: getLastLocation failed. error message = "+e.getMessage());
                        }
                    });

            return location;
        }
    }

    public Location getBestLocation() {
        return bestLocation;
    }

    public boolean isLocationAccurate() {
        return isLocationAccurate;
    }

    public boolean isOnLocationResultWorks() {
        return onLocationResultWorks;
    }

    public boolean isBestLocationTaken() { return bestLocationTaken; }

    public boolean isLocationEnabledLM(){
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        isLocationEnabledLM = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d(Constants.LOCATION_LOG, "isLocationEnabledLM: "+isLocationEnabledLM);

        return isLocationEnabledLM;
    }



    public void setBestLocationTaken(boolean bestLocationTaken) {
        this.bestLocationTaken = bestLocationTaken;
    }



    public void checkDeviceLocationSettings() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Constants.IS_LOCATION_ENABLED = true;
                Log.d(Constants.LOCATION_LOG, "onSuccess: location update requested");
                LocationsFetch.this.startLocationUpdates();
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Constants.IS_LOCATION_ENABLED = false;

                if(e instanceof ResolvableApiException){
                    try{

                        ResolvableApiException resolvable = (ResolvableApiException) e;

                        //TODO: override onActivityResult() from calling Activity
                        resolvable.startResolutionForResult(LocationsFetch.this.activity,
                                Constants.LOCATION_CHECK_CODE); //runs onActivityResult() callback

                    }catch (IntentSender.SendIntentException sendEx){
                        //ignore
                        Log.d(Constants.LOCATION_LOG, "onFailure: ignore?");
                    }
                }
            }
        });

    }

    public void startLocationUpdates() {

        if(!isUpdating) {
            isUpdating = true;
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
        else
            Log.d(Constants.LOCATION_LOG, "startLocationUpdates: already updating location");
    }

    public void stopLocationUpdates(){

        if(isUpdating) {
            Log.d(Constants.LOCATION_LOG, "stopLocationUpdates: stop location updates");

            LocationsFetch.this.fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            isUpdating = false;
        }

        else
            Log.d(Constants.LOCATION_LOG, "stopLocationUpdates: location update already stopped");
    }


}