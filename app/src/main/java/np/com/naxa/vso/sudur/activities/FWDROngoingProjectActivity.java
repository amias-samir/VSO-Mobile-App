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
import np.com.naxa.vso.sudur.adapter.DevActivityRecyclerViewAdapter;
import np.com.naxa.vso.sudur.model.DevActivity_Pojo;

public class FWDROngoingProjectActivity extends AppCompatActivity {

    private static final String TAG = "FWDROngoing";
    HamroSudurPaschimActivity hamro_sudur;

    public static String dist_id = "0";

    ProgressDialog mProgressDlg;
    public static final String MyPREFERENCES = "hamro_sudurpaschhim";
    SharedPreferences sharedpreferences;
    private boolean setData;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imageView;

    DevActivityRecyclerViewAdapter ca;
    public static List<DevActivity_Pojo> resultCur = new ArrayList<>();
    public static List<DevActivity_Pojo> filteredList = new ArrayList<>();

    String text = null;
    JSONArray data = null;

    public static final String EXTRA_NAME = "cheese_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fwdr_ongoing_project);



        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("चालु परियोजना");
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


                    Intent intent = new Intent(FWDROngoingProjectActivity.this, ProjectDetailsActivity.class);
                    intent.putExtra("project_title_np", resultCur.get(position).dev_title_np);
                    intent.putExtra("project_desc_np", resultCur.get(position).dev_desc_np);

                    intent.putExtra("project_district_np", resultCur.get(position).district_name_np);
                    intent.putExtra("project_contractor_np", resultCur.get(position).dev_contractor_np);
                    intent.putExtra("project_budget_np", resultCur.get(position).dev_budget_np);
                    intent.putExtra("project_image", resultCur.get(position).mThumbnail);

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

    public void jsonParse(){

        String dev_status_id, dev_title_en = null, dev_title_np = null,
                dev_desc_en = null, dev_desc_np = null, dev_contractor_en = null,
                dev_contractor_np = null, dev_budget = null, district_name_en = null,
                district_name_np = null, district_id;

        JSONObject jsonObj = null;
        try {

            text = sharedpreferences.getString("hamro_sudurpaschhim", "");
            if (text!= null){
                Log.e("M_JSON", "" + text.toString());
                jsonObj = new JSONObject(text);

                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    dev_status_id = c.getString("dev_status_id");
                    district_id = c.getString("sudur_district_id");

                    Log.e("c", "" + c.toString());

                    Log.e("DEV_STATUS", "" + dev_status_id.toString());
                    Log.e("DEV_DIST", "" + district_id.toString());

                    if(dev_status_id.equals("2" ) && district_id.equals(dist_id) ){
                        DevActivity_Pojo newData = new DevActivity_Pojo();
                        Log.d(TAG, "jsonParse: if" );
//                newData.set(c.getString("dev_status_id"));
                        newData.setDev_title_en(c.getString("dev_title_en"));
                        newData.setDev_title_np(c.getString("dev_title_np"));
                        newData.setDev_desc_en(c.getString("dev_desc_en"));
                        newData.setDev_desc_np(c.getString("dev_desc_np"));
                        newData.setDev_contractor_en(c.getString("dev_contractor_en"));
                        newData.setDev_contractor_np(c.getString("dev_contractor_np"));
                        newData.setDev_budget_en(c.getString("dev_budget"));
                        newData.setDev_budget_np(c.getString("dev_budget"));
                        newData.setDistrict_name_en(c.getString("district_name_en"));
                        newData.setDistrict_name_np(c.getString("district_name_np"));

                        newData.setmThumbnail(c.getString("dev_img"));

                        resultCur.add(newData);
                        Log.e("POJO", "" + newData.toString());

                    }

                    else if(dev_status_id.equals("2" ) && dist_id.equals("0") ){
                        DevActivity_Pojo newData = new DevActivity_Pojo();
                        Log.d(TAG, "jsonParse: else if" );
//                newData.set(c.getString("dev_status_id"));
                        newData.setDev_title_en(c.getString("dev_title_en"));
                        newData.setDev_title_np(c.getString("dev_title_np"));
                        newData.setDev_desc_en(c.getString("dev_desc_en"));
                        newData.setDev_desc_np(c.getString("dev_desc_np"));
                        newData.setDev_contractor_en(c.getString("dev_contractor_en"));
                        newData.setDev_contractor_np(c.getString("dev_contractor_np"));
                        newData.setDev_budget_en(c.getString("dev_budget"));
                        newData.setDev_budget_np(c.getString("dev_budget"));
                        newData.setDistrict_name_en(c.getString("district_name_en"));
                        newData.setDistrict_name_np(c.getString("district_name_np"));

                        newData.setmThumbnail(c.getString("dev_img"));

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
        ca = new DevActivityRecyclerViewAdapter(this, filteredList);
        recyclerView.setAdapter(ca);

    }
}
