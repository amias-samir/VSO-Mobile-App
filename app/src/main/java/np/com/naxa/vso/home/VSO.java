package np.com.naxa.vso.home;

import android.app.Application;
import android.content.Context;

public class VSO extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getInstance() {
        return context;
    }
}
