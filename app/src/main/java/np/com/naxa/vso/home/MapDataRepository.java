//package np.com.naxa.vso.home;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.RawRes;
//
//import org.osmdroid.bonuspack.kml.KmlDocument;
//import org.osmdroid.bonuspack.kml.KmlFeature;
//import org.osmdroid.bonuspack.kml.Style;
//import org.osmdroid.views.MapView;
//import org.osmdroid.views.overlay.FolderOverlay;
//
//import java.io.InputStream;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//import np.com.naxa.vso.R;
//
//public class MapDataRepository {
//
//    private Context context;
//
//    public MapDataRepository() {
//        context = VSO.getInstance().getApplicationContext();
//    }
//
//    public Observable<FolderOverlay> getMunicipalityBorder(MapView mapView) {
//        return loadGeoJSON(R.raw.changunarayan_boundary, mapView)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());
//    }
//
//    public Observable<FolderOverlay> getHealthServicesLayer(MapView mapView) {
//        return loadGeoJSON(R.raw.health_facilities, mapView)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());
//
//    }
//
//    public Observable<FolderOverlay> getSchoolsLayer(MapView mapView) {
//        return loadGeoJSON(R.raw.educational_institutes, mapView)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());
//
//    }
//
//    public Observable<FolderOverlay> getOpenSpacesLayer(MapView mapView) {
//        return loadGeoJSON(R.raw.open_space, mapView)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());
//
//    }
//
//    private Observable<FolderOverlay> loadGeoJSON(@RawRes final int resource, final MapView mapView) {
//        return Observable.create(new ObservableOnSubscribe<FolderOverlay>() {
//            @Override
//            public void subscribe(ObservableEmitter<FolderOverlay> emitter) throws Exception {
//                String jsonString = null;
//
//                try {
//                    InputStream jsonStream = context.getResources().openRawResource(resource);
//                    int size = jsonStream.available();
//                    byte[] buffer = new byte[size];
//                    jsonStream.read(buffer);
//                    jsonStream.close();
//                    jsonString = new String(buffer, "UTF-8");
//
//                    final KmlDocument kmlDocument = new KmlDocument();
//                    kmlDocument.parseGeoJSON(jsonString);
//
//                    KmlFeature.Styler styler = new KMLStyler();
//
//                    Drawable defaultMarker = context.getResources().getDrawable(R.drawable.marker_default);
//                    Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
//                    final Style defaultStyle = new Style(defaultBitmap, Color.BLUE, 5f, 0x20AA1010);
//                    FolderOverlay myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
//
//                    emitter.onNext(myOverLay);
//                } catch (Exception e) {
//                    emitter.onError(e);
//                }
//
//                emitter.onComplete();
//            }
//        });
//    }
//
//
//}
