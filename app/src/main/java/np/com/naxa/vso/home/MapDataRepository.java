package np.com.naxa.vso.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.RawRes;
import android.util.Pair;


import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;

public class MapDataRepository extends RawAssetLoader {

    private Context context;


    public MapDataRepository() {
        context = VSO.getInstance().getApplicationContext();
    }


    public Observable<Pair> getGeoJsonString(int pos) {
        String assetName = getMapAssetName(pos);
        return loadTextFromAsset(assetName);
    }



    public String getMapAssetName(int pos) {
        String assetName;
        switch (pos) {
            case 0:
                assetName = "health_facilities.geojson";
                break;
            case 1:
                assetName = "open_space.geojson";
                break;
            case 2:
                assetName = "educational_institutes.geojson";
                break;
            default:
                assetName = "ward_changu.geojson";
                break;

        }

        return assetName;
    }


}
