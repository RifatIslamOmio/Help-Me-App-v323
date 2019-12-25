package com.example.helpme.Externals;

import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.helpme.Activities.PostActivity;
import com.example.helpme.Extras.Constants;

public class AccurateLocationAsync extends AsyncTask <LocationsFetch,Integer, String> {

    private PostActivity postActivity;
    private int count;
    private boolean progressWasNeeded = false, asyncLocationDone= false;
    public boolean isAsyncLocationDone() { return asyncLocationDone; }

    public AccurateLocationAsync(PostActivity postActivity) {
        this.postActivity = postActivity;
        this.count = 10;
    }

    @Override
    protected String doInBackground(LocationsFetch... locationsFetches) {
        Log.d(Constants.LOCATION_LOG, "doInBackground: ");

        //halt till location is enabled
        while (!Constants.IS_LOCATION_ENABLED){
            if(isCancelled()){
                return "location update canceled";
            }

            if(locationsFetches[0].isLocationEnabledLM()) {
                Constants.IS_LOCATION_ENABLED = true;
                break;
            }
        }

        while (!locationsFetches[0].isLocationAccurate()){

            if(isCancelled()){
                return "location update canceled";
            }

            try {
                Thread.sleep(1000);

                if(!locationsFetches[0].isOnLocationResultWorks()) { //location request callback not working

                    Log.d(Constants.LOCATION_LOG, "doInBackground: location request callback didn't work");

                    locationsFetches[0].getLocation();
                    if(locationsFetches[0].isLocationAccurate())
                        break;
                }

                this.count--;
                publishProgress(this.count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(this.count<=0) {
                Log.d(Constants.LOCATION_LOG, "doInBackground: best location taken");

                locationsFetches[0].setBestLocationTaken(true);

                locationsFetches[0].stopLocationUpdates();
                return locationsFetches[0].getBestLocation().getLatitude() + " " + locationsFetches[0].getBestLocation().getLongitude();
            }
        }

        Log.d(Constants.LOCATION_LOG, "doInBackground: location with proper accuracy found!");
        locationsFetches[0].stopLocationUpdates();
        return locationsFetches[0].getLocation().getLatitude()+" "+locationsFetches[0].getLocation().getLongitude();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(Constants.LOCATION_LOG, "onProgressUpdate: progress at="+values[0]);

        if(PostActivity.postClicked) {
            //TODO: show progress UI only after post click
            Toast demo = Toast.makeText(postActivity, "please wait and press again getting accurate location.", Toast.LENGTH_SHORT);
            demo.show();

            progressWasNeeded = true;
        }
    }

    @Override
    protected void onPostExecute(String latlong) {
        Log.d(Constants.LOCATION_LOG, "onPostExecute: latlong = "+latlong);

        asyncLocationDone = true;

        if(progressWasNeeded) {
            Toast demo = Toast.makeText(postActivity, "ready to send POST now", Toast.LENGTH_LONG);
            demo.show();
        }

        //if(postActivity)
        ConnectNearby.setLatlongStringToSend(latlong);
        postActivity.helpPost.setLatlong(latlong);

        Log.d(Constants.LOCATION_LOG, "onPostExecute: "+postActivity.getLocationsFetch().isLocationAccurate());
        //starting reverse geo here. CHECKKK!
        if(postActivity.getLocationsFetch().isLocationAccurate()) {

            if(Geocoder.isPresent())
                postActivity.startAddressFetchService();
            else{
                Log.d(Constants.ADDRESS_LOG, "onPostExecute: geocoder not available");
            }

        }

        else
            Log.d(Constants.ADDRESS_LOG, "location not accurate?");

    }

    @Override
    protected void onCancelled(String latlong) {

    }
}
