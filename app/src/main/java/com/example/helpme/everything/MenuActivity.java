package com.example.helpme.everything;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helpme.Activities.PostActivity;
import com.example.helpme.Externals.ConnectNearby;
import com.example.helpme.Externals.LocationsFetch;
import com.example.helpme.Extras.Constants;
import com.example.helpme.Extras.Notifications;
import com.example.helpme.Extras.Permissions;
import com.example.helpme.R;
import com.google.firebase.auth.FirebaseAuth;


public class MenuActivity extends AppCompatActivity {


    private static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,

            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSIONS_REQUEST_CODE = 336;

    private Permissions permissionObject;


    Button logout;
    Button seek_help;
    Button help_feed;
    Button profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        startService(new Intent(getApplicationContext(),MyService.class));

        promptPermissions();

        init();

        logout = findViewById(R.id.logout_btn);
        seek_help = findViewById(R.id.btn_seek_help);
        help_feed = findViewById(R.id.btn_help_feed);
        profile = findViewById(R.id.btn_profile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                stopService(new Intent(getApplicationContext(),MyService.class));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();


            }
        });


        help_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HelpFeedActivity.class));
            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
            }
        });


    }

    private void init() {

        Notifications.createNotificationChannel(this);

        if(Constants.IS_SENDER)
            Constants.IS_SENDER = false;

        ConnectNearby.username = "test_username"; //username for advertising //TODO: set unique(MUST!!!) username
        ConnectNearby.menuActivity = this;

    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectNearby.clearRequestedPeerList(); //clear out requested peer list
        if(Constants.IS_DISCOVERING)
            ConnectNearby.stopDiscovery();

        turnOnNearby();

        //Log.d(Constants.NEARBY_LOG, "ManuActivity onResume: start advertising");
        //ConnectNearby.startAdvertising();
        Constants.IS_SENDER = false; //start as receiver, advertising to be found
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Log.d(Constants.NEARBY_LOG, "MenuActivity onPause: stopping advertising");
        //ConnectNearby.stopAdvertising();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        turnOffNearby();
    }

    /**Permission methods*/
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

                Log.d(Constants.PERMISSIONS_LOG, "MainActivity->onRequestPermissionsResult: case "+permissionObject.getPERMISSION_REQUEST_CODE());

                permissionObject.resolvePermissions(permissions, grantResults,
                        getString(R.string.location_permission)+"\n"
                                +getString(R.string.wifi_permission)+"\n"
                                +getString(R.string.internet_permission)+"\n"
                                +getString(R.string.files_write_permission)
                );

                return;
            }

            //TODO: add other cases when/if needed


        }

    }

    public void seekHelpClick(View view) {

        if(Constants.isIsWifiEnabled(this) && new LocationsFetch(this).isLocationEnabledLM()) {

            turnOffNearby();

            Constants.IS_SENDER = true;

            //ConnectNearby.stopAdvertising();
            //Log.d(Constants.NEARBY_LOG, "helpClick: stop advertising");

            //start PostActivity
            Intent intent = new Intent(getApplicationContext(), PostActivity.class);

            intent.putExtra("handle later", true);

            startActivity(intent);
        }

        else{
            //show dialog let user turn on location & wifi or alternatively input Custom Location

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.l_w_title)
                    .setMessage(R.string.location_wifi_access)
                    .setCancelable(false)
                    .setPositiveButton(R.string.location_wifi_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //turn on wifi and ask to turn on location

                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            wifiManager.setWifiEnabled(true);

                            if(!wifiManager.isWifiEnabled())
                                Toast.makeText(getApplicationContext(), "Enable Location & WiFi Please", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "Enable Location Please", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton(R.string.custom_location, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO: add cutom location input functionality
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert =  builder.create();
            alert.show();

        }

    }

    private void turnOnNearby(){

        if(!Constants.IS_ADVERTISING) {
            Log.d(Constants.NEARBY_LOG, "turnOnNearby: start advertising");
            ConnectNearby.startAdvertising();
        }
    }

    private void turnOffNearby(){

        if(Constants.IS_ADVERTISING) {
            Log.d(Constants.NEARBY_LOG, "turnOffNearby: stop advertising");
            ConnectNearby.stopAdvertising();
        }

    }
}
