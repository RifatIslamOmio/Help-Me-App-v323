package com.example.helpme.Externals;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.helpme.Activities.PostActivity;
import com.example.helpme.Extras.Constants;

public class AccurateLocationAsync extends AsyncTask <LocationsFetch,Integer, String> {

    private PostActivity postActivity;
    private int count;
    private boolean progressNeeded = false;

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
            Toast demo = Toast.makeText(postActivity, "please wait and press again getting accurate location.", Toast.LENGTH_LONG);

            progressNeeded = true;
        }
    }

    @Override
    protected void onPostExecute(String latlong) {
        Log.d(Constants.LOCATION_LOG, "onPostExecute: latlong = "+latlong);

        if(progressNeeded) {
            Toast demo = Toast.makeText(postActivity, "ready to send POST now", Toast.LENGTH_LONG);
        }

        ConnectNearby.setLatlongStringToSend(latlong);
        postActivity.post.setLatlong(latlong);
    }

    @Override
    protected void onCancelled(String latlong) {

    }
}
