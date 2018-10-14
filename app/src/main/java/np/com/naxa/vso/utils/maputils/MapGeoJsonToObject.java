package np.com.naxa.vso.utils.maputils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.database.entity.CommonPlacesAttrb;

public class MapGeoJsonToObject {
    private static final String TAG = "MapGeoJsonToObject";
    GeoPoint centerPoint = new GeoPoint(27.657531140175244, 85.46161651611328);


    public List<CommonPlacesAttrb> getCommonPlacesListObj(Context context, String geoJson, String fileName, MapView mapView,
                                                          MapMarkerOverlayUtils mapMarkerOverlayUtils, FolderOverlay myOverLay,
                                                          int marker_image) {
        List<CommonPlacesAttrb> commonPlacesAttrbList = new ArrayList<CommonPlacesAttrb>();
        FolderOverlay folderOverlay = new FolderOverlay();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(geoJson);
            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            Log.d(TAG, "getCommonPlacesListObj: filename " + fileName);

//            if(fileName.equals("open_spaces")){
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
//                    String name = properties.getString("name");
//                    String address = " ";
//                    double latitude = Double.parseDouble(properties.getString("y"));
//                    double longitude = Double.parseDouble(properties.getString("x"));
////                    String remarks = properties.getString("Remarks");
//                    String remarks = "";
//                    CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, fileName, latitude, longitude, remarks);
//
//                            mapView.getOverlays().add(mapMarkerOverlayUtils.overlayFromCommonPlaceAttrib(context,
//                                    commonPlacesAttrb, mapView , marker_image));
//                            mapView.getOverlays().add(myOverLay);
//                            mapView.invalidate();
//
//                }
//            }else {
            String remarks = "";
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                String name = properties.has("name") ? properties.getString("name") :properties.has("Name of Bank Providing ATM Service") ? properties.getString("Name of Bank Providing ATM Service") : properties.getString("Name");
                String address = properties.has("address") ? properties.getString("address") : properties.getString("Address");
                double latitude = properties.has("Y") ? Double.parseDouble(properties.getString("Y")) : Double.parseDouble(properties.getString("y"));
                double longitude = properties.has("X") ? Double.parseDouble(properties.getString("X")) : Double.parseDouble(properties.getString("x"));
                if(properties.has("remarks") || properties.has("Remarks")) {
                    remarks = properties.has("remarks") ? properties.getString("remarks") : properties.getString("Remarks");
                }
                CommonPlacesAttrb commonPlacesAttrb = new CommonPlacesAttrb(name, address, fileName, latitude, longitude, remarks, properties.toString());

                folderOverlay.add(mapMarkerOverlayUtils.overlayFromCommonPlaceAttrib(context,
                        commonPlacesAttrb, mapView, marker_image, properties.toString()));
            }

            if (mapView.getOverlays().size() >= 2) {
                mapView.getOverlays().set(1, folderOverlay);
            } else {
                mapView.getOverlays().add(folderOverlay);
            }
            MapCommonUtils.zoomToMapBoundary(mapView, centerPoint);
            mapView.invalidate();
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commonPlacesAttrbList;
    }


    public void getWardDetailsListObj(Context context, String geoJson, String fileName, MapView mapView,
                                      MapMarkerOverlayUtils mapMarkerOverlayUtils, FolderOverlay myOverLay,
                                      int marker_image) {
        List<WardDetailsModel> wardDetailsModelList = new ArrayList<WardDetailsModel>();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(geoJson);
            JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));

            Log.d(TAG, "getCommonPlacesListObj: filename " + fileName);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
                String name = properties.getString("GaPa_NaPa") + " " + properties.getString("Type_GN");
                String ward = properties.getString("NEW_WARD_N");
                String area = properties.getString("Area_SQKM");
                String district = properties.getString("DISTRICT");
                double latitude = Double.parseDouble(properties.getString("Cent_Y"));
                double longitude = Double.parseDouble(properties.getString("Cent_X"));
                WardDetailsModel wardDetailsModel = new WardDetailsModel(name, ward, area, district, latitude, longitude);

                mapView.getOverlays().add(mapMarkerOverlayUtils.overlayFromWardDetailsModel(context,
                        wardDetailsModel, mapView, marker_image));
                mapView.getOverlays().add(myOverLay);
                mapView.invalidate();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
