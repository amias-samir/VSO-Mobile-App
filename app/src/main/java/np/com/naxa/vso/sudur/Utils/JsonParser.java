package np.com.naxa.vso.sudur.Utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import np.com.naxa.vso.sudur.model.InterestLocation;
import np.com.naxa.vso.sudur.model.PlaceTypes;

/**
 * Has methods for parsing Json Formateed String
 * @author nishon.tan
 * Usage in MapFragment
 *
 */

public class JsonParser{

    String TAG = "JsonParser";
    JSONObject goodJson;
    private ArrayList<PlaceTypes> typesDetailsList;
    private ArrayList<InterestLocation> interestLocationList;

    public  JsonParser(){

    }




    /**
     * Returns a array of PlacesTypes objects
     * @param rawJSON
     * @return
     */
    public ArrayList<PlaceTypes> placesTypeJSONParser(String rawJSON) {

        typesDetailsList = new ArrayList<>();

        try {


            JSONObject jsonObj = new JSONObject(rawJSON);
            JSONArray dataArray = jsonObj.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject data = dataArray.getJSONObject(i);

                PlaceTypes placeTypes = new PlaceTypes(data.getString("place_type_id"), data.getString("place_type_name_en"), data.getString("place_type_name_np"));
                typesDetailsList.add(placeTypes);

            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return  typesDetailsList;
    }


    /**
     * Check if  places type json is valid
     *
     */
    public  boolean isPlacesTypeJsonValid(String rawJSON){

        typesDetailsList = new ArrayList<>();
        Boolean answer = false;

        try {
            JSONObject jsonObj = new JSONObject(rawJSON);
            JSONArray dataArray = jsonObj.getJSONArray("data");
            answer = dataArray.getJSONObject(0).has("place_type_id");

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return answer;
    }


    /**
     *
     * Check if  places type json is valid
     *
     **/

    public  boolean isPlacesJsonValid(String rawJSON){

        interestLocationList = new ArrayList<>();
        Boolean answer = false;

        try {
            JSONObject jsonObj = new JSONObject(rawJSON);
            JSONArray dataArray = jsonObj.getJSONArray("data");
            answer = dataArray.getJSONObject(0).has("place_type_id");

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return answer;
    }


    public ArrayList<InterestLocation> InterestLocationJSONParser(String rawJSON) {

        interestLocationList = new ArrayList<>();
        try {


            JSONObject jsonObj = new JSONObject(rawJSON);
            JSONArray dataArray = jsonObj.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject data = dataArray.getJSONObject(i);

                LatLng placeLocation = new LatLng( Double.parseDouble(data.getString("place_lat")) , Double.parseDouble(data.getString("place_lon")));

                InterestLocation interestLocation = new InterestLocation(placeLocation,data.getString("place_type_id"), data.getString("place_name_np"), data.getString("place_desc_np"));
                interestLocationList.add(interestLocation);

            }

        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        //Log.d("Nishon",interestLocationList.size()+"");

        return interestLocationList;
    }


}

