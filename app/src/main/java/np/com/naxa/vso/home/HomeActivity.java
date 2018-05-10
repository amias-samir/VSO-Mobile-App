package np.com.naxa.vso.home;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.R;
import np.com.naxa.vso.ReportActivity;
import np.com.naxa.vso.database.databaserepository.CommonPlacesAttrbRepository;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.emergencyContacts.ExpandableUseActivity;
import np.com.naxa.vso.home.model.MapMarkerItem;
import np.com.naxa.vso.home.model.MapMarkerItemBuilder;
import np.com.naxa.vso.utils.JSONParser;
import np.com.naxa.vso.utils.ToastUtils;
import np.com.naxa.vso.viewmodel.CommonPlacesAttribViewModel;
import np.com.naxa.vso.viewmodel.EducationalInstitutesViewModel;
import np.com.naxa.vso.viewmodel.HospitalFacilitiesVewModel;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static np.com.naxa.vso.activity.OpenSpaceActivity.LOCATION_RESULT;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPanel;

    @BindView(R.id.mapView)
    MapView mapboxMapview;

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

    private String latitude;
    private String longitude;

    private final int RESULT_LOCATION_PERMISSION = 10;
    private final int RESULT_LAT_LONG = 15;


    private MapDataRepository repo;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new_new);
        ButterKnife.bind(this);
        repo = new MapDataRepository();

        fabLocationToggle.setOnClickListener(this);
        setupMapBox();
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
            // Add an observer on the LiveData returned by getAlphabetizedWords.
            // The onChanged() method fires when the observed data changes and the activity is
            // in the foreground.
            commonPlacesAttribViewModel.getmAllCommonPlacesAttrb().observe(this, new android.arch.lifecycle.Observer<List<CommonPlacesAttrb>>() {
                @Override
                public void onChanged(@Nullable final List<CommonPlacesAttrb> commonPlacesAttrbs) {
                    // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);

                    for (int i = 0; i < commonPlacesAttrbs.size(); i++) {
                        commonPlacesAttrbsList.add(commonPlacesAttrbs.get(i));
                    }
                    Log.d("HomeActivity", "onChanged: insert " + "one more new  data inserted");
                }
            });

        } catch (NullPointerException e) {

            Log.d(TAG, "Exception: " + e.toString());
        }


    }


    private void setupMapBox() {
        Mapbox.getInstance(this, getString(R.string.access_token));

        mapboxMapview.getMapAsync(mapboxMap -> {
            this.mapboxMap = mapboxMap;
            clusterManagerPlugin = new ClusterManagerPlugin<>(this, mapboxMap);
            mapboxMap.addOnCameraIdleListener(clusterManagerPlugin);
            mapboxMap.getUiSettings().setAllGesturesEnabled(true);

            showOverlayOnMap(-1);
            moveCamera(new LatLng(27.657531140175244, 85.46161651611328));

        });
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
        }, 1000);
        // toggleSliderHeight();

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
                        loadLineLayers(assetName, fileContent);
                        loadMarkersFromGeoJson(assetName, fileContent);

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
                        ((CategoriesDetailAdapter) recyclerViewDataDetails.getAdapter()).replaceData(myItems);
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

            mapboxMapview.getMapAsync(mapboxMap -> {
                ToastUtils.showToast("Marker Added");
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                        .title("Current Location")

                );
            });

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case RESULT_LOCATION_PERMISSION:
                handleGps();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case RESULT_LOCATION_PERMISSION:
                ToastUtils.showToast("Give location permission to take full advantage.");
                break;
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
                if (!EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    EasyPermissions.requestPermissions(this, "Provide location permission.",
                            RESULT_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
                } else {
                    handleGps();
                }
                break;
        }
    }

    private void handleGps() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!statusOfGPS) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        } else {

        }
    }

    private void saveGeoJsonDataToDatabase(int pos, String geoJson) {
        if (pos == 0) {
//            save hospital data
            saveHospitalData(geoJson);
        }

        if (pos == 1) {
//            save openspace data
            saveOpenSpaces(geoJson);
        }

        if (pos == 2) {
//            save school data
            saveEducationalInstitutes(geoJson);
        }

    }

    private void saveHospitalData(String geoJsonString) {

        CommonPlacesAttrbRepository.pID.clear();

        JSONObject jsonObject = null;
        String name = null, address = null, type = null, remarks = null, ambulance = null, contact_no = null, contact_pe = null,
                earthquake = null, emergency = null, fire_extin = null, icu_service = null, number_of = null, open_space = null,
                structure = null, toilet_fac;
        Long fk_common_places = null;
        Double latitude = 0.0, longitude = 0.0;
        try {
            jsonObject = new JSONObject(geoJsonString);

            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                type = properties.getString("Type");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, type, latitude, longitude, remarks);
                commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                long insertedRowId = CommonPlacesAttrbRepository.rowID;
                Log.d(TAG, "saveHospitalData: " + insertedRowId);

            }

            ArrayList<Long> insertedRowPiD = CommonPlacesAttrbRepository.pID;
            if (insertedRowPiD.size() == jsonarray.length()) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));

                    fk_common_places = insertedRowPiD.get(i);
                    ambulance = properties.getString("Ambulance_");
                    contact_no = properties.getString("Contact_Nu");
                    contact_pe = properties.getString("Contact_Pe");
                    earthquake = properties.getString("Earthquake");
                    emergency = properties.getString("Emergency_");
                    fire_extin = properties.getString("Fire_Extin");
                    icu_service = properties.getString("ICU_Servic");
                    number_of = properties.getString("Number_of_");
                    open_space = properties.getString("Open_Space");
                    structure = properties.getString("Structure_");
                    toilet_fac = properties.getString("Toilet_Fac");

                    HospitalFacilities hospitalFacilities = new HospitalFacilities(fk_common_places, ambulance, contact_no, contact_pe, earthquake, emergency,
                            fire_extin, icu_service, number_of, open_space, structure, toilet_fac);
                    hospitalFacilitiesVewModel.insert(hospitalFacilities);
