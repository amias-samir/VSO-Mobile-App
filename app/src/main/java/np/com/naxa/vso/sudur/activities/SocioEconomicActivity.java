package np.com.naxa.vso.sudur.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.adapter.SocioEconomicStatRecyclerAdapter;
import np.com.naxa.vso.sudur.model.SosioEconomicStatModel;
import np.com.naxa.vso.sudur.model.UrlClass;

public class SocioEconomicActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;

    private View coordinatorLayoutView;

    Toolbar toolbar;
    String jsonToSend = null;
    ProgressDialog mProgressDlg;

    SocioEconomicStatRecyclerAdapter ca;
    public static List<SosioEconomicStatModel> resultCur = new ArrayList<>();
    public static List<SosioEconomicStatModel> filteredList = new ArrayList<>();

    private static final String TAG = "SocioEconomic";
    public static final String MyPREFERENCES = "socio_economic";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    public static ArrayList<String> stat_vlue = new ArrayList<String>();

    String Chart_Type;


    private boolean setData;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ImageView imageView;


    String text = null;
    JSONArray data = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socio_economic);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("सामाजिक/आर्थिक तथ्यांक");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        recyclerView = (RecyclerView) findViewById(R.id.NewsList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();




        if (sharedpreferences.getString("socio_economic", "").trim().isEmpty()) {
            if (networkInfo != null && networkInfo.isConnected()) {

                mProgressDlg = new ProgressDialog(this);
                mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
                mProgressDlg.setIndeterminate(false);
                //  mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                convertDataToJson();
                createList();
                fillTable();

            } else {
                coordinatorLayoutView = findViewById(R.id.main_content);
                Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
            }
        }
        else {
            convertDataToJson();
            createList();
            fillTable();
        }
//        if (networkInfo != null && networkInfo.isConnected()) {

//        } else {
//            coordinatorLayoutView = findViewById(R.id.main_content);
//            Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
//                    .setAction("Retry", null).show();
//        }


        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkInfo != null && networkInfo.isConnected()) {
                    editor.clear();
                    editor.commit();

                    convertDataToJson();
                    createList();
                    fillTable();

                } else {
                    coordinatorLayoutView = findViewById(R.id.main_content);
                    Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                            .setAction("Retry", null).show();
                    swipeContainer.setRefreshing(false);
                }

                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


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

                    Chart_Type = resultCur.get(position).getStat_type_id();

                    if (Chart_Type.equals("3")) {

                        Log.e(TAG, "onInterceptTouchEvent if: " + Chart_Type.toString());

                        Intent intent = new Intent(SocioEconomicActivity.this, PieChartDetailsActivity.class);
                        intent.putExtra("stat_name", resultCur.get(position).getStat_name_np());
//                    intent.putExtras(bundel);
                        intent.putExtra("dist1_value", resultCur.get(position).getDist1_value());
                        intent.putExtra("dist2_value", resultCur.get(position).getDist2_value());
                        intent.putExtra("dist3_value", resultCur.get(position).getDist3_value());
                        intent.putExtra("dist4_value", resultCur.get(position).getDist4_value());
                        intent.putExtra("dist5_value", resultCur.get(position).getDist5_value());
                        intent.putExtra("dist6_value", resultCur.get(position).getDist6_value());
                        intent.putExtra("dist7_value", resultCur.get(position).getDist7_value());
                        intent.putExtra("dist8_value", resultCur.get(position).getDist8_value());
                        intent.putExtra("dist9_value", resultCur.get(position).getDist9_value());
                        startActivity(intent);
                    } else if (Chart_Type.equals("1")) {
                        Log.e(TAG, "onInterceptTouchEvent else if: " + Chart_Type.toString());
                        Intent intent = new Intent(SocioEconomicActivity.this, BarChartDetailsActivity.class);
                        intent.putExtra("stat_name", resultCur.get(position).getStat_name_np());
//                    intent.putExtras(bundel);
                        intent.putExtra("dist1_value", resultCur.get(position).getDist1_value());
                        intent.putExtra("dist2_value", resultCur.get(position).getDist2_value());
                        intent.putExtra("dist3_value", resultCur.get(position).getDist3_value());
                        intent.putExtra("dist4_value", resultCur.get(position).getDist4_value());
                        intent.putExtra("dist5_value", resultCur.get(position).getDist5_value());
                        intent.putExtra("dist6_value", resultCur.get(position).getDist6_value());
                        intent.putExtra("dist7_value", resultCur.get(position).getDist7_value());
                        intent.putExtra("dist8_value", resultCur.get(position).getDist8_value());
                        intent.putExtra("dist9_value", resultCur.get(position).getDist9_value());
                        startActivity(intent);
                    }


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


    // data convert
    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button

        try {

            JSONObject header = new JSONObject();

            header.put("token", "bf5d483811");
            jsonToSend = header.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void createList() {
        resultCur.clear();

        SocioEconomicService restApi = new SocioEconomicService();
        restApi.execute();

    }

    private class SocioEconomicService extends AsyncTask<String, Void, String> {
        JSONArray data = null;

        protected String getASCIIContentFromEntity(HttpURLConnection entity)
                throws IllegalStateException, IOException {
            InputStream in = (InputStream) entity.getContent();

            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);

                if (n > 0)
                    out.append(new String(b, 0, n));
            }

            return out.toString();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String text = "";

            if (sharedpreferences.getString("socio_economic", "").trim().isEmpty()) {
                if (networkInfo != null && networkInfo.isConnected()) {

                    text = POST(UrlClass.URL_SOCIO_ECONOMIC);
                    editor.putString("socio_economic", text);
                    editor.commit();

                    Log.e(TAG, "doInBackground: " + text);
                } else {
                    coordinatorLayoutView = findViewById(R.id.main_content);
                    Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                            .setAction("Retry", null).show();
                }
            } else {
                text = sharedpreferences.getString("socio_economic", "");
            }

            JSONArray list;
            String txtDisp = null;
            ArrayList<String> question = new ArrayList<>();
            try {


                JSONObject jsonObj = new JSONObject(text);

                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());


                for (int i = 0; i < data.length(); i++) {

                    JSONObject c = data.getJSONObject(i);
                    SosioEconomicStatModel newData = new SosioEconomicStatModel();

//                    Chart_Type = c.getString("type_id");
                    newData.setStat_type_id(c.getString("type_id"));
                    newData.setStat_name_np(c.getString("data_name"));
                    newData.setDist1_value(c.getString("Kailali"));
                    newData.setDist2_value(c.getString("Achham"));
                    newData.setDist3_value(c.getString("Bajhang"));
                    newData.setDist4_value(c.getString("Bajura"));
                    newData.setDist5_value(c.getString("Kanchanpur"));
                    newData.setDist6_value(c.getString("Dadeldhura"));
                    newData.setDist7_value(c.getString("Baitadi"));
                    newData.setDist8_value(c.getString("Darchula"));
                    newData.setDist9_value(c.getString("Doti"));

                    resultCur.add(newData);

                }
            } catch (Exception e) {
                Log.e(TAG, "catch: " + e.toString());
                return e.getLocalizedMessage();

            }

            return text.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if ((mProgressDlg != null) && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }
            if (result != null) {
                fillTable();
                swipeContainer.setRefreshing(false);
                if ((mProgressDlg != null) && mProgressDlg.isShowing()) {
                    mProgressDlg.dismiss();
                }
            }

        }

        public String POST(String myurl) {

            URL url;
            String response = "";
            try {
                url = new URL(myurl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("data", jsonToSend);
                String query = builder.build().getEncodedQuery();
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

    }

    public void fillTable() {

        filteredList = resultCur;
        ca = new SocioEconomicStatRecyclerAdapter(this, filteredList);
        recyclerView.setAdapter(ca);

    }
}
