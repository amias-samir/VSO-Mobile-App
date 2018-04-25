package np.com.naxa.vso;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.zagum.expandicon.ExpandIconView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;

import np.com.naxa.vso.home.MySection;
import np.com.naxa.vso.home.SectionAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPanel;

    @BindView(R.id.recylcer_view_map_categories)
    RecyclerView recyclerView;


    @BindView(R.id.mapView)
    MapView mapboxMapview;

    @BindView(R.id.expand_icon_view)
    ExpandIconView expandIconView;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    private static final String SRC_WARD_LAYER = "geoJsonData";




    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupSlidingPanel();
        setupBottomBar();

        setupMapBox();
        Mapbox.getInstance(this, getString(R.string.access_token));


    }

    private void setupMapBox() {
        Mapbox.getInstance(this, getString(R.string.access_token));
        mapboxMapview.getMapAsync(mapboxMap -> {
            try {


                LatLng latlng = new LatLng(27.657531140175244, 85.46161651611328);

                CameraPosition position = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(11)
                        .tilt(20)
                        .build();

                GeoJsonSource source = new GeoJsonSource(SRC_WARD_LAYER, new URL("https://gist.githubusercontent.com/nishontan/c7cd998d3ec44062d138fbcf01d2cdc8/raw/824acdceb0d4e4e992920629e217adabe9c37a8f/changu.geojson"));


                mapboxMap.addSource(source);
                mapboxMap.addLayer(new LineLayer("line", SRC_WARD_LAYER));

                RasterSource webMapSource = new RasterSource(
                        "web-map-source",
                        new TileSet("tileset", "https://api.mapbox.com/styles/v1/banmedo/ciiibvf1k0011alki4gp6if1s.html?fresh=true&title=true&access_token=pk.eyJ1IjoiYmFubWVkbyIsImEiOiJhSklqeEZzIn0.rzfSxO3cVUhghA2sJN378A#0.9/-0.000000/20.912836/0"), 256);

                mapboxMap.addSource(webMapSource);

                // Add the web map source to the map.
                RasterLayer webMapLayer = new RasterLayer("web-map-layer", "web-map-source");
                mapboxMap.addLayer(webMapLayer);

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            } catch (Throwable throwable) {
                Log.e("ClickOnLayerActivity", "Couldn't add GeoJsonSource to map", throwable);
            }
        });
    }

    private void setupBottomBar() {
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);


        bnve.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_ask_for_help:
                    ReportActivity.start(HomeActivity.this);
                    break;
                case R.id.menu_emergency_contacts:
                    ExpandableUseActivity.start(HomeActivity.this);
                    break;
                case R.id.menu_open_spaces:
                    break;
            }
            return true;
        });
    }

    private void setupSlidingPanel() {
        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                expandIconView.setFraction(slideOffset, true);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, MySection.getMapDataCatergorySections());
        recyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            switch (position) {
                case 1:
                    break;
            }

        });
    }

    @OnClick(R.id.fab_location_toggle)
    public void turnGPSon() {
        if (!hasGPSPermissions()) {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_gps),
                    Permissions.RC_GPS_PERM,
                    Manifest.permission.CAMERA);
            return;
        }

        showGPSSetting();
    }

    @OnClick(R.id.fab_map_layer)
    public void showMaplayerMenu() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.fab_map_layer));
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_map_layer, popup.getMenu());
        popup.show();
    }

    @AfterPermissionGranted(Permissions.RC_GPS_PERM)
    public void showGPSSetting() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

    }


    private boolean hasGPSPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
