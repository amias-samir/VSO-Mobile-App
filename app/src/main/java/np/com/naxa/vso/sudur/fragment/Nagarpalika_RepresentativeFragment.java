package np.com.naxa.vso.sudur.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;


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
import np.com.naxa.vso.sudur.activities.NameListOfRepresentativeActivity;
import np.com.naxa.vso.sudur.adapter.NagarpalikaRepresentative_Adapter;
import np.com.naxa.vso.sudur.model.Local_Level_Representative_Model;
import np.com.naxa.vso.sudur.model.UrlClass;

/**
 * Created by Samir on 6/26/2017.
 */

public class Nagarpalika_RepresentativeFragment extends Fragment {

    //Susan
    View view;

    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    //Susan
    private SwipeRefreshLayout swipeContainer;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    ProgressDialog mProgressDlg;

    NagarpalikaRepresentative_Adapter ca;
    public static List<Local_Level_Representative_Model> resultCur = new ArrayList<>();
    public static List<Local_Level_Representative_Model> filteredList = new ArrayList<>();

    public static final String MyPREFERENCES = "nagarpalika_representative";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String jsonToSend = null;


    public Nagarpalika_RepresentativeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.local_chief_member_fragment, container, false);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        recyclerView = (RecyclerView) view.findViewById(R.id.NewsList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (sharedpreferences.getString("nagarpalika_representative", "").trim().isEmpty()) {
            if (networkInfo != null && networkInfo.isConnected()) {

                mProgressDlg = new ProgressDialog(getActivity());
                mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
                mProgressDlg.setIndeterminate(false);
                //  mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                convertDataToJson();
                createList();

            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            convertDataToJson();
            createList();
        }

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
                    int position = recyclerView.getChildPosition(child);

//                    Intent intent = new Intent(getActivity(), NameListOfRepresentativeActivity.class);
//                    intent.putExtra("district_np", resultCur.get(position).get_name_np());
//                    startActivity(intent);

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

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (networkInfo != null && networkInfo.isConnected()) {
                    editor.clear();
                    editor.commit();

                    convertDataToJson();
                    refreshContent();
                    swipeContainer.setRefreshing(false);
                } else {
                    Snackbar.make(view, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
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
        NagarpalikaAPI restApi = new NagarpalikaAPI();
        restApi.execute();
    }

    private class NagarpalikaAPI extends AsyncTask<String, Void, String> {
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
            if (sharedpreferences.getString("nagarpalika_representative", "").trim().isEmpty()) {
                if (networkInfo != null && networkInfo.isConnected()) {

                    text = POST(UrlClass.URL_POLTICIAN_LIST);
                    editor.putString("nagarpalika_representative", text);
                    editor.commit();
                } else {
                    try {
                        Snackbar.make(view, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                text = sharedpreferences.getString("nagarpalika_representative", "");
            }

            try {
                JSONObject jsonObj = new JSONObject(text);
                data = jsonObj.getJSONArray("data");
                Log.e("DATA", "" + data.toString());
                String district = NameListOfRepresentativeActivity.district_name ;
                Log.e("Nagar", "doInBackground: "+ district );


                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);



                    Local_Level_Representative_Model newData = new Local_Level_Representative_Model();

                    if(district.equals(c.getString("district_name_np"))) {

                        newData._palika_head_name_np = c.getString("head");
                        newData._palika_subhead_name_np = c.getString("sub_head");
                        newData._palika_name_np = c.getString("nagar_gau_palika_np");

//                        Log.e("Nagar", "doInBackground: inside loop "+ district );


                        resultCur.add(newData);
                    }

                }
            } catch (Exception e) {
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
        ca = new NagarpalikaRepresentative_Adapter(getActivity(), filteredList);
        recyclerView.setAdapter(ca);
        ca.notifyDataSetChanged();
    }
}
