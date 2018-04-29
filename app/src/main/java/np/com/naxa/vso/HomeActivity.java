package np.com.naxa.vso;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.github.zagum.expandicon.ExpandIconView;
import com.google.gson.JsonObject;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPlugin;
import com.mapbox.mapboxsdk.plugins.geojson.GeoJsonPluginBuilder;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnLoadingGeoJsonListener;
import com.mapbox.mapboxsdk.plugins.geojson.listener.OnMarkerEventListener;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.home.CategoriesDetailAdapter;
import np.com.naxa.vso.home.MapDataRepository;
import np.com.naxa.vso.home.MySection;
import np.com.naxa.vso.home.SectionAdapter;
import np.com.naxa.vso.home.model.CategoriesDetail;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements OnLoadingGeoJsonListener, OnMarkerEventListener {
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

    @BindView(R.id.view_switcher_slide_layout)
    ViewSwitcher viewSwitcher;

    @BindView(R.id.recylcer_view_categories_detail)
    RecyclerView recyclerViewCatDetails;

    @BindView(R.id.toggle_slide_panel_main_grid)
    ImageView sliderToggleMainGrid;

    @BindView(R.id.toggle_slide_panel_list_grid)
    ImageView sliderToggleList;

    private static final String SRC_WARD_LAYER = "geoJsonData";
    private int rotationAngle;
    private MapDataRepository repo;
    private MapboxMap mapboxMap;
    private GeoJsonPlugin geoJsonPlugin;

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        repo = new MapDataRepository();

        setupRecyclerView();
        setupCatDetailsRecylcerView();
        setupSlidingPanel();
        setupBottomBar();

        setupMapBox();
        setupViewSwitcher();



    }

    private void setupMapBoxGeoJSON() {
        geoJsonPlugin = new GeoJsonPluginBuilder()
                .withContext(this)
                .withMap(mapboxMap)
                .withOnLoadingFileAssets(this)
                .withMarkerClickListener(this)
                .build();
    }

    private void setupCatDetailsRecylcerView() {

    }

    private void setupViewSwitcher() {
        Animation out = AnimationUtils.loadAnimation(this, R.anim.bottom_down); // load an animation
        Animation in = AnimationUtils.loadAnimation(this, R.anim.bottom_up); // load an animation
        viewSwitcher.setOutAnimation(out); // set out Animation for ViewSwitcher
        viewSwitcher.setInAnimation(in); // set out Animation for ViewSwitcher


    }

    private void setupMapBox() {
        Mapbox.getInstance(this, getString(R.string.access_token));
        mapboxMapview.getMapAsync(mapboxMap -> {
            try {

                this.mapboxMap = mapboxMap;
                setupMapBoxGeoJSON();

                LatLng latlng = new LatLng(27.657531140175244, 85.46161651611328);

                CameraPosition position = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(11)
                        .tilt(20)
                        .build();

//                GeoJsonSource source = new GeoJsonSource(SRC_WARD_LAYER, new URL("https://gist.githubusercontent.com/nishontan/c7cd998d3ec44062d138fbcf01d2cdc8/raw/824acdceb0d4e4e992920629e217adabe9c37a8f/changu.geojson"));
//
//
//                mapboxMap.addSource(source);
//                mapboxMap.addLayer(new LineLayer("line", SRC_WARD_LAYER));


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

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case COLLAPSED:
                        sliderToggleList.animate().rotation(180).setDuration(500).start();
                        sliderToggleMainGrid.animate().rotation(180).setDuration(500).start();
                        break;
                    case EXPANDED:
                        sliderToggleList.animate().rotation(0).setDuration(500).start();
                        sliderToggleMainGrid.animate().rotation(0).setDuration(500).start();


                        break;
                }
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, MySection.getMapDataCatergorySections());
        recyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            showListSlider();
            //showOverlayOnMap(position);
            String assetName = repo.getMapAssetName(position);

            if (mapboxMap != null && geoJsonPlugin != null) {
                mapboxMap.clear();
                geoJsonPlugin.setAssetsName(assetName);
             //   geoJsonPlugin.setAssetsName(repo.getMapAssetName(-1));
            }


        });



        CategoriesDetailAdapter categoriesDetailAdapter = new CategoriesDetailAdapter(R.layout.item_catagories_detail, dummyCategoryData());
        recyclerViewCatDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCatDetails.setAdapter(categoriesDetailAdapter);
        categoriesDetailAdapter.setOnItemClickListener((adapter, view, position) -> Toast.makeText(HomeActivity.this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show());
    }

    private List<CategoriesDetail> dummyCategoryData() {
        List<CategoriesDetail> list = new ArrayList<>();
        list.add(new CategoriesDetail(0, "Ram", "Bafal", "Last fata manche"));
        list.add(new CategoriesDetail(0, "Shyam", "Kalanki", "Last super fata manche"));
        list.add(new CategoriesDetail(0, "Hari", "Naxal", "First fata manche"));
        return list;
    }


    @Override
    public void onBackPressed() {
        switch (viewSwitcher.getCurrentView().getId()) {
            case R.id.drag_view_main_slider:
                super.onBackPressed();
                break;
            case R.id.drag_view_list_slider:
                showGridSlider();
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                break;
        }
    }

    private void showGridSlider() {
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        viewSwitcher.showPrevious();
    }

    private void showListSlider() {
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        viewSwitcher.showNext();
   //     new Handler().postDelayed(() -> slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED), 500);

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

    @OnClick({R.id.toggle_slide_panel_list_grid, R.id.toggle_slide_panel_main_grid})
    public void toggleSlidePanel() {
        SlidingUpPanelLayout.PanelState currentState = slidingPanel.getPanelState();
        if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }


    }

    @AfterPermissionGranted(Permissions.RC_GPS_PERM)
    public void showGPSSetting() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

    }


    private boolean hasGPSPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onPreLoading() {

    }

    @Override
    public void onLoaded() {

    }

    @Override
    public void onLoadFailed(Exception exception) {
        Toast.makeText(this, "Error occur during load GeoJson data. see logcat", Toast.LENGTH_LONG).show();
        exception.printStackTrace();
    }

    @Override
    public void onMarkerClickListener(Marker marker, JsonObject properties) {

    }
}
