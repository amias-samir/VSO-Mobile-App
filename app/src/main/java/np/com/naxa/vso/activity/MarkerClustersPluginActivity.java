package np.com.naxa.vso.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterManagerPlugin;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;
import np.com.naxa.vso.home.MapDataRepository;

/**
 * Use the marker cluster plugin to automatically add colored circles with numbers, so that a user knows
 * how many markers are in a certain area at a higher zoom level.
 */
public class MarkerClustersPluginActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private ClusterManagerPlugin<MyItem> clusterManagerPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_marker_clusters_plugin);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        setupMapBox();

    }

    private void setupMapBox() {

        mapView.getMapAsync(mapboxMap -> {
            MarkerClustersPluginActivity.this.mapboxMap = mapboxMap;
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(27.657531140175244, 85.46161651611328), 10.8), 2800);
            // Initializing the cluster plugin
            clusterManagerPlugin = new ClusterManagerPlugin<>(MarkerClustersPluginActivity.this, mapboxMap);
            initCameraListener();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    protected void initCameraListener() {
        mapboxMap.addOnCameraIdleListener(clusterManagerPlugin);
        showOverlayOnMap(1);
    }


    /**
     * Custom class for use by the marker cluster plugin
     */
    public static class MyItem implements ClusterItem {
        private final LatLng position;
        private String title;
        private String snippet;

        public MyItem(double lat, double lng) {
            position = new LatLng(lat, lng);
            title = null;
            snippet = null;
        }

        public MyItem(double lat, double lng, String title, String snippet) {
            position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSnippet() {
            return snippet;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }
    }


    private void loadMarkersFromGeoJson(String assetName, String geojson) {
        mapboxMap.addOnCameraIdleListener(clusterManagerPlugin);

        Observable<MyItem> observable = Observable.create(e -> {
            try {
                FeatureCollection featureCollection = FeatureCollection.fromJson(geojson);
                List<Feature> features = featureCollection.getFeatures();
                for (Feature feature : features) {
                    if (feature.getGeometry() instanceof Point) {
                        Position coordinates = (Position)
                                feature.getGeometry().getCoordinates();
                        e.onNext(new MyItem(coordinates.getLatitude(), coordinates.getLongitude()));
                    }
                }
            } catch (Exception exception) {
                e.onError(exception);
            } finally {
                e.onComplete();
            }
        });

        observable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MyItem markerItem) {
                        clusterManagerPlugin.addItem(markerItem);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void showOverlayOnMap(int position) {

        new MapDataRepository().getGeoJsonString(position)
                .subscribe(new DisposableObserver<Pair>() {
                    @Override
                    public void onNext(Pair pair) {
                        String assetName = (String) pair.first;
                        String fileContent = (String) pair.second;
                        loadMarkersFromGeoJson(assetName, fileContent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MarkerClustersPluginActivity.this, "An error occurred while loading geojson", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}