package com.example.helpme.Extras;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public abstract class Constants {

    /**Global Variables*/
    public static boolean IS_SENDER = false;
    public static boolean IS_CONNECTED = false;
    public static boolean IS_ADVERTISING = false;
    public static boolean IS_DISCOVERING = false;

    public static boolean IS_LOCATION_ENABLED = false;

    private static boolean IS_WIFI_ENABLED = false;
    private static boolean IS_INTERNET_ENABLED = false;

    public static boolean isIsWifiEnabled(Activity activity) {

        WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IS_WIFI_ENABLED =  wifi.isWifiEnabled();

        Log.d("connectivity_debug", "isIsWifiEnabled: wifi status = "+IS_WIFI_ENABLED);

        return IS_WIFI_ENABLED;
    }

    public static boolean isIsInternetEnabled(Activity activity) {

        ConnectivityManager internet = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(internet.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                internet.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            IS_INTERNET_ENABLED = true;
        else
            IS_INTERNET_ENABLED = false;

        Log.d("connectivity_debug", "isIsWifiEnabled: wifi status = "+IS_INTERNET_ENABLED);

        return IS_INTERNET_ENABLED;

    }

    /***/

    /**Log Tags*/
    public static final String PERMISSIONS_LOG = "permissions_debug";
    public static final String WIFI_LOG = "wifi_debug";
    public static final String NEARBY_LOG = "nearby_debug";
    public static final String RECEIVER_END_POST_ACTIVITY = "receiverend_activity";
    public static final String LOCATION_LOG = "location_debug";
    public static final String PHOTO_LOG = "photo_debug";
    public static final String NEW_VIEW_LOG = "view_debug";

    public static final String DB_LOG = "database_debug";

    public static final String ADDRESS_LOG = "address_debug";

    public static final String NOTIFICATION_LOG = "notification_debug";
    /***/

    /**Intent Tags*/
    public static final String RECEIVED_MESSAGE_KEY = "received_message";
    public static final String RECEIVED_PHOTO_PATH_KEY = "received_photo_path";
    public static final String RECEIVED_LOCATION_KEY = "received_location";
    public static final String MAP_LATITUDE_KEY = "location_latitude";
    public static final String MAP_LONGITUDE_KEY = "location_longitude";
    /***/

    /**Result Codes*/
    public static final int LOCATION_CHECK_CODE = 100;
    public static final int REQUEST_TAKE_PHOTO = 101;
    /***/

    /**geo coding intent tags*/
    public static final String POST_GEO_LOCATION = "location_tofetch";
    public static final String GEO_POST_RECEIVER = "postA_fetchC_receiver";
    public static final String GEO_POST_ADDRESS = "address_toSubmit";

    public static final int GEO_SUCCESS = 91;
    public static final int GEO_FAILURE = 97;
    /**/

    /*Notification Channel ID */
    public static final String CHANNEL_ID = "notsch";
    public static final int NOTIFICATION_ID = 256;
    /**/
}
