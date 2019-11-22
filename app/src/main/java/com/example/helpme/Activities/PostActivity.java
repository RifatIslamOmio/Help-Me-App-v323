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

        ConnectNearby.startDiscovery(this);
        Log.d(Constants.NEARBY_LOG, "PostActivity onCreate: discovery started");

        //TODO: stop this discovery properly
    }
}
