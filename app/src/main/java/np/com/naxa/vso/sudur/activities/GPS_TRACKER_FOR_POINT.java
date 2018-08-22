package np.com.naxa.vso.sudur.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by ramaan on 3/15/2016.
 */
public class GPS_TRACKER_FOR_POINT extends Service implements LocationListener {

        ProgressDialog mProgressDlg;
        String latti , longi ;
        public static boolean GPS_POINT_INITILIZED ;

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPS_TRACKER_FOR_POINT(Context context) {
        this.mContext = context;
        showInitilizationDialog();
        getLocation();
    }

    public void showInitilizationDialog(){
        GPS_POINT_INITILIZED = false;
        SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        latti = wmbPreference.getString("LATTI","" );
        longi = wmbPreference.getString("LONGI", "");

        Log.e("CoordinatesFirst", "" + latti + " new lat " + " longi " + longi);

    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.e("GPSTRACKER", "NO network provider  is enabled");
            } else {
                Log.e("GPSTRACKER", "Must return true network provider  is enabled");
                this.canGetLocation = true;

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = (float) location.getLatitude();
                                longitude = (float) location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
//    public void stopUsingGPS() {
//        if (locationManager != null) {
//            locationManager.removeUpdates(GpsTracker.this);
//        }
//    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = (float) location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = (float) location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Use Location?");

        //Setting Diaglog Message
        alertDialog.setMessage("This app wants to change your device settings:" +
        "\n\nUse GPS, Wi-Fi and mobile network for high accuracy location." +
                "\n\nEnable your GPS using SETTINGS BUTTON");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }

        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /// ufortunate stop
    @Override
    public void onLocationChanged(Location location2) throws NullPointerException {
        try{
            if((float)location.getLatitude()!= (float)location2.getLatitude() || (float)location.getLongitude()!=(float)location2.getLongitude()) {
                Log.e("Coordinates", "" + (float) location.getLatitude() + " lat  " + (float) location2.getLatitude() + "   " + (float) location.getLongitude() + " long  " + (float) location2.getLongitude());
                GPS_POINT_INITILIZED = true ;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager = null ;
    }
}
