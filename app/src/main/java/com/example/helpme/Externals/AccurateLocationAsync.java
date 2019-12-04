package com.example.helpme.Externals;

import android.os.AsyncTask;

import com.example.helpme.Activities.PostActivity;

public class AccurateLocationAsync extends AsyncTask <LocationsFetch,Integer, String> {

    private PostActivity postActivity;
    public int count;

    public AccurateLocationAsync(PostActivity postActivity) {
        this.postActivity = postActivity;
        this.count = 0;
    }

    @Override
    protected String doInBackground(LocationsFetch... locationsFetches) {

        while (!locationsFetches[0].isLocationAccurate()){
            try {
                Thread.sleep(1000);
                this.count++;
                publishProgress(this.count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(this.count>10)
                return locationsFetches[0].getBestLocation().getLatitude()+" "+locationsFetches[0].getBestLocation().getLongitude();
        }

        return locationsFetches[0].getLocation().getLatitude()+" "+locationsFetches[0].getLocation().getLongitude();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        postActivity.countDownText.setText(values[0]);
    }

    @Override
    protected void onPostExecute(String latlong) {
        postActivity.locationText.setText(latlong);
    }
}
