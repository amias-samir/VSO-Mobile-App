package np.com.naxa.vso.sudur.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.geojson.GeoJsonLayer;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import np.com.naxa.vso.R;

/**
 * Created by Susan on 3/7/2016.
 */
public class MapPointActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener /*GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener*/ {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;


    //this is so that sudur is always in center when the user loads the map tab
    protected LatLng mCenterLocation = new LatLng(29.319998, 81.096780);

    LatLng latLng;
    static ArrayList<LatLng> list = new ArrayList<LatLng>();

    Button getLocationbtn;

    private GoogleMap mMap;
    GoogleMap mGoogleMap;
    Marker currLocationMarker;

    double final_latitude;
    double final_longitude;
    public static double lat = 0;
    public static double lon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sudur);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationbtn = (Button) findViewById(R.id.getLocationbtn);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        latLng = list.get(0);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(this);
//        buildGoogleApiClient();
//        mGoogleApiClient.connect();

        setSudurCamera();
         }


    private void setSudurCamera() {
        CameraPosition cameraPositon = CameraPosition.builder()
                .target(mCenterLocation)
                .zoom(8f)
                .bearing(0.0f)
                .tilt(20f)
                .build();


        final LatLngBounds SUDUR = new LatLngBounds(new LatLng(28.248326, 80.046272), new LatLng(30.771248, 82.296098));


        mMap.setLatLngBoundsForCameraTarget(SUDUR);
        mMap.setMinZoomPreference(8f);

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPositon), null);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }


    private void setDistrictGeoJSON() {
        //set district layer
        try {
            GeoJsonLayer districtLayer;
            try {
                districtLayer = new GeoJsonLayer(mMap, R.raw.sudur,
                        getApplicationContext());
                districtLayer.addLayerToMap();

            } catch (JSONException e) {
            }

        } catch (IOException e) {

        }

    }


    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(latLng.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        final_latitude = latLng.latitude;
        final_longitude = latLng.longitude;


        //Fire that second activity
//        startActivity(i);
        getLocationbtn.setVisibility(View.VISIBLE);

        getLocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add your data to bundle


                lat = final_latitude;
                lon = final_longitude;

                //Add the bundle to the intent

                MapPointActivity.super.onBackPressed();
            }
        });
        //latLng.toString()
//        Toast.makeText(getApplicationContext(),
//                "New marker added" + latLng.latitude + "susan" + latLng.longitude, Toast.LENGTH_LONG)
//                .show();
    }
//
//    protected synchronized void buildGoogleApiClient() {
////        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
////                .addConnectionCallbacks(this)
////                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }

//    @Override
//    public void onConnected(Bundle bundle) {
//
////        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            //place marker at current position
//            //mGoogleMap.clear();
//            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            currLocationMarker = mGoogleMap.addMarker(markerOptions);
//        }
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(5000); //5 seconds
//        mLocationRequest.setFastestInterval(3000); //3 seconds
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
//
//       // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        //place marker at current position
//        //mGoogleMap.clear();
//        if (currLocationMarker != null) {
//            currLocationMarker.remove();
//        }
//        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        currLocationMarker = mGoogleMap.addMarker(markerOptions);
//
//        Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
//
//        //zoom to current position:
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(latLng).zoom(14).build();
//
//        mMap.animateCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));
//
//        //If you only need one location, unregister the listener
//        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//
//    }

}
