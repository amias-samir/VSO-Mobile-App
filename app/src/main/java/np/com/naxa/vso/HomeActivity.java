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
import android.util.Pair;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.andrognito.flashbar.Flashbar;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterManagerPlugin;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.home.CategoriesDetailAdapter;
import np.com.naxa.vso.home.MapDataRepository;
import np.com.naxa.vso.home.MySection;
import np.com.naxa.vso.home.SectionAdapter;
import np.com.naxa.vso.home.model.CategoriesDetail;
import np.com.naxa.vso.home.model.MapMarkerItem;
import np.com.naxa.vso.home.model.MapMarkerItemBuilder;
import np.com.naxa.vso.utils.JSONParser;
import np.com.naxa.vso.utils.ToastUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPanel;

    @BindView(R.id.recylcer_view_map_categories)
    RecyclerView recyclerView;

    @BindView(R.id.mapView)
    MapView mapboxMapview;

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

    @BindView(R.id.tv_list_data_set)
    TextView tvListDataSet;

    private MapDataRepository repo;
    private MapboxMap mapboxMap;
    private ClusterManagerPlugin<MapMarkerItem> clusterManagerPlugin;

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

        setupSlidingPanel();
        setupBottomBar();
        setupMapBox();
        setupViewSwitcher();
        setupGridRecycler();
        setupListRecycler();

    }


    private void setupListRecycler() {
        CategoriesDetailAdapter categoriesDetailAdapter = new CategoriesDetailAdapter(R.layout.item_catagories_detail, null);
        recyclerViewCatDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCatDetails.setAdapter(categoriesDetailAdapter);
        categoriesDetailAdapter.setOnItemClickListener((adapter, view, position) -> Toast.makeText(HomeActivity.this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show());

    }

    private void setupViewSwitcher() {
        Animation out = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        Animation in = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        viewSwitcher.setOutAnimation(out);
        viewSwitcher.setInAnimation(in);
    }


    private void setupMapBox() {
        Mapbox.getInstance(this, getString(R.string.access_token));

        mapboxMapview.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;

            clusterManagerPlugin = new ClusterManagerPlugin<>(this, mapboxMap);
            mapboxMap.addOnCameraIdleListener(clusterManagerPlugin);

            mapboxMap.getUiSettings().setAllGesturesEnabled(true);
            moveCamera(new LatLng(27.657531140175244, 85.46161651611328));
            showOverlayOnMap(-1);
        });
    }

    private void moveCamera(LatLng latLng) {
        if (mapboxMap != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.8), 2800);
        }
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
        sliderToggleList.animate().rotation(180).setDuration(500).start();



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

    private void setupGridRecycler() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, MySection.getMapDataCatergorySections());
        recyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            showOverlayOnMap(position);
            showListSlider();
        });


    }


    private void loadLineLayers(String assetName, String fileContent) {

        if (mapboxMap.getSource(assetName) == null) {
            GeoJsonSource source = new GeoJsonSource(assetName, fileContent);
            mapboxMap.addSource(source);
            mapboxMap.addLayer(new LineLayer(assetName, assetName));
            Timber.i("Adding source %s to map", assetName);
        }
    }

    private void addMarker(boolean animate, LatLng latLng) {
        mapboxMap.addMarker(
                new MarkerViewOptions()
                        .position(latLng));
    }

    private List<CategoriesDetail> dummyCategoryData() {
        return new ArrayList<>();
    }


    @Override
    public void onBackPressed() {


        switch (viewSwitcher.getCurrentView().getId()) {
            case R.id.drag_view_main_slider:
                super.onBackPressed();
                break;
            case R.id.drag_view_list_slider:
                showGridSlider();

                break;
        }
    }

    private void showGridSlider() {
        viewSwitcher.showPrevious();
        slidingPanel.setAnchorPoint(0.2f);
        //slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
       // new Handler().postDelayed(() -> slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED), 1000);

    }

    private void showListSlider() {
        viewSwitcher.showNext();


        //slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
     //   new Handler().postDelayed(() -> slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED), 500);

        loadDataOnOverviewCard();
    }

    private void loadDataOnOverviewCard() {
        Collection<MapMarkerItem> currentDisplayedItems = clusterManagerPlugin.getAlgorithm().getItems();
        if (currentDisplayedItems != null) {
            int totalPOI = clusterManagerPlugin.getAlgorithm().getItems().size();
            tvListDataSet.setText(getString(R.string.dataset_overview, totalPOI));
        }
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
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_map_satelite_layer:
                    mapboxMap.setStyleUrl(Style.SATELLITE_STREETS);

                    break;
                case R.id.menu_map_dark_layer:
                    mapboxMap.setStyleUrl(Style.DARK);
                    break;
                case R.id.menu_map_light_layer:
                    mapboxMap.setStyleUrl(Style.LIGHT);
                    break;
            }
            return false;
        });
        popup.show();
    }

    @OnClick({R.id.toggle_slide_panel_list_grid, R.id.toggle_slide_panel_main_grid})
    public void toggleSlidePanel() {
        SlidingUpPanelLayout.PanelState currentState = slidingPanel.getPanelState();
        if (currentState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @AfterPermissionGranted(Permissions.RC_GPS_PERM)
    public void showGPSSetting() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }


    private void loadMarkersFromGeoJson(String assetName, String geojson) {
        Observable<MapMarkerItem> observable = Observable.create(e -> {
            try {
                FeatureCollection featureCollection = FeatureCollection.fromJson(geojson);
                List<Feature> features = featureCollection.getFeatures();
                for (Feature feature : features) {
                    if (feature.getGeometry() instanceof Point) {
                        Position coordinates = (Position)
                                feature.getGeometry().getCoordinates();


                        JSONParser jsonParser = new JSONParser(feature.getProperties());

                        MapMarkerItem mapMarkerItem = new MapMarkerItemBuilder()
                                .setLat(coordinates.getLatitude())
                                .setLng(coordinates.getLongitude())
                                .setTitle(jsonParser.getName())
                                .setSnippet(jsonParser.getAddress())
                                .setGeoJsonProperties(feature.getProperties().entrySet())
                                .createMapMarkerItem();

                        e.onNext(mapMarkerItem);
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
                .toList()
                .subscribe(new DisposableSingleObserver<List<MapMarkerItem>>() {
                    @Override
                    public void onSuccess(List<MapMarkerItem> myItems) {
                        clusterManagerPlugin.addItems(myItems);
                        clusterManagerPlugin.cluster();
                        ((CategoriesDetailAdapter) recyclerViewCatDetails.getAdapter()).replaceData(myItems);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void showOverlayOnMap(int position) {


        repo.getGeoJsonString(position)
                .subscribe(new Observer<Pair>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        clusterManagerPlugin.getMarkerCollection().clear();
                        clusterManagerPlugin.getClusterMarkerCollection().clear();
                    }

                    @Override
                    public void onNext(Pair pair) {
                        String assetName = (String) pair.first;
                        String fileContent = (String) pair.second;
                        loadLineLayers(assetName, fileContent);
                        loadMarkersFromGeoJson(assetName, fileContent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(HomeActivity.this, "An error occurred while loading geojson", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private boolean hasGPSPermissions() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }


}
