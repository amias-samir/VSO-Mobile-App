package np.com.naxa.vso.sudur.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


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
import np.com.naxa.vso.sudur.activities.ProjectDetailsActivity;
import np.com.naxa.vso.sudur.adapter.DevActivityRecyclerViewAdapter;
import np.com.naxa.vso.sudur.model.DevActivity_Pojo;
import np.com.naxa.vso.sudur.model.UrlClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnGoingProjectsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressDialog mProgressDlg;
    public static final String MyPREFERENCES = "dev_activities";
    SharedPreferences sharedpreferences;
    private boolean setData;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    DevActivityRecyclerViewAdapter ca;
    public static List<DevActivity_Pojo> resultCur = new ArrayList<>();
    public static List<DevActivity_Pojo> filteredList = new ArrayList<>();

    String text = null;
    JSONArray data = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout rootLayout;

    // TODO: Rename and change types and number of parameters
    public static OnGoingProjectsFragment newInstance(String param1, String param2) {
        OnGoingProjectsFragment fragment = new OnGoingProjectsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OnGoingProjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_completed_projects, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_fragment_all_projects);
        setupSwipeToRefresh();

        rootLayout = (LinearLayout) rootView.findViewById(R.id.all_project_fragment_root);


        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.NewsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //        sharedpreferences = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());
//        setData = sharedpreferences.getBoolean("SET_ENGLISH_ON", true);
//
//        if (setData) {
        mProgressDlg = new ProgressDialog(getActivity());
        mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
        mProgressDlg.setIndeterminate(false);
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
        createList();
        mProgressDlg.dismiss();
//        } else {
//
//            mProgressDlg = new ProgressDialog(getActivity());
//            mProgressDlg.setMessage("Loading please Wait...");
//            mProgressDlg.setIndeterminate(false);
//            mProgressDlg.show();
//            createList();
//            fillTable();
//            mProgressDlg.dismiss();
//        }

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

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

//                    Fragment_YouTube f = Fragment_YouTube.newInstance("tzZB6mPn-JQ");
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();

                    Intent intent = new Intent(getActivity(), ProjectDetailsActivity.class);
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

        return rootView;
    }

    private void createList() {
        resultCur.clear();
        jsonParse();
        fillTable();
    }

    public void jsonParse(){

        String dev_status_id = null, dev_title_en = null, dev_title_np = null,
                dev_desc_en = null, dev_desc_np = null, dev_contractor_en = null,
                dev_contractor_np = null, dev_budget = null, district_name_en = null,
                district_name_np = null;

        JSONObject jsonObj = null;
        try {

            text = sharedpreferences.getString("dev_activities", "");
            if (text!= null){
                Log.e("M_JSON", "" + text.toString());
                jsonObj = new JSONObject(text);

                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    dev_status_id = c.getString("dev_status_id");
                    Log.e("DEV_STATUS", "" + dev_status_id.toString());

                    if(dev_status_id.equals("2")){
                        DevActivity_Pojo newData = new DevActivity_Pojo();
//                newData.set(c.getString("dev_status_id"));
                        newData.setDev_title_en(c.getString("dev_title_en"));
                        newData.setDev_title_np(c.getString("dev_title_np"));
                        newData.setDev_desc_en(c.getString("dev_desc_en"));
                        newData.setDev_desc_np(c.getString("dev_desc_np"));
                        newData.setDev_contractor_en(c.getString("dev_contractor_en"));
                        newData.setDev_contractor_np(c.getString("dev_contractor_np"));
                        newData.setDev_budget_en(c.getString("dev_budget"));
//                newData.setDev_budget_en(c.getString("dev_budget"));
                        newData.setDistrict_name_en(c.getString("district_name_en"));
                        newData.setDistrict_name_np(c.getString("district_name_np"));
                        newData.setmThumbnail(c.getString("dev_img"));

                        resultCur.add(newData);
                    Log.e("POJO", "" + newData.toString());

                    }

                }

//                dev_status_id = c.getString("dev_status_id");
//                dev_title_en = c.getString("dev_title_en");
//                dev_title_np = c.getString("dev_title_np");
//                dev_desc_en = c.getString("dev_desc_en");
//                dev_desc_np = c.getString("dev_desc_np");
//                dev_contractor_en = c.getString("dev_contractor_en");
//                dev_contractor_np = c.getString("dev_contractor_np");
//                dev_budget = c.getString("dev_budget");
//                district_name_en = c.getString("district_name_en");
//                district_name_np = c.getString("district_name_np");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fillTable() {

        filteredList = resultCur;
        ca = new DevActivityRecyclerViewAdapter(getActivity(), filteredList);
        recyclerView.setAdapter(ca);

    }

    private void setupSwipeToRefresh() {


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


    }

    @Override
    public void onRefresh() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();



        if (networkInfo != null && !networkInfo.isConnected()) {
            showSnackMsg("ईन्टरनेट कनेक्सन छैन ।");
            return;
        }

        ApiCall apiCall = new ApiCall();
        apiCall.execute();
    }

    private void showSnackMsg(String s) {
        Snackbar.make(rootLayout, s, Snackbar.LENGTH_LONG)
                .show();


    }

    public class ApiCall extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String dev_status_id = null, dev_title_en = null, dev_title_np = null,
                dev_desc_en = null, dev_desc_np = null, dev_contractor_en = null,
                dev_contractor_np = null, dev_budget = null, district_name_en = null,
                district_name_np = null;

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
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String text = "";

            text = POST(UrlClass.URL_DEV_ACTIVITIES);



            if (isValidResponse(text)) {



                saveAPIResponse(text);


            }


            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            if (isValidResponse(result)) {
                createList();
            }

            swipeRefreshLayout.setRefreshing(false);

        }


        public String getJsonToSend() {
            //function in the activity that corresponds to the layout button

            String jsonToSend = "";

            try {
                JSONObject header = new JSONObject();
                header.put("token", "bf5d483811");
                jsonToSend = header.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return jsonToSend;
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
                        .appendQueryParameter("data", getJsonToSend());
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

    private boolean isValidResponse(String response) {
        try {
            return checkIfAPIGaveValidRes(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkIfAPIGaveValidRes(String text) throws JSONException {

        if (text == null || text.length() == 0) {
            return false;
        }

        int status = new JSONObject(text).getInt("status");
        if (status == 200) {
            return true;
        }

        return false;

    }

    private void saveAPIResponse(String text) {
        SharedPreferences sharedpreferences;
        sharedpreferences = getActivity().getSharedPreferences("dev_activities", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString("dev_activities", text);
        edit.apply();
    }
}

