package np.com.naxa.vso.home;

import android.content.Context;
import android.util.Pair;


import io.reactivex.Observable;

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
                assetName = "schools.geojson";
                break;
            default:
                assetName = "ward_changu.geojson";
                break;

        }

        return assetName;
    }


}
