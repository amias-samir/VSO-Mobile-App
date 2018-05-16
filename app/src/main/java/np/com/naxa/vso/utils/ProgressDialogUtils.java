package np.com.naxa.vso.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Majestic on 3/16/2018.
 */

public class ProgressDialogUtils {

    public ProgressDialog getProgressDialog(Context context, @NonNull String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle(msg);
        return dialog;
    }
}
