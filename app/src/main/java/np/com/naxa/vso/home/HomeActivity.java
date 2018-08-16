package np.com.naxa.vso.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import com.github.zagum.expandicon.ExpandIconView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterManagerPlugin;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
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
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
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
import np.com.naxa.vso.OverlayPopupHiddenStyler;
import np.com.naxa.vso.R;
import np.com.naxa.vso.activity.ReportActivity;
import np.com.naxa.vso.database.combinedentity.EducationAndCommon;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.combinedentity.OpenAndCommon;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.GeoJsonListEntity;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.detailspage.MarkerDetailsDisplayActivity;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.home.model.MapDataCategory;
import np.com.naxa.vso.home.model.MapMarkerItem;
import np.com.naxa.vso.home.model.MapMarkerItemBuilder;
import np.com.naxa.vso.hospitalfilter.HospitalFilterActivity;
import np.com.naxa.vso.hospitalfilter.SortedHospitalItem;
import np.com.naxa.vso.utils.JSONParser;
import np.com.naxa.vso.utils.SharedPreferenceUtils;
import np.com.naxa.vso.utils.ToastUtils;
import np.com.naxa.vso.utils.maputils.MapCommonUtils;
import np.com.naxa.vso.utils.maputils.MapGeoJsonToObject;
import np.com.naxa.vso.utils.maputils.MapMarkerOverlayUtils;
import np.com.naxa.vso.utils.maputils.MyLocationService;
import np.com.naxa.vso.utils.maputils.SortingDistance;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.EducationalInstitutesViewModel;
import np.com.naxa.vso.viewmodel.GeoJsonListViewModel;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static np.com.naxa.vso.activity.OpenSpaceActivity.LOCATION_RESULT;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, MapEventsReceiver {

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
    @BindView(R.id.fab_map_layer)
    FloatingActionButton fabMapLayer;
    @BindView(R.id.card_view)
    CardView cardView;
    @BindView(R.id.expand_icon_up_down_toggle)
    ExpandIconView updownloadToggleIcon;

    private IMapController mapController;
    private GeoPoint centerPoint;
    private MapDataRepository mapDataRepository;
    FolderOverlay myOverLay;
    FolderOverlay myOverLayBoarder;
    FolderOverlay myOverLayWardBoarder;
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
    private int gridPosition = -1;
    private int mainCategoryPosition = 0;

    //location listner
    protected DirectedLocationOverlay myLocationOverlay;
    MapEventsOverlay mapEventsOverlay;
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

    GeoJsonListViewModel geoJsonListViewModel;

    @BindView(R.id.tv_go_back)
    public TextView tvGoBack;

    LinearLayout rlMainCategoryList;

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


    SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new_new);
        ButterKnife.bind(this);

        rlMainCategoryList = (LinearLayout) findViewById(R.id.main_categories_list);

        sharedPreferenceUtils = new SharedPreferenceUtils(HomeActivity.this);

        repo = new MapDataRepository();

        handleStoragePermission();

        fabLocationToggle.setOnClickListener(this);

