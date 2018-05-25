package np.com.naxa.vso.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import np.com.naxa.vso.home.VSO;

public class NetworkUtils {
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) VSO.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
