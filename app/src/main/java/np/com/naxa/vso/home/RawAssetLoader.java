package np.com.naxa.vso.home;

import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RawAssetLoader {

    private String rawResourceToInputStream(@RawRes int resId) throws IOException {
        InputStream jsonStream = VSO.getInstance().getResources().openRawResource(resId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("n");
        }
        reader.close();
        return sb.toString();
    }

    protected Observable<String> loadTextFromRaw(@RawRes int resId) {
        return Observable.create(e -> {
            try {
                e.onNext(rawResourceToInputStream(resId));
            } catch (Exception exception) {
                e.onError(exception);
            } finally {
                e.onComplete();
            }
        });
    }


}
