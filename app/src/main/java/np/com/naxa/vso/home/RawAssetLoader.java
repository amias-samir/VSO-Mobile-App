package np.com.naxa.vso.home;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;

public class RawAssetLoader {

    private Pair<String, String> rawResourceToInputStream(String assetName) throws IOException {
        InputStream jsonStream = VSO.getInstance().getAssets().open(assetName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return Pair.create(assetName, sb.toString());

    }

    protected Observable<Pair> loadTextFromAsset(String assetName) {
        return Observable.create(e -> {
            try {
                e.onNext(rawResourceToInputStream(assetName));
            } catch (Exception exception) {
                e.onError(exception);
            } finally {
                e.onComplete();
            }
        });
    }


}
