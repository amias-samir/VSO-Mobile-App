package np.com.naxa.vso.sudur.model;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by user on 5/31/2017.
 */

public class JsonDownloadService {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;


    public static final String MyPREFERENCES = "dev_activities";
    public static final String MyPREFERENCES1 = "fwdc_json";

    SharedPreferences sharedpreferences;
    SharedPreferences sharedpreferences1;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;

    String jsonToSend = null;




}
