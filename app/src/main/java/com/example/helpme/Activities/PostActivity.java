package com.example.helpme.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpme.Externals.AccurateLocationAsync;
import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Externals.LocationsFetch;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;

public class PostActivity extends AppCompatActivity {

    private LocationsFetch locationsFetch;
    private AccurateLocationAsync accurateLocationAsync;

    public TextView locationText, countDownText; //TODO: remove these

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //TODO: remove below initialization
        locationText = findViewById(R.id.locationText);
        countDownText = findViewById(R.id.countDownText);
    }

    private void init(){

        ConnectNearby.postActivity = this; //set the new activity

        locationsFetch = new LocationsFetch(this);
        locationsFetch.checkDeviceLocationSettings();
        locationsFetch.startLocationUpdates();

        accurateLocationAsync = new AccurateLocationAsync(this);
        accurateLocationAsync.execute(locationsFetch);
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
                else {
                    Toast.makeText(this,"Please Turn On Locations",Toast.LENGTH_LONG).show();
                    Constants.IS_LOCATION_ENABLED = false;

                    Log.d(Constants.LOCATION_LOG, "onActivityResult: location is turned off");
                }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: start discovery on button press not here
        Log.d(Constants.NEARBY_LOG, "PostActivity onResume: start discovery");
        ConnectNearby.startDiscovery();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(Constants.IS_DISCOVERING) {
            Log.d(Constants.NEARBY_LOG, "PostActivity onPause: stop discovery");
            ConnectNearby.stopDiscovery();
        }
    }
}
