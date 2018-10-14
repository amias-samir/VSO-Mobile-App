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
import android.os.StrictMode;
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
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterManagerPlugin;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
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
import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;
import np.com.naxa.vso.database.entity.GeoJsonListEntity;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.detailspage.MarkerDetailsDisplayActivity;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.firebase.MessageActivity;
import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.lineString.LineStringFeature;
import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.lineString.LineStringFeatureCollection;
import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.lineString.LineStringGeometry;
import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.multiLineString.MultiLineStringFeature;
import np.com.naxa.vso.geojasonPojo.lineStringAndMultiLineString.multiLineString.MultiLineStringFeatureCollection;
import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.multipolygon.MultiPolygonFeature;
import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.multipolygon.MultiPolygonFeatureCollection;
import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.polygon.PolygonFeature;
import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.polygon.PolygonFeatureCollection;
import np.com.naxa.vso.geojasonPojo.polygonAndMultipolygon.polygon.PolygonGeometry;
import np.com.naxa.vso.home.model.MapDataCategory;
import np.com.naxa.vso.home.model.MapMarkerItem;
import np.com.naxa.vso.hospitalfilter.HospitalFilterActivity;
import np.com.naxa.vso.hospitalfilter.SortedHospitalItem;
import np.com.naxa.vso.sudur.activities.SplashScreenActivity;
import np.com.naxa.vso.utils.NetworkUtils;
import np.com.naxa.vso.utils.SharedPreferenceUtils;
import np.com.naxa.vso.utils.ToastUtils;
import np.com.naxa.vso.utils.maputils.MapCommonUtils;
import np.com.naxa.vso.utils.maputils.MapGeoJsonToObject;
import np.com.naxa.vso.utils.maputils.MapMarkerOverlayUtils;
import np.com.naxa.vso.utils.maputils.MyLocationService;
import np.com.naxa.vso.utils.maputils.SortingDistance;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.EducationalInstitutesViewModel;
import np.com.naxa.vso.viewmodel.GeoJsonCategoryViewModel;
import np.com.naxa.vso.viewmodel.GeoJsonListViewModel;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

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

    @BindView(R.id.iv_options_hamburger)
    ImageView ivOptionsHamburger;

    private IMapController mapController;
    private GeoPoint centerPoint;

    FolderOverlay myOverLay;
    FolderOverlay myOverLayBoarder;
    FolderOverlay myOverlayMunicipalityBorder;
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
    private ClusterManagerPlugin<MapMarkerItem> clusterManagerPlugin;
    private boolean isGridShown = true;
    private int gridPosition = -1;
    private int mainCategoryPosition = 0;

    //location listner
    protected DirectedLocationOverlay myLocationOverlay;
    protected LocationManager mLocationManager;
    private GeoPoint currentLocation;
    private ArrayList<GeoPoint> municipalityGeoPointList;
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

    List<GeoJsonCategoryEntity> geoJsonCategoryEntityList;

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

        fabLocationToggle.setOnClickListener(this);

        overlaysList = new ArrayList<>();

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

        setupMap();

        setupBottomBar();
        setupListRecycler();


        GeoJsonCategoryViewModel geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);
        geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Exposure_Data").toObservable()
                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                    @Override
                    public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {
//                        geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                        setupGridRecycler(MySection.getResourcesCatergorySections(geoJsonCategoryEntities));


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: init Resource");

                    }
                });


        slidingPanel.setAnchorPoint(0.4f);
        slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        setupSlidingPanel();


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

        loadBorder("wards");
        loadBorder("municipal_boundary");

//        loadMunicipalityBoarder();

        mapController.setCenter(centerPoint);

