package com.example.helpme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Extras.Constants;
import com.example.helpme.Extras.Permissions;
import com.example.helpme.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button helpButton;

    private String permissions[] = {
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.CHANGE_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    };
    private static final int PERMISSIONS_REQUEST_CODE = 336;

    private Permissions permissionObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptPermissions();

        init();

        //ConnectNearby.startAdvertising(); //start advertising instantly
        //Constants.IS_SENDER = false; //start as receiver, advertising to be found

    }

    private void init() {

        helpButton = findViewById(R.id.helpButton);

        ConnectNearby.username = "test_username"; //username for advertising //TODO: set unique(MUST!!!) username
        ConnectNearby.mainActivity = this;

    }

    private void promptPermissions() {

        permissionObject = new Permissions(this, permissions,PERMISSIONS_REQUEST_CODE);

        if( !permissionObject.checkPermissions() ){
            permissionObject.askPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE: {

                Log.d(Constants.PERMISSIONS_LOG, "onRequestPermissionsResult: case "+permissionObject.getPERMISSION_REQUEST_CODE());

                HashMap<String, Integer> permissionResult = new HashMap<>();

                for (int i = 0; i < grantResults.length; i++) {
                    //get the still not allowed permissions

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        permissionResult.put(permissions[i], grantResults[i]);
                        Log.d(Constants.PERMISSIONS_LOG, "onRequestPermissionsResult: denied permission = "+permissions[i]+" grant result = "+grantResults[i]);
                    }
                }

                if (!permissionResult.isEmpty()) {

                    String alertBoxMessage = getString(R.string.location_permission)+"\n"
                            +getString(R.string.wifi_permission)+"\n"
                            +getString(R.string.internet_permission);
                    Log.d(Constants.PERMISSIONS_LOG, "onRequestPermissionsResult: alert box message = "+alertBoxMessage);

                    for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                        //request permission one by one with proper explanation
                        String permission = entry.getKey();
                        int resultCode = entry.getValue();
                        Log.d(Constants.PERMISSIONS_LOG, "onRequestPermissionsResult: permission = "+permission+" result code = "+resultCode);

                        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                            //user denied collective permission once but hasn't picked never allow

                            permissionObject.alertDialog(

                                    alertBoxMessage,

                                    //positive listener
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            permissionObject.checkPermissions();
                                            permissionObject.askPermissions(); /**check*/
                                        }
                                    },

                                    //negative listener
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    }

                            );

                        }
                        else {
                            //user has picked never allow

                            Log.d(Constants.PERMISSIONS_LOG, "onRequestPermissionsResult: never allow disos kerreee!!!!!!");
                            //TODO: show user dialog box then go to settings to allow
                        }

                    }

                }

                else
                    Log.d(Constants.PERMISSIONS_LOG, "onRequestPermissionsResult: All permissions granted");

                return;
            }

            //TODO: add other cases when/if needed


        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Constants.NEARBY_LOG, "MainActivity onResume: start advertising");
        ConnectNearby.startAdvertising();
        Constants.IS_SENDER = false; //start as receiver, advertising to be found
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(Constants.NEARBY_LOG, "MainActivity onPause: stopping advertising");
        ConnectNearby.stopAdvertising();
    }

    public void helpClick(View view){

        Constants.IS_SENDER = true;

        ConnectNearby.stopAdvertising();
        Log.d(Constants.NEARBY_LOG, "helpClick: stop advertising");

        //start PostActivity
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);

    }
}
