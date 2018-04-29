package np.com.naxa.vso.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.RawRes;


import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;

public class MapDataRepository  {

    private Context context;


    public MapDataRepository() {
        context = VSO.getInstance().getApplicationContext();
    }

    public String getMapAssetName(int pos) {
        String assetName;
        switch (pos) {
            case 1:
                assetName = "health_facilities.geojson";
                break;
            case 2:
                assetName = "open_space.geojson";
                break;
            case 3:
                assetName = "educational_institutes.geojson";
                break;
            default:
                assetName = "ward_changu.geojson";
                break;

        }

        return assetName;
    }



}
