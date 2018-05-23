package np.com.naxa.vso.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterManagerPlugin;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.bonuspack.routing.GoogleRoadManager;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.vso.FloatingSuggestion;
import np.com.naxa.vso.R;
import np.com.naxa.vso.activity.ReportActivity;
import np.com.naxa.vso.database.combinedentity.EducationAndCommon;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.home.model.MapMarkerItem;
import np.com.naxa.vso.home.model.MapMarkerItemBuilder;
import np.com.naxa.vso.home.model.MarkerItem;
import np.com.naxa.vso.hospitalfilter.HospitalFilterActivity;
import np.com.naxa.vso.hospitalfilter.SortedHospitalItem;
import np.com.naxa.vso.utils.JSONParser;
import np.com.naxa.vso.utils.ToastUtils;
import np.com.naxa.vso.utils.maputils.OsmMarkerCluster;
import np.com.naxa.vso.utils.maputils.SortingDistance;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.EducationalInstitutesViewModel;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static np.com.naxa.vso.activity.OpenSpaceActivity.LOCATION_RESULT;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPanel;

//    @BindView(R.id.mapView)
//    MapView mapboxMapview;

    @BindView(R.id.bnve)
    BottomNavigationViewEx bnve;

    @BindView(R.id.recylcer_view_categories_detail)
    RecyclerView recyclerViewDataDetails;

    @BindView(R.id.recylcer_view_map_categories)
    RecyclerView recyclerDataCategories;

    @BindView(R.id.toggle_slide_panel_main_grid)
    ImageView sliderToggleMainGrid;

    @BindView(R.id.view_switcher_slide_layout)
    ViewSwitcher viewSwitcherSlideLayout;

    @BindView(R.id.drag_view_main_slider)
    LinearLayout dragView;

    @BindView(R.id.tv_data_set)
    TextView tvDataSet;

    @BindView(R.id.fab_location_toggle)
    FloatingActionButton fabLocationToggle;

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;

    private IMapController mapController;
    private GeoPoint centerPoint;
    private MapDataRepository mapDataRepository;
    FolderOverlay myOverLay;
    FolderOverlay myOverLayBoarder;
    RadiusMarkerClusterer poiMarkers;
    List<Overlay> overlaysList ;

    List<HospitalAndCommon> hospitalAndCommonListForSorting = new ArrayList<>() ;

    private String latitude;
    private String longitude;

    private ArrayList<String> assetList;
    private ArrayList<String> contentList;

    private final int RESULT_STORAGE_PERMISSION = 50;
    private final int RESULT_LOCATION_PERMISSION = 100;
    private final int RESULT_LAT_LONG = 150;


    private MapDataRepository repo;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapboxMap mapboxMap;
    private ClusterManagerPlugin<MapMarkerItem> clusterManagerPlugin;
    private boolean isGridShown = true;

    private PermissionsManager permissionsManager;
    //    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;

    CommonPlacesAttribViewModel commonPlacesAttribViewModel;
    List<CommonPlacesAttrb> commonPlacesAttrbsList = new ArrayList<>();

    HospitalFacilitiesVewModel hospitalFacilitiesVewModel;
    List<HospitalFacilities> hospitalFacilitiesList = new ArrayList<>();

    EducationalInstitutesViewModel educationalInstitutesViewModel;
    List<EducationalInstitutes> educationalInstitutesList = new ArrayList<>();

    OpenSpaceViewModel openSpaceViewModel;
    List<OpenSpace> openSpacesList = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, ArrayList<HospitalAndCommon> hospitalAndCommonList) {
        Intent intent = new Intent(context, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", hospitalAndCommonList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new_new);
        ButterKnife.bind(this);
        repo = new MapDataRepository();

        handleStoragePermission();

        fabLocationToggle.setOnClickListener(this);

//        setupMapBox();

        setupMap();
        setupBottomBar();
        setupListRecycler();
        setupGridRecycler();

        slidingPanel.setAnchorPoint(0.4f);

        try {
            // Get a new or existing ViewModel from the ViewModelProvider.
            commonPlacesAttribViewModel = ViewModelProviders.of(this).get(CommonPlacesAttribViewModel.class);
            hospitalFacilitiesVewModel = ViewModelProviders.of(this).get(HospitalFacilitiesVewModel.class);
            educationalInstitutesViewModel = ViewModelProviders.of(this).get(EducationalInstitutesViewModel.class);
            openSpaceViewModel = ViewModelProviders.of(this).get(OpenSpaceViewModel.class);
        } catch (NullPointerException e) {

            Log.d(TAG, "Exception: " + e.toString());
        }

        setupFloatingToolbar();

//        loadAllMarker();

        if (getIntent().getParcelableArrayListExtra("data") != null) {
            List<HospitalAndCommon> hospitalAndCommonList = getIntent().getParcelableArrayListExtra("data");
            loadFilteredHospitalMarker(hospitalAndCommonList);
        }

    }

    private void loadAllMarker() {
        Observable.just(0, 1, 2)
                .flatMap((Function<Integer, ObservableSource<Pair>>) integer -> repo.getGeoJsonString(integer))
                .subscribe(new DisposableObserver<Pair>() {
                    @Override
                    public void onNext(Pair pair) {
                        String assetName = (String) pair.first;
                        String fileContent = (String) pair.second;
                        loadlayerToMap(fileContent);
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

    private void setupFloatingToolbar() {
        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            List<FloatingSuggestion> suggestionList = new ArrayList<>();
            if (suggestionList.size() == 0 || newQuery.isEmpty() || oldQuery.isEmpty()) {
                floatingSearchView.swapSuggestions(suggestionList);
            }
            commonPlacesAttribViewModel.getPlacesContaining(newQuery)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMapIterable((Function<List<CommonPlacesAttrb>, Iterable<CommonPlacesAttrb>>) commonPlacesAttrbs -> commonPlacesAttrbs)
                    .subscribe(new DisposableSubscriber<CommonPlacesAttrb>() {
                        @Override
                        public void onNext(CommonPlacesAttrb commonPlacesAttrb) {
                            suggestionList.add(new FloatingSuggestion(commonPlacesAttrb.getName()));
                            floatingSearchView.swapSuggestions(suggestionList);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        });

        floatingSearchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) -> textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, textView.getText(), Toast.LENGTH_SHORT).show();
            }
        }));

        floatingSearchView.setDimBackground(true);


    }

    private void setupMap() {
        myOverLay = new FolderOverlay();
        myOverLayBoarder = new FolderOverlay();

        mapDataRepository = new MapDataRepository();
        centerPoint = new GeoPoint(27.657531140175244, 85.46161651611328);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                return false;
            }
        });
        mapController = mapView.getController();
        mapController.setZoom(12);
        poiMarkers = new RadiusMarkerClusterer(this);

        loadMunicipalityBoarder();

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                InfoWindow.closeAllInfoWindowsOn(mapView);
                slidingPanel.setPanelHeight(110);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });

        mapView.getOverlays().add(0, mapEventsOverlay);
        mapController.setCenter(centerPoint);


