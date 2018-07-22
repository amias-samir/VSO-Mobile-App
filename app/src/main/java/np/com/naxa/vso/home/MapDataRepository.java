package np.com.naxa.vso.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;


import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MapDataRepository extends RawAssetLoader {

    private Context context;


    public MapDataRepository() {
        context = VSO.getInstance().getApplicationContext();
    }


    public Observable<Pair> getGeoJsonString(int pos) {
        String assetName = getMapAssetName(pos);
        return loadTextFromAsset(assetName);
    }

    public Observable<Pair> getGeoJsonString(@NonNull String assetName) {
        return loadTextFromAsset(assetName);
    }

    private String getMapAssetName(int pos) {
        String assetName;
        switch (pos) {
            case 0:
                assetName = "health_facilities.geojson";
                break;
            case 1:
                assetName = "open_space.geojson";
                break;
            case 2:
                assetName = "schools.geojson";
                break;
            default:
                assetName = "ward_changu.geojson";
                break;

        }

        return assetName;
    }



    private Observable<String> geoJsonPropertiesParser(FeatureCollection collection) {

        return Observable.just(collection.getFeatures())
                .flatMapIterable((Function<List<Feature>, Iterable<Feature>>) features -> features)
                .flatMap(new Function<Feature, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Feature feature) throws Exception {
                        return Observable.just(feature.getProperties().toString());

                    }
                });
    }

}
