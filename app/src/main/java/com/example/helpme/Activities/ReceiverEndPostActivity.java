package com.example.helpme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;

public class ReceiverEndPostActivity extends AppCompatActivity {

    private TextView testText; //TODO: remove this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_end_post);

        Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "onCreate: ReceiverEndPostActivity started");

        init();

    }

    private void init() {

        testText = findViewById(R.id.testText);
        testText.setText(getIntent().getStringExtra(Constants.RECEIVED_STRING_KEY));

        if(Constants.IS_ADVERTISING)
            ConnectNearby.stopAdvertising();
    }
}
