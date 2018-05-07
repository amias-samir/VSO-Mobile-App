package np.com.naxa.vso.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.vso.R;
import np.com.naxa.vso.database.entity.OpenSpace;
import np.com.naxa.vso.gps.GeoPointActivity;
import np.com.naxa.vso.viewmodel.OpenSpaceViewModel;

public class OpenSpaceActivity extends AppCompatActivity {

    private static final String TAG = "ContactActivity";
    @BindView(R.id.openspace_name)
    TextView openspaceName;
    @BindView(R.id.openspace_distance)
    TextView openspaceDistance;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.btn_start_route)
    Button btnStartRoute;

    private OpenSpaceViewModel openSpaceViewModel;
    List<OpenSpace> openSpaceList = new ArrayList<>();


    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";
    double myLat = 0.0;
    double myLong = 0.0;
    Map<OpenSpace, Float> hashMapWithDistance;
    List<OpenSpace> sortedOpenSpacesList;
    List<Float> sortedOpenSpacesDistanceList;



    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_space);
        Mapbox.getInstance(this, getString(R.string.access_token));
//        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        // Get a new or existing ViewModel from the ViewModelProvider.
        openSpaceViewModel = ViewModelProviders.of(this).get(OpenSpaceViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        openSpaceViewModel.getmAllOpenSpaces().observe(this, new Observer<List<OpenSpace>>() {
            @Override
            public void onChanged(@Nullable final List<OpenSpace> openSpaces) {
                // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);

                for (int i = 0; i < openSpaces.size(); i++) {
                    openSpaceList.add(openSpaces.get(i));
                }
                sortingOpenSpaceDistanceData();

                Log.d(TAG, "onChanged: insert " + "one more new  data inserted");
            }
        });




        saveOpenSpaceDataViaRoom();
        getUserCurrenLocation();

    }


    private void getUserCurrenLocation() {
        Intent toGeoPointActivity = new Intent(this, GeoPointActivity.class);
        startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GEOPOINT_RESULT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String location = data.getStringExtra(LOCATION_RESULT);

                    Log.d(TAG, "onActivityResult: " + location.toString());

                    String string = location;
                    String[] parts = string.split(" ");
                    String split_lat = parts[0]; // 004
                    String split_lon = parts[1]; // 034556

                    if (!split_lat.equals("") && !split_lon.equals("")) {
                        myLat = Double.parseDouble(split_lat);
                        myLong = Double.parseDouble(split_lon);
//                        showLoading("Please wait ... \nCalculating distance");
                        Toast.makeText(this, "Please wait ... \nCalculating distance", Toast.LENGTH_SHORT).show();
                        sortingOpenSpaceDistanceData();
                    } else {
//                        showInfoToast("Cannot calculate distance");
                        Toast.makeText(this, "Cannot calculate distance", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    }


    public void sortingOpenSpaceDistanceData() {

        hashMapWithDistance = new HashMap<OpenSpace, Float>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (openSpaceList.size() > 1) {
//                    sortServiceData(servicesData, myLat, myLon);
                    for (int i = 0; i < openSpaceList.size(); i++) {
                        double latfirst = openSpaceList.get(i).getLatitude();
                        double longfirst = openSpaceList.get(i).getLongitude();

                        float[] result1 = new float[3];
                        Location.distanceBetween(myLat, myLong, latfirst, longfirst, result1);
                        Float distance1 = result1[0];

                        hashMapWithDistance.put(openSpaceList.get(i), distance1);
                    }
                    sortMapByValuesWithDuplicates(hashMapWithDistance);
                }
            }
        }).start();
    }

    private void sortMapByValuesWithDuplicates(Map passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
//        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                Object comp1 = passedMap.get(key);
                Float comp2 = Float.parseFloat(val.toString());

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((OpenSpace) key, (Float) val);
                    break;
                }
            }
        }
        //Getting Set of keys from HashMap
        Set<OpenSpace> keySet = sortedMap.keySet();
        //Creating an ArrayList of keys by passing the keySet
        sortedOpenSpacesList = new ArrayList<OpenSpace>(keySet);


        //Getting Collection of values from HashMap
        Collection<Float> values = sortedMap.values();
        //Creating an ArrayList of values
        sortedOpenSpacesDistanceList = new ArrayList<Float>(values);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                initServicesList(sortedOpenSpacesList, sortedOpenSpacesDistanceList);

                openspaceName.setText(sortedOpenSpacesList.get(0).getName());
                openspaceDistance.setText(sortedOpenSpacesDistanceList.get(0).toString());


            }
        });

    }

    public void saveOpenSpaceDataViaRoom() {
        OpenSpace openSpace = new OpenSpace("ram", 27.451, 85.24);
        openSpaceViewModel.insert(openSpace);

        openSpace = new OpenSpace("Hattisar", 27.712365, 85.327498);
        openSpaceViewModel.insert(openSpace);

        openSpace = new OpenSpace("Tudikhel", 27.700799, 85.314341);
        openSpaceViewModel.insert(openSpace);

        openSpace = new OpenSpace("Dashrath Rangasala", 27.694317, 85.315161);
        openSpaceViewModel.insert(openSpace);

        openSpace = new OpenSpace("Narayan Chaur", 27.714686, 85.325462);
        openSpaceViewModel.insert(openSpace);

        openSpace = new OpenSpace("Naxal", 27.716100, 85.325754);
        openSpaceViewModel.insert(openSpace);

//        for (int i = 0; i < 10; i++) {
        Log.d(TAG, "saveOpenSpaceDataViaRoom: insert inserting ");
//            contact.setFirstName( "Name_" + i);
//            contact.setLastName("Lastname_" + i);
//            contact.setAge(41 + i);
//        }


    }



    @OnClick(R.id.btn_start_route)
    public void onViewClicked() {
    }


}
