package com.example.helpme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;

public class ReceiverEndPostActivity extends AppCompatActivity {

    private TextView testText; //TODO: remove this
    private ImageView postImage;
    private Button showMapBtn;

    private String message, location, photoFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_end_post);

        Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "onCreate: ReceiverEndPostActivity started");

        init();

    }

    private void init() {

        message = getIntent().getStringExtra(Constants.RECEIVED_MESSAGE_KEY);
        location = getIntent().getStringExtra(Constants.RECEIVED_LOCATION_KEY);

        testText = findViewById(R.id.testText);
        testText.setText(message
                + "\n\nlocation: "+location);

        postImage = findViewById(R.id.postImageView);
        String photoPath = getIntent().getStringExtra(Constants.RECEIVED_PHOTO_PATH_KEY);
        Bitmap imageBitmap;
        if(!photoPath.equals("null")) {

            Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "init: photo received = "+photoPath);

            imageBitmap = BitmapFactory.decodeFile("/storage/emulated/0/Download/Nearby/" + "temp_image.jpg");
            postImage.setImageBitmap(imageBitmap);
        }

        showMapBtn = findViewById(R.id.show_map_button); //why???

        if(Constants.IS_ADVERTISING)
            ConnectNearby.stopAdvertising();
    }

    public void showMapClicked(View view) {

        String latlang[] = location.split(" ");
        double latitude = Double.parseDouble(latlang[0]), longitude = Double.parseDouble(latlang[1]);

        Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "showMapClicked: latitude = "+latitude+" longitude = "+longitude);

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(Constants.MAP_LATITUDE_KEY, latitude);
        intent.putExtra(Constants.MAP_LONGITUDE_KEY, longitude);
        startActivity(intent);

    }
}