//        for clustering
//        overlaysList = this.mapView.getOverlays();
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
                    if (NetworkUtils.isNetworkAvailable()) {
                        progressbar.setVisibility(View.VISIBLE);
                        routeLocation();
                    } else {
                        ToastUtils.showToast("Internet Not Available");
                    }
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
//                intent.putExtra("data", new Gson().toJson(item));
                intent.putExtra("data", item.getDetailed_properties());
                startActivity(intent);
            }
        });

    }

    String geoJsonFileName = "", geoJsonType = "", geoJsonName = "";
    int geoJsonmarkerImage;


    SectionAdapter sectionAdapter;

    private void setupGridRecycler(List<MySection> mySections) {
        Log.d(TAG, "setupGridRecycler: size :--> " + mySections.size());
        if (mainCategoryPosition <= 0) {
            LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            recyclerDataCategories.setLayoutManager(mLayoutManager);
            sectionAdapter = new SectionAdapter(R.layout.square_image_title, R.layout.list_section_header, mySections);
            recyclerDataCategories.setAdapter(sectionAdapter);
            Log.d(TAG, "setupGridRecycler: inside if");
        } else {
            sectionAdapter.replaceData(mySections);
            sectionAdapter.notifyDataSetChanged();
            recyclerDataCategories.notify();
            Log.d(TAG, "setupGridRecycler: inside else");

        }

        Log.d(TAG, "setupGridRecycler:  setup success");

        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            wardShowCount = 0;
            MySection section = sectionAdapter.getData().get(position);
            MapDataCategory gridItem = section.t;

            if (gridItem.getName() == null) {
                ToastUtils.showToast("Error loading " + section.t.getName());
                return;
            }

            geoJsonFileName = section.t.getFileName();
            geoJsonName = section.t.getName();
            geoJsonType = section.t.getType();
            geoJsonmarkerImage = section.t.getMarker_image();

            showOverlayOnMap(geoJsonFileName, geoJsonType, geoJsonmarkerImage);

            InfoWindow.closeAllInfoWindowsOn(mapView);

            gridPosition = position;

        });

    }


    private void showDataOnList(String name, String type) {
        commonPlacesAttribViewModel.getPlaceByType(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<CommonPlacesAttrb>>() {
                    @SuppressLint("StringFormatInvalid")
                    @Override
                    public void onNext(List<CommonPlacesAttrb> commonPlacesAttrbs) {

                        if (commonPlacesAttrbs.size() == 0) {
                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            return;
                        }

                        CategoryListAdapter adapter = ((CategoryListAdapter) recyclerViewDataDetails.getAdapter());
                        adapter.replaceData(commonPlacesAttrbs);

                        switchViews();

                        dataSetInfoText = getString(R.string.dataset_overview, commonPlacesAttrbs.size() + "");

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
                rlMainCategoryList.setVisibility(View.VISIBLE);
                clearClusterAndMarkers();
                break;
            case 1:
//                tvDataSet.setText(generateDataCardText());
                llInsetData.setVisibility(View.GONE);

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

    private void showOverlayOnMap(String name, String type, int marker_image) {

        Log.d(TAG, "showOverlayOnMap: " + name);

        Publisher<GeoJsonListEntity> pub = LiveDataReactiveStreams.toPublisher(this, geoJsonListViewModel.getmSpecificGeoJsonEntity(name));
        Observable.fromPublisher(pub)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<GeoJsonListEntity, ObservableSource<GeoJsonListEntity>>) geoJsonListEntity -> Observable.just(geoJsonListEntity))
                .subscribe(new DisposableObserver<GeoJsonListEntity>() {
                    @Override
                    public void onNext(GeoJsonListEntity geoJsonListEntity) {
                        String fileContent = geoJsonListEntity.getCategoryJson();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(fileContent);
                            String dataType = jsonObject.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getString("type");
                            switch (dataType) {
                                case "Point":
                                    loadlayerToMap(fileContent, type, name, marker_image);
                                    showDataOnList(name, type);
                                    break;
                                case "MultiLineString":
                                    switch (name) {
                                        case "river":
                                            loadBorder("river");
                                            return;
//                                        case "road":
//                                            loadBorder("road");
//                                            return;
                                        default:
                                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                    }
                                    break;
                                case "MultiPolygon":
                                    switch (name) {
                                        case "municipal_boundary":
                                            primaryLayerChange(myOverlayMunicipalityBorder);
                                            secondaryLayerChange(null);
                                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                            break;
                                        case "wards":
                                            primaryLayerChange(myOverLayWardBoarder);
                                            secondaryLayerChange(null);
                                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                            break;
                                        case "open_spaces":
                                            loadlayerToMap(fileContent, type, name, marker_image);
                                            loadBorder("open_spaces");
                                            showDataOnList(geoJsonFileName, geoJsonType);
                                        default:
                                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                    }
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            ToastUtils.showToast("No Dataset Found.");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            ToastUtils.showToast("Error Loading.");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Failed to load geojson ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onError: GeoJson loaded sucessfully");

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

                if (isPointInPolygon(currentLocation, municipalityGeoPointList)) {
                    mapView.getOverlays().add(myLocationNewOverlay);
                    myLocationNewOverlay.enableMyLocation();
//                myLocationNewOverlay.enableFollowLocation();
                    mapView.invalidate();
                } else {
                    ToastUtils.showToast("Current Location Not In ChanguNarayan");
                }


            }
        } else {
            EasyPermissions.requestPermissions(this, "Provide location permission.",
                    RESULT_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    //
    //Ray Casting algorithm which identifies point in polygon
    //
    private boolean isPointInPolygon(GeoPoint tap, ArrayList<GeoPoint> vertices) {
        int intersectCount = 0;
        if (vertices == null) return false;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(GeoPoint tap, GeoPoint vertA, GeoPoint vertB) {

        double aY = vertA.getLatitude();
        double aX = vertA.getLongitude();
        double bY = vertB.getLatitude();
        double bX = vertB.getLongitude();
        double pY = tap.getLatitude();
        double pX = tap.getLongitude();

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
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
            secondaryLayerChange(null);
            switchViews();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_location_toggle:
                handleLocationPermission();
                if (currentLocation == null) {
                    Toast.makeText(this, "Searching Current Location", Toast.LENGTH_SHORT).show();
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
        myOverLay = new FolderOverlay();

        SortingDistance sortingDistance = new SortingDistance();

        points[0] = currentLocation;
        final String[] nearestOpenSpace = new String[2];

        commonPlacesAttribViewModel.getPlaceByType("open_spaces")
                .subscribeOn(Schedulers.io())
                .flatMap((Function<List<CommonPlacesAttrb>, Publisher<Polyline>>) openAndCommons -> {
                    LinkedHashMap linkedOpenAndCommon = sortingDistance.sortingOpenSpaceDistanceData(openAndCommons,
                            currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                    Set<CommonPlacesAttrb> keySet = linkedOpenAndCommon.keySet();
                    List<CommonPlacesAttrb> sortedOpenlist = new ArrayList<CommonPlacesAttrb>(keySet);

                    Collection<Float> values = linkedOpenAndCommon.values();
                    ArrayList<Float> sortedDistanceList = new ArrayList<Float>(values);

                    nearestOpenSpace[0] = sortedOpenlist.get(0).getName();
                    Float distance = sortedDistanceList.get(0);
                    if (sortedDistanceList.get(0) > 1000) {
                        nearestOpenSpace[1] = (distance / 1000) + " Kms. away";
                    } else {
                        nearestOpenSpace[1] = distance + " Meters away";
                    }

                    points[1] = new GeoPoint(sortedOpenlist.get(0).getLatitude(),
                            sortedOpenlist.get(0).getLongitude());

                    return routeGenerateObservable(points);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<Polyline>() {
                    @Override
                    public void onNext(Polyline roadOverlay) {
                        if (roadOverlay.getPoints().size() != 2) {

                            if (viewSwitcherSlideLayout.getDisplayedChild() == 1) switchViews();

                            llInsetData.setVisibility(View.VISIBLE);
                            tvNameTitle.setText(nearestOpenSpace[0]);
                            tvDistanceSubtitle.setText(nearestOpenSpace[1]);

//                            mapView.getOverlays().add(getMarkerOverlay(points));
//                            mapView.getOverlays().add(roadOverlay
// );
                            myOverLay.add(roadOverlay);
                            myOverLay.add(getMarkerOverlay(points));

                            secondaryLayerChange(myOverLay);
                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            mapView.getController().animateTo(points[0]);

                            // handleLocationPermission for current location position overlay
                            handleLocationPermission();
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

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
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
                                myOverLay.add(nodeMarker);
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

        try {
            final KmlDocument kmlDocument = new KmlDocument();
            kmlDocument.parseGeoJSON(geoJson);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Drawable defaultMarker = ContextCompat.getDrawable(HomeActivity.this, R.drawable.map_marker_blue);

        List<MySection> gridItems = new ArrayList<>();
        GeoJsonCategoryViewModel geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);

        geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Exposure_Data").toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                    @Override
                    public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {

//                        geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                        gridItems.addAll(MySection.getResourcesCatergorySections(geoJsonCategoryEntities));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: loadLayer Resource");
                    }
                });

        geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Hazard_Data").toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                    @Override
                    public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {
//                        geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                        gridItems.addAll(MySection.getHazardCatergorySections(geoJsonCategoryEntities));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: loadLayer Hazard");


                    }
                });


        geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Baseline_Data").toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                    @Override
                    public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {
//                        geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                        gridItems.addAll(MySection.getBaseDataCatergorySections(geoJsonCategoryEntities));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: loadLayer Basedata");

                    }
                });


//        gridItems.addAll(MySection.getBaseDataCatergorySections());
//        gridItems.addAll(MySection.getHazardCatergorySections());

        // defaultMarker.setColorFilter(color, PorterDuff.Mode.DST_ATOP);
        Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
        poiMarkers.setIcon(defaultBitmap);

        MapMarkerOverlayUtils mapMarkerOverlayUtils = new MapMarkerOverlayUtils();
        MapGeoJsonToObject mapGeoJsonToObject = new MapGeoJsonToObject();
//        mapGeoJsonToObject.getCommonPlacesListObj(HomeActivity.this, geoJson, name, mapView, mapMarkerOverlayUtils, myOverLay, marker_image);
        mapGeoJsonToObject.getCommonPlacesListObj(HomeActivity.this, geoJson, name, mapView, mapMarkerOverlayUtils, null, marker_image);


    }

    private void loadBorder(String key) {
        Publisher<GeoJsonListEntity> publisher = LiveDataReactiveStreams.toPublisher(this, geoJsonListViewModel.getmSpecificGeoJsonEntity(key));
        Observable.fromPublisher(publisher)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<GeoJsonListEntity>() {
                    @Override
                    public void onNext(GeoJsonListEntity geoJsonListEntity) {
                        try {
                            loadGeoJsonByKml(geoJsonListEntity.getCategoryJson(), key);
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                            ToastUtils.showToast("Error loading geojson");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i("Shree", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private void loadGeoJsonByKml(String categoryJson, String key) throws JSONException {

        JSONObject jsonObject = new JSONObject(categoryJson);
        String type = jsonObject.getJSONArray("features").getJSONObject(0).getJSONObject("geometry").getString("type");
        switch (type) {
            case "MultiPolygon":
                MultiPolygonFeatureCollection multipolygonFeatureCollection = new Gson().fromJson(categoryJson, MultiPolygonFeatureCollection.class);

                Observable.just(multipolygonFeatureCollection)
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Function<MultiPolygonFeatureCollection, ObservableSource<List<MultiPolygonFeature>>>() {
                            @Override
                            public ObservableSource<List<MultiPolygonFeature>> apply(MultiPolygonFeatureCollection multipolygonFeatureCollection1) throws Exception {
                                return Observable.just(multipolygonFeatureCollection1.getFeatures());
                            }
                        })
                        .flatMapIterable(new Function<List<MultiPolygonFeature>, Iterable<MultiPolygonFeature>>() {
                            @Override
                            public Iterable<MultiPolygonFeature> apply(List<MultiPolygonFeature> multiPolygonFeatures) throws Exception {
                                return multiPolygonFeatures;
                            }
                        })
                        .flatMap(new Function<MultiPolygonFeature, ObservableSource<PolygonFeatureCollection>>() {
                            @Override
                            public ObservableSource<PolygonFeatureCollection> apply(MultiPolygonFeature multiPolygonFeature) throws Exception {
                                PolygonFeatureCollection polygonFeatureCollection = new PolygonFeatureCollection();
                                PolygonFeature polygonFeature = new PolygonFeature();
                                PolygonGeometry polygonGeometry = new PolygonGeometry();
                                polygonGeometry.setType("Polygon");
                                polygonGeometry.setCoordinates(multiPolygonFeature.getGeometry().getCoordinates().get(0));
                                polygonFeature.setType(multiPolygonFeature.getType());
                                polygonFeature.setProperties(multiPolygonFeature.getProperties());
                                polygonFeature.setGeometry(polygonGeometry);
                                List<PolygonFeature> polygonFeatureList = new ArrayList<>();
                                polygonFeatureList.add(polygonFeature);
                                polygonFeatureCollection.setFeatures(polygonFeatureList);
                                polygonFeatureCollection.setType(multipolygonFeatureCollection.getType());
                                return Observable.just(polygonFeatureCollection);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<PolygonFeatureCollection>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                switch (key) {
                                    case "municipal_boundary":
                                        municipalityGeoPointList = new ArrayList<>();
                                        myOverlayMunicipalityBorder = new FolderOverlay();
                                        break;
                                    case "wards":
                                        myOverLayWardBoarder = new FolderOverlay();
                                        break;
                                    case "open_spaces":
                                        break;
                                }
                            }

                            @Override
                            public void onNext(PolygonFeatureCollection polygonFeatureCollection) {
                                String geoJason = new Gson().toJson(polygonFeatureCollection);
                                final KmlDocument kmlDocument = new KmlDocument();
                                kmlDocument.parseGeoJSON(geoJason);
                                KmlFeature.Styler styler = new OverlayPopupHiddenStyler();
                                switch (key) {
                                    case "municipal_boundary":
                                        for (PolygonFeature polygonFeature : polygonFeatureCollection.getFeatures()) {
                                            for (List<List<Double>> list2 : polygonFeature.getGeometry().getCoordinates()) {
                                                for (List<Double> list : list2) {
                                                    municipalityGeoPointList.add(new GeoPoint(list.get(1), list.get(0)));
                                                }
                                            }
                                        }
                                        myOverlayMunicipalityBorder.add(kmlDocument.mKmlRoot.buildOverlay(mapView, null, styler, kmlDocument));
                                        break;
                                    case "wards":
                                        myOverLayWardBoarder.add(kmlDocument.mKmlRoot.buildOverlay(mapView, null, styler, kmlDocument));
                                        break;
                                    case "open_spaces":
                                        FolderOverlay tempFolderOverlay = (FolderOverlay) mapView.getOverlays().get(1);
                                        tempFolderOverlay.add(kmlDocument.mKmlRoot.buildOverlay(mapView, null, styler, kmlDocument));
                                        secondaryLayerChange(tempFolderOverlay);
                                        break;
                                }

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                if (key.equals("municipal_boundary")) {
                                    primaryLayerChange(myOverlayMunicipalityBorder);
                                    MapCommonUtils.zoomToMapBoundary(mapView, centerPoint);
                                    mapView.invalidate();
                                }

                            }
                        });


                break;
            case "MultiLineString":
                MultiLineStringFeatureCollection multiLineStringFeatureCollection = new Gson().fromJson(categoryJson, MultiLineStringFeatureCollection.class);
                Observable.just(multiLineStringFeatureCollection)
                        .subscribeOn(Schedulers.io())
                        .flatMap((Function<MultiLineStringFeatureCollection, ObservableSource<List<MultiLineStringFeature>>>) multiLineStringFeatureCollection1 -> Observable.just(multiLineStringFeatureCollection1.getFeatures()))
                        .flatMapIterable((Function<List<MultiLineStringFeature>, Iterable<MultiLineStringFeature>>) multiLineStringFeatures -> multiLineStringFeatures)
                        .flatMap((Function<MultiLineStringFeature, ObservableSource<LineStringFeatureCollection>>) multiLineStringFeature -> {
                            LineStringFeatureCollection lineStringFeatureCollection = new LineStringFeatureCollection();
                            LineStringFeature lineStringFeature = new LineStringFeature();
                            LineStringGeometry lineStringGeometry = new LineStringGeometry();
                            lineStringGeometry.setType("LineString");
                            lineStringGeometry.setCoordinates(multiLineStringFeature.getGeometry().getCoordinates().get(0));
                            lineStringFeature.setType(multiLineStringFeature.getType());
                            lineStringFeature.setProperties(multiLineStringFeature.getProperties());
                            lineStringFeature.setGeometry(lineStringGeometry);
                            List<LineStringFeature> lineStringFeatureList = new ArrayList<>();
                            lineStringFeatureList.add(lineStringFeature);
                            lineStringFeatureCollection.setFeatures(lineStringFeatureList);
                            lineStringFeatureCollection.setType(multiLineStringFeatureCollection.getType());
                            return Observable.just(lineStringFeatureCollection);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LineStringFeatureCollection>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                myOverLay = new FolderOverlay();
                            }

                            @Override
                            public void onNext(LineStringFeatureCollection lineStringFeatureCollection) {
                                String geoJason = new Gson().toJson(lineStringFeatureCollection);
                                final KmlDocument kmlDocument = new KmlDocument();
                                kmlDocument.parseGeoJSON(geoJason);
                                Drawable defaultMarker = ContextCompat.getDrawable(HomeActivity.this, R.drawable.map_marker_blue);
                                Bitmap defaultBitmap = ((BitmapDrawable) defaultMarker).getBitmap();
                                Style defaultStyle;
                                switch (key) {
                                    case "river":
                                        defaultStyle = new Style(defaultBitmap, Color.BLUE, 5f, 0x20AA1010);
                                        break;
                                    case "road":
                                        defaultStyle = new Style(defaultBitmap, Color.DKGRAY, 5f, 0x20AA1010);
                                        break;
                                    default:
                                        defaultStyle = null;
                                }
                                myOverLay.add(kmlDocument.mKmlRoot.buildOverlay(mapView, defaultStyle, null, kmlDocument));
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                secondaryLayerChange(myOverLay);
                                MapCommonUtils.zoomToMapBoundary(mapView, centerPoint);
                            }
                        });
                break;
        }


    }

    private void primaryLayerChange(FolderOverlay folderOverlay) {
        if (mapView.getOverlays().size() == 0) mapView.getOverlays().add(folderOverlay);
        else if (mapView.getOverlays().get(0) == folderOverlay) return;
        else mapView.getOverlays().set(0, folderOverlay);
        mapView.invalidate();
    }

    private void secondaryLayerChange(FolderOverlay folderOverlay) {
        if (mapView.getOverlays().size() <= 1) mapView.getOverlays().add(folderOverlay);
        else if (mapView.getOverlays().get(1) == folderOverlay) return;
        else mapView.getOverlays().set(1, folderOverlay);
        mapView.invalidate();
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

    @OnClick(R.id.iv_options_hamburger)
    public void optionsClick() {
        PopupMenu popup = new PopupMenu(HomeActivity.this, ivOptionsHamburger);
        popup.getMenuInflater()
                .inflate(R.menu.main_menu_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
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
                    case R.id.action_view_message:
                        startActivity(new Intent(HomeActivity.this, MessageActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });
        popup.show(); //showing popup menu
    }


    @OnClick({R.id.tv_resources, R.id.tv_hazard_and_vulnerability, R.id.tv_base_data})
    public void onMainCategoriesViewClicked(View view) {
        GeoJsonCategoryViewModel geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);

        switch (view.getId()) {
            case R.id.tv_resources:
                mainCategoryPosition = 1;
//                setupGridRecycler(MySection.getResourcesCatergorySections(geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Exposure_Data")));

                geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Exposure_Data").toObservable()
                        .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                            @Override
                            public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {
//                                geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                                setupGridRecycler(MySection.getResourcesCatergorySections(geoJsonCategoryEntities));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: Resources");
//                                setupGridRecycler(MySection.getResourcesCatergorySections(geoJsonCategoryEntityList));

                            }
                        });
                break;

            case R.id.tv_hazard_and_vulnerability:
                mainCategoryPosition = 2;
//                setupGridRecycler(MySection.getHazardCatergorySections());
                geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Hazard_Data").toObservable()
                        .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                            @Override
                            public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {
//                                geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                                setupGridRecycler(MySection.getHazardCatergorySections(geoJsonCategoryEntities));

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: Hazard");
//                                setupGridRecycler(MySection.getHazardCatergorySections(geoJsonCategoryEntityList));

                            }
                        });
                break;

            case R.id.tv_base_data:
                mainCategoryPosition = 3;
//                setupGridRecycler(MySection.getBaseDataCatergorySections());
                geoJsonCategoryViewModel.getAllGeoJsonCategoryEntityByType("Baseline_Data").toObservable()
                        .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<List<GeoJsonCategoryEntity>>() {
                            @Override
                            public void onNext(List<GeoJsonCategoryEntity> geoJsonCategoryEntities) {
//                                geoJsonCategoryEntityList.addAll(geoJsonCategoryEntities);
                                setupGridRecycler(MySection.getBaseDataCatergorySections(geoJsonCategoryEntities));

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: Base");
//                                setupGridRecycler(MySection.getBaseDataCatergorySections(geoJsonCategoryEntityList));

                            }
                        });
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
                case R.id.menu_municipal:
                    primaryLayerChange(myOverlayMunicipalityBorder);
                    break;
                case R.id.menu_ward:
                    primaryLayerChange(myOverLayWardBoarder);
                    break;
                case R.id.menu_sudur:
                    startActivity(new Intent(HomeActivity.this, SplashScreenActivity.class));
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


