package com.example.helpme.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Extras.Constants;
import com.example.helpme.R;

public class ReceiverEndPostActivity extends AppCompatActivity {

    private TextView testText; //TODO: remove this
    private ImageView postImage;

    private String photoFilePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_end_post);

        Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "onCreate: ReceiverEndPostActivity started");

        init();

    }

    private void init() {

        testText = findViewById(R.id.testText);
        testText.setText(getIntent().getStringExtra(Constants.RECEIVED_MESSAGE_KEY)
                + "\n\nlocation: "+getIntent().getStringExtra(Constants.RECEIVED_LOCATION_KEY));

        postImage = findViewById(R.id.postImageView);
        String photoPath = getIntent().getStringExtra(Constants.RECEIVED_PHOTO_PATH_KEY);
        Bitmap imageBitmap;
        if(!photoPath.equals("null")) {

            Log.d(Constants.RECEIVER_END_POST_ACTIVITY, "init: photo received = "+photoPath);

            imageBitmap = BitmapFactory.decodeFile("/storage/emulated/0/Download/Nearby/" + "temp_image.jpg");
            postImage.setImageBitmap(imageBitmap);
        }

        if(Constants.IS_ADVERTISING)
            ConnectNearby.stopAdvertising();
    }
}