//        setupMapBox();

        setupMap();

        setupBottomBar();
        setupListRecycler();
        setupGridRecycler(MySection.getResourcesCatergorySections());

        slidingPanel.setAnchorPoint(0.4f);
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        setupSlidingPanel();

        try {
            // Get a new or existing ViewModel from the ViewModelProvider.
            commonPlacesAttribViewModel = ViewModelProviders.of(this).get(CommonPlacesAttribViewModel.class);
            hospitalFacilitiesVewModel = ViewModelProviders.of(this).get(HospitalFacilitiesVewModel.class);
            educationalInstitutesViewModel = ViewModelProviders.of(this).get(EducationalInstitutesViewModel.class);
            openSpaceViewModel = ViewModelProviders.of(this).get(OpenSpaceViewModel.class);

            geoJsonListViewModel = ViewModelProviders.of(this).get(GeoJsonListViewModel.class);
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

        floatingSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_switch_to_english:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            LocaleChanger.setLocale(new Locale("en", "US"));
                            ActivityRecreationHelper.recreate(HomeActivity.this, true);
                        }
                        break;
                    case R.id.action_switch_to_nepali:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            LocaleChanger.setLocale(new Locale("ne", "NP"));
                            ActivityRecreationHelper.recreate(HomeActivity.this, true);
                        }
                        break;
                }
            }
        });
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


    }

    private void setupMap() {
        myOverLay = new FolderOverlay();
        myOverLayBoarder = new FolderOverlay();
        myOverLayWardBoarder = new FolderOverlay();

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


        mapEventsOverlay = new MapEventsOverlay(this, this);
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
        int gridHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 550, getResources().getDisplayMetrics());
        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, gridHeight);
        dragView.setLayoutParams(params);
    }

    private void toggleSliderHeight() {

        Resources r = getResources();
        int gridHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 550, r.getDisplayMetrics());

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
//        CategoriesDetailAdapter categoriesDetailAdapter = new CategoriesDetailAdapter(R.layout.item_catagories_detail, null);

        CategoryListAdapter categoriesDetailAdapter = new CategoryListAdapter(R.layout.item_catagories_detail, null);
        recyclerViewDataDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDataDetails.setAdapter(categoriesDetailAdapter);

        categoriesDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommonPlacesAttrb item = categoriesDetailAdapter.getData().get(position);
                Intent intent = new Intent(HomeActivity.this, MarkerDetailsDisplayActivity.class);
                intent.putExtra("data", new Gson().toJson(item));
                startActivity(intent);
            }
        });

    }

    String geoJsonFileName = "", geoJsonType = "", geoJsonName = "";
    int geoJsonmarkerImage;

    private void setupGridRecycler(List<MySection> mySections) {
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerDataCategories.setLayoutManager(mLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, mySections);
        recyclerDataCategories.setAdapter(sectionAdapter);


        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            wardShowCount = 0;
            MySection a = sectionAdapter.getData().get(position);
            MapDataCategory gridItem = a.t;

            if (gridItem.getName() == null) {
                ToastUtils.showToast("Error loading " + a.t.getName());
                return;
            }


            geoJsonFileName = a.t.getFileName();
            geoJsonName = a.t.getName();
            geoJsonType = a.t.getType();
            geoJsonmarkerImage = a.t.getMarker_image();

            showOverlayOnMap(a.t.getFileName(), a.t.getType(), a.t.getMarker_image());
//            showDataOnList(a.t.getName(), a.t.getType());
            showDataOnList(geoJsonFileName, a.t.getType());
            InfoWindow.closeAllInfoWindowsOn(mapView);

            gridPosition = position;

        });
    }


    private void showDataOnList(String name, String type) {
        commonPlacesAttribViewModel.getPlaceByType(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<CommonPlacesAttrb>>() {
                    @Override
                    public void onNext(List<CommonPlacesAttrb> commonPlacesAttrbs) {

                        CategoryListAdapter adapter = ((CategoryListAdapter) recyclerViewDataDetails.getAdapter());
                        adapter.replaceData(commonPlacesAttrbs);

                        switch (type) {
                            case MapDataCategory.POINT:
                                if (commonPlacesAttrbs.isEmpty()) {
                                    ToastUtils.showToast(getString(R.string.dataset_overview, "No"));
                                }
                                break;
                            default:
                                ToastUtils.showToast("Map layer updated");
                                break;
                        }

                        dataSetInfoText = getString(R.string.dataset_overview, commonPlacesAttrbs.size() + "");

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
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
                tvGoBack.setVisibility(View.GONE);

                clearClusterAndMarkers();
                break;
            case 1:
//                tvDataSet.setText(generateDataCardText());
                tvDataSet.setText(dataSetInfoText);
                tvDataFilter.setVisibility(View.GONE);
                tvGoBack.setVisibility(View.VISIBLE);
                rlMainCategoryList.setVisibility(View.GONE);
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
                string = getString(R.string.dataset_overview, "0");
            }
        } catch (NullPointerException e) {

        }
        return string;
    }


    private void showOverlayOnMap(String name, String type, int marker_image) {

        Log.d(TAG, "showOverlayOnMap: " + name);

        Publisher<GeoJsonListEntity> pub = LiveDataReactiveStreams.toPublisher(this, geoJsonListViewModel.getmSpecificGeoJsonEntity(name));
        Observable.fromPublisher(pub)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<GeoJsonListEntity, ObservableSource<GeoJsonListEntity>>() {
                    @Override
                    public ObservableSource<GeoJsonListEntity> apply(GeoJsonListEntity geoJsonListEntity) throws Exception {
                        return Observable.just(geoJsonListEntity);
                    }
                })
                .subscribe(new DisposableObserver<GeoJsonListEntity>() {
                    @Override
                    public void onNext(GeoJsonListEntity geoJsonListEntity) {
                        String fileContent = geoJsonListEntity.getCategoryJson();
                        mapView.getOverlays().clear();
                        if (name.equals("wards")) {
                            Log.d(TAG, "onNext: changunarayan_new_wards");
                            loadWardBoarderlayerToMap(fileContent, type, "",
                                    marker_image);
                        } else {
                            loadlayerToMap(fileContent, type, name, marker_image);

                            if (type.equals(MapDataCategory.POINT)) {
                                switchViews();
                            } else {
                                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("Failed to load geojson ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("GeoJson loaded sucessfully");
                    }
                });

//
//        repo.getGeoJsonString(name)
//                .subscribe(new DisposableObserver<Pair>() {
//                    @Override
//                    public void onNext(Pair pair) {
//                        String fileContent = (String) pair.second;
//                        mapView.getOverlays().clear();
//                        if (name.equals("wards")) {
//                            Log.d(TAG, "onNext: changunarayan_new_wards");
//                            loadWardBoarderlayerToMap(fileContent, type, "",
//                                    marker_image);
//                        } else {
//                            loadlayerToMap(fileContent, type, name, marker_image);
//
//                            if (type.equals(MapDataCategory.POINT)) {
//                                switchViews();
//                            } else {
//                                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.e("Failed to load geojson ");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Timber.i("GeoJson loaded sucessfully");
//                    }
//                });
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
                                //  loadlayerToMap(fileContent);
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
            Log.d(TAG, "handleStoragePermission: outside ");

            if (sharedPreferenceUtils.getBoolanValue(SharedPreferenceUtils.IS_STORAGE_PERMISSION_GRANTED, true)) {
                sharedPreferenceUtils.setValue(SharedPreferenceUtils.IS_STORAGE_PERMISSION_GRANTED, false);
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                Log.d(TAG, "handleStoragePermission: inside ");
//             finish();
            }


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

    @OnClick(R.id.tv_go_back)
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
            rlMainCategoryList.setVisibility(View.VISIBLE);
            switchViews();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_location_toggle:
                handleLocationPermission();
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

    private void loadlayerToMap(String geoJson, String lineType, String name, int marker_image) {
        mapView.getOverlays().clear();
        mapView.getOverlays().add(myOverLayBoarder);

        if (gridPosition != -1 && wardShowCount % 2 != 0) {
            mapView.getOverlays().add(myOverLayWardBoarder);
            mapView.getOverlays().remove(myOverLayBoarder);
        }

        MarkerClusterer markerClusterer = new RadiusMarkerClusterer(this);

        final KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(geoJson);


        Drawable defaultMarker = ContextCompat.getDrawable(HomeActivity.this, R.drawable.map_marker_blue);

        List<MySection> gridItems = new ArrayList<>();
        gridItems.addAll(MySection.getBaseDataCatergorySections());
        gridItems.addAll(MySection.getHazardCatergorySections());
        gridItems.addAll(MySection.getResourcesCatergorySections());


        // defaultMarker.setColorFilter(color, PorterDuff.Mode.DST_ATOP);
        Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
        poiMarkers.setIcon(defaultBitmap);


        Style defaultStyle;

        switch (lineType) {
            case MapDataCategory.ROAD:
                defaultStyle = new Style(defaultBitmap, Color.DKGRAY, 5f, 0x20AA1010);

                break;
            case MapDataCategory.RIVER:
                defaultStyle = new Style(defaultBitmap, Color.BLUE, 5f, 0x20AA1010);
                break;
            default:
                defaultStyle = new Style(defaultBitmap, Color.BLACK, 2f, 0x20AA1010);
                break;
        }

        myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument);

        MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
        MapGeoJsonToObject mapGeoJsonToObject = new MapGeoJsonToObject();
        mapGeoJsonToObject.getCommonPlacesListObj(HomeActivity.this, geoJson, name, mapView, mapMarkerOverlayUtils, myOverLay, marker_image);


    }

    private void loadWardBoarderlayerToMap(String geoJson, String lineType, String name, int marker_image) {
        MarkerClusterer markerClusterer = new RadiusMarkerClusterer(this);

        final KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(geoJson);

        myOverLayWardBoarder = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, new OverlayPopupHiddenStyler(), kmlDocument);
        mapView.getOverlays().add(myOverLayWardBoarder);
        mapView.invalidate();

        if (gridPosition == -1) {
            MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
            MapGeoJsonToObject mapGeoJsonToObject = new MapGeoJsonToObject();
            mapGeoJsonToObject.getWardDetailsListObj(HomeActivity.this, geoJson, name, mapView, mapMarkerOverlayUtils, myOverLay, marker_image);
        }

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
                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_municipality_boundary.geojson");
//                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_boundary.geojson");
//                InputStream jsonStream = VSO.getInstance().getAssets().open("changunarayan_new_wards.geojson");
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

//            kmlDocument.parseGeoJSON("{\"type\": \"FeatureCollection\",\"crs\": { \"type\": \"name\", \"properties\": { \"name\": \"urn:ogc:def:crs:OGC:1.3:CRS84\" } },\"features\": [{ \"type\": \"Feature\", \"properties\": { \"OID_\": 0, \"Name\": \"Agriculture Field Open Space\", \"FolderPath\": \"Changunarayan .kml\\/Changunarayan\", \"SymbolID\": 0, \"AltMode\": 0, \"Base\": 0.000000, \"Clamped\": -1, \"Extruded\": 0, \"Snippet\": null, \"PopupInfo\": null, \"Shape_Leng\": 0.013488, \"Shape_Area\": 0.000003, \"centroid_x\": 85.428792, \"centroid_y\": 27.704107 }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [ 85.429059884000083, 27.703782494000052, 0.0 ], [ 85.428977313000075, 27.70381560900006, 0.0 ], [ 85.428891729000043, 27.703834139000037, 0.0 ], [ 85.428777163000063, 27.703851605000068, 0.0 ], [ 85.428687569000033, 27.703907505000075, 0.0 ], [ 85.428678292000086, 27.703915060000043, 0.0 ], [ 85.42866910500004, 27.703920645000039, 0.0 ], [ 85.428599854000083, 27.703969451000034, 0.0 ], [ 85.428590844000041, 27.703971094000053, 0.0 ], [ 85.428572824000071, 27.703974382000069, 0.0 ], [ 85.428514112000073, 27.703988026000047, 0.0 ], [ 85.428505100000052, 27.703989670000055, 0.0 ], [ 85.428489398000067, 27.703991067000061, 0.0 ], [ 85.428386829000033, 27.703987278000056, 0.0 ], [ 85.428262346000054, 27.703929322000079, 0.0 ], [ 85.42825633700005, 27.703915273000064, 0.0 ], [ 85.428254399000082, 27.703909276000047, 0.0 ], [ 85.428246066000042, 27.703897120000079, 0.0 ], [ 85.428218751000031, 27.703862550000053, 0.0 ], [ 85.428212554000083, 27.703852453000025, 0.0 ], [ 85.428206261000071, 27.703844328000059, 0.0 ], [ 85.428152664000038, 27.703755580000063, 0.0 ], [ 85.428145723000057, 27.703715919000047, 0.0 ], [ 85.42814631300007, 27.703704123000023, 0.0 ], [ 85.428157764000048, 27.703653361000079, 0.0 ], [ 85.428164732000084, 27.703647714000056, 0.0 ], [ 85.428171797000061, 27.703640104000044, 0.0 ], [ 85.428183603000036, 27.703626769000039, 0.0 ], [ 85.428195309000046, 27.703615399000057, 0.0 ], [ 85.428214754000066, 27.70358268800004, 0.0 ], [ 85.428222007000045, 27.70357116100007, 0.0 ], [ 85.428229162000036, 27.703561598000078, 0.0 ], [ 85.428268107000065, 27.703486720000058, 0.0 ], [ 85.42827081400003, 27.703476979000072, 0.0 ], [ 85.428277386000047, 27.703433938000046, 0.0 ], [ 85.428277579000053, 27.703430012000069, 0.0 ], [ 85.428278447000082, 27.703412345000061, 0.0 ], [ 85.428281345000073, 27.703398690000029, 0.0 ], [ 85.42828288700008, 27.703367300000025, 0.0 ], [ 85.428290614000048, 27.703345974000058, 0.0 ], [ 85.428293222000036, 27.703338213000052, 0.0 ], [ 85.428301042000044, 27.703314936000027, 0.0 ], [ 85.428303552000045, 27.703309138000066, 0.0 ], [ 85.428279306000036, 27.703153964000023, 0.0 ], [ 85.428148440000029, 27.703229889000056, 0.0 ], [ 85.427961734000064, 27.703217079000069, 0.0 ], [ 85.427861861000054, 27.703207478000024, 0.0 ], [ 85.42767443300005, 27.703212377000057, 0.0 ], [ 85.427673732000073, 27.703226149000045, 0.0 ], [ 85.427661840000042, 27.703328287000033, 0.0 ], [ 85.427661338000064, 27.703338140000028, 0.0 ], [ 85.427664892000053, 27.703356042000053, 0.0 ], [ 85.427693294000051, 27.703412408000077, 0.0 ], [ 85.427694924000036, 27.70342432700005, 0.0 ], [ 85.427703144000077, 27.703438469000048, 0.0 ], [ 85.427706107000063, 27.703468241000053, 0.0 ], [ 85.427715165000052, 27.703510123000058, 0.0 ], [ 85.427712734000067, 27.703513991000079, 0.0 ], [ 85.427710104000084, 27.70352181100003, 0.0 ], [ 85.427670068000054, 27.703561911000065, 0.0 ], [ 85.427667234000069, 27.703573690000042, 0.0 ], [ 85.427666832000057, 27.703581599000074, 0.0 ], [ 85.427662389000034, 27.703625031000058, 0.0 ], [ 85.427661886000067, 27.703634925000074, 0.0 ], [ 85.427665950000062, 27.70364300500006, 0.0 ], [ 85.427673903000084, 27.703706776000047, 0.0 ], [ 85.427672598000072, 27.703732531000071, 0.0 ], [ 85.427658436000058, 27.703759806000051, 0.0 ], [ 85.427602664000062, 27.703755760000035, 0.0 ], [ 85.427594121000084, 27.703747501000066, 0.0 ], [ 85.427576741000053, 27.703736931000037, 0.0 ], [ 85.427565767000033, 27.703732554000055, 0.0 ], [ 85.427501971000083, 27.703710347000026, 0.0 ], [ 85.427490901000056, 27.703707953000048, 0.0 ], [ 85.427466729000059, 27.703699121000056, 0.0 ], [ 85.427407225000081, 27.703681049000068, 0.0 ], [ 85.427396158000079, 27.703678656000079, 0.0 ], [ 85.427140791000056, 27.703663268000071, 0.0 ], [ 85.427127196000072, 27.703666734000024, 0.0 ], [ 85.427015489000041, 27.703708252000069, 0.0 ], [ 85.426997024000059, 27.703719482000054, 0.0 ], [ 85.426983025000084, 27.703730879000034, 0.0 ], [ 85.42696912200006, 27.703740296000035, 0.0 ], [ 85.426962022000055, 27.70374797900007, 0.0 ], [ 85.426945088000082, 27.703773181000031, 0.0 ], [ 85.426935154000034, 27.703792687000032, 0.0 ], [ 85.426924768000049, 27.703865884000038, 0.0 ], [ 85.426919697000073, 27.703877636000072, 0.0 ], [ 85.426913629000069, 27.703909259000056, 0.0 ], [ 85.426974889000064, 27.703973977000032, 0.0 ], [ 85.42697449700006, 27.703981860000056, 0.0 ], [ 85.426973812000085, 27.703995657000064, 0.0 ], [ 85.42696815100004, 27.704064598000059, 0.0 ], [ 85.426967465000075, 27.704078411000069, 0.0 ], [ 85.426965894000034, 27.704109993000031, 0.0 ], [ 85.426957601000083, 27.704186867000033, 0.0 ], [ 85.426956913000083, 27.704200704000073, 0.0 ], [ 85.426954650000084, 27.704246185000045, 0.0 ], [ 85.426960561000044, 27.704262259000075, 0.0 ], [ 85.426964436000048, 27.704274296000051, 0.0 ], [ 85.426976362000062, 27.704304480000076, 0.0 ], [ 85.426996934000044, 27.704340949000027, 0.0 ], [ 85.427023546000044, 27.704345901000067, 0.0 ], [ 85.427043651000076, 27.704346644000054, 0.0 ], [ 85.427061232000085, 27.704353247000029, 0.0 ], [ 85.427170606000061, 27.704359273000023, 0.0 ], [ 85.427172840000082, 27.704359355000065, 0.0 ], [ 85.42718624400004, 27.70435985000006, 0.0 ], [ 85.427228503000038, 27.704365381000059, 0.0 ], [ 85.427244142000063, 27.704365959000029, 0.0 ], [ 85.427264249000075, 27.704366702000073, 0.0 ], [ 85.427370005000057, 27.704402372000061, 0.0 ], [ 85.427374103000034, 27.70441046600007, 0.0 ], [ 85.427396653000073, 27.704455001000042, 0.0 ], [ 85.427396191000071, 27.704464920000078, 0.0 ], [ 85.427400201000069, 27.704475005000063, 0.0 ], [ 85.427390996000042, 27.704528351000079, 0.0 ], [ 85.427388481000037, 27.704534225000032, 0.0 ], [ 85.427385874000038, 27.70454208600006, 0.0 ], [ 85.427365936000058, 27.704585131000044, 0.0 ], [ 85.427360903000078, 27.704596890000062, 0.0 ], [ 85.427356055000075, 27.704604676000031, 0.0 ], [ 85.427345799000079, 27.704632180000033, 0.0 ], [ 85.427332534000072, 27.70477042400006, 0.0 ], [ 85.427270173000068, 27.704809899000054, 0.0 ], [ 85.427256279000062, 27.70481933700006, 0.0 ], [ 85.427207396000085, 27.704857349000065, 0.0 ], [ 85.427198159000056, 27.704862982000066, 0.0 ], [ 85.427191254000036, 27.704866710000033, 0.0 ], [ 85.427158868000049, 27.704887426000028, 0.0 ], [ 85.427061029000072, 27.70490843500005, 0.0 ], [ 85.426997903000085, 27.704965571000059, 0.0 ], [ 85.427004079000085, 27.705023332000053, 0.0 ], [ 85.427008071000046, 27.705033403000073, 0.0 ], [ 85.427007976000084, 27.705035385000031, 0.0 ], [ 85.427020338000034, 27.705057680000039, 0.0 ], [ 85.427044163000062, 27.705074447000072, 0.0 ], [ 85.427086344000031, 27.705081964000044, 0.0 ], [ 85.427132252000035, 27.705105512000046, 0.0 ], [ 85.427189445000067, 27.705127496000046, 0.0 ], [ 85.427296574000081, 27.705135429000052, 0.0 ], [ 85.427410596000072, 27.705139642000063, 0.0 ], [ 85.427519055000062, 27.70511781600004, 0.0 ], [ 85.427554335000082, 27.705079395000041, 0.0 ], [ 85.427559417000055, 27.705065685000079, 0.0 ], [ 85.427564410000059, 27.705053959000054, 0.0 ], [ 85.427564497000049, 27.705051977000039, 0.0 ], [ 85.42762115000005, 27.704984632000048, 0.0 ], [ 85.427623470000071, 27.704982735000044, 0.0 ], [ 85.42767989400005, 27.704919413000027, 0.0 ], [ 85.42768736000005, 27.704901861000053, 0.0 ], [ 85.427699630000063, 27.704876571000057, 0.0 ], [ 85.427704762000076, 27.704809475000047, 0.0 ], [ 85.427848590000053, 27.704789079000079, 0.0 ], [ 85.427861721000056, 27.704795496000031, 0.0 ], [ 85.427890299000069, 27.704806440000027, 0.0 ], [ 85.427964795000037, 27.704787440000075, 0.0 ], [ 85.427971567000043, 27.704785713000035, 0.0 ], [ 85.427994191000039, 27.704778641000075, 0.0 ], [ 85.428003272000069, 27.704775022000035, 0.0 ], [ 85.428031885000053, 27.70473062800005, 0.0 ], [ 85.427972069000077, 27.704609984000058, 0.0 ], [ 85.427942576000078, 27.704571432000023, 0.0 ], [ 85.427940438000064, 27.704569381000056, 0.0 ], [ 85.427924229000041, 27.704533310000045, 0.0 ], [ 85.427926811000077, 27.704525525000065, 0.0 ], [ 85.427929303000042, 27.704519708000078, 0.0 ], [ 85.427945504000036, 27.704506519000063, 0.0 ], [ 85.427956721000044, 27.704504964000023, 0.0 ], [ 85.427972564000072, 27.704499641000041, 0.0 ], [ 85.427988406000054, 27.704494319000048, 0.0 ], [ 85.428015373000051, 27.704489408000029, 0.0 ], [ 85.428053377000083, 27.704486874000054, 0.0 ], [ 85.428069214000061, 27.704481553000051, 0.0 ], [ 85.428096088000075, 27.704478609000034, 0.0 ], [ 85.428114147000031, 27.704473371000063, 0.0 ], [ 85.428141104000076, 27.704468462000079, 0.0 ], [ 85.428174733000048, 27.704463800000042, 0.0 ], [ 85.428195096000081, 27.704456680000078, 0.0 ], [ 85.428253865000045, 27.704437208000058, 0.0 ], [ 85.428285341000048, 27.704430503000026, 0.0 ], [ 85.428307833000076, 27.70442543300004, 0.0 ], [ 85.428336996000041, 27.704420610000057, 0.0 ], [ 85.428361791000043, 27.704413659000068, 0.0 ], [ 85.42837966500008, 27.704412353000066, 0.0 ], [ 85.428393172000085, 27.704408919000059, 0.0 ], [ 85.428408903000047, 27.704405568000027, 0.0 ], [ 85.428420104000054, 27.704404015000023, 0.0 ], [ 85.428433528000085, 27.70440254500005, 0.0 ], [ 85.428455846000077, 27.704401403000077, 0.0 ], [ 85.428471574000071, 27.704398052000045, 0.0 ], [ 85.428534477000085, 27.704384650000065, 0.0 ], [ 85.428631584000073, 27.704350902000044, 0.0 ], [ 85.428647694000063, 27.704399715000079, 0.0 ], [ 85.42865190200007, 27.704405822000069, 0.0 ], [ 85.428895544000056, 27.704464440000038, 0.0 ], [ 85.428933531000041, 27.704465843000037, 0.0 ], [ 85.42894470400006, 27.704466256000046, 0.0 ], [ 85.428960345000064, 27.704466833000026, 0.0 ], [ 85.428964815000086, 27.704466998000044, 0.0 ], [ 85.429096573000038, 27.704473851000046, 0.0 ], [ 85.42910798500003, 27.704468316000032, 0.0 ], [ 85.429152266000074, 27.704424304000042, 0.0 ], [ 85.429175078000071, 27.704413244000023, 0.0 ], [ 85.42917747000007, 27.70440936500006, 0.0 ], [ 85.429219555000032, 27.704363329000046, 0.0 ], [ 85.429228956000031, 27.704351784000039, 0.0 ], [ 85.429236044000049, 27.704342136000037, 0.0 ], [ 85.429264, 27.704313450000029, 0.0 ], [ 85.429268697000055, 27.704307681000046, 0.0 ], [ 85.429292253000085, 27.704276867000033, 0.0 ], [ 85.429373121000083, 27.70419675100004, 0.0 ], [ 85.429391045000045, 27.704195435000031, 0.0 ], [ 85.429462897000064, 27.704239627000049, 0.0 ], [ 85.429462648000083, 27.704245554000067, 0.0 ], [ 85.429462317000059, 27.70425345700005, 0.0 ], [ 85.429461488000072, 27.704273219000072, 0.0 ], [ 85.429476111000042, 27.704351002000067, 0.0 ], [ 85.429482313000051, 27.704363122000075, 0.0 ], [ 85.429484380000076, 27.704367163000029, 0.0 ], [ 85.429483719000075, 27.704382997000039, 0.0 ], [ 85.429489512000032, 27.704458587000033, 0.0 ], [ 85.429491333000044, 27.704468578000046, 0.0 ], [ 85.429495058000043, 27.704486582000072, 0.0 ], [ 85.429499032000081, 27.704498642000033, 0.0 ], [ 85.429513287000077, 27.704586594000034, 0.0 ], [ 85.429515194000032, 27.704594617000055, 0.0 ], [ 85.429516854000042, 27.704608599000039, 0.0 ], [ 85.429529880000075, 27.704672748000064, 0.0 ], [ 85.429536264000035, 27.704680947000043, 0.0 ], [ 85.429542566000066, 27.704691134000029, 0.0 ], [ 85.429548870000076, 27.704701323000052, 0.0 ], [ 85.429573684000047, 27.704752039000027, 0.0 ], [ 85.429580155000053, 27.704758256000048, 0.0 ], [ 85.42961060500005, 27.70478130500004, 0.0 ], [ 85.429623878000086, 27.704785783000034, 0.0 ], [ 85.429639390000034, 27.70479034300007, 0.0 ], [ 85.429641630000049, 27.704790426000045, 0.0 ], [ 85.429722639000033, 27.704783450000036, 0.0 ], [ 85.429742790000034, 27.704784194000069, 0.0 ], [ 85.429754065000054, 27.704782617000035, 0.0 ], [ 85.42977539900005, 27.704753511000035, 0.0 ], [ 85.429782823000039, 27.704735855000024, 0.0 ], [ 85.429785455000058, 27.704725993000068, 0.0 ], [ 85.429785848000051, 27.704716049000069, 0.0 ], [ 85.429789071000073, 27.70463456400006, 0.0 ], [ 85.429790721000074, 27.704592864000062, 0.0 ], [ 85.429791113000078, 27.704582939000034, 0.0 ], [ 85.429791585000032, 27.704571031000057, 0.0 ], [ 85.429793311000083, 27.704527384000073, 0.0 ], [ 85.429793860000075, 27.704513503000044, 0.0 ], [ 85.429797349000069, 27.704481865000048, 0.0 ], [ 85.42980028900007, 27.70446411100005, 0.0 ], [ 85.429800680000085, 27.704454204000058, 0.0 ], [ 85.429813100000047, 27.704422923000038, 0.0 ], [ 85.429818191000038, 27.704407246000073, 0.0 ], [ 85.429820970000037, 27.70439347000007, 0.0 ], [ 85.42982590400004, 27.704381758000068, 0.0 ], [ 85.429830681000055, 27.704374006000023, 0.0 ], [ 85.42983584600006, 27.704356362000055, 0.0 ], [ 85.42994124300003, 27.704378088000055, 0.0 ], [ 85.429958928000076, 27.704382706000047, 0.0 ], [ 85.430075195000086, 27.704434584000069, 0.0 ], [ 85.430084043000079, 27.704436894000025, 0.0 ], [ 85.430097101000058, 27.70444531000004, 0.0 ], [ 85.430114628000069, 27.70445389300005, 0.0 ], [ 85.43013422000007, 27.70446652000004, 0.0 ], [ 85.430194912000047, 27.704512427000054, 0.0 ], [ 85.430201616000033, 27.704512674000057, 0.0 ], [ 85.430217258000084, 27.704513252000027, 0.0 ], [ 85.430264117000036, 27.704463381000039, 0.0 ], [ 85.430266851000056, 27.704451579000079, 0.0 ], [ 85.430276284000058, 27.704440026000043, 0.0 ], [ 85.430281083000068, 27.70443227100003, 0.0 ], [ 85.430283483000039, 27.704428393000057, 0.0 ], [ 85.430307304000053, 27.704393588000073, 0.0 ], [ 85.430311935000077, 27.704389795000054, 0.0 ], [ 85.430330619000074, 27.70437066900007, 0.0 ], [ 85.430351613000084, 27.704349653000065, 0.0 ], [ 85.43035384500007, 27.70434973600004, 0.0 ], [ 85.430364083000086, 27.704318429000068, 0.0 ], [ 85.43037135000003, 27.704304840000077, 0.0 ], [ 85.430378534000056, 27.704293229000029, 0.0 ], [ 85.43038100900003, 27.704287383000064, 0.0 ], [ 85.430392653000069, 27.704275940000059, 0.0 ], [ 85.430413460000068, 27.704258903000039, 0.0 ], [ 85.430427572000042, 27.70424162300003, 0.0 ], [ 85.430437221000034, 27.704224182000075, 0.0 ], [ 85.430439935000038, 27.704212421000079, 0.0 ], [ 85.430455248000044, 27.704165557000067, 0.0 ], [ 85.430457879000073, 27.704155778000029, 0.0 ], [ 85.430472216000055, 27.704132608000066, 0.0 ], [ 85.430493152000054, 27.704111663000049, 0.0 ], [ 85.430497608000053, 27.704111828000066, 0.0 ], [ 85.430554261000054, 27.704090235000024, 0.0 ], [ 85.430587993000074, 27.70408358800006, 0.0 ], [ 85.430621876000032, 27.70407300100004, 0.0 ], [ 85.430651067000042, 27.704068161000066, 0.0 ], [ 85.430664664000062, 27.704062745000044, 0.0 ], [ 85.430698458000052, 27.704054131000078, 0.0 ], [ 85.430719424000074, 27.704031242000042, 0.0 ], [ 85.430724415000043, 27.704017627000042, 0.0 ], [ 85.430726949000075, 27.704009836000068, 0.0 ], [ 85.430769507000036, 27.704029149000064, 0.0 ], [ 85.430772052000066, 27.704021358000034, 0.0 ], [ 85.430799156000035, 27.703957337000077, 0.0 ], [ 85.430801777000056, 27.703947587000073, 0.0 ], [ 85.430802172000085, 27.703937757000062, 0.0 ], [ 85.430802884000059, 27.703920065000034, 0.0 ], [ 85.430803279000031, 27.703910238000049, 0.0 ], [ 85.430803358000048, 27.703908273000025, 0.0 ], [ 85.430804227000067, 27.703886660000023, 0.0 ], [ 85.430800094000062, 27.703878637000059, 0.0 ], [ 85.430791514000077, 27.703870452000046, 0.0 ], [ 85.430765937000047, 27.703841974000056, 0.0 ], [ 85.430766175000031, 27.703836084000045, 0.0 ], [ 85.430766811000069, 27.703820381000071, 0.0 ], [ 85.430763452000065, 27.703758373000028, 0.0 ], [ 85.430759073000047, 27.703756234000025, 0.0 ], [ 85.43074617700006, 27.703743899000074, 0.0 ], [ 85.430721761000086, 27.703685704000065, 0.0 ], [ 85.430722083000035, 27.703677817000028, 0.0 ], [ 85.430718755000044, 27.703650055000026, 0.0 ], [ 85.430708261000063, 27.703633879000051, 0.0 ], [ 85.430706195000084, 27.703629856000077, 0.0 ], [ 85.430669543000079, 27.70359890900005, 0.0 ], [ 85.430658651000044, 27.703592590000028, 0.0 ], [ 85.430628452000064, 27.703567811000028, 0.0 ], [ 85.430619955000054, 27.703557639000053, 0.0 ], [ 85.430617892000043, 27.703553621000026, 0.0 ], [ 85.430607662000057, 27.703531562000023, 0.0 ], [ 85.430605600000035, 27.703527545000043, 0.0 ], [ 85.430599743000073, 27.703507625000043, 0.0 ], [ 85.430567842000073, 27.703470996000078, 0.0 ], [ 85.430535044000067, 27.703456002000053, 0.0 ], [ 85.430530677000036, 27.703453873000058, 0.0 ], [ 85.430517660000078, 27.703445518000024, 0.0 ], [ 85.430513293000047, 27.703443388000039, 0.0 ], [ 85.43048692900004, 27.703434542000025, 0.0 ], [ 85.430475889000036, 27.703432166000027, 0.0 ], [ 85.430471523000051, 27.703430037000032, 0.0 ], [ 85.43044277100006, 27.703425039000024, 0.0 ], [ 85.430431816000066, 27.703420699000048, 0.0 ], [ 85.430423002000055, 27.703418406000026, 0.0 ], [ 85.430414104000079, 27.703418077000038, 0.0 ], [ 85.430385185000034, 27.703417009000077, 0.0 ], [ 85.43036969800005, 27.703414470000041, 0.0 ], [ 85.430349849000038, 27.703409802000067, 0.0 ], [ 85.430323241000053, 27.703406852000057, 0.0 ], [ 85.430310066000061, 27.70340243000004, 0.0 ], [ 85.430305618000034, 27.703402266000069, 0.0 ], [ 85.430245994000074, 27.703390228000046, 0.0 ], [ 85.43020613300007, 27.703384822000032, 0.0 ], [ 85.430173035000053, 27.703377699000043, 0.0 ], [ 85.430166451000048, 27.703375489000052, 0.0 ], [ 85.430157731000065, 27.703371233000041, 0.0 ], [ 85.430144476000066, 27.703368777000037, 0.0 ], [ 85.430137804000083, 27.703368531000024, 0.0 ], [ 85.430111649000082, 27.703355766000072, 0.0 ], [ 85.430105156000081, 27.703351593000036, 0.0 ], [ 85.430096350000042, 27.703349302000049, 0.0 ], [ 85.430074382000043, 27.70334259200007, 0.0 ], [ 85.43004574400004, 27.703335637000066, 0.0 ], [ 85.43003258300007, 27.703331219000063, 0.0 ], [ 85.430028136000033, 27.703331055000035, 0.0 ], [ 85.430008216000033, 27.703328354000064, 0.0 ], [ 85.429992742000081, 27.703325817000064, 0.0 ], [ 85.429990519000057, 27.703325735000078, 0.0 ], [ 85.429968285000029, 27.703324914000063, 0.0 ], [ 85.429961615000082, 27.703324667000061, 0.0 ], [ 85.429928174000054, 27.703325398000061, 0.0 ], [ 85.429921412000056, 27.703327114000047, 0.0 ], [ 85.429916874000071, 27.703328912000075, 0.0 ], [ 85.429891865000059, 27.703339785000026, 0.0 ], [ 85.429880194000077, 27.703351151000049, 0.0 ], [ 85.429848044000039, 27.703371598000047, 0.0 ], [ 85.42984109300005, 27.703377243000034, 0.0 ], [ 85.429831733000071, 27.703386734000048, 0.0 ], [ 85.429801326000074, 27.703417098000045, 0.0 ], [ 85.429765892000034, 27.703459106000025, 0.0 ], [ 85.429756521000058, 27.703468608000037, 0.0 ], [ 85.429738150000048, 27.703479750000042, 0.0 ], [ 85.429687380000075, 27.70351531700004, 0.0 ], [ 85.429675773000042, 27.703524745000038, 0.0 ], [ 85.429654876000086, 27.703541719000043, 0.0 ], [ 85.429641037000067, 27.703551068000024, 0.0 ], [ 85.429608419000033, 27.703579453000032, 0.0 ], [ 85.429603772000064, 27.703583227000024, 0.0 ], [ 85.429587698000034, 27.70359250000007, 0.0 ], [ 85.429566876000081, 27.703607520000048, 0.0 ], [ 85.429534811000053, 27.703624102000049, 0.0 ], [ 85.429525606000084, 27.703629686000056, 0.0 ], [ 85.429507390000083, 27.703636911000046, 0.0 ], [ 85.429448369000056, 27.703656457000079, 0.0 ], [ 85.429436930000065, 27.703661961000023, 0.0 ], [ 85.429427916000066, 27.703663603000052, 0.0 ], [ 85.429416278000076, 27.703673052000056, 0.0 ], [ 85.429384277000054, 27.703687678000051, 0.0 ], [ 85.42937739100006, 27.703691376000052, 0.0 ], [ 85.42935023900003, 27.703698279000037, 0.0 ], [ 85.429329774000053, 27.70370543000007, 0.0 ], [ 85.429220418000057, 27.703746872000067, 0.0 ], [ 85.429206832000034, 27.703750326000034, 0.0 ], [ 85.429120842000032, 27.703770892000023, 0.0 ], [ 85.429111918000046, 27.703770563000035, 0.0 ], [ 85.42910968800004, 27.70377048000006, 0.0 ], [ 85.429100661000064, 27.703772126000047, 0.0 ], [ 85.429059884000083, 27.703782494000052, 0.0 ] ] ] } },{ \"type\": \"Feature\", \"properties\": { \"OID_\": 0, \"Name\": \"Changunarayan Park\", \"FolderPath\": \"Changunarayan .kml\\/Changunarayan\", \"SymbolID\": 1, \"AltMode\": 0, \"Base\": 0.000000, \"Clamped\": -1, \"Extruded\": 0, \"Snippet\": null, \"PopupInfo\": null, \"Shape_Leng\": 0.001155, \"Shape_Area\": 0.000000, \"centroid_x\": 85.431470, \"centroid_y\": 27.716311 }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [ 85.431330054000057, 27.716181361000054, 0.0 ], [ 85.431303828000068, 27.71622093600007, 0.0 ], [ 85.431302666000079, 27.716239440000038, 0.0 ], [ 85.431281169000044, 27.716326567000067, 0.0 ], [ 85.431276985000068, 27.71633556200004, 0.0 ], [ 85.431293287000074, 27.716388809000023, 0.0 ], [ 85.431301586000075, 27.716401942000061, 0.0 ], [ 85.431308558000069, 27.716407606000075, 0.0 ], [ 85.431315452000035, 27.716415188000042, 0.0 ], [ 85.431329726000058, 27.716422809000051, 0.0 ], [ 85.431374680000033, 27.716449776000047, 0.0 ], [ 85.431383815000061, 27.716453667000053, 0.0 ], [ 85.431391175000044, 27.716455617000065, 0.0 ], [ 85.431398544000047, 27.716457569000056, 0.0 ], [ 85.431413401000043, 27.716457572000024, 0.0 ], [ 85.431430080000041, 27.716459529000076, 0.0 ], [ 85.431437482000035, 27.716461486000071, 0.0 ], [ 85.431441201000041, 27.716461487000061, 0.0 ], [ 85.431457936000072, 27.716461491000075, 0.0 ], [ 85.431476530000054, 27.716461495000033, 0.0 ], [ 85.431554629000061, 27.716461514000059, 0.0 ], [ 85.431595406000042, 27.716457616000071, 0.0 ], [ 85.431612606000044, 27.71642287800006, 0.0 ], [ 85.431619556000044, 27.716413363000072, 0.0 ], [ 85.431622731000061, 27.716402020000032, 0.0 ], [ 85.431624065000051, 27.716390758000045, 0.0 ], [ 85.431651928000065, 27.716333847000044, 0.0 ], [ 85.431651639000052, 27.716328449000059, 0.0 ], [ 85.431654713000057, 27.71631949600004, 0.0 ], [ 85.431654420000086, 27.716314149000027, 0.0 ], [ 85.431659014000047, 27.716270311000073, 0.0 ], [ 85.431660266000051, 27.716261695000071, 0.0 ], [ 85.431659576000072, 27.71624971500006, 0.0 ], [ 85.431659478000086, 27.716248011000062, 0.0 ], [ 85.431657347000055, 27.716211021000049, 0.0 ], [ 85.431655162000084, 27.716202742000064, 0.0 ], [ 85.431623878000039, 27.716191224000056, 0.0 ], [ 85.431615228000055, 27.716187949000073, 0.0 ], [ 85.431603266000081, 27.716186313000037, 0.0 ], [ 85.431591260000062, 27.716183049000051, 0.0 ], [ 85.431572549000066, 27.71618141700003, 0.0 ], [ 85.431530038000062, 27.716173295000033, 0.0 ], [ 85.431518184000083, 27.716171675000055, 0.0 ], [ 85.431509722000044, 27.716170058000046, 0.0 ], [ 85.431487763000064, 27.716170053000042, 0.0 ], [ 85.431433893000076, 27.716160387000059, 0.0 ], [ 85.431432210000082, 27.71616038600007, 0.0 ], [ 85.431422109000039, 27.716160384000034, 0.0 ], [ 85.431406957000036, 27.716160380000076, 0.0 ], [ 85.431364870000039, 27.716160371000058, 0.0 ], [ 85.431361503000062, 27.716160370000068, 0.0 ], [ 85.431353086000058, 27.716160368000033, 0.0 ], [ 85.431330054000057, 27.716181361000054, 0.0 ] ] ] } },{ \"type\": \"Feature\", \"properties\": { \"OID_\": 0, \"Name\": \"Paddy Field\", \"FolderPath\": \"Changunarayan .kml\\/Changunarayan\", \"SymbolID\": 1, \"AltMode\": 0, \"Base\": 0.000000, \"Clamped\": -1, \"Extruded\": 0, \"Snippet\": null, \"PopupInfo\": null, \"Shape_Leng\": 0.015719, \"Shape_Area\": 0.000012, \"centroid_x\": 85.403943, \"centroid_y\": 27.688497 }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [ 85.402336803000082, 27.687625248000074, 0.0 ], [ 85.402377311000066, 27.687771428000076, 0.0 ], [ 85.402376996000044, 27.687844372000029, 0.0 ], [ 85.402484149000031, 27.688478376000035, 0.0 ], [ 85.402483999000083, 27.688514947000044, 0.0 ], [ 85.402372796000066, 27.688818955000045, 0.0 ], [ 85.402358893000041, 27.688855455000066, 0.0 ], [ 85.402261569000075, 27.689098887000057, 0.0 ], [ 85.40224753900003, 27.689159871000072, 0.0 ], [ 85.402233618000082, 27.689196421000076, 0.0 ], [ 85.402136286000029, 27.689415710000048, 0.0 ], [ 85.402136111000061, 27.689452415000062, 0.0 ], [ 85.402122115000054, 27.689501245000031, 0.0 ], [ 85.40242382200006, 27.689761002000068, 0.0 ], [ 85.402547704000085, 27.689774326000077, 0.0 ], [ 85.402712948000044, 27.689775755000028, 0.0 ], [ 85.402947045000076, 27.689777780000043, 0.0 ], [ 85.403098519000082, 27.689779090000059, 0.0 ], [ 85.403222164000056, 27.689890451000053, 0.0 ], [ 85.403882987000031, 27.690129159000037, 0.0 ], [ 85.403910454000084, 27.690203018000034, 0.0 ], [ 85.403910376000056, 27.690264385000035, 0.0 ], [ 85.403896038000084, 27.690694246000078, 0.0 ], [ 85.403909787000032, 27.69073125500006, 0.0 ], [ 85.40402001700005, 27.690867516000026, 0.0 ], [ 85.40407519300004, 27.690892603000066, 0.0 ], [ 85.404116585000054, 27.690905267000062, 0.0 ], [ 85.404199383000048, 27.690918289000024, 0.0 ], [ 85.404240787000049, 27.690918647000046, 0.0 ], [ 85.404378800000075, 27.690919841000039, 0.0 ], [ 85.404530615000056, 27.690933461000043, 0.0 ], [ 85.404627231000063, 27.690958912000042, 0.0 ], [ 85.404682441000034, 27.690959389000056, 0.0 ], [ 85.404737651000062, 27.690959867000061, 0.0 ], [ 85.40484807100006, 27.690960822000079, 0.0 ], [ 85.404917084000033, 27.690961419000075, 0.0 ], [ 85.404958481000051, 27.690949469000032, 0.0 ], [ 85.405068861000075, 27.690913504000036, 0.0 ], [ 85.405151626000077, 27.690877305000072, 0.0 ], [ 85.40522059400007, 27.690853294000078, 0.0 ], [ 85.40528955700006, 27.690829286000053, 0.0 ], [ 85.40534471400008, 27.690805161000071, 0.0 ], [ 85.405413643000031, 27.690768858000069, 0.0 ], [ 85.405454970000051, 27.690732322000031, 0.0 ], [ 85.405510062000076, 27.690683615000069, 0.0 ], [ 85.405537557000059, 27.690634679000027, 0.0 ], [ 85.405551254000045, 27.690585633000069, 0.0 ], [ 85.405578690000084, 27.690512141000056, 0.0 ], [ 85.40559240500005, 27.69047540400004, 0.0 ], [ 85.405606092000085, 27.690426389000038, 0.0 ], [ 85.405605990000083, 27.690377264000062, 0.0 ], [ 85.405633407000039, 27.690303832000041, 0.0 ], [ 85.405647060000035, 27.69024257600006, 0.0 ], [ 85.405646954000076, 27.690193487000045, 0.0 ], [ 85.405660548000071, 27.69010772200005, 0.0 ], [ 85.405660415000057, 27.690046394000035, 0.0 ], [ 85.405660308000051, 27.689997342000026, 0.0 ], [ 85.405660121000039, 27.68991152500007, 0.0 ], [ 85.405659961000083, 27.689837989000068, 0.0 ], [ 85.405659721000063, 27.689727727000047, 0.0 ], [ 85.405659587000059, 27.689666490000036, 0.0 ], [ 85.405659428000035, 27.689593025000079, 0.0 ], [ 85.405659348000086, 27.689556301000039, 0.0 ], [ 85.405645452000044, 27.689494987000046, 0.0 ], [ 85.405645295000056, 27.689421572000072, 0.0 ], [ 85.40564519000003, 27.689372640000045, 0.0 ], [ 85.405631301000085, 27.689311370000041, 0.0 ], [ 85.405631119000077, 27.68922578300004, 0.0 ], [ 85.405631016000086, 27.689176889000066, 0.0 ], [ 85.405630913000039, 27.689128004000054, 0.0 ], [ 85.405644456000061, 27.689030381000066, 0.0 ], [ 85.405658100000039, 27.688981643000034, 0.0 ], [ 85.405685408000068, 27.688896404000047, 0.0 ], [ 85.405726451000078, 27.688811312000041, 0.0 ], [ 85.405753767000078, 27.688738331000025, 0.0 ], [ 85.405781164000075, 27.688701967000043, 0.0 ], [ 85.405794816000082, 27.688665489000073, 0.0 ], [ 85.405822115000035, 27.688592550000067, 0.0 ], [ 85.405821811000067, 27.688470636000034, 0.0 ], [ 85.405821690000039, 27.688421887000061, 0.0 ], [ 85.405821478000064, 27.688336598000035, 0.0 ], [ 85.405821357000036, 27.688287874000025, 0.0 ], [ 85.405779991000031, 27.688214451000078, 0.0 ], [ 85.405738693000046, 27.688165395000055, 0.0 ], [ 85.405697403000033, 27.68811634900004, 0.0 ], [ 85.405656148000048, 27.688079482000035, 0.0 ], [ 85.405587452000077, 27.688042384000028, 0.0 ], [ 85.405532535000077, 27.688029742000026, 0.0 ], [ 85.405463919000056, 27.688029151000023, 0.0 ], [ 85.405409027000076, 27.688028677000034, 0.0 ], [ 85.405326689000049, 27.688027967000039, 0.0 ], [ 85.405258107000066, 27.688051711000071, 0.0 ], [ 85.40520321200006, 27.688051237000025, 0.0 ], [ 85.40516204100004, 27.688050882000027, 0.0 ], [ 85.405093423000039, 27.688050290000035, 0.0 ], [ 85.405038528000034, 27.688049816000046, 0.0 ], [ 85.404956187000039, 27.688049106000051, 0.0 ], [ 85.404805227000054, 27.688047803000075, 0.0 ], [ 85.404709159000049, 27.688034806000076, 0.0 ], [ 85.404613094000069, 27.688009643000044, 0.0 ], [ 85.404530760000057, 27.68798460000005, 0.0 ], [ 85.404517044000045, 27.68792366200006, 0.0 ], [ 85.40451704700007, 27.68788717700005, 0.0 ], [ 85.404517055000042, 27.687802066000074, 0.0 ], [ 85.404530775000069, 27.687753562000069, 0.0 ], [ 85.404530779000083, 27.687680647000036, 0.0 ], [ 85.404530782000052, 27.687644197000054, 0.0 ], [ 85.404530787000056, 27.687559167000074, 0.0 ], [ 85.40453079200006, 27.687486307000029, 0.0 ], [ 85.404530799000042, 27.687377056000059, 0.0 ], [ 85.404475997000077, 27.687267379000048, 0.0 ], [ 85.404434921000075, 27.687145742000041, 0.0 ], [ 85.404380182000068, 27.686987689000034, 0.0 ], [ 85.404339152000034, 27.686866185000042, 0.0 ], [ 85.404311818000053, 27.686769071000072, 0.0 ], [ 85.40427079300008, 27.686696082000026, 0.0 ], [ 85.404243466000082, 27.686623233000034, 0.0 ], [ 85.404202464000036, 27.686550286000056, 0.0 ], [ 85.404147782000052, 27.686489336000079, 0.0 ], [ 85.404106782000042, 27.686440610000034, 0.0 ], [ 85.404065778000074, 27.686403983000048, 0.0 ], [ 85.404024793000076, 27.686355273000061, 0.0 ], [ 85.403956477000065, 27.686294252000039, 0.0 ], [ 85.403901852000047, 27.686233363000042, 0.0 ], [ 85.40384720600008, 27.686196648000077, 0.0 ], [ 85.403792568000085, 27.68615993800006, 0.0 ], [ 85.403710636000085, 27.68609884500006, 0.0 ], [ 85.403656040000044, 27.686050075000026, 0.0 ], [ 85.403615054000056, 27.686037648000024, 0.0 ], [ 85.40350580300003, 27.685988419000068, 0.0 ], [ 85.403396488000055, 27.685975405000079, 0.0 ], [ 85.40328721000003, 27.685950323000043, 0.0 ], [ 85.403232543000058, 27.685949851000032, 0.0 ], [ 85.403164209000067, 27.685949262000065, 0.0 ], [ 85.403082208000058, 27.685948554000049, 0.0 ], [ 85.403013874000067, 27.685947964000036, 0.0 ], [ 85.402945541000065, 27.685947374000023, 0.0 ], [ 85.402877207000074, 27.685946784000066, 0.0 ], [ 85.402808874000073, 27.685946194000053, 0.0 ], [ 85.402685873000053, 27.685945133000075, 0.0 ], [ 85.402603873000032, 27.68594442400007, 0.0 ], [ 85.40250805900007, 27.685979806000034, 0.0 ], [ 85.40243941600005, 27.686051647000056, 0.0 ], [ 85.402425643000072, 27.686075678000066, 0.0 ], [ 85.402384424000047, 27.686123627000029, 0.0 ], [ 85.402343144000042, 27.686183665000044, 0.0 ], [ 85.402342984000086, 27.686219907000066, 0.0 ], [ 85.402315420000036, 27.686268002000077, 0.0 ], [ 85.402287634000061, 27.686364455000046, 0.0 ], [ 85.402287470000033, 27.686400723000077, 0.0 ], [ 85.402259891000085, 27.686448852000069, 0.0 ], [ 85.402259616000038, 27.686509321000074, 0.0 ], [ 85.402259340000057, 27.686569805000033, 0.0 ], [ 85.402258898000071, 27.686666610000032, 0.0 ], [ 85.402258622000033, 27.686727131000055, 0.0 ], [ 85.40228572500007, 27.686787904000028, 0.0 ], [ 85.402326311000081, 27.686897260000023, 0.0 ], [ 85.402353378000043, 27.686970190000068, 0.0 ], [ 85.40242138800005, 27.687079861000029, 0.0 ], [ 85.402448682000056, 27.687104344000034, 0.0 ], [ 85.402475827000046, 27.687165206000032, 0.0 ], [ 85.402516730000059, 27.687214072000074, 0.0 ], [ 85.402543939000054, 27.687262829000076, 0.0 ], [ 85.402571152000064, 27.68731159500004, 0.0 ], [ 85.402625642000032, 27.68739701800007, 0.0 ], [ 85.402666441000065, 27.687482351000028, 0.0 ], [ 85.402666168000053, 27.687555211000074, 0.0 ], [ 85.402665941000066, 27.687615944000072, 0.0 ], [ 85.402638425000077, 27.687640005000048, 0.0 ], [ 85.402569862000064, 27.687639412000067, 0.0 ], [ 85.40250130000004, 27.687638819000028, 0.0 ], [ 85.40244645000007, 27.687638345000039, 0.0 ], [ 85.402364175000059, 27.687637634000055, 0.0 ], [ 85.402336803000082, 27.687625248000074, 0.0 ] ] ] } },{ \"type\": \"Feature\", \"properties\": { \"OID_\": 0, \"Name\": \"Paddy Field\", \"FolderPath\": \"Changunarayan .kml\\/Changunarayan\", \"SymbolID\": 1, \"AltMode\": 0, \"Base\": 0.000000, \"Clamped\": -1, \"Extruded\": 0, \"Snippet\": null, \"PopupInfo\": null, \"Shape_Leng\": 0.012607, \"Shape_Area\": 0.000007, \"centroid_x\": 85.417179, \"centroid_y\": 27.680395 }, \"geometry\": { \"type\": \"Polygon\", \"coordinates\": [ [ [ 85.415303373000086, 27.679989768000041, 0.0 ], [ 85.415277308000043, 27.680134926000051, 0.0 ], [ 85.415827045000071, 27.680514818000063, 0.0 ], [ 85.415852616000052, 27.680570309000075, 0.0 ], [ 85.415862708000077, 27.680603395000048, 0.0 ], [ 85.41587284700006, 27.680636636000031, 0.0 ], [ 85.416106529000047, 27.681049091000034, 0.0 ], [ 85.416137108000044, 27.681089148000069, 0.0 ], [ 85.416173448000052, 27.681118384000058, 0.0 ], [ 85.416193147000058, 27.681152036000071, 0.0 ], [ 85.416227097000046, 27.68120175100006, 0.0 ], [ 85.416247048000059, 27.681235813000058, 0.0 ], [ 85.416462025000044, 27.681501283000046, 0.0 ], [ 85.416497210000045, 27.681523008000056, 0.0 ], [ 85.416526995000083, 27.681556507000039, 0.0 ], [ 85.416592538000032, 27.681612219000044, 0.0 ], [ 85.416646683000067, 27.68166099900003, 0.0 ], [ 85.416844922000053, 27.681822559000068, 0.0 ], [ 85.416890616000046, 27.681843208000032, 0.0 ], [ 85.416957741000033, 27.68186935600005, 0.0 ], [ 85.417037677000053, 27.681903366000029, 0.0 ], [ 85.417376898000043, 27.682026827000072, 0.0 ], [ 85.417402627000058, 27.682042662000072, 0.0 ], [ 85.417468525000061, 27.682059585000047, 0.0 ], [ 85.417543761000047, 27.682074269000054, 0.0 ], [ 85.417578934000062, 27.682087940000031, 0.0 ], [ 85.417910330000041, 27.682163674000037, 0.0 ], [ 85.417978207000033, 27.68218108700006, 0.0 ], [ 85.418046356000048, 27.682198570000025, 0.0 ], [ 85.418096320000075, 27.682220814000061, 0.0 ], [ 85.41897162600003, 27.682258963000038, 0.0 ], [ 85.419032911000045, 27.682255419000057, 0.0 ], [ 85.419238048000068, 27.682179251000036, 0.0 ], [ 85.419233632000044, 27.681978620000052, 0.0 ], [ 85.419178307000038, 27.681887716000062, 0.0 ], [ 85.419125445000077, 27.681843378000053, 0.0 ], [ 85.418650998000032, 27.681406640000034, 0.0 ], [ 85.418637315000069, 27.681377816000065, 0.0 ], [ 85.418601082000066, 27.681301488000031, 0.0 ], [ 85.418592082000032, 27.681282528000054, 0.0 ], [ 85.418542241000068, 27.68105468300007, 0.0 ], [ 85.418533120000063, 27.680974964000029, 0.0 ], [ 85.418524289000061, 27.680936496000072, 0.0 ], [ 85.418498127000078, 27.680842248000033, 0.0 ], [ 85.418476598000041, 27.680777831000057, 0.0 ], [ 85.418400205000069, 27.680698445000075, 0.0 ], [ 85.418362259000048, 27.680678684000043, 0.0 ], [ 85.418274050000036, 27.680652332000079, 0.0 ], [ 85.418232443000079, 27.680624105000049, 0.0 ], [ 85.418199422000043, 27.680593884000075, 0.0 ], [ 85.418162208000069, 27.680574504000049, 0.0 ], [ 85.418108829000062, 27.680540241000074, 0.0 ], [ 85.417953272000034, 27.680484976000059, 0.0 ], [ 85.417916903000048, 27.68046600100007, 0.0 ], [ 85.417843838000067, 27.680447104000052, 0.0 ], [ 85.417727944000035, 27.680392909000034, 0.0 ], [ 85.417685023000047, 27.680357733000051, 0.0 ], [ 85.417662307000057, 27.680326834000027, 0.0 ], [ 85.417552404000048, 27.680138326000076, 0.0 ], [ 85.417507141000044, 27.680035102000033, 0.0 ], [ 85.417503687000078, 27.68002722600005, 0.0 ], [ 85.417734299000074, 27.679907624000066, 0.0 ], [ 85.417778455000075, 27.679887729000029, 0.0 ], [ 85.417846409000049, 27.679861796000068, 0.0 ], [ 85.417846217000033, 27.679767201000061, 0.0 ], [ 85.417710734000082, 27.679666076000046, 0.0 ], [ 85.417695926000079, 27.679653067000061, 0.0 ], [ 85.417646608000041, 27.67956576000006, 0.0 ], [ 85.417636321000032, 27.679543625000065, 0.0 ], [ 85.417614922000041, 27.679516192000051, 0.0 ], [ 85.41746773300008, 27.679272139000034, 0.0 ], [ 85.417426679000073, 27.679219751000062, 0.0 ], [ 85.41689659900004, 27.678480911000065, 0.0 ], [ 85.416869220000081, 27.678419296000072, 0.0 ], [ 85.416844817000083, 27.678364378000026, 0.0 ], [ 85.416815692000057, 27.678351531000033, 0.0 ], [ 85.416753356000072, 27.678333763000069, 0.0 ], [ 85.416739365000069, 27.678337319000036, 0.0 ], [ 85.416659034000077, 27.678330969000058, 0.0 ], [ 85.416628535000086, 27.678332046000037, 0.0 ], [ 85.416593645000034, 27.678340913000056, 0.0 ], [ 85.416516889000036, 27.678360422000026, 0.0 ], [ 85.416482, 27.678369290000035, 0.0 ], [ 85.41631936400006, 27.678423982000027, 0.0 ], [ 85.416300818000082, 27.678435384000068, 0.0 ], [ 85.416270666000059, 27.678456445000052, 0.0 ], [ 85.416182140000046, 27.678505821000044, 0.0 ], [ 85.416165809000063, 27.678523450000057, 0.0 ], [ 85.416121289000046, 27.678588956000056, 0.0 ], [ 85.416102423000041, 27.678621012000065, 0.0 ], [ 85.416090630000042, 27.678630842000075, 0.0 ], [ 85.416052733000072, 27.678674743000045, 0.0 ], [ 85.416043040000034, 27.678711651000071, 0.0 ], [ 85.41603329700007, 27.678748749000079, 0.0 ], [ 85.415996816000074, 27.678841846000068, 0.0 ], [ 85.415910991000032, 27.678998536000051, 0.0 ], [ 85.415795986000035, 27.679290817000037, 0.0 ], [ 85.415790323000067, 27.679322110000044, 0.0 ], [ 85.415725646000055, 27.679444047000061, 0.0 ], [ 85.415585763000081, 27.679695442000025, 0.0 ], [ 85.415552368000078, 27.679719600000055, 0.0 ], [ 85.415528664000078, 27.679749195000056, 0.0 ], [ 85.41548970000008, 27.679782746000058, 0.0 ], [ 85.415385164000043, 27.679872758000045, 0.0 ], [ 85.41537203300004, 27.679884065000067, 0.0 ], [ 85.41535547400008, 27.679912236000064, 0.0 ], [ 85.415303373000086, 27.679989768000041, 0.0 ] ] ] } }]}");

            Drawable defaultMarker = getResources().getDrawable(R.drawable.mapbox_marker_icon_default);
            Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
//
            KmlFeature.Styler styler = new OverlayPopupHiddenStyler();
            myOverLayBoarder = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, styler, kmlDocument);

            runOnUiThread(() -> {
                mapView.getOverlays().add(myOverLayBoarder);
                mapView.getOverlays().add(0, mapEventsOverlay);
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
//                        MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
//                        mapView.getOverlayFs().add(mapMarkerOverlayUtils.overlayFromHospitalAndCommon(HomeActivity.this, hospitalAndCommon, mapView));
//                        mapView.invalidate();
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


    @OnClick(R.id.fab_map_layer)
    public void showMapLayerPopup() {

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

        emulateTabBehavaiour(view.getId());
        if (slidingPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        }
    }

    private void arrowAnimation() {

        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);

        CountDownTimer timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(20), TimeUnit.SECONDS.toMillis(2)) {
            public void onTick(long millisUntilFinished) {
                updownloadToggleIcon.startAnimation(animShake);
            }

            public void onFinish() {
            }
        };
        timer.start();
    }


    private void setupSlidingPanel() {
        updownloadToggleIcon.setState(ExpandIconView.LESS, true);
        arrowAnimation();

        slidingPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case EXPANDED:
                        updownloadToggleIcon.setState(ExpandIconView.MORE, true);
                        break;
                    default:
                        updownloadToggleIcon.setState(ExpandIconView.LESS, true);

                        break;
                }
            }
        });
    }

    private void emulateTabBehavaiour(int tappedID) {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(R.id.tv_resources);
        a.add(R.id.tv_hazard_and_vulnerability);
        a.add(R.id.tv_base_data);
        a.remove((Integer) tappedID);

        ((TextView) findViewById(tappedID)).setTypeface(null, Typeface.BOLD);
        ((TextView) findViewById(tappedID)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        ((TextView) findViewById(tappedID)).setBackgroundColor(getResources().getColor(R.color.mapboxWhite));
        for (Integer curId : a) {
            ((TextView) findViewById(curId)).setBackgroundColor(getResources().getColor(R.color.colorWindowBackground));
            ((TextView) findViewById(curId)).setTypeface(null, Typeface.NORMAL);
            ((TextView) findViewById(curId)).setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }


    int wardShowCount = 0;

    @OnClick(R.id.fab_map_layer)
    public void onViewClicked(View view) {
        PopupMenu popup = new PopupMenu(HomeActivity.this, fabMapLayer);
        popup.getMenuInflater().inflate(R.menu.layer_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_ward:
                    wardShowCount++;
                    if (wardShowCount % 2 == 0) {
                        mapView.getOverlays().clear();
                        mapView.getOverlays().remove(myOverLayWardBoarder);
                        mapView.removeAllViews();
                        mapView.getOverlays().add(myOverLayBoarder);
                        mapView.invalidate();

                        showOverlayOnMap(geoJsonFileName, geoJsonType, geoJsonmarkerImage);
                        showDataOnList(geoJsonName, geoJsonType);

                        Log.d(TAG, "onViewClicked: hide boundary");
                        return true;
                    }
                    showOverlayOnMap("changunarayan_new_wards.geojson", MapDataCategory.BOUNDARY, R.drawable.marker_default);
                    showOverlayOnMap(geoJsonFileName, geoJsonType, geoJsonmarkerImage);
                    Log.d(TAG, "onViewClicked: show boundary with dataset");
                    showDataOnList(geoJsonName, geoJsonType);

                    break;
                case R.id.menu_municipal:
                    loadMunicipalityBoarder();
                    break;
                case R.id.menu_office:
                    break;
            }
            return true;
        });
        popup.show();
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Log.d(TAG, "singleTapConfirmedHelper:  ");
        InfoWindow.closeAllInfoWindowsOn(mapView);
        InfoWindow.getOpenedInfoWindowsOn(mapView).clear();
        slidingPanel.setPanelHeight(110);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        Log.d(TAG, "longPressHelper: ");
        return false;
    }


}


