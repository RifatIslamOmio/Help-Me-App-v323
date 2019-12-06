package com.example.helpme.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpme.Externals.AccurateLocationAsync;
import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Externals.LocationsFetch;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class PostActivity extends AppCompatActivity {

    private LocationsFetch locationsFetch;
    private AccurateLocationAsync accurateLocationAsync;

    public static boolean postClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        init();
    }

    private void init(){

        ConnectNearby.postActivity = this; //set the new activity

        locationsFetch = new LocationsFetch(this);
        locationsFetch.checkDeviceLocationSettings();

        accurateLocationAsync = new AccurateLocationAsync(this);

        //Log.d(Constants.LOCATION_LOG, "onResume: start aync task");
        accurateLocationAsync.execute(locationsFetch);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //locationsFetch.startLocationUpdates();
        //Log.d(Constants.LOCATION_LOG, "PostActivity onResume: location request initiated");

        /*if(onResumeCount==0) {
            Log.d(Constants.LOCATION_LOG, "onResume: start aync task");
            accurateLocationAsync.execute(locationsFetch);
        }
        onResumeCount++;*/

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.IS_DISCOVERING) {
            Log.d(Constants.NEARBY_LOG, "PostActivity onPause: stop discovery");
            ConnectNearby.stopDiscovery();
        }

        accurateLocationAsync.cancel(true);
        locationsFetch.stopLocationUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case Constants.LOCATION_CHECK_CODE:
                if (Activity.RESULT_OK == resultCode) {
                    Constants.IS_LOCATION_ENABLED = true;

                    Log.d(Constants.LOCATION_LOG, "onActivityResult: location enabled");
                }

                else if(Activity.RESULT_CANCELED == resultCode){
                    Log.d(Constants.LOCATION_LOG, "onActivityResult: user picked no");
                    Constants.IS_LOCATION_ENABLED = false;

                    //TODO: show dialog and open settings for manual location enabling
                }

                else {

                    //TODO: show dialog and open settings for manual location enabling
                    Toast.makeText(this,"Please Turn On Locations",Toast.LENGTH_LONG).show();
                    Constants.IS_LOCATION_ENABLED = false;

                    Log.d(Constants.LOCATION_LOG, "onActivityResult: location is turned off");
                }

                break;

            default:
                break;

        }
    }

    public void postClick(View view) {

        if(!postClicked) //TODO: use in AccurateLocationAsync class. show progress dialog only after postClick
            postClicked = true;

        Log.d(Constants.NEARBY_LOG, "postClick: start discovery");
        ConnectNearby.startDiscovery();

        //TODO: start new activity and delete this activity from stack to avoid calling startDiscovery() multiple times
    }
}
