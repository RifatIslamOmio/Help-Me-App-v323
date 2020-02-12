package com.example.helpme.External_Models;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.helpme.Extras.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

    protected ResultReceiver receiver;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * class name Used to name the worker thread, important only for debugging.
     */

    public FetchAddressIntentService() {
        super(FetchAddressIntentService.class.getName());
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(Constants.ADDRESS_LOG, "onHandleIntent: inside FetchAIS class");

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if (intent == null) {
            return;
        }
        String errorMessage = "";

        // Get the location and receiver passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                Constants.POST_GEO_LOCATION);

        //get the receiver from PostActivity
        receiver = intent.getParcelableExtra(Constants.GEO_POST_RECEIVER);

        List<Address> addresses = null;

        try {

            Log.d(Constants.ADDRESS_LOG, "onHandleIntent: latlong - "+location.getLatitude()+" "+location.getLongitude());

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // get just a single address.
                    1);

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service not available";
            Log.e(Constants.ADDRESS_LOG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid lat_long used";
            Log.e(Constants.ADDRESS_LOG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (!errorMessage.isEmpty()) {
                errorMessage = "tap here to view address";
                Log.d(Constants.ADDRESS_LOG, errorMessage);
            }
            deliverResultToReceiver(Constants.GEO_FAILURE, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the UI thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            Log.d(Constants.ADDRESS_LOG, "address found");

            deliverResultToReceiver(Constants.GEO_SUCCESS,
                    TextUtils.join(", ", addressFragments));
        }

    }

    private void deliverResultToReceiver(int resultCode, String message) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.GEO_POST_ADDRESS, message);

        receiver.send(resultCode, bundle);

    }

}
