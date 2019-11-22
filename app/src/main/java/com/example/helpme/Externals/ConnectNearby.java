package com.example.helpme.Externals;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.helpme.Extras.Constants;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ConnectNearby {

    private static final Strategy strategy = Strategy.P2P_POINT_TO_POINT;
    private static final String SERVICE_ID = "com.example.helpme"; //TODO: come up with unique SERVICE_ID for App


    private static final AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(strategy).build();

    //TODO: define
    private static final  ConnectionLifecycleCallback connectionLifecycleCallback = null;

    public static void startAdvertising(Activity activity, String myUsername) {

        Nearby.getConnectionsClient(activity.getApplicationContext())
                .startAdvertising(
                        myUsername, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // We're advertising!

                                Log.d(Constants.NEARBY_LOG, "onSuccess: advertising success");

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // We were unable to start advertising.

                                Log.d(Constants.NEARBY_LOG, "onFailure: advertising failed");
                                e.printStackTrace();
                            }
                        });
    }

    public static void stopAdvertising(Activity activity){
        Nearby.getConnectionsClient(activity.getApplicationContext())
                .stopAdvertising();
    }


    private static final DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(strategy).build();

    //TODO: define
    private static final EndpointDiscoveryCallback endpointDiscoveryCallback = null;

    public static void startDiscovery(Activity activity) {

        Nearby.getConnectionsClient(activity.getApplicationContext())
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // We're discovering!

                                Log.d(Constants.NEARBY_LOG, "onSuccess: discovery success");

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // We're unable to start discovering.

                                Log.d(Constants.NEARBY_LOG, "onFailure: discovery failed");
                                e.printStackTrace();
                            }
                        });

    }

    public static void stopDiscovery(Activity activity){
        Nearby.getConnectionsClient(activity.getApplicationContext())
                .stopDiscovery();
    }



}
