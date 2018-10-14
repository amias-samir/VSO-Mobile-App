package np.com.naxa.vso.utils.maputils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.combinedentity.EducationAndCommon;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.combinedentity.OpenAndCommon;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.detailspage.MarkerDetailsDisplayActivity;

import static np.com.naxa.vso.R.color.white;

public class MapMarkerOverlayUtils {
    private static final String TAG = "MapMarkerOverlayUtils";


    public void MarkerOnClickEvent(Context context, OverlayItem item) {
        Log.d("Title", "Marker Clicked" + item.getTitle());
        Log.d("Snippet", "Marker Clicked" + item.getSnippet());
        Log.d("Id", "Marker Clicked" + item.getUid());


        //set up dialog
        Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.marker_tap_popup_layout);

        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        //there are a lot of settings, for dialog, check them all out!

        //set up text
        TextView map_popup_header = (TextView) dialog.findViewById(R.id.map_popup_header);
        map_popup_header.setText(item.getTitle());

        //set up button
        TextView imgMoreInfo = (TextView) dialog.findViewById(R.id.map_more_info_textView);
        imgMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "more info");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, MarkerDetailsDisplayActivity.class);
                bundle.putString("data", item.getSnippet());
                intent.putExtras(bundle);
                ((Activity) context).startActivity(intent);

            }
        });
        dialog.show();
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromHospitalAndCommon(Context context, HospitalAndCommon hospitalAndCommon, MapView mapView) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = hospitalAndCommon.getCommonPlacesAttrb().getName()
                + "\n" + hospitalAndCommon.getCommonPlacesAttrb().getType()
                + "\n" + hospitalAndCommon.getCommonPlacesAttrb().getAddress();
        double latitude = hospitalAndCommon.getCommonPlacesAttrb().getLatitude();
        double longitude = hospitalAndCommon.getCommonPlacesAttrb().getLongitude();

        Gson gson = new Gson();
          HospitalAndCommon obj = hospitalAndCommon;
         String jsonInString = gson.toJson(obj).toString();
         Log.d(TAG, "overlayFromHospitalAndCommon: " + jsonInString);

        //items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                context.getResources().getDrawable(R.drawable.ic_marker_hospital),
                context.getResources().getDrawable(R.drawable.ic_marker_hospital),
                white,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        mapView.getController().animateTo(new GeoPoint(latitude, longitude));
                        mapView.getController().setZoom(12.0);
                        mapView.invalidate();
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                },
                context);
        return mOverlay;
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromEductionalAndCommon(Context context, EducationAndCommon educationAndCommon, MapView mapView) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = educationAndCommon.getCommonPlacesAttrb().getName()
                + "\n" + educationAndCommon.getCommonPlacesAttrb().getType()
                + "\n" + educationAndCommon.getCommonPlacesAttrb().getAddress();
        double latitude = educationAndCommon.getCommonPlacesAttrb().getLatitude();
        double longitude = educationAndCommon.getCommonPlacesAttrb().getLongitude();

        Gson gson = new Gson();
        EducationAndCommon obj = educationAndCommon;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromEducationAndCommonAndCommon: " + jsonInString);

        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                context.getResources().getDrawable(R.drawable.ic_marker_education),
                context.getResources().getDrawable(R.drawable.ic_marker_education),
                white,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        mapView.getController().animateTo(new GeoPoint(latitude, longitude));
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                },
                context);

        return mOverlay;
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromOpenSpaceAndCommon(Context context, OpenAndCommon openAndCommon, MapView mapView) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = openAndCommon.getCommonPlacesAttrb().getName()
                + "\n" + openAndCommon.getCommonPlacesAttrb().getType()
                + "\n" + openAndCommon.getCommonPlacesAttrb().getAddress();
        double latitude = openAndCommon.getCommonPlacesAttrb().getLatitude();
        double longitude = openAndCommon.getCommonPlacesAttrb().getLongitude();

        Gson gson = new Gson();
        OpenAndCommon obj = openAndCommon;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromOpenAndCommonAndCommon: " + jsonInString);

        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                context.getResources().getDrawable(R.drawable.ic_marker_openspace),
                context.getResources().getDrawable(R.drawable.ic_marker_openspace),
                white,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        mapView.getController().animateTo(new GeoPoint(latitude, longitude));
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                },
                context);

        return mOverlay;
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromCommonPlaceAttrib(Context context, CommonPlacesAttrb commonPlacesAttrb, MapView mapView, int marker_image, String jsonProperties) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = commonPlacesAttrb.getName()
                + "\n" + commonPlacesAttrb.getType()
                + "\n" + commonPlacesAttrb.getAddress();
        double latitude = commonPlacesAttrb.getLatitude();
        double longitude = commonPlacesAttrb.getLongitude();

        Gson gson = new Gson();
        CommonPlacesAttrb obj = commonPlacesAttrb;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromCommon: " + jsonInString);

//        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));
        items.add(new OverlayItem(name, jsonProperties, new GeoPoint(latitude, longitude)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                context.getResources().getDrawable(marker_image),
                context.getResources().getDrawable(marker_image),
                white,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        mapView.getController().setZoom(14.0);
                        mapView.getController().animateTo(new GeoPoint(latitude, longitude));
                        mapView.invalidate();
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                },
                context);

        return mOverlay;
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromWardDetailsModel(Context context, WardDetailsModel wardDetailsModel, MapView mapView, int marker_image) {
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = wardDetailsModel.getName()
                + "\n" + wardDetailsModel.getDistrict()
                + "\n" +"Ward no. : "+ wardDetailsModel.getWard()
                + "\n" +"Area : "+ wardDetailsModel.getArea()+"Km. Square";
        double latitude = wardDetailsModel.getLatitude();
        double longitude = wardDetailsModel.getLongitude();

        Gson gson = new Gson();
        WardDetailsModel obj = wardDetailsModel;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromWardDetailsModel: " + jsonInString);

        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                context.getResources().getDrawable(marker_image),
                context.getResources().getDrawable(marker_image),
                white,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        mapView.getController().animateTo(new GeoPoint(latitude, longitude));
                        MarkerOnWardClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                },
                context);

        return mOverlay;
    }

    public void MarkerOnWardClickEvent(Context context, OverlayItem item) {
        Log.d("Title", "Marker Clicked" + item.getTitle());
        Log.d("Snippet", "Marker Clicked" + item.getSnippet());
        Log.d("Id", "Marker Clicked" + item.getUid());


        //set up dialog
        Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.marker_tap_popup_layout);

        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        //there are a lot of settings, for dialog, check them all out!

        //set up text
        TextView map_popup_header = (TextView) dialog.findViewById(R.id.map_popup_header);
        map_popup_header.setText(item.getTitle());

        //set up button
        TextView imgMoreInfo = (TextView) dialog.findViewById(R.id.map_more_info_textView);
        imgMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "more info");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, MarkerDetailsDisplayActivity.class);
                bundle.putString("data", item.getSnippet());
                intent.putExtras(bundle);
                ((Activity) context).startActivity(intent);

            }
        });
        dialog.show();
    }
}
