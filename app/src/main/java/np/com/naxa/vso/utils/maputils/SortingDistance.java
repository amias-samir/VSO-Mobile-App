package np.com.naxa.vso.utils.maputils;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import np.com.naxa.vso.database.entity.OpenSpace;

public class SortingDistance {

    Map<OpenSpace, Float> hashMapWithDistance;
    List<OpenSpace> sortedOpenSpacesList;
    List<Float> sortedOpenSpacesDistanceList;


    public void sortingOpenSpaceDistanceData(List<OpenSpace> openSpaceList, ) {

        hashMapWithDistance = new HashMap<OpenSpace, Float>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (openSpaceList.size() > 1) {
//                    sortServiceData(servicesData, myLat, myLon);
                    for (int i = 0; i < openSpaceList.size(); i++) {

//                        double latfirst = openSpaceList.get(i).getLatitude();
//                        double longfirst = openSpaceList.get(i).getLongitude();
                        double latfirst = 27.1254545;
                        double longfirst = 85.3265655;

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

                openspaceName.setText(sortedOpenSpacesList.get(0).getAccess_roa());
                openspaceDistance.setText(sortedOpenSpacesDistanceList.get(0).toString());


            }
        });

    }
}
