package com.example.helpme.Extras;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.helpme.R;

import java.util.ArrayList;
import java.util.List;

public class Permissions{


    Activity activity;

    private String[] appPermissions;
    /* = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET
    };*/

    private int PERMISSION_REQUEST_CODE;

    private static final List<String> permissionsRequired = new ArrayList<>();

    public Permissions(Activity activity, String[] appPermissions, int PERMISSION_REQUEST_CODE) {
        this.activity = activity;
        this.appPermissions = appPermissions;
        this.PERMISSION_REQUEST_CODE = PERMISSION_REQUEST_CODE;
    }

    public int getPERMISSION_REQUEST_CODE() {
        return PERMISSION_REQUEST_CODE;
    }

    public boolean checkPermissions(){

        //get required permissions into permissionsRequired List<>
        for(String permission: appPermissions){
            if(ContextCompat.checkSelfPermission(this.activity.getApplicationContext(),permission) == PackageManager.PERMISSION_DENIED){
                permissionsRequired.add(permission);
                Log.d(Constants.PERMISSIONS_LOG, "checkPermissions: "+permission+" not granted");
            }
        }

        if(!permissionsRequired.isEmpty())
            return false;

        Log.d(Constants.PERMISSIONS_LOG, "checkPermissions: all permissions granted hurrah!");
        return true;

    }

    public void askPermissions(){
        //ask for permission initially

        ActivityCompat.requestPermissions(
                activity,
                permissionsRequired.toArray(new String[permissionsRequired.size()]),
                PERMISSION_REQUEST_CODE
        );
    }


    public void alertDialog(String message, DialogInterface.OnClickListener positiveListener,
                            DialogInterface.OnClickListener negativeListener)
    {
        //TODO:common alert box
        Log.d(Constants.PERMISSIONS_LOG, "alertDialog: creating alert-box");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle((R.string.dialogbox_title))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton( (R.string.dialogbox_positive) , positiveListener)
                .setNegativeButton( (R.string.dialogbox_negative) , negativeListener)
        ;


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
