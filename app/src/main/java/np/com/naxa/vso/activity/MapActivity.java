package np.com.naxa.vso.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import np.com.naxa.vso.OverlayPopupHiddenStyler;
import np.com.naxa.vso.R;
import np.com.naxa.vso.home.VSO;
import np.com.naxa.vso.osm_convertor.multipolygon.Feature;
import np.com.naxa.vso.osm_convertor.multipolygon.FeatureCollection;
import np.com.naxa.vso.osm_convertor.multipolygon.Properties;
import np.com.naxa.vso.osm_convertor.polygon.Properties_;
import np.com.naxa.vso.utils.DialogFactory;
import np.com.naxa.vso.utils.ToastUtils;
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

            loadGeoJSON("wards.geojson").subscribe(loadStringSubscriber());
        } else {
            EasyPermissions.requestPermissions(this, "Some rationale",
                    REQUEST_WRITE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private Observer<? super KmlDocument> loadStringSubscriber() {
        return new Observer<KmlDocument>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(KmlDocument kmlDocument) {
                ToastUtils.showToast("GeoJSON string read :)");
                putKmlInMap(map, kmlDocument);


            }

            @Override
            public void onError(Throwable e) {
                DialogFactory.createGenericErrorDialog(MapActivity.this, e.getMessage()).show();
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


    public Observable<KmlDocument> loadGeoJSON(String fileName) {

        return Observable.create(new ObservableOnSubscribe<KmlDocument>() {
            @Override
            public void subscribe(ObservableEmitter<KmlDocument> e) throws Exception {
                try {
                    String str = readStringFromAsset(fileName);

                    boolean isMultipolygon = str.contains("Multipolygon");


                    if (isMultipolygon) {
                        FeatureCollection featureCollection = new Gson().fromJson(str, FeatureCollection.class);
                        mapMultilinePolygonToPolygon(featureCollection.getFeatures(), e);
                    } else {
                        e.onNext(generateOSMKML(fileName));
                    }


                    e.onComplete();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    e.onError(ex);
                }
            }
        });


    }

    private KmlDocument generateOSMKML(String fileName) throws IOException {
        InputStream jsonStream = null;
        jsonStream = VSO.getInstance().getAssets().open(fileName);

        int size = jsonStream.available();
        byte[] buffer = new byte[size];
        jsonStream.read(buffer);
        jsonStream.close();
        String jsonString = new String(buffer, "UTF-8");

        KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(jsonString);

        return kmlDocument;
    }

    private void mapMultilinePolygonToPolygon(List<Feature> features, ObservableEmitter<KmlDocument> e) {


        for (Feature feature : features) {
            String type = feature.getGeometry().getType();
            Properties props = feature.getProperties();

            switch (type) {
                case "MultiPolygon":
                    for (int i = 0; i < feature.getGeometry().getCoordinates().size(); i++) {
                        np.com.naxa.vso.osm_convertor.polygon.Feature feature1 = new np.com.naxa.vso.osm_convertor.polygon.Feature();

                        np.com.naxa.vso.osm_convertor.polygon.Geometry geometry = new np.com.naxa.vso.osm_convertor.polygon.Geometry();
                        geometry.setCoordinates(feature.getGeometry().getCoordinates().get(i));
                        geometry.setType("Polygon");

                        feature1.setType("Feature");
                        feature1.setGeometry(geometry);


                        Properties_ p = new Properties_();
                        p.setAreaSqm(11);

                        feature1.setProperties(p);

                        List<np.com.naxa.vso.osm_convertor.polygon.Feature> featureList = new ArrayList<>();
                        featureList.add(feature1);


                        np.com.naxa.vso.osm_convertor.polygon.FeatureCollection featureCollection1 = new np.com.naxa.vso.osm_convertor.polygon.FeatureCollection();

                        featureCollection1.setType("FeatureCollection");
                        featureCollection1.setName("Wards");
                        featureCollection1.setFeatures(featureList);

                        String polygonStr = new Gson().toJson(featureCollection1);


                        KmlDocument kmlDocument = new KmlDocument();
                        boolean isSucess = kmlDocument.parseGeoJSON(polygonStr);


                        if (!isSucess) {
                            throw new RuntimeException("Failed to parse geojson");
                        }

                        e.onNext(kmlDocument);
                    }

                    break;


            }
        }
    }

    private String readStringFromAsset(String fileName) throws IOException {

        InputStream jsonStream = VSO.getInstance().getAssets().open(fileName);
        int size = jsonStream.available();
        byte[] buffer = new byte[size];
        jsonStream.read(buffer);
        jsonStream.close();
        String jsonString = new String(buffer, "UTF-8");

        return jsonString;
    }


    public void putKmlInMap(@NonNull MapView map, KmlDocument kmlDocument) {
        KmlFeature.Styler style = new OverlayPopupHiddenStyler();
        FolderOverlay overlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, style, kmlDocument);
        map.getOverlays().add(overlay);
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


}
