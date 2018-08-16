package np.com.naxa.vso.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.mapbox.mapboxsdk.style.sources.VectorSource;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.vso.R;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class MapBoxActivity extends AppCompatActivity {

    MapView mapView;
    @BindView(R.id.btnosm)
    Button btnosm;
    @BindView(R.id.btnmuseam)
    Button btnmuseam;
    private MapboxMap mapboxMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.mapbox_activity);
        ButterKnife.bind(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
//               renderVectorSource(mapboxMap);
                renderWithOSMSource(mapboxMap);

            }
        });

    }


    @OnClick({R.id.btnmuseam,R.id.btnosm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnmuseam:
                renderVectorSource(mapboxMap);
                break;
            default:
                renderWithOSMSource(mapboxMap);
                break;
        }
    }
    public void renderVectorSource(MapboxMap mapboxMap) {

        VectorSource vectorSource = new VectorSource("terrain-data", "mapbox://mapbox.mapbox-terrain-v2");
        mapboxMap.addSource(vectorSource);

        LineLayer terrainData = new LineLayer("terrain-data", "terrain-data");
        terrainData.setSourceLayer("contour");
        terrainData.setProperties(
                lineJoin(Property.LINE_JOIN_ROUND),
                lineCap(Property.LINE_CAP_ROUND),
                lineColor(Color.parseColor("#ff69b4")),
                lineWidth(1f)
        );

        mapboxMap.addLayer(terrainData);
        mapboxMap.removeLayer("web-map-layer");

    }

    public void renderWithOSMSource(MapboxMap mapboxMap) {
        RasterSource webMapSource = new RasterSource(
                "web-map-source",
                new TileSet("tileset", "http://tile.osm.org/{z}/{x}/{y}.png"), 256);

        mapboxMap.addSource(webMapSource);

        // Add the web map source to the map.
        RasterLayer webMapLayer = new RasterLayer("web-map-layer", "web-map-source");
        mapboxMap.addLayerBelow(webMapLayer, "aeroway-taxiway");

        mapboxMap.removeLayer("terrain-data");

    }



    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
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

}
