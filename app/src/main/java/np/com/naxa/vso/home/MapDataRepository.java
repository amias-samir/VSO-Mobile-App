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

public class MapDataRepository extends RawAssetLoader {

    private Context context;


    public MapDataRepository() {
        context = VSO.getInstance().getApplicationContext();
    }

    public Observable<String> getMapLayerObserver(int pos) {
        Observable<String> observable;

        switch (pos) {
            case 1:
                observable = getHealthServicesLayer();
                break;
            case 2:
                observable = getOpenSpacesLayer();
                break;
            case 3:
                observable = getSchoolsLayer();
                break;
            default:
                observable = getMunicipalityBorder();
                break;
        }
        return observable;
    }

    private Observable<String> getMunicipalityBorder() {
        return loadTextFromRaw(R.raw.changunarayan_boundary)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    private Observable<String> getHealthServicesLayer() {
        return loadTextFromRaw(R.raw.health_facilities)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

    private Observable<String> getSchoolsLayer() {
        return loadTextFromRaw(R.raw.educational_institutes)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

    private Observable<String> getOpenSpacesLayer() {
        return loadTextFromRaw(R.raw.open_space)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

}