//                long insertedRowId = CommonPlacesAttrbRepository.rowID;
//                Log.d(TAG, "saveHospitalData: " + insertedRowId);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void saveEducationalInstitutes(String geoJsonString) {

        CommonPlacesAttrbRepository.pID.clear();

        JSONObject jsonObject = null;

        Double latitude = 0.0, longitude = 0.0;
        String name = null, address = null, type = null, remarks = null;

        Long fk_common_places = null;
        String college_he = null, contact_no = null, contact_pe = null, earthquake = null, evacuation = null, fire_extin = null,
                female_student = null, male_student = null, total_student = null, level = null, structure = null, open_space = null;
        try {
            jsonObject = new JSONObject(geoJsonString);

            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                type = properties.getString("Type");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, type, latitude, longitude, remarks);
                commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                long insertedRowId = CommonPlacesAttrbRepository.rowID;
                Log.d(TAG, "saveEducationalInstitutes: " + insertedRowId);

            }

            ArrayList<Long> insertedRowPiD = CommonPlacesAttrbRepository.pID;
            if (insertedRowPiD.size() == jsonarray.length()) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));

                    fk_common_places = insertedRowPiD.get(i);
                    college_he = properties.getString("College_He");
                    contact_no = properties.getString("Contact_Nu");
                    contact_pe = properties.getString("Contact_Pe");
                    earthquake = properties.getString("Earthquake");
                    evacuation = properties.getString("Evacuation");
                    fire_extin = properties.getString("Fire_Extin");
                    male_student = properties.getString("Male_Stude");
                    female_student = properties.getString("Female_Stu");
                    total_student = properties.getString("Total_Stud");
                    open_space = properties.getString("Open_Space");
                    structure = properties.getString("Structure_");

                    EducationalInstitutes educationalInstitutes = new EducationalInstitutes(fk_common_places, college_he, contact_no, contact_pe, earthquake,
                            evacuation, female_student, male_student, total_student, fire_extin, level, open_space, structure);
                    educationalInstitutesViewModel.insert(educationalInstitutes);
//                long insertedRowId = CommonPlacesAttrbRepository.rowID;
//                Log.d(TAG, "saveHospitalData: " + insertedRowId);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveOpenSpaces(String geoJsonString){
        CommonPlacesAttrbRepository.pID.clear();

        JSONObject jsonObject = null;

        Double latitude = 0.0, longitude = 0.0;
        String name = null, address = null, type = null, remarks = null;

        Long fk_common_places = null;
        String access_roa = null, accommodat = null, area_sqm = null, hign_tensi = null, road_access = null, shape_area = null,
                shape_leng = null, terrain_ty = null, toilet_fac = null, water_faci = null, wifi_facil = null;
        try {
            jsonObject = new JSONObject(geoJsonString);

            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                name = properties.getString("name");
                address = properties.getString("Address");
                type = properties.getString("Type");
                latitude = Double.parseDouble(properties.getString("Y"));
                longitude = Double.parseDouble(properties.getString("X"));
                remarks = properties.getString("Remarks");

                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, type, latitude, longitude, remarks);
                commonPlacesAttribViewModel.insert(commonPlacesAttrb);
                long insertedRowId = CommonPlacesAttrbRepository.rowID;
                Log.d(TAG, "saveOpenSpaces: " + insertedRowId);

            }

            ArrayList<Long> insertedRowPiD = CommonPlacesAttrbRepository.pID;
            if (insertedRowPiD.size() == jsonarray.length()) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));

                    fk_common_places = insertedRowPiD.get(i);
                    access_roa = properties.getString("Access_Roa");
                    accommodat = properties.getString("Accommodat");
                    area_sqm = properties.getString("Area_SqM");
                    hign_tensi = properties.getString("High_Tensi");
                    road_access = properties.getString("Road_Acces");
                    shape_area = properties.getString("Shape_Area");
                    shape_leng = properties.getString("Shape_Leng");
                    terrain_ty = properties.getString("Terrain_Ty");
                    toilet_fac = properties.getString("Toilet_Fac");
                    water_faci = properties.getString("Water_Faci");
                    wifi_facil = properties.getString("Wifi_Facil");

                    OpenSpace openSpace = new OpenSpace(fk_common_places, access_roa, accommodat, area_sqm, hign_tensi, road_access, shape_area,
                            shape_leng, terrain_ty, toilet_fac, water_faci, wifi_facil);
                    openSpaceViewModel.insert(openSpace);
//                long insertedRowId = CommonPlacesAttrbRepository.rowID;
//                Log.d(TAG, "saveHospitalData: " + insertedRowId);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}