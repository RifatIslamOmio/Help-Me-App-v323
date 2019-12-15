package com.example.helpme.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.helpme.Extras.Permissions;
import com.example.helpme.Models.Photo;
import com.example.helpme.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.io.File;
import java.io.IOException;

public class PostActivity extends AppCompatActivity {

    private LocationsFetch locationsFetch;
    private AccurateLocationAsync accurateLocationAsync;

    public static boolean postClicked = false;

    private Photo photo;
    private static final String permissions[] = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_REQUEST_CODE = 337;

    private Permissions permissionObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        init();
    }

    private void init(){

        permissionObject = new Permissions(this, permissions, PERMISSIONS_REQUEST_CODE);

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
                    Toast.makeText(this,"Please Turn On Locations",Toast.LENGTH_LONG).show();
                }

                break;

            case Constants.REQUEST_TAKE_PHOTO:

                if(Activity.RESULT_OK == resultCode){
                    //called after photo is taken successfully

                    Log.d(Constants.PHOTO_LOG, "onActivityResult: camera open success?");
                    File checkfile = null;
                    try {
                        checkfile = photo.getCompressPhotoFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(checkfile==null)
                        Log.d(Constants.PHOTO_LOG, "onActivityResult: checkfile is null");
                }
                else if(Activity.RESULT_CANCELED == resultCode){
                    //called if user didn't take photo

                    Log.d(Constants.PHOTO_LOG, "onActivityResult: open camera intent failed");
                }

                break;

            default:
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case PERMISSIONS_REQUEST_CODE: {

                Log.d(Constants.PERMISSIONS_LOG, "PostActivity->onRequestPermissionsResult: case "+permissionObject.getPERMISSION_REQUEST_CODE()
                        +"permissions = "+permissions);

                permissionObject.resolvePermissions(permissions, grantResults,
                        getString(R.string.camera_permission)+"\n"
                        +getString(R.string.files_write_permission)
                        );

            }

        }
    }

    /**button click listeners*/

    public void takePhotoClick(View view) {

        if(!permissionObject.checkPermissions())
            permissionObject.askPermissions();

        else{

            try {
                photo = new Photo(this);
                photo.takePhoto(); //this method invokes onActivityResult

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open camera. Try again please.", Toast.LENGTH_LONG).show();
                Log.d(Constants.PHOTO_LOG, "takePhotoClick: camera open failed = "+e.getMessage());
            }

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
