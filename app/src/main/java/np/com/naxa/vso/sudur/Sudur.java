package np.com.naxa.vso.sudur;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by nishon on 7/23/17.
 */

public class Sudur extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Sudur.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Sudur.context;
    }
}
