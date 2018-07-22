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
import android.support.v4.content.ContextCompat;
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
import android.widget.ProgressBar;
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
import com.mapbox.services.commons.geojson.Feature;
import com.mapbox.services.commons.geojson.FeatureCollection;
import com.mapbox.services.commons.geojson.Point;
import com.mapbox.services.commons.models.Position;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.bonuspack.routing.GoogleRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.NetworkLocationIgnorer;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
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
import np.com.naxa.vso.database.combinedentity.OpenAndCommon;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.home.model.MapMarkerItem;
import np.com.naxa.vso.home.model.MapMarkerItemBuilder;
import np.com.naxa.vso.hospitalfilter.HospitalFilterActivity;
import np.com.naxa.vso.hospitalfilter.SortedHospitalItem;
import np.com.naxa.vso.utils.JSONParser;
import np.com.naxa.vso.utils.ToastUtils;
import np.com.naxa.vso.utils.maputils.MapCommonUtils;
import np.com.naxa.vso.utils.maputils.MapMarkerOverlayUtils;
import np.com.naxa.vso.utils.maputils.MyLocationService;
import np.com.naxa.vso.utils.maputils.SortingDistance;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.EducationalInstitutesViewModel;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static np.com.naxa.vso.activity.OpenSpaceActivity.LOCATION_RESULT;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

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

    @BindView(R.id.tv_data_filter)
    TextView tvDataFilter;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.tv_name_title)
    TextView tvNameTitle;
    @BindView(R.id.tv_distance_subtitle)
    TextView tvDistanceSubtitle;
    @BindView(R.id.ll_inset_data)
    LinearLayout llInsetData;
    @BindView(R.id.tv_resources)
    TextView tvResources;
    @BindView(R.id.tv_hazard_and_vulnerability)
    TextView tvHazardAndVulnerability;
    @BindView(R.id.tv_base_data)
    TextView tvBaseData;

    private IMapController mapController;
    private GeoPoint centerPoint;
    private MapDataRepository mapDataRepository;
    FolderOverlay myOverLay;
    FolderOverlay myOverLayBoarder;
    RadiusMarkerClusterer poiMarkers;
    List<Overlay> overlaysList;

    List<HospitalAndCommon> sortedHospitalList = new ArrayList<>();

    private String latitude;
    private String longitude;
    private String dataSetInfoText;

    private ArrayList<String> assetList;
    private ArrayList<String> contentList;

    private final int RESULT_STORAGE_PERMISSION = 50;
    private final int RESULT_LOCATION_PERMISSION = 100;
    private final int RESULT_LAT_LONG = 150;

    private boolean BACKBUTTONCOUNTER = false;

    private MapDataRepository repo;
    private FusedLocationProviderClient mFusedLocationClient;
    private MapboxMap mapboxMap;
    private ClusterManagerPlugin<MapMarkerItem> clusterManagerPlugin;
    private boolean isGridShown = true;
    private int gridPosition;
    private int mainCategoryPosition = 0;

    //location listner
    protected DirectedLocationOverlay myLocationOverlay;
    //MyLocationNewOverlay myLocationNewOverlay;
    protected LocationManager mLocationManager;
    private GeoPoint currentLocation;
    Location location = null;


    CommonPlacesAttribViewModel commonPlacesAttribViewModel;
    List<CommonPlacesAttrb> commonPlacesAttrbsList = new ArrayList<>();

    HospitalFacilitiesVewModel hospitalFacilitiesVewModel;
    List<HospitalFacilities> hospitalFacilitiesList = new ArrayList<>();

    EducationalInstitutesViewModel educationalInstitutesViewModel;
    List<EducationalInstitutes> educationalInstitutesList = new ArrayList<>();

    OpenSpaceViewModel openSpaceViewModel;
    List<OpenSpace> openSpacesList = new ArrayList<>();

    List<HospitalAndCommon> hospitalAndCommonList;

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
        setupGridRecycler(MySection.getMapDataCatergorySections());

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

        try {
//            get list from filter activity
            if (getIntent().getParcelableArrayListExtra("data") != null) {
                hospitalAndCommonList = getIntent().getParcelableArrayListExtra("data");
                Log.d(TAG, "onCreate: data received");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        handleLocationPermission();

    }

    public void initLocationListner() {

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        myLocationOverlay = new DirectedLocationOverlay(this);
        mapView.getOverlays().add(myLocationOverlay);

//        if (savedInstanceState == null) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Intent serviceIntent = new Intent(this, MyLocationService.class);
            startService(serviceIntent);

            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (location != null) {
            //location known:
            onLocationChanged(location);
            currentLocation = new GeoPoint(location);

        } else {
            //no location known: hide myLocationOverlay
//            myLocationOverlay.setEnabled(false);
        }

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
//        mapView.zoomToBoundingBox(boundingBox, true);
//        mapController.zoomToSpan(boundingBox.getLatitudeSpan(), boundingBox.getLongitudeSpan());
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
                    progressbar.setVisibility(View.VISIBLE);
                    routeLocation();
//                    HospitalFilterActivity.start(HomeActivity.this);
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

    private void setupGridRecycler(List<MySection> mySections) {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerDataCategories.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, mySections);
        recyclerDataCategories.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            
            showOverlayOnMap(position);
            switchViews();
            gridPosition = position;
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
                tvDataFilter.setVisibility(View.GONE);
                clearClusterAndMarkers();
                break;
            case 1:
//                tvDataSet.setText(generateDataCardText());
                tvDataSet.setText(dataSetInfoText);
                tvDataFilter.setVisibility(View.VISIBLE);
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

        llInsetData.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                Collection<? extends SortedHospitalItem> something = new ArrayList<>();
                ((CategoriesDetailAdapter) recyclerViewDataDetails.getAdapter()).replaceData(something);
                dataSetInfoText = "";

            }
        });

        MapCommonUtils.zoomToMapBoundary(mapView, centerPoint);


        switch (position) {
            case 0:
                loadFilteredHospitalMarkerFlowable(hospitalFacilitiesVewModel.getAllHospitalDetailList());
                hospitalFacilitiesVewModel.getAllHospitalDetailList()
                        .subscribe(new DisposableSubscriber<List<HospitalAndCommon>>() {
                            @Override
                            public void onNext(List<HospitalAndCommon> hospitalAndCommonList) {
                                HospitalWithDistance(hospitalAndCommonList);
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                return;
            case 1:
                //Loading data of open spaces from geoJson file
                loadFilteredOpenPlacesMarkerFlowable(openSpaceViewModel.getAllOpenSpaceList());
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
//                              loadLineLayers(assetName, fileContent);
//                              loadMarkersFromGeoJson(assetName, fileContent);
                                mapView.getOverlays().clear();
                                loadlayerToMap(fileContent);
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
                break;
            case 2:
                loadFilteredEducationMarkerFlowable(educationalInstitutesViewModel.getAllEducationDetailList());
                return;
        }


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

                initLocationListner();

                GpsMyLocationProvider provider = new GpsMyLocationProvider(HomeActivity.this);
                provider.addLocationSource(LocationManager.GPS_PROVIDER);
                provider.addLocationSource(LocationManager.NETWORK_PROVIDER);

                MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(provider, mapView);
                myLocationNewOverlay.setDrawAccuracyEnabled(true);

                if (myLocationNewOverlay.getMyLocation() != null) {
                    currentLocation = new GeoPoint(myLocationNewOverlay.getMyLocation());
                }

                mapView.getOverlays().add(myLocationNewOverlay);
                myLocationNewOverlay.enableMyLocation();
//                myLocationNewOverlay.enableFollowLocation();
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
                if (BACKBUTTONCOUNTER) {
                    super.onBackPressed();
                } else {
                    ToastUtils.showToast("Press Again To Exit.");
                    BACKBUTTONCOUNTER = true;
                }
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
//                handleLocationPermission();
                if (currentLocation == null) {
                    Toast.makeText(this, "searching current location", Toast.LENGTH_SHORT).show();
                    handleLocationPermission();
                } else {
                    handleLocationPermission();
                    mapView.getController().animateTo(currentLocation);
                    Intent serviceIntent = new Intent(this, MyLocationService.class);
                    stopService(serviceIntent);
                }
                break;
        }
    }


    private void routeLocation() {

        final GeoPoint[] points = new GeoPoint[2];

        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);

        SortingDistance sortingDistance = new SortingDistance();

        points[0] = currentLocation;
        final String[] nearestOpenSpace = new String[2];

        openSpaceViewModel.getAllOpenSpaceList()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<OpenAndCommon>, Publisher<Polyline>>) openAndCommons -> {
                    LinkedHashMap linkedOpenAndCommon = sortingDistance.sortingOpenSpaceDistanceData(openAndCommons,
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                    Set<OpenAndCommon> keySet = linkedOpenAndCommon.keySet();
                    List<OpenAndCommon> sortedOpenlist = new ArrayList<OpenAndCommon>(keySet);

                    Collection<Float> values = linkedOpenAndCommon.values();
                    ArrayList<Float> sortedDistanceList = new ArrayList<Float>(values);

                    nearestOpenSpace[0] = sortedOpenlist.get(0).getCommonPlacesAttrb().getName();
                    Float distance = sortedDistanceList.get(0);
                    if (sortedDistanceList.get(0) > 1000) {
                        nearestOpenSpace[1] = (distance / 1000) + " Kms. away";
                    } else {
                        nearestOpenSpace[1] = distance + " Meters away";
                    }

                    points[1] = new GeoPoint(sortedOpenlist.get(0).getCommonPlacesAttrb().getLatitude(),
                            sortedOpenlist.get(0).getCommonPlacesAttrb().getLongitude());

                    return routeGenerateObservable(points);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<Polyline>() {
                    @Override
                    public void onNext(Polyline roadOverlay) {
                        if (roadOverlay.getPoints().size() != 2) {

                            llInsetData.setVisibility(View.VISIBLE);
                            tvNameTitle.setText(nearestOpenSpace[0]);
                            tvDistanceSubtitle.setText(nearestOpenSpace[1]);

                            mapView.getOverlays().add(getMarkerOverlay(points));
                            mapView.getOverlays().add(roadOverlay);
                            mapView.getController().animateTo(points[0]);
                            // handleLocationPermission for current location position overlay
                            handleLocationPermission();
                            mapView.invalidate();
                        } else {
                            ToastUtils.showToast("Try Again Later");
                        }
                        progressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public Flowable<Polyline> routeGenerateObservable(GeoPoint[] points) {
        return Observable.create((ObservableOnSubscribe<Polyline>) e -> {
            try {
                RoadManager roadManager = new GoogleRoadManager();

                ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                waypoints.add(points[0]);
                waypoints.add(points[1]);

                Road road = roadManager.getRoad(waypoints);

                final int[] step = {1};
                Drawable nodeIcon = getResources().getDrawable(R.drawable.ic_circle_marker);
                Drawable icon = getResources().getDrawable(R.drawable.ic_call_black_24dp);
                Observable.just(road.mNodes)
                        .flatMapIterable(new Function<ArrayList<RoadNode>, Iterable<RoadNode>>() {
                            @Override
                            public Iterable<RoadNode> apply(ArrayList<RoadNode> roadNodes) throws Exception {
                                return roadNodes;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<RoadNode>() {
                            @Override
                            public void onNext(RoadNode roadNode) {
                                Marker nodeMarker = new Marker(mapView);
                                nodeMarker.setPosition(roadNode.mLocation);
                                nodeMarker.setIcon(nodeIcon);
                                nodeMarker.setTitle("Step " + step[0]++);
                                nodeMarker.setSnippet(roadNode.mInstructions);
                                nodeMarker.setSubDescription(Road.getLengthDurationText(HomeActivity.this, roadNode.mLength, roadNode.mDuration));
                                nodeMarker.setImage(icon);
                                mapView.getOverlays().add(nodeMarker);
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                Polyline roadOverlay = RoadManager.buildRoadOverlay(road);

                e.onNext(roadOverlay);
            } catch (Exception ex) {
                e.onError(ex);
            } finally {
                e.onComplete();
            }
        }).toFlowable(BackpressureStrategy.BUFFER);
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

    private void loadlayerToMap(String geoJson) {
        mapView.getOverlays().clear();
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
//            vMapView.getOverlays().clear();

            String jsonString = null;
            try {
//                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_boundary.geojson");
                InputStream jsonStream = VSO.getInstance().getAssets().open("changu_ward.geojson");
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

            Drawable defaultMarker = getResources().getDrawable(R.drawable.mapbox_marker_icon_default);
            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
//
            final Style defaultStyle = new Style(defaultBitmap, 0x901010AA, 3f, 0x20AA1010);
//            KmlFeature.Styler styler = new MyKmlStyler();
            myOverLayBoarder = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);

            runOnUiThread(() -> {
                mapView.getOverlays().add(myOverLayBoarder);
                MapCommonUtils.zoomToMapBoundary(mapView, centerPoint);

                //load filtered list
                if (hospitalAndCommonList == null) {
                } else {
                    loadFilteredHospitalMarker(hospitalAndCommonList);
                }

            });
        }).start();
    }


    private LinkedHashMap HospitalWithDistance(List<HospitalAndCommon> hospitalAndCommonList) {

        List<Float> sortedDistanceList = new ArrayList<Float>();

        double latitude = 27.71635;
        double longitude = 85.32507;

        SortingDistance sortingDistance = new SortingDistance();
        LinkedHashMap linkedHospitalAndDistance = sortingDistance.sortingHospitalDistanceData(hospitalAndCommonList, latitude, longitude);

        Set<HospitalAndCommon> keySet = linkedHospitalAndDistance.keySet();
        sortedHospitalList = new ArrayList<HospitalAndCommon>(keySet);

        Collection<Float> values = linkedHospitalAndDistance.values();
        sortedDistanceList = new ArrayList<Float>(values);

        List<SortedHospitalItem> sortedHospitalItemList = new ArrayList<SortedHospitalItem>();
        for (int i = 0; i < linkedHospitalAndDistance.size(); i++) {
            Float distance = sortedDistanceList.get(i);
            String distanceInMeterKm;
            if (sortedDistanceList.get(i) > 1000) {
                distanceInMeterKm = (distance / 1000) + " Kms. away";
            } else {
                distanceInMeterKm = distance + " Meters away";
            }
            SortedHospitalItem sortedHospitalItem = new SortedHospitalItem(sortedHospitalList.get(i), distanceInMeterKm);
            sortedHospitalItemList.add(sortedHospitalItem);
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // Stuff that updates the UI
                ((CategoriesDetailAdapter) recyclerViewDataDetails.getAdapter()).replaceData(sortedHospitalItemList);
                dataSetInfoText = (sortedHospitalItemList.size() + " Hospitals found ");
                recyclerViewDataDetails.getAdapter().notifyDataSetChanged();
            }
        });

        return linkedHospitalAndDistance;
    }


    private void loadFilteredHospitalMarkerFlowable(Flowable<List<HospitalAndCommon>> flowableList) {
        flowableList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<HospitalAndCommon>>() {
                    @Override
                    public void onNext(List<HospitalAndCommon> hospitalAndCommonList) {
                        loadFilteredHospitalMarker(hospitalAndCommonList);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadFilteredHospitalMarker(List<HospitalAndCommon> filteredHospitalList) {
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);
        Observable.just(filteredHospitalList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<HospitalAndCommon>, Iterable<HospitalAndCommon>>() {
                    @Override
                    public Iterable<HospitalAndCommon> apply(List<HospitalAndCommon> hospitalAndCommonList) throws Exception {
                        return hospitalAndCommonList;
                    }
                })
                .subscribe(new DisposableObserver<HospitalAndCommon>() {
                    @Override
                    public void onNext(HospitalAndCommon hospitalAndCommon) {
                        MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
                        mapView.getOverlays().add(mapMarkerOverlayUtils.overlayFromHospitalAndCommon(HomeActivity.this, hospitalAndCommon, mapView));
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

    private void loadFilteredOpenPlacesMarkerFlowable(Flowable<List<OpenAndCommon>> flowableList) {
        flowableList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<OpenAndCommon>>() {
                    @Override
                    public void onNext(List<OpenAndCommon> openAndCommons) {
                        loadFilteredOpenPlacesMarker(openAndCommons);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadFilteredOpenPlacesMarker(List<OpenAndCommon> filteredOpenList) {
//        mapView.getOverlays().clear();
//        mapView.getOverlays().add(myOverLayBoarder);
        Observable.just(filteredOpenList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<OpenAndCommon>, Iterable<OpenAndCommon>>() {
                    @Override
                    public Iterable<OpenAndCommon> apply(List<OpenAndCommon> openAndCommons) throws Exception {
                        return openAndCommons;
                    }
                })
                .subscribe(new DisposableObserver<OpenAndCommon>() {
                    @Override
                    public void onNext(OpenAndCommon openAndCommon) {
                        MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
                        mapView.getOverlays().add(mapMarkerOverlayUtils.overlayFromOpenSpaceAndCommon(HomeActivity.this, openAndCommon, mapView));
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

    private void loadFilteredEducationMarkerFlowable(Flowable<List<EducationAndCommon>> flowableList) {
        flowableList.subscribe(new DisposableSubscriber<List<EducationAndCommon>>() {
            @Override
            public void onNext(List<EducationAndCommon> educationAndCommons) {
                loadFilteredEducationMarker(educationAndCommons);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void loadFilteredEducationMarker(List<EducationAndCommon> educationAndCommons) {
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);
        Observable.just(educationAndCommons)
                .flatMapIterable((Function<List<EducationAndCommon>, Iterable<EducationAndCommon>>) educationAndCommons1 -> educationAndCommons1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<EducationAndCommon>() {
                    @Override
                    public void onNext(EducationAndCommon educationAndCommon) {
                        MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
                        mapView.getOverlays().add(mapMarkerOverlayUtils.overlayFromEductionalAndCommon(HomeActivity.this, educationAndCommon, mapView));
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


    public Overlay getMarkerOverlay(GeoPoint[] points) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        for (GeoPoint point : points) {
            items.add(new OverlayItem("", "", point));
        }

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(HomeActivity.this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });

        return mOverlay;
    }

    @OnClick(R.id.tv_data_filter)
    public void onViewCategorizedDataFilter() {

        if (gridPosition == 0) {
            HospitalFilterActivity.start(HomeActivity.this);
        }
    }


    //------------ LocationListener implementation
    private final NetworkLocationIgnorer mIgnorer = new NetworkLocationIgnorer();
    long mLastTime = 0; // milliseconds
    double mSpeed = 0.0; // km/h

    @Override
    public void onLocationChanged(final Location pLoc) {
        long currentTime = System.currentTimeMillis();
        if (mIgnorer.shouldIgnore(pLoc.getProvider(), currentTime))
            return;
        double dT = currentTime - mLastTime;
        if (dT < 100.0) {
            //Toast.makeText(this, pLoc.getProvider()+" dT="+dT, Toast.LENGTH_SHORT).show();
            return;
        }
        mLastTime = currentTime;

        GeoPoint newLocation = new GeoPoint(pLoc);
        currentLocation = newLocation;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }




    @OnClick({R.id.tv_resources, R.id.tv_hazard_and_vulnerability, R.id.tv_base_data})
    public void onMainCategoriesViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_resources:

                mainCategoryPosition = 1;
                setupGridRecycler(MySection.getResourcesCatergorySections());
               break;

            case R.id.tv_hazard_and_vulnerability:
                mainCategoryPosition = 2;
                setupGridRecycler(MySection.getHazardCatergorySections());
                break;

            case R.id.tv_base_data:
                mainCategoryPosition = 3;
                setupGridRecycler(MySection.getBaseDataCatergorySections());
                break;
        }
    }
}