//        for clustering
        overlaysList = this.mapView.getOverlays();

        showOverlayOnMap(-1);
    }


    private void setupMapBox() {
        Mapbox.getInstance(this, getString(R.string.access_token));

//        mapboxMapview.getMapAsync(mapboxMap -> {
//            this.mapboxMap = mapboxMap;
//            clusterManagerPlugin = new ClusterManagerPlugin<>(this, mapboxMap);
//            mapboxMap.addOnCameraIdleListener(clusterManagerPlugin);
//            mapboxMap.getUiSettings().setAllGesturesEnabled(true);

        showOverlayOnMap(-1);
        moveCamera(new LatLng(27.657531140175244, 85.46161651611328));

//        });
    }

    private void moveCamera(LatLng latLng) {
        if (mapboxMap != null) {
            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.8), 2800);
        }
    }

    private void setupViewSwitcher() {
        Animation out = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        Animation in = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        viewSwitcherSlideLayout.setOutAnimation(out);
        viewSwitcherSlideLayout.setInAnimation(in);
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
                    HospitalFilterActivity.start(HomeActivity.this);

                    break;
            }
            return true;
        });
    }

    private void toggleSliderHeight() {

        Resources r = getResources();
        int gridHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, r.getDisplayMetrics());

        int listHeight = SlidingUpPanelLayout.LayoutParams.MATCH_PARENT;
        int newHeight;

        if (isGridShown) {
            newHeight = listHeight;
        } else {
            newHeight = gridHeight;
        }

        isGridShown = !isGridShown;

        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, newHeight);
        dragView.setLayoutParams(params);

    }

    private void setupListRecycler() {
        CategoriesDetailAdapter categoriesDetailAdapter = new CategoriesDetailAdapter(R.layout.item_catagories_detail, null);
        recyclerViewDataDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDataDetails.setAdapter(categoriesDetailAdapter);

    }

    private void setupGridRecycler() {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerDataCategories.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, MySection.getMapDataCatergorySections());
        recyclerDataCategories.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            showOverlayOnMap(position);
            switchViews();
        });
    }

    /**
     * 1.collapse layout
     * 2.wait for 1 sec
     * 3.switch view
     * 4.anchor layout
     */
    private void switchViews() {
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        new Handler().postDelayed(() -> {
            viewSwitcherSlideLayout.showNext();
            int visbleItemIndex = viewSwitcherSlideLayout.getDisplayedChild();
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            changeDataOverviewText(visbleItemIndex);
        }, 500);
        toggleSliderHeight();

    }

    private void changeDataOverviewText(int visbleItemIndex) {

        switch (visbleItemIndex) {
            case 0:
                tvDataSet.setText(R.string.browse_data_by_categories);
                clearClusterAndMarkers();
                break;
            case 1:
                tvDataSet.setText(generateDataCardText());
                break;
        }
    }

    private void clearClusterAndMarkers() {

        try {
            clusterManagerPlugin.getMarkerCollection().clear();
            clusterManagerPlugin.getClusterMarkerCollection().clear();
        } catch (NullPointerException e) {

        }
    }

    private String generateDataCardText() {

        String string = null;
        try {
            Collection<MapMarkerItem> currentDisplayedItems = clusterManagerPlugin.getAlgorithm().getItems();

            string = getString(R.string.browse_data_category);
            if (currentDisplayedItems != null) {
                int totalPOI = clusterManagerPlugin.getAlgorithm().getItems().size();
                string = getString(R.string.dataset_overview, totalPOI);
            }
        } catch (NullPointerException e) {

        }
        return string;
    }


    private void showOverlayOnMap(int position) {
        if (position == 0) {
            loadFilteredHospitalMarkerFlowable(hospitalFacilitiesVewModel.getAllHospitalDetailList());

            hospitalFacilitiesVewModel.getAllHospitalDetailList()
                    .subscribe(new DisposableSubscriber<List<HospitalAndCommon>>() {
                        @Override
                        public void onNext(List<HospitalAndCommon> hospitalAndCommonList) {

                            HospitalWithDIstance(hospitalAndCommonList);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });


            return;
        }
        if (position == 2) {
            loadFilteredEducationMarkerFlowable(educationalInstitutesViewModel.getAllEducationDetailList());
            return;
        }
        repo.getGeoJsonString(position)
                .subscribe(new Observer<Pair>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        clearClusterAndMarkers();
                    }

                    @Override
                    public void onNext(Pair pair) {
                        String assetName = (String) pair.first;
                        String fileContent = (String) pair.second;
//                        loadLineLayers(assetName, fileContent);
//                        loadMarkersFromGeoJson(assetName, fileContent);
                        mapView.getOverlays().clear();
                        saveGeoJsonDataToDatabase(position, fileContent);
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

    private void loadLineLayers(String assetName, String fileContent) {

        if (mapboxMap.getSource(assetName) == null) {
            GeoJsonSource source = new GeoJsonSource(assetName, fileContent);
            mapboxMap.addSource(source);
            mapboxMap.addLayer(new LineLayer(assetName, assetName));
            Timber.i("Adding source %s to map", assetName);
        }
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
//                        ((CategoriesDetailAdapter) recyclerViewDataDetails.getAdapter()).replaceData(myItems);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LAT_LONG) {
            String location = data.getStringExtra(LOCATION_RESULT);
            String string = location;
            String[] parts = string.split(" ");
            latitude = parts[0]; // 004
            longitude = parts[1]; // 034556

            ToastUtils.showToast("Latitude: " + latitude + " and Longitude: " + longitude);

//            mapboxMapview.getMapAsync(mapboxMap -> {
//                ToastUtils.showToast("Marker Added");
//                mapboxMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
//                        .title("Current Location")
//
//                );
//            });

        }
    }

    @AfterPermissionGranted(RESULT_STORAGE_PERMISSION)
    private void handleStoragePermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mapView.invalidate();
        } else {
            EasyPermissions.requestPermissions(this, "Provide storage permission to load map.",
                    RESULT_STORAGE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(RESULT_LOCATION_PERMISSION)
    private void handleLocationPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!statusOfGPS) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                GpsMyLocationProvider provider = new GpsMyLocationProvider(HomeActivity.this);
                provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
                provider.startLocationProvider(new IMyLocationConsumer() {
                    @Override
                    public void onLocationChanged(Location location, IMyLocationProvider source) {
                        Log.i("Shree", "Current Location: " + location.getLatitude() + location.getLongitude());
                        routeLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
                    }
                });
                MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(provider, mapView);
                myLocationNewOverlay.enableMyLocation();
                mapView.getOverlays().add(myLocationNewOverlay);
                mapView.invalidate();
            }
        } else {
            EasyPermissions.requestPermissions(this, "Provide location permission.",
                    RESULT_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onBackPressed() {
        int visibleItemIndex = viewSwitcherSlideLayout.getDisplayedChild();
        if (visibleItemIndex == 0) {
            if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                super.onBackPressed();
            } else {
                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        } else {
            switchViews();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_location_toggle:
//                routeLocation();
                handleLocationPermission();
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void routeLocation(GeoPoint startPoint) {

        Observable.create((ObservableOnSubscribe<Polyline>) e -> {
            try {
                RoadManager roadManager = new OSRMRoadManager(HomeActivity.this);
                GeoPoint endPoint = new GeoPoint(27.617458, 85.526783);
                ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                waypoints.add(startPoint);
                waypoints.add(endPoint);
                Road road = roadManager.getRoad(waypoints);
                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                roadOverlay.setGeodesic(true);
                e.onNext(roadOverlay);
            } catch (Exception ex) {
                e.onError(ex);
            } finally {
                e.onComplete();
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Polyline>() {
                    @Override
                    public void onNext(Polyline roadOverlay) {
                        mapView.getOverlays().add(roadOverlay);
                        clearClusterAndMarkers();
                        mapView.invalidate();
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

    @SuppressLint("MissingPermission")
    public GeoPoint getGeoPointUsingFused() {
        final GeoPoint[] geoPoint = new GeoPoint[1];
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                geoPoint[0] = new GeoPoint(location.getLatitude(), location.getLongitude());
            }
        });

        return geoPoint[0];
    }

    private void saveGeoJsonDataToDatabase(int pos, String geoJson) {
        if (pos == 0) {
            loadlayerToMap(geoJson);
        }
        if (pos == 1) {
            loadlayerToMap(geoJson);
        }
        if (pos == 2) {
            loadlayerToMap(geoJson);
        }
    }

    private void loadlayerToMap(String geoJson) {
//        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);

        MarkerClusterer markerClusterer = new RadiusMarkerClusterer(this);

        final KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(geoJson);

        Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);
        Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
        poiMarkers.setIcon(defaultBitmap);

        final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 5f, 0x20AA1010);

        myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);
        mapView.getOverlays().add(myOverLay);
        mapView.invalidate();
    }

    private void loadMunicipalityBoarder() {
        mapView.getOverlays().clear();

        RadiusMarkerClusterer radiusMarkerClusterer = new RadiusMarkerClusterer(this);
        // marker icons
         List<Marker> markerIconBmps_ = new ArrayList<Marker>();

        new Thread(() -> {
            Log.d(TAG, "loadMunicipalityBoarder: ");
//            vMapView.getOverlays().clear();

            String jsonString = null;
            try {
//                    InputStream jsonStream = getResources().openRawResource(R.raw.changunarayan_boundary);
                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_boundary.geojson");
                int size = jsonStream.available();
                byte[] buffer = new byte[size];
                jsonStream.read(buffer);
                jsonStream.close();
                jsonString = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }

            final KmlDocument kmlDocument = new KmlDocument();
            kmlDocument.parseGeoJSON(jsonString);

            Drawable defaultMarker = getResources().getDrawable(R.drawable.marker_default);

            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();

            final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3f, 0x20AA1010);
            myOverLayBoarder = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);

            runOnUiThread(() -> {
                Log.d(TAG, "run: load boarder");
                mapView.getOverlays().add(myOverLayBoarder);
                mapView.invalidate();
                mapController.animateTo(centerPoint);
            });


        }).start();
    }

    private void loadFilteredHospitalMarkerFlowable(Flowable<List<HospitalAndCommon>> flowableList) {
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);
        flowableList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<HospitalAndCommon>, Iterable<HospitalAndCommon>>() {
                    @Override
                    public Iterable<HospitalAndCommon> apply(List<HospitalAndCommon> hospitalAndCommonList) throws Exception {

                        return hospitalAndCommonList;
                    }
                })
                .subscribe(new DisposableSubscriber<HospitalAndCommon>() {
                    @Override
                    public void onNext(HospitalAndCommon hospitalAndCommon) {
                        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
                        String name = hospitalAndCommon.getCommonPlacesAttrb().getName();
                        double latitude = hospitalAndCommon.getCommonPlacesAttrb().getLatitude();
                        double longitude = hospitalAndCommon.getCommonPlacesAttrb().getLongitude();
                        items.add(new OverlayItem(null, null, new GeoPoint(latitude, longitude)));
                        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(HomeActivity.this, items,
                                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                    @Override
                                    public boolean onItemSingleTapUp(int index, OverlayItem item) {


                                        return true;
                                    }

                                    @Override
                                    public boolean onItemLongPress(int index, OverlayItem item) {
                                        return false;
                                    }
                                });

                        Log.d(TAG, "onNext: ");
//                        overlaysList.add(mOverlay);

//                        mapView.getOverlays().add(OsmMarkerCluster.createPointOfInterestOverlay(overlaysList, getApplicationContext()));
                        mapView.getOverlays().add(mOverlay);
                        mapView.invalidate();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    List<HospitalAndCommon> sortedHospitalList = new ArrayList<HospitalAndCommon>();
    private LinkedHashMap HospitalWithDIstance(List<HospitalAndCommon> hospitalAndCommonList){

        List<Float> sortedDistanceList = new ArrayList<Float>();
        GpsMyLocationProvider provider = new GpsMyLocationProvider(HomeActivity.this);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);

//        double latitude = provider.getLastKnownLocation().getLatitude();
//        double longitude = provider.getLastKnownLocation().getLongitude();

        double latitude = 27.71635;
        double longitude = 85.32507;

        SortingDistance sortingDistance = new SortingDistance();
        LinkedHashMap linkedHospitalAndDistance =  sortingDistance.sortingHospitalDistanceData(hospitalAndCommonList, latitude, longitude );


        Set<HospitalAndCommon> keySet = linkedHospitalAndDistance.keySet();
        sortedHospitalList = new ArrayList<HospitalAndCommon>(keySet);

        Collection<Float> values = linkedHospitalAndDistance.values();
        sortedDistanceList = new ArrayList<Float>(values);

        List<SortedHospitalItem> sortedHospitalItemList = new ArrayList<SortedHospitalItem>() ;
        for(int i = 0; i< linkedHospitalAndDistance.size(); i++){
            Float distance = sortedDistanceList.get(i);
            String distanceInMeterKm;
            if(sortedDistanceList.get(i)>1000){
                distanceInMeterKm = (distance/1000)+" Kms. away" ;
            }else {
                distanceInMeterKm = distance+ " Meters away";
            }
            SortedHospitalItem sortedHospitalItem = new SortedHospitalItem(sortedHospitalList.get(i), distanceInMeterKm);
            sortedHospitalItemList.add(sortedHospitalItem);
        }


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // Stuff that updates the UI
                ((CategoriesDetailAdapter) recyclerViewDataDetails.getAdapter()).replaceData(sortedHospitalItemList);


            }
        });





        return linkedHospitalAndDistance;
    }

    private void loadFilteredEducationMarkerFlowable(Flowable<List<EducationAndCommon>> flowableList) {
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);
        flowableList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<EducationAndCommon>, Iterable<EducationAndCommon>>() {
                    @Override
                    public Iterable<EducationAndCommon> apply(List<EducationAndCommon> educationAndCommons) throws Exception {
                        return educationAndCommons;
                    }
                })
                .subscribe(new DisposableSubscriber<EducationAndCommon>() {
                    @Override
                    public void onNext(EducationAndCommon educationAndCommon) {
                        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
                        String name = educationAndCommon.getCommonPlacesAttrb().getName();
                        double latitude = educationAndCommon.getCommonPlacesAttrb().getLatitude();
                        double longitude = educationAndCommon.getCommonPlacesAttrb().getLongitude();
                        items.add(new OverlayItem(name, "Description", new GeoPoint(latitude, longitude)));
                        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(HomeActivity.this, items,
                                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                    @Override
                                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onItemLongPress(int index, OverlayItem item) {
                                        return false;
                                    }
                                });
                        mapView.getOverlays().add(mOverlay);
                        mapView.invalidate();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void loadFilteredHospitalMarker(List<HospitalAndCommon> filteredHospitalList) {

        mapView.getOverlays().clear();
        Observable.just(filteredHospitalList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<HospitalAndCommon>, Iterable<HospitalAndCommon>>() {
                    @Override
                    public Iterable<HospitalAndCommon> apply(List<HospitalAndCommon> hospitalAndCommons) throws Exception {
                        return hospitalAndCommons;
                    }
                })
                .subscribe(new DisposableObserver<HospitalAndCommon>() {
                    @Override
                    public void onNext(HospitalAndCommon hospitalAndCommon) {
                        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
                        String name = hospitalAndCommon.getCommonPlacesAttrb().getName();
                        double latitude = hospitalAndCommon.getCommonPlacesAttrb().getLatitude();
                        double longitude = hospitalAndCommon.getCommonPlacesAttrb().getLongitude();
                        items.add(new OverlayItem(name, "Description", new GeoPoint(latitude, longitude)));
                        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(HomeActivity.this, items,
                                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                    @Override
                                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onItemLongPress(int index, OverlayItem item) {
                                        return false;
                                    }
                                });

                        mapView.getOverlays().add(mOverlay);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mapView.invalidate();
                    }
                });
    }
}



