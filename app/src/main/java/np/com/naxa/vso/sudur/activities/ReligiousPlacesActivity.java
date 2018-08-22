package np.com.naxa.vso.sudur.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.adapter.LocalAttractionRecyclerAdapter;
import np.com.naxa.vso.sudur.model.LocalAttractionModel;

public class ReligiousPlacesActivity extends AppCompatActivity {

    private static final String TAG = "FWDRReligious";
    HamroSudurPaschimActivity hamro_sudur;

    public static String dist_id = "0";

    ProgressDialog mProgressDlg;
    public static final String MyPREFERENCES = "sudur_attract";
    SharedPreferences sharedpreferences;
    private boolean setData;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imageView;

    LocalAttractionRecyclerAdapter ca;
    public static List<LocalAttractionModel> resultCur = new ArrayList<>();
    public static List<LocalAttractionModel> filteredList = new ArrayList<>();

    String text = null;
    JSONArray data = null;

    public static final String EXTRA_NAME = "cheese_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_religious_places);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("धार्मिक स्थलहरू");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

//===============get district id from Hamro Sudur Paschhim Activity ================//
        hamro_sudur = new HamroSudurPaschimActivity();
        dist_id = hamro_sudur.dist_spinner_id;
        Log.e("district_id:  ", dist_id);


        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.NewsList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //        sharedpreferences = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());
//        setData = sharedpreferences.getBoolean("SET_ENGLISH_ON", true);
//
//        if (setData) {
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
        createList();


//        setContentView(R.layout.activity_fwdr_completed_projects);

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
//                    Drawer.closeDrawers();
                    int position = recyclerView.getChildPosition(child);


                    Intent intent = new Intent(ReligiousPlacesActivity.this, AttractionDetailsActivity.class);
                    intent.putExtra("place_title_np", resultCur.get(position).place_title_np);
                    intent.putExtra("plaece_desc_np", resultCur.get(position).plaece_desc_np);
                    intent.putExtra("project_district_np", resultCur.get(position).district_name_np);
                    intent.putExtra("address_name_np", resultCur.get(position).address_name_np);
                    intent.putExtra("place_image", resultCur.get(position).mThumbnail);
                    intent.putExtra("place_lat", resultCur.get(position).getPlace_lat());
                    intent.putExtra("place_lon", resultCur.get(position).getPlace_lon());

                    startActivity(intent);


                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }


    private void createList() {
        resultCur.clear();
        jsonParse();
        mProgressDlg.dismiss();
        fillTable();
    }

    public void jsonParse() {

        String place_type_id, district_id;

        JSONObject jsonObj = null;
        try {

            text = sharedpreferences.getString("sudur_attract", "");
            if (text != null) {
                Log.e("M_JSON", "" + text.toString());
                jsonObj = new JSONObject(text);

                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());
                for (int i = 0; i < data.length(); i++) {

                    JSONObject c = data.getJSONObject(i);

                    Log.e("cc", c.toString());

                    place_type_id = c.getString("place_type_id");
                    district_id = c.getString("district_id");

                    Log.e("PLACE_TYPE", place_type_id.toString());
                    Log.e("ATTR_DIST", district_id.toString());

                    if (place_type_id.equals("1") && district_id.equals(dist_id)) {
                        LocalAttractionModel newData = new LocalAttractionModel();
                        Log.d(TAG, "jsonParse: if");
//                newData.set(c.getString("dev_status_id"));
                        newData.setPlace_title_en(c.getString("place_name_en"));
                        newData.setPlace_title_np(c.getString("place_name_np"));
                        newData.setPlace_desc_en(c.getString("place_desc_en"));
                        newData.setPlaece_desc_np(c.getString("place_desc_np"));
                        newData.setAddress_name_en(c.getString("place_address_en"));
                        newData.setAddress_name_np(c.getString("place_address_np"));
                        newData.setDistrict_name_en(c.getString("district_name_en"));
                        newData.setDistrict_name_np(c.getString("district_name_np"));
                        newData.setmThumbnail(c.getString("large_photo_path"));
                        newData.setPlace_lat(c.getString("place_lat"));
                        newData.setPlace_lon(c.getString("place_lon"));

                        resultCur.add(newData);
                        Log.e("POJO", "" + newData.toString());

                    } else if (place_type_id.equals("1") && dist_id.equals("0")) {
                        LocalAttractionModel newData = new LocalAttractionModel();
                        Log.d(TAG, "jsonParse: if");
//                newData.set(c.getString("dev_status_id"));


                        String address_np = c.getString("place_address_np");
                        String dist_np = c.getString("district_name_np");


                        if (address_np == null || address_np.trim().length() == 0 || address_np.contains("null")) {
                            address_np = "";
                        } else {
                            address_np = "," +c.getString("place_address_np");
                        }

                        if (dist_np == null || dist_np.trim().length() == 0 || address_np.contains("null")) {
                            dist_np = "";

                        } else {
                            dist_np =  c.getString("district_name_np");
                        }

                        String address = "ठेगाना: "+dist_np+address_np;

                        newData.setAddress_name_np("");

                        newData.setDistrict_name_np(address);



                        newData.setPlace_title_en(c.getString("place_name_en"));
                        newData.setPlace_title_np(c.getString("place_name_np"));
                        newData.setPlace_desc_en(c.getString("place_desc_en"));
                        newData.setPlaece_desc_np(c.getString("place_desc_np"));
                        newData.setAddress_name_en(c.getString("place_address_en"));
                        newData.setDistrict_name_en(c.getString("district_name_en"));
                        newData.setmThumbnail(c.getString("large_photo_path"));
                        newData.setPlace_lat(c.getString("place_lat"));
                        newData.setPlace_lon(c.getString("place_lon"));

                        resultCur.add(newData);
                        Log.e("POJO", "" + newData.toString());


                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fillTable() {

        filteredList = resultCur;
        ca = new LocalAttractionRecyclerAdapter(this, filteredList);
        recyclerView.setAdapter(ca);

    }
}

