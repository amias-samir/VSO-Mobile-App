package np.com.naxa.vso.utils.maputils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.combinedentity.EducationAndCommon;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.combinedentity.OpenAndCommon;
import np.com.naxa.vso.detailspage.MarkerDetailsDisplayActivity;

public class MapMarkerOverlayUtils {
    private static final String TAG = "MapMarkerOverlayUtils";

    public void MarkerOnClickEvent(Context context, OverlayItem item){
        Log.d("Title","Marker Clicked"+ item.getTitle());
        Log.d("Snippet", "Marker Clicked"+item.getSnippet());
        Log.d("Id","Marker Clicked"+ item.getUid());


        //set up dialog
        Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.marker_tap_popup_layout);

        dialog.setCancelable(true);
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
                ((Activity)context).startActivity(intent);

            }
        });
        dialog.show();
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromHospitalAndCommon(Context context , HospitalAndCommon hospitalAndCommon){
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = hospitalAndCommon.getCommonPlacesAttrb().getName()
                +"\n" +hospitalAndCommon.getHospitalFacilities().getType()
                +"\n" +hospitalAndCommon.getCommonPlacesAttrb().getAddress();
        double latitude = hospitalAndCommon.getCommonPlacesAttrb().getLatitude();
        double longitude = hospitalAndCommon.getCommonPlacesAttrb().getLongitude();

        Gson gson = new Gson();
        HospitalAndCommon obj = hospitalAndCommon;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromHospitalAndCommon: " + jsonInString);

        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                });

        return mOverlay;
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromEductionalAndCommon(Context context , EducationAndCommon educationAndCommon){
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = educationAndCommon.getCommonPlacesAttrb().getName()
                +"\n" +educationAndCommon.getCommonPlacesAttrb().getType()
                +"\n" +educationAndCommon.getCommonPlacesAttrb().getAddress();
        double latitude = educationAndCommon.getCommonPlacesAttrb().getLatitude();
        double longitude = educationAndCommon.getCommonPlacesAttrb().getLongitude();

        Gson gson = new Gson();
        EducationAndCommon obj = educationAndCommon;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromEducationAndCommonAndCommon: " + jsonInString);

        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                });

        return mOverlay;
    }

    public ItemizedOverlayWithFocus<OverlayItem> overlayFromOpenSpaceAndCommon(Context context , OpenAndCommon openAndCommon){
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        String name = openAndCommon.getCommonPlacesAttrb().getName()
                +"\n" +openAndCommon.getCommonPlacesAttrb().getType()
                +"\n" +openAndCommon.getCommonPlacesAttrb().getAddress();
        double latitude = openAndCommon.getCommonPlacesAttrb().getLatitude();
        double longitude = openAndCommon.getCommonPlacesAttrb().getLongitude();

        Gson gson = new Gson();
        OpenAndCommon obj = openAndCommon;
        String jsonInString = gson.toJson(obj).toString();
        Log.d(TAG, "overlayFromOpenAndCommonAndCommon: " + jsonInString);

        items.add(new OverlayItem(name, jsonInString, new GeoPoint(latitude, longitude)));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int index, OverlayItem item) {
                        MarkerOnClickEvent(context, item);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, OverlayItem item) {
                        return false;
                    }
                });

        return mOverlay;
    }

}
