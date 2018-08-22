package np.com.naxa.vso.sudur.fragment;


import android.content.Context;
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
import np.com.naxa.vso.sudur.adapter.PolticalParties_List_Adapter;
import np.com.naxa.vso.sudur.model.PolticalParties_List_Model;
import np.com.naxa.vso.sudur.model.UrlClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoliticalPartiesFragment extends Fragment {

    View rootView;

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


    PolticalParties_List_Adapter ca;
    public static List<PolticalParties_List_Model> resultCur = new ArrayList<>();
    public static List<PolticalParties_List_Model> filteredList = new ArrayList<>();
    public static final String MyPREFERENCES = "poltical_parties_json";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private boolean setData;
    String jsonToSend = null;

    // TODO: Rename and change types and number of parameters
    public static PolticianListFragment newInstance(String param1, String param2) {
        PolticianListFragment fragment = new PolticianListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PoliticalPartiesFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_political_parties, container, false);

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

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (!sharedpreferences.getString("poltical_parties_json", "").trim().isEmpty()) {
        convertDataToJson();
        createList();
//        } else {
//            try {
//                Snackbar.make(getView(), "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
//                        .setAction("Retry", null).show();
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
//        }

        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeContainerParties);
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
                    try {
                        Snackbar.make(rootView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
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

        PoticalPartiesListService restApi = new PoticalPartiesListService();
        restApi.execute();
    }

    private class PoticalPartiesListService extends AsyncTask<String, Void, String> {
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

            if (sharedpreferences.getString("poltical_parties_json", "").trim().isEmpty()) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    text = POST(UrlClass.URL_PARTY_LIST);
                    editor.putString("poltical_parties_json", text);
                    editor.commit();
                } else {
                    try {
                        Snackbar.make(rootView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                text = sharedpreferences.getString("poltical_parties_json", "");
            }

            try {
                JSONObject jsonObj = new JSONObject(text);
                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());

                for (int i = 0; i < data.length(); i++) {

                    JSONObject c = data.getJSONObject(i);

                    PolticalParties_List_Model newData = new PolticalParties_List_Model();
                    newData.poltical_party_name_en = c.getString("sudur_political_party_name_en");
                    newData.poltical_party_name_np = c.getString("sudur_political_party_name_np");
                    newData.mThumbnail = c.getString("photo");

                    resultCur.add(newData);

                }
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

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
        ca = new PolticalParties_List_Adapter(getActivity(), filteredList);
        recyclerView.setAdapter(ca);

    }
}

