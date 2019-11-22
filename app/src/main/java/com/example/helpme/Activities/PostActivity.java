package com.example.helpme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //ConnectNearby.startDiscovery();
        //Log.d(Constants.NEARBY_LOG, "PostActivity onCreate: discovery started");

        ConnectNearby.postActivity = this; //set the new activity
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

        Log.d(Constants.NEARBY_LOG, "PostActivity onPause: stop discovery");
        ConnectNearby.stopDiscovery();
    }
}
