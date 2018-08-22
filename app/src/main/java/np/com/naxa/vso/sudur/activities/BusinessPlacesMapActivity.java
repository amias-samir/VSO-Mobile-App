package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonLayer;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.fragment.PlaceDetailsBottomSheet;
import np.com.naxa.vso.sudur.model.local.Bussiness;
import np.com.naxa.vso.sudur.model.local.DatabaseHelper;
import np.com.naxa.vso.sudur.model.rest.ApiClient;
import np.com.naxa.vso.sudur.model.rest.ApiInterface;
import np.com.naxa.vso.sudur.model.rest.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusinessPlacesMapActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.business_list_spinner)
    Spinner businessListSpinner;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GoogleMap map;
    private ArrayList<Marker> markersPresentOnMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_places_map);
        ButterKnife.bind(this);

        markersPresentOnMap = new ArrayList<>();

        initMap();
        setToolbar();

        tryToSetSpinner();
        String lastSyncDate = DatabaseHelper.getInstance(getApplicationContext()).getLastSyncDate(DatabaseHelper.TABLE_BUSINESS_PLACES);


        fetchBussinesFromServer(lastSyncDate);

    }

    private void setToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {

        this.map = map;

        map.setOnMarkerClickListener(this);
        map.setPadding(0, 120, 0, 120);//to stop UI buttons to being overlapped and hidden

        String lastSyncDate = DatabaseHelper.getInstance(getApplicationContext()).getLastSyncDate(DatabaseHelper.TABLE_BUSINESS_PLACES);
        fetchBussinesFromServer(lastSyncDate);

        setSudurCamera(map);

        try {
            setDistrictGeoJSON(map);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    private void setDistrictGeoJSON(GoogleMap myMap) throws IOException, JSONException {


        GeoJsonLayer districtLayer;

        districtLayer = new GeoJsonLayer(myMap, R.raw.sudur,
                getApplicationContext());

        districtLayer.addLayerToMap();

    }

    private void fetchBussinesFromServer(String lastSyncDateTime) {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Data> call = apiService.getMenu(lastSyncDateTime);
        call.enqueue(new Callback<Data>() {
            @Override


            public void onResponse(Call<Data> call, Response<Data> response) {
                handleResponse(response);
            }

            private void handleResponse(Response<Data> response) {
                if (response.code() != 200 || response.body().getData() == null) {
                    return;
                }


                Data data = response.body();
                DatabaseHelper.getInstance(getApplicationContext()).saveSyncedBussinesses(data.getData());
                tryToSetSpinner();

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

                showToast(t.getMessage());
            }
        });

    }


    private void setSudurCamera(GoogleMap myMap) {

        final LatLngBounds SUDUR = new LatLngBounds(new LatLng(28.248326, 80.046272), new LatLng(30.771248, 82.296098));


        myMap.setLatLngBoundsForCameraTarget(SUDUR);
        myMap.setMinZoomPreference(8f);

        LatLng mCenterLocation = new LatLng(29.319998, 81.096780);

        CameraPosition cameraPositon = CameraPosition.builder()
                .target(mCenterLocation)
                .zoom(8f)
                .bearing(0.0f)
                .tilt(20f)
                .build();

        myMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPositon), null);

        myMap.getUiSettings().setZoomControlsEnabled(false);
        myMap.getUiSettings().setCompassEnabled(false);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.getUiSettings().setMapToolbarEnabled(false);

    }

    private void tryToSetSpinner() {
        List<String> foodList = DatabaseHelper.getInstance(getApplicationContext()).getBusinessCategories();
        if (foodList == null || foodList.size() == 0) {
            showToast("Failed to load categories");
            return;
        }


        ArrayList<String> bussinesscategorieslist = DatabaseHelper.getInstance(getApplicationContext()).getBusinessCategories();
        String[] bussinesscategories = bussinesscategorieslist.toArray(new String[bussinesscategorieslist.size()]);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_simple_spinner_item, bussinesscategories);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item);
        businessListSpinner.setAdapter(spinnerArrayAdapter);
        businessListSpinner.setOnItemSelectedListener(this);


    }

    private void showToast(String s) {

        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.floatingActionButton)
    public void toBusinessActivity() {

        Intent intent = new Intent(BusinessPlacesMapActivity.this, AddYourBusinessActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tryToPopulateMap();
    }

    private void tryToPopulateMap() {
        ArrayList<Bussiness> bussinesses = DatabaseHelper.getInstance(getApplicationContext()).getBusinessFromTypes(businessListSpinner.getSelectedItem().toString());

        removeMarkersIfPresent();
        placeMarkersOnMap(bussinesses);

    }

    private void removeMarkersIfPresent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (Marker marker : markersPresentOnMap) {
                    marker.remove();
                }

                markersPresentOnMap.clear();
            }
        }).run();
    }


    private void placeMarkersOnMap(final ArrayList<Bussiness> bussinesses) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    for (Bussiness bussiness : bussinesses) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(bussiness.getLatitude()), Double.parseDouble(bussiness.getLongitude())))
                                .title(bussiness.getBusinessName()));

                        marker.setTag(bussiness);

                        markersPresentOnMap.add(marker);
                    }
                } catch (NumberFormatException e) {
                    showToast("Server sent bad data");
                }


            }
        }).run();


    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Bussiness bussiness = (Bussiness) marker.getTag();
        delayBeforeSheetOpen(bussiness);


        return false;
    }

    private void delayBeforeSheetOpen(final Bussiness bussiness) {


        int ANIMATE_DELAY = 250;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PlaceDetailsBottomSheet placeDetailsBottomSheet = PlaceDetailsBottomSheet.getInstance(bussiness);
                placeDetailsBottomSheet.show(getSupportFragmentManager(), "a");


            }
        }, ANIMATE_DELAY);
    }

}
