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
import np.com.naxa.vso.sudur.activities.NewsDetailsActivity;
import np.com.naxa.vso.sudur.adapter.NewsAndEvents_List_Adapter;
import np.com.naxa.vso.sudur.model.NewsAndEventsModel;
import np.com.naxa.vso.sudur.model.UrlClass;

/**
 * Created by Samir on 4/29/2016.
 */
public class NewsAndEventsFragment extends Fragment {

    //Susan
    private SwipeRefreshLayout swipeContainer;

    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog mProgressDlg;

    NewsAndEvents_List_Adapter ca;
    public static List<NewsAndEventsModel> resultCur = new ArrayList<>();
    public static List<NewsAndEventsModel> filteredList = new ArrayList<>();
    public static final String MyPREFERENCES = "news_and_events_data";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private boolean setData;
    String jsonToSend = null;
    private String date, time;

    // TODO: Rename and change types and number of parameters
    public static NewsAndEventsFragment newInstance(String param1, String param2) {
        NewsAndEventsFragment fragment = new NewsAndEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public NewsAndEventsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_news_and_events, container, false);

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.NewsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//        localYotube();
        createList();
        convertDataToJson();

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


                    Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);

                    intent.putExtra("news_title_np", resultCur.get(position).news_title_np);
                    intent.putExtra("news_desc_np", resultCur.get(position).news_desc_np);

                    intent.putExtra("news_date_np", resultCur.get(position).news_date_np);
                    intent.putExtra("news_image", resultCur.get(position).mThumbnail);


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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainerNews);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (networkInfo != null && networkInfo.isConnected()) {

                    editor.clear();
                    editor.commit();

                    refreshContent();
                    swipeContainer.setRefreshing(false);
                } else {
                    Snackbar.make(getView(), "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                            .setAction("Retry", null).show();
                    swipeContainer.setRefreshing(false);
                }
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }


    private void refreshContent() {

        createList();
        fillTable();

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

//        if (setData) {
//        mProgressDlg = new ProgressDialog(getActivity());
//        mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
//        mProgressDlg.setIndeterminate(false);
//        mProgressDlg.show();

        NewsAndEventsListService restApi = new NewsAndEventsListService();
        restApi.execute();

//        } else {
//
//            mProgressDlg = new ProgressDialog(getActivity());
//            mProgressDlg.setMessage("Loading please Wait...");
//            mProgressDlg.setIndeterminate(false);
//            mProgressDlg.show();
//            PoticianListService restApi = new PoticianListService();
//            restApi.execute();
//        }


    }


    private class NewsAndEventsListService extends AsyncTask<String, Void, String> {
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

            if (networkInfo != null && networkInfo.isConnected()) {
                text = POST(UrlClass.URL_NEWS_AND_EVENTS);
                Boolean isValidAPIResposne = isValidNewsResponse(text);

                if (isValidAPIResposne) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("news_and_events_data");
                    editor.putString("news_and_events_data", text);
                    editor.apply();
                }


            } else {
                //Snackbar.make(getView(), "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                //   .setAction("Retry", null).show();
            }

            text = sharedpreferences.getString("news_and_events_data", "");

            Log.e("DATA", "" + text.toString());


            JSONArray list;
            String txtDisp = null;
            ArrayList<String> question = new ArrayList<>();
            try {


                JSONObject jsonObj = new JSONObject(text);
                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());


                for (int i = 0; i < data.length(); i++) {

                    JSONObject c = data.getJSONObject(i);

                    NewsAndEventsModel newData = new NewsAndEventsModel();

                    newData.news_title_np = c.getString("sudur_news_title");
                    newData.news_desc_np = c.getString("sudur_news_desc");
                    newData.setmThumbnail(c.getString("news_image_thumb"));

                    newData.news_date_np = c.getString("sudur_news_date");


//                    newData.mThumbnail = c.getString("video_img");

                    resultCur.add(newData);

                }
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text.toString();
        }


        private boolean isValidNewsResponse(String response) {
            try {
                return checkIfNewsAPIGaveValidRes(response);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean checkIfNewsAPIGaveValidRes(String text) throws JSONException {

            if (text == null || text.length() == 0) {
                return false;
            }

            int status = new JSONObject(text).getInt("status");
            if (status == 200) {
                return true;
            }

            return false;

        }





        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
//            mProgressDlg.dismiss();
            if (result != null) {
                fillTable();
                swipeContainer.setRefreshing(false);
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



        ca = new NewsAndEvents_List_Adapter(getActivity(), filteredList);
        recyclerView.setAdapter(ca);

    }
}