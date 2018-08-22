package np.com.naxa.vso.sudur.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by user1 on 9/11/2015.
 */

public class ConnectonDetector {

    private Context _context;

    public ConnectonDetector(Context context){
        this._context = context;
    }

    public boolean isConnectedToInternet() {

        SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(_context);
        boolean setData = wmbPreference.getBoolean("SET_MOBILE_DATA_ON", true);

        if (setData){
            boolean b = isConnectedToWifi();
            boolean data = isConnectedToData();
        if (b) {
            return true;
        } else {

            if (data) {
                return true;
//                textView.setText("Connected to DAta");
            } else {
                return false;
//                textView.setText("No Internet Access");
            }

        }
    }else{
            boolean b = isConnectedToWifi();
            if (b) {
                return true;
            }
        }

        return false;
    }

    public boolean isConnectedToWifi () {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

        if (netInfo != null && netInfo.isConnected()) {
//        if(isWiFi){
            return true;
        }
        return false;
    }
    public boolean isConnectedToData () {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo;
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ( wifiInfo != null && wifiInfo.isConnected()) {
            return  true;
        }
        return false;
    }
}
