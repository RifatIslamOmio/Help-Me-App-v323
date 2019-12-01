package com.example.helpme.Externals;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.helpme.Activities.MainActivity;
import com.example.helpme.Activities.PostActivity;
import com.example.helpme.Activities.ReceiverEndPostActivity;
import com.example.helpme.Extras.Constants;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class ConnectNearby {

    public static String username;
    public static MainActivity mainActivity;
    public static PostActivity postActivity;

    private static final Strategy strategy = Strategy.P2P_POINT_TO_POINT;
    private static final String SERVICE_ID = "com.example.helpme"; //TODO: come up with unique SERVICE_ID for App
    private static final AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(strategy).build();

    private static List<String> requestedPeerEPIs = new ArrayList<String>();

    private static class ReceiveDataPayloadListener extends PayloadCallback  {

        private String receivedMessage;

        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            //TODO: receive text,file (open audio stream?)

            // This always gets the full data of the payload. Will be null if it's not a BYTES
            // payload. You can check the payload type with payload.getType().

            //testing by receiving byte
            receivedMessage = new String(payload.asBytes(), StandardCharsets.UTF_8);
            Toast debug;
            debug = Toast.makeText(mainActivity, receivedMessage, Toast.LENGTH_LONG);
            debug.setGravity(Gravity.CENTER,0,0);
            debug.show();

            //start ReceiverEndPostActivity
            Intent intent = new Intent(mainActivity, ReceiverEndPostActivity.class);
            intent.putExtra(Constants.RECEIVED_STRING, receivedMessage);
            mainActivity.startActivity(intent);
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
            Log.d(Constants.NEARBY_LOG, "onPayloadTransferUpdate: disconnecting endpoint and starting new activity");

            //disconnecting from sender endpoint
            Nearby.getConnectionsClient(ConnectNearby.mainActivity)
                    .disconnectFromEndpoint(endpointId);
        }
    }

    private static ReceiveDataPayloadListener payloadListener = new ReceiveDataPayloadListener();


    private static final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {

                    Log.d(Constants.NEARBY_LOG, "onConnectionInitiated: endPointId = "+endpointId+
                                                     " endPointName = "+connectionInfo.getEndpointName());

                    // Automatically accept the connection on both sides.
                    if(!Constants.IS_SENDER) {

                        Log.d(Constants.NEARBY_LOG, "onConnectionInitiated: this is the receiver play alert sound here");

                        //receiver is at MainActivity

                        //TODO: show permissions dialog to connect and play sound
                        Nearby.getConnectionsClient(ConnectNearby.mainActivity) /**check context*/
                                .acceptConnection(endpointId, payloadListener); //TODO: define payLoadCallback
                    }
                    else {
                        //sender at PostActivity
                        Nearby.getConnectionsClient(ConnectNearby.postActivity) /**check context*/
                                .acceptConnection(endpointId, payloadListener); //TODO: define payLoadCallback
                    }
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {

                    switch (result.getStatus().getStatusCode()) {

                        case ConnectionsStatusCodes.STATUS_OK:
                            // We're connected! Can now start sending and receiving data.
                            if(Constants.IS_SENDER) {
                                ConnectNearby.displayToast("Sender connected to: " + endpointId, postActivity);

                                Payload bytesPayload = Payload.fromBytes(new byte[] {97, 98, 99, 100}); //what the hell is this?
                                Nearby.getConnectionsClient(postActivity).sendPayload(endpointId, bytesPayload);

                                Byte bt[] = {97, 98, 99, 100};
                                Log.d(Constants.NEARBY_LOG, "onConnectionResult: send byte = "+bt);

                                //disconnect from receiver immediately after sending
                                //Nearby.getConnectionsClient(postActivity).disconnectFromEndpoint(endpointId);

                            }
                            else
                                ConnectNearby.displayToast("Receiver connected to: "+endpointId, mainActivity);

                            Log.d(Constants.NEARBY_LOG, "onConnectionResult: connected to = "+endpointId);
                            break;

                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            // The connection was rejected by one or both sides.
                            Log.d(Constants.NEARBY_LOG, "onConnectionResult: connected rejected by = "+endpointId);
                            break;

                        case ConnectionsStatusCodes.STATUS_ERROR:
                            // The connection broke before it was able to be accepted.
                            Log.d(Constants.NEARBY_LOG, "onConnectionResult: connection broke!");
                            break;

                        default:
                            // Unknown status code
                            Log.d(Constants.NEARBY_LOG, "onConnectionResult: UNKNOWN ERROR???");

                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    // We've been disconnected from this endpoint. No more data can be
                    // sent or received.

                    Log.d(Constants.NEARBY_LOG, "onDisconnected: disconnected from = "+endpointId);
                }
            };


    public static void startAdvertising() {

        Nearby.getConnectionsClient(mainActivity.getApplicationContext())
                .startAdvertising(
                        ConnectNearby.username, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
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

                                Log.d(Constants.NEARBY_LOG, "onFailure: advertising failed see error stack trace"+e.getMessage());
                            }
                        });

        Constants.IS_ADVERTISING = true; //keep track of advertising
    }

    public static void stopAdvertising(){
        Log.d(Constants.NEARBY_LOG, "stopAdvertising(listener): stopping advertising");

        Nearby.getConnectionsClient(mainActivity.getApplicationContext())
                .stopAdvertising();

        Constants.IS_ADVERTISING = false; //keep track of advertising
    }


    private static final DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(strategy).build();

    //discover devices that are advertising
    private static final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    // An endpoint was found. We request a connection to it.

                    final String decoyEndpointId = endpointId;

                    Log.d(Constants.NEARBY_LOG,
                            "onEndpointFound: endPointId = "+endpointId +
                                 " info = "+info.getEndpointName()+"(username?), "+info.getServiceId());

                    if(requestedPeerEPIs.indexOf(endpointId)==-1) { //only connect to new endpoints.

                        Nearby.getConnectionsClient(ConnectNearby.postActivity) //check context
                                .requestConnection(ConnectNearby.username, endpointId, connectionLifecycleCallback)
                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // We successfully requested a connection. Now both sides
                                                // must accept before the connection is established.

                                                Log.d(Constants.NEARBY_LOG, "requestConncetion onSuccess: success!");

                                                //requestedPeerEPIs.add(decoyEndpointId); //Here?
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Nearby Connections failed to request the connection.

                                                Log.d(Constants.NEARBY_LOG, "requestConnection onFailure: discovery failed see error stack trace"
                                                        + e.getMessage());
                                            }
                                        });

                        requestedPeerEPIs.add(endpointId); //or here?
                    }

                }

                @Override
                public void onEndpointLost(String endpointId) {
                    // A previously discovered endpoint has gone away.

                    Log.d(Constants.NEARBY_LOG, "onEndpointLost: lost "+endpointId);
                }
            };

    public static void startDiscovery() {

        Nearby.getConnectionsClient(postActivity.getApplicationContext())
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

                                Log.d(Constants.NEARBY_LOG, "onFailure: discovery failed see error stack trace"+e.getMessage());
                            }
                        });


        Constants.IS_DISCOVERING = true; //keep track of discovery state
    }

    public static void stopDiscovery(){
        Nearby.getConnectionsClient(postActivity.getApplicationContext())
                .stopDiscovery();

        Constants.IS_DISCOVERING = false; //keep track of discovery state
    }

    private static void displayToast(String text, Activity activity){
        Toast.makeText(activity,text,Toast.LENGTH_LONG).show();
    }


    public static void clearRequestedPeerList(){
        if(!requestedPeerEPIs.isEmpty())
            requestedPeerEPIs.clear();
    }

}
