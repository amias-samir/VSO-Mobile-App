package np.com.naxa.vso.sudur.fragment;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonLayer;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.Interface.OnDistrictTaskCompleted;
import np.com.naxa.vso.sudur.Interface.OnPlacesTaskCompleted;
import np.com.naxa.vso.sudur.Tasks.DistrictTask;
import np.com.naxa.vso.sudur.Tasks.PlacesTask;
import np.com.naxa.vso.sudur.Utils.JsonParser;
import np.com.naxa.vso.sudur.model.InterestLocation;
import np.com.naxa.vso.sudur.model.PlaceTypes;


public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnDistrictTaskCompleted, OnPlacesTaskCompleted {

    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    //Susan
    private int MULTIPLE_PERMISSION_CODE = 22;


    private HashMap<String, String[]> districtListEnNp;
    private static final String TAG = "MapFragment";
    private View rootView;
    GoogleMap myMap;
    MapView mMapView;
    GoogleApiClient mGoogleApiClient;
    ArrayList<InterestLocation> interestLocationList;
    Spinner placesTypesSpinner;
    MapFragment mContext = this;

    ArrayList<Marker> markerList;
    GeoJsonLayer districtLayer;

    SharedPreferences districtSharedPref;
    SharedPreferences.Editor districtEditor;
    public static final String DISRICTPREF = "district_data";

    SharedPreferences placesSharedPref;
    SharedPreferences.Editor placesEditor;
    public static final String PLACESDATA = "places_data";


    //this is so that sudur is always in center when the user loads the map tab
    protected LatLng mCenterLocation = new LatLng(29.319998, 81.096780);

    // Declare a variable for the cluster manager.
    MarkerOptions options;
    private boolean markerFlag = false;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        districtSharedPref = getActivity().getSharedPreferences(DISRICTPREF, Context.MODE_PRIVATE);
        districtEditor = districtSharedPref.edit();

        placesSharedPref = getActivity().getSharedPreferences(PLACESDATA, Context.MODE_PRIVATE);
        placesEditor = placesSharedPref.edit();
        getDistrictsFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        try {
            rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
            MapsInitializer.initialize(this.getActivity());
            mMapView = (MapView) rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);

            placesTypesSpinner = (Spinner) rootView.findViewById(R.id.district_list_spinner);

        } catch (InflateException e) {
            Log.e(TAG, "Inflate exception");
        }

        return rootView;
    }

    //Susan
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Susan
        try {
            //First checking if the app is already having the permission
            if (isPermissionAllowed()) {
                //If permission is already having then showing the toast
//                Toast.makeText(AddYourBusinessActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
                //Existing the method with return
                return;
            } else {
                //If the app has not the permission then asking for the permission
                requestMultiplePermission();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.getMessage();
        }
    }

    //Susan
    private void getDistrictsFromServer() {
        //this to set delegate/listener back to this class
        DistrictTask districtTask = new DistrictTask(MapFragment.this);
        //execute the async task
        districtTask.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.getMapAsync(this);
        mMapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        new Thread(new Runnable() {
            public void run() {
                districtListEnNp = new HashMap<String, String[]>();
            }
        }).start();

        myMap = googleMap;
        myMap.setPadding(0, 120, 0, 120);//to stop UI buttons to being overlapped and hidden
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                myMap.setMyLocationEnabled(false);
                myMap.getUiSettings().setZoomControlsEnabled(true);
                setSudurCamera();
            }
        } else {
            buildGoogleApiClient();
            setSudurCamera();
        }
        myMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

        showSudurOffice();
        setDistrictGeoJSON();

        myMap.setMyLocationEnabled(true);
    }


    private void delayBeforeMarkerReplaced(final int newPosition) {

        setSudurCamera();

        int ANIMATE_DELAY = 250;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setMarkers(interestLocationList, String.valueOf(newPosition));

            }
        }, ANIMATE_DELAY);
    }

    /**
     * Sets the camera on sudur when the map loads
     */
    private void setSudurCamera() {

        final LatLngBounds SUDUR = new LatLngBounds(new LatLng(28.248326, 80.046272), new LatLng(30.771248, 82.296098));


        myMap.setLatLngBoundsForCameraTarget(SUDUR);
        myMap.setMinZoomPreference(8f);


        CameraPosition cameraPositon = CameraPosition.builder()
                .target(mCenterLocation)
                .zoom(8f)
                .bearing(0.0f)
                .tilt(20f)
                .build();

        myMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPositon), null);

        myMap.getUiSettings().setZoomControlsEnabled(true);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * A method that returns location from latitude and logigude
     *
     * @param latLng
     * @return
     */
    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (IOException e) {
        }

        return address;
    }

    /**
     * Returns a array of places types
     *
     * @param typesDetailsList
     * @return
     */
    private String[] cleanPlacesTypesData(ArrayList<PlaceTypes> typesDetailsList) {

        ArrayList<String> bussinessCategoryList = new ArrayList<>();

        bussinessCategoryList.add("गन्तव्य छनौट गर्नुहोस् ");
        bussinessCategoryList.add("सबै गन्तव्यहरू");

        for (int i = 0; i < typesDetailsList.size(); i++) {
            bussinessCategoryList.add(typesDetailsList.get(i).getNpName());
        }

        String[] types = new String[bussinessCategoryList.size()];
        bussinessCategoryList.toArray(types);

        return types;
    }

    /**
     * Sets the listener of the items in the spinner
     */
    private void setSpinnerListener() {

        placesTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int newPosition = position - 1;


                delayBeforeMarkerReplaced(newPosition);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onTaskCompleted(String response) {


        try {


            //feeding an Arraylist with PlacesTypes Object
            if (new JsonParser().isPlacesTypeJsonValid(response)) {
                //if it has valid response
                //assume it is importnat and replace the local data
                districtEditor.putString(DISRICTPREF, response);
                districtEditor.commit();
            }

            if (!districtSharedPref.getString(DISRICTPREF, "").trim().isEmpty()) {
                //if district pref has data
                String districtCache = districtSharedPref.getString(DISRICTPREF, " ").trim();
                String[] types = cleanPlacesTypesData(new JsonParser().placesTypeJSONParser(districtCache));
                //pass the cleaned data to spinner

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.my_spinner_style, types);
                placesTypesSpinner.setAdapter(adapter);

                //execute places task
                PlacesTask placesTask = new PlacesTask(MapFragment.this);
                //this to set delegate/listener back to this class
                //execute the async task
                placesTask.execute();
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onPlacesTaskCompleted(String response) {

        if (new JsonParser().isPlacesTypeJsonValid(response)) {
            //if it has valid response
            //assume it is importnat and replace the local data
            placesEditor.putString(PLACESDATA, response);
            placesEditor.commit();
            Log.e(TAG, "setting marker data in shared");
        }
        //we have marker data now
        //feed marker arraylist to setMarkers
        if (!placesSharedPref.getString(PLACESDATA, "").trim().isEmpty()) {
            //if district pref has data
            String placesCache = placesSharedPref.getString(PLACESDATA, " ").trim();
            interestLocationList = new JsonParser().InterestLocationJSONParser(placesCache);
            Log.e(TAG, "getting marker data in array");
        }


        setSpinnerListener();
    }

    private void showSudurOffice() {

        MarkerOptions sudurOfficeMarker;
        LatLng sudurOfficeLatLan = new LatLng(29.241776, 80.9376628);
        sudurOfficeMarker = new MarkerOptions().position(sudurOfficeLatLan).flat(true);
        sudurOfficeMarker.title("सुदूर पश्चिम बिकास आयोग");
        Marker marker = myMap.addMarker(sudurOfficeMarker);

        marker.showInfoWindow();
    }


    private void setDistrictGeoJSON() {
        //set district layer
        try {
            try {
                districtLayer = new GeoJsonLayer(myMap, R.raw.sudur,
                        getActivity().getApplicationContext());
            } catch (JSONException e) {
                Log.d(TAG, "Error ! while parsing GeoJSON raw file /n" + e);

                e.printStackTrace();
            }
            districtLayer.addLayerToMap();
        } catch (IOException e) {
            Log.d(TAG, "Error ! While Applying GeoJSON to map /n" + e);
            e.printStackTrace();

        }

    }

    private void setMarkers(ArrayList<InterestLocation> markersListFromServer, String typeHighligter) {


        if (markerFlag) {
            myMap.clear();

            //set geoJson again
            setDistrictGeoJSON();
            Log.d(TAG, "Clearing all markers");
        }


        for (int i = 0; i < markersListFromServer.size(); i++) {
            markerFlag = true;
            InterestLocation curInterestLocation = markersListFromServer.get(i);

            LatLng latLag = new LatLng(curInterestLocation.getLatLng().latitude, curInterestLocation.getLatLng().longitude);


            options = new MarkerOptions().position(latLag);
            options.title(curInterestLocation.getTitle());


            markerList = new ArrayList<>();

            if (typeHighligter.equals(curInterestLocation.getType())) {
                Marker marker = myMap.addMarker(options);
                markerList.add(marker);
            } else if (typeHighligter.equals("0")) {
                Marker marker = myMap.addMarker(options);
                marker.showInfoWindow();
                markerList.add(marker);
            }


            myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();


                    myMap.getUiSettings().setMapToolbarEnabled(true);
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            marker.getPosition(), 10));

                    return false;
                }
            });
        }
    }


    /**
     * @return Susan Permissions: Camera, Storage, Location, Internet, etc.
     */
    //We are calling this method to check the permission status
    private boolean isPermissionAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestMultiplePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MULTIPLE_PERMISSION_CODE);

    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MULTIPLE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }
}