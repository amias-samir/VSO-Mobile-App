package np.com.naxa.vso.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import np.com.naxa.vso.OverlayPopupHiddenStyler;
import np.com.naxa.vso.R;
import np.com.naxa.vso.home.VSO;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends AppCompatActivity {


    private final int REQUEST_WRITE_PERMISSION = 101;
    @BindView(R.id.map)
    MapView map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        handleOSMLoadPermisssion();

    }

    @AfterPermissionGranted(REQUEST_WRITE_PERMISSION)
    public void handleOSMLoadPermisssion() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadOSM();
            setupMapUI();
            loadGeoJSON("").subscribe(loadGeoJSONSubscriber());
        } else {
            EasyPermissions.requestPermissions(this, "Some rationale",
                    REQUEST_WRITE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private Observer<? super KmlDocument> loadGeoJSONSubscriber() {
        return new Observer<KmlDocument>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(KmlDocument kmlDocument) {
                putKmlInMap(map, kmlDocument);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void loadOSM() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map.setTileSource(TileSourceFactory.MAPNIK);
    }

    private void setupMapUI() {
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint centerPoint = new GeoPoint(27.657531140175244, 85.46161651611328);
        mapController.setCenter(centerPoint);
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    public void putKmlInMap(@NonNull MapView map, KmlDocument kmlDocument) {
        KmlFeature.Styler styler = new OverlayPopupHiddenStyler();
        FolderOverlay overlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, styler, kmlDocument);
        map.getOverlays().add(overlay);
    }


    public io.reactivex.Observable<KmlDocument> loadGeoJSON(String fileName) {

        return io.reactivex.Observable.create(e -> {
            try {
                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_municipality_boundary.geojson");
//                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_boundary.geojson");
//                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_new_wards.geojson");
                int size = jsonStream.available();
                byte[] buffer = new byte[size];
                jsonStream.read(buffer);
                jsonStream.close();
                String jsonString = new String(buffer, "UTF-8");

                KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(jsonString);

                e.onNext(kmlDocument);
                e.onComplete();
            } catch (IOException ex) {
                ex.printStackTrace();
                e.onError(ex);
            }

        });


    }
}
