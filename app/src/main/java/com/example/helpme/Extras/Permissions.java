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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permissions{


    private Activity activity;

    private String[] appPermissions;
    /* = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET
    };*/

    private int PERMISSION_REQUEST_CODE;

    private List<String> permissionsRequired = new ArrayList<>();

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
                this.permissionsRequired.add(permission);
                Log.d(Constants.PERMISSIONS_LOG, "checkPermissions: "+permission+" not granted");
            }
        }

        if(!this.permissionsRequired.isEmpty())
            return false;

        Log.d(Constants.PERMISSIONS_LOG, "checkPermissions: all permissions granted hurrah!");
        return true;

    }

    public void askPermissions(){
        //ask for permission initially

        ActivityCompat.requestPermissions(
                activity,
                this.permissionsRequired.toArray(new String[this.permissionsRequired.size()]),
                PERMISSION_REQUEST_CODE
        );
    }

    public void resolvePermissions(String[] permissions, int[] grantResults, String alertBoxMessage){

        HashMap<String, Integer> permissionResult = new HashMap<>();

        for (int i = 0; i < grantResults.length; i++) {
            //get the still not allowed permissions

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionResult.put(permissions[i], grantResults[i]);
                Log.d(Constants.PERMISSIONS_LOG, "resolvePermissions: denied permission = "+permissions[i]+" grant result = "+grantResults[i]);
            }
        }

        if (!permissionResult.isEmpty()) {

            Log.d(Constants.PERMISSIONS_LOG, "resolvePermissions: alert box message = " + alertBoxMessage);

            for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                //request permission one by one with proper explanation
                String permission = entry.getKey();
                int resultCode = entry.getValue();
                Log.d(Constants.PERMISSIONS_LOG, "resolvePermissions: permission = " + permission + " result code = " + resultCode);

                if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, permission)) {
                    //user denied collective permission once but hasn't picked never allow

                    Permissions.this.alertDialog(

                            alertBoxMessage,

                            //positive listener
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Log.d(Constants.PERMISSIONS_LOG, "onClick: dialog.dismiss() called");

                                    Permissions.this.checkPermissions();
                                    Permissions.this.askPermissions(); /**check*/
                                }
                            },

                            //negative listener
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Permissions.this.activity.finish();
                                }
                            }

                    );

                } else if(resultCode == PackageManager.PERMISSION_DENIED){
                    //user has picked never allow

                    Log.d(Constants.PERMISSIONS_LOG, "resolvePermissions: never allow disos kerreee!!!!!!");
                    //TODO: show user dialog box then prompt user to go to settings and allow
                }

            }
        }

        else {
            Log.d(Constants.PERMISSIONS_LOG, "resolvePermissions: All permissions granted");

            this.permissionsRequired.clear();
        }
    }



    private void alertDialog(String message, DialogInterface.OnClickListener positiveListener,
                            DialogInterface.OnClickListener negativeListener)
    {
        //TODO:common alert box
        Log.d(Constants.PERMISSIONS_LOG, "alertDialog: creating alert-box");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());

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
