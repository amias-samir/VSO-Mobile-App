package np.com.naxa.vso.utils.maputils;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.combinedentity.OpenAndCommon;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.OpenSpace;

public class SortingDistance {
    private static final String TAG = "SortingDistance";

    Map<CommonPlacesAttrb, Float> hashMapOpenWithDistance;
    Map<HospitalAndCommon, Float> hashMapHospitalWithDistance;
    List<OpenSpace> sortedOpenSpacesList;
    List<Float> sortedOpenSpacesDistanceList;

    LinkedHashMap sortedMap = new LinkedHashMap();


    public LinkedHashMap sortingOpenSpaceDistanceData(List<CommonPlacesAttrb> openSpaceList, Double myLat, Double myLong ) {

        hashMapOpenWithDistance = new HashMap<CommonPlacesAttrb, Float>();

                if (openSpaceList.size() > 1) {
                    for (int i = 0; i < openSpaceList.size(); i++) {

                        double latfirst = openSpaceList.get(i).getLatitude();
                        double longfirst = openSpaceList.get(i).getLongitude();
//                        double latfirst = 27.1254545;
//                        double longfirst = 85.3265655;

                        float[] result1 = new float[3];
                        Location.distanceBetween(myLat, myLong, latfirst, longfirst, result1);
                        Float distance1 = result1[0];

                        hashMapOpenWithDistance.put(openSpaceList.get(i), distance1);
                    }
                    sortMapByValuesWithDuplicatesOpen(hashMapOpenWithDistance);
                }

        return sortedMap;
    }


    public LinkedHashMap sortingHospitalDistanceData(List<HospitalAndCommon> hospitalAndCommonList, Double myLat, Double myLong ) {

        hashMapHospitalWithDistance = new HashMap<HospitalAndCommon, Float>();

        Log.d(TAG, "sortingHospitalDistanceData: "+ myLat + myLong);
        if (hospitalAndCommonList.size() > 1) {
            for (int i = 0; i < hospitalAndCommonList.size(); i++) {

                Log.d(TAG, "sortingHospitalDistanceData: "+ i);

                double latfirst = hospitalAndCommonList.get(i).getCommonPlacesAttrb().getLatitude();
                double longfirst = hospitalAndCommonList.get(i).getCommonPlacesAttrb().getLongitude();
//                        double latfirst = 27.1254545;
//                        double longfirst = 85.3265655;

                float[] result1 = new float[3];
                Location.distanceBetween(myLat, myLong, latfirst, longfirst, result1);
                Float distance1 = result1[0];

                hashMapHospitalWithDistance.put(hospitalAndCommonList.get(i), distance1);
            }
            sortMapByValuesWithDuplicates(hashMapHospitalWithDistance);
        }

        return sortedMap;
    }

    private void sortMapByValuesWithDuplicates(Map passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
//        Collections.sort(mapKeys);



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
                    sortedMap.put((HospitalAndCommon) key, (Float) val);
                    break;
                }
            }
        }


//        //Getting Set of keys from HashMap
//        Set<OpenSpace> keySet = sortedMap.keySet();
//        //Creating an ArrayList of keys by passing the keySet
//        sortedOpenSpacesList = new ArrayList<OpenSpace>(keySet);
//
//
//        //Getting Collection of values from HashMap
//        Collection<Float> values = sortedMap.values();
//        //Creating an ArrayList of values
//        sortedOpenSpacesDistanceList = new ArrayList<Float>(values);


    }

    private void sortMapByValuesWithDuplicatesOpen(Map passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
//        Collections.sort(mapKeys);



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
                    sortedMap.put((CommonPlacesAttrb) key, (Float) val);
                    break;
                }
            }
        }


    }
}
