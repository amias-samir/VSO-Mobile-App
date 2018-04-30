package np.com.naxa.vso.utils;

import android.app.Activity;

import com.andrognito.flashbar.Flashbar;

public final class ToastUtils {

    public static Flashbar infoAlert(Activity context, String msg) {
        return new Flashbar.Builder(context)
                .gravity(Flashbar.Gravity.TOP)
                .message(
                        "Flashbar is shown at the top. You can also have more than one line in "
                                + "the flashbar. The bar will dynamically adjust its size.")
                .build();

    }
}
