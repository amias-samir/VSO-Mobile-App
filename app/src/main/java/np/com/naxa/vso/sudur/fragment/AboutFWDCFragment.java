package np.com.naxa.vso.sudur.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.naxa.nepal.sudurpaschimanchal.R;
import com.naxa.nepal.sudurpaschimanchal.activities.GathanAadeshPdfActivity;
import com.naxa.nepal.sudurpaschimanchal.adapter.ExpandableListAdapter;
import com.naxa.nepal.sudurpaschimanchal.model.UrlClass;

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
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import at.blogc.android.views.ExpandableTextView;

/**
 * Created by Nishon Tandukar on 27 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class AboutFWDCFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "AboutFWDCFragment";
    private ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> mission = new ArrayList<String>();
    List<String> vision = new ArrayList<String>();
    List<String> objective = new ArrayList<String>();
    List<String> main_works = new ArrayList<String>();
    List<String> gathan_aadesh = new ArrayList<String>();
    ExpandableListAdapter listAdapter;
    NestedScrollView scrollView;

    private SwipeRefreshLayout swipeRefreshLayout;


    TextView tvTitle;
    ImageView ivImageView;

    ExpandableTextView tvDesc;
    private Button buttonToggle;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fwdc_about, container, false);

        initUI(rootView);
        prepareListData();
        try {
            setFWDCDesc();
        } catch (JSONException e) {

            Log.e("shit", " " + e.toString());
        }
        setExpListView();
        setupSwipeToRefresh();

        return rootView;
    }

    private void initUI(View rootView) {

        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        tvTitle = (TextView) rootView.findViewById(R.id.textView_label_about_fwdc);
        tvDesc = (ExpandableTextView) rootView.findViewById(R.id.textView_about_fwdc);
        ivImageView = (ImageView) rootView.findViewById(R.id.backdrop);
        buttonToggle = (Button) rootView.findViewById(R.id.button_toggle);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_fragment_fwdc_about);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollview_fragment_fwdc_about);

    }


    private void setupSwipeToRefresh() {


        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


    }


    private void setExpListView() {

        tvDesc.setAnimationDuration(750L);

        // set interpolators for both expanding and collapsing animations
        tvDesc.setInterpolator(new OvershootInterpolator());

        // or set them separately
        tvDesc.setExpandInterpolator(new OvershootInterpolator());
        tvDesc.setCollapseInterpolator(new OvershootInterpolator());

        // toggle the ExpandableTextView
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(final View v) {
                tvDesc.toggle();
                buttonToggle.setText(tvDesc.isExpanded() ? R.string.collapse : R.string.expand);
            }
        });

        // but, you can also do the checks yourself
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (tvDesc.isExpanded()) {
                    tvDesc.collapse();
                    buttonToggle.setText(R.string.expand);
                } else {
                    tvDesc.expand();
                    buttonToggle.setText(R.string.collapse);
                }
            }
        });

        // listen for expand / collapse events
        tvDesc.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView collapsed");
            }
        });


        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setDividerHeight(15);


        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (groupPosition == 4) {
                    switch (childPosition) {
                        case 0:
                            Intent intent = new Intent(getActivity(), GathanAadeshPdfActivity.class);
                            startActivity(intent);
                            break;
                    }

                }

                return false;
            }
        });

    }


    private void saveFWDCAPIResponse(String text) {
        SharedPreferences sharedpreferences;
        sharedpreferences = getActivity().getSharedPreferences("fwdc_json", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString("fwdc_json", text);
        edit.apply();
    }


    private void setFWDCDesc() throws JSONException {
        SharedPreferences sharedpreferences;
        sharedpreferences = getActivity().getSharedPreferences("fwdc_json", Context.MODE_PRIVATE);
        String text = sharedpreferences.getString("fwdc_json", "");
        JSONObject fwdcDescJSON = new JSONObject(text);
        JSONArray jsonArray = fwdcDescJSON.getJSONArray("data");
        JSONObject jsonObject = jsonArray.getJSONObject(0);


        String fwdcDescTitle = jsonObject.getString("title_np");
        String fwdcDescDetail = jsonObject.getString("desc_np");
        String fwdcPhoto = jsonObject.getString("office_photo");

        Glide.with(getActivity().getApplicationContext())
                .load(fwdcPhoto)
                .thumbnail(0.5f)
                .override(200, 400)
                .into(ivImageView);

        tvDesc.setText(fwdcDescDetail);
        tvTitle.setText(fwdcDescTitle);
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("ध्येय ");
        listDataHeader.add("लक्ष्य ");
        listDataHeader.add("उधेश्य");
        listDataHeader.add("मुख्य कार्यहरु");
        listDataHeader.add("गठन आदेश");

        mission.add(getString(R.string.mission));
        vision.add(getString(R.string.vision));
        objective.add(getString(R.string.objective));
        objective.add(getString(R.string.objective1));
        objective.add(getString(R.string.objective2));
        main_works.add(getString(R.string.main_works));
        main_works.add(getString(R.string.main_works1));
        main_works.add(getString(R.string.main_works2));
        main_works.add(getString(R.string.main_works3));
        main_works.add(getString(R.string.main_works4));
        main_works.add(getString(R.string.main_works5));
        main_works.add(getString(R.string.main_works6));
        main_works.add(getString(R.string.main_works7));
        main_works.add(getString(R.string.main_works8));
        main_works.add(getString(R.string.main_works9));
        main_works.add(getString(R.string.main_works10));
        main_works.add(getString(R.string.main_works11));

        gathan_aadesh.add(" गठन आदेश पि.डी.एफ हेर्नको लागि यहाँ क्लिक गर्नुहोस");


        listDataChild.put(listDataHeader.get(0), mission); // Header, Child data
        listDataChild.put(listDataHeader.get(1), vision);
        listDataChild.put(listDataHeader.get(2), objective);
        listDataChild.put(listDataHeader.get(3), main_works);
        listDataChild.put(listDataHeader.get(4), gathan_aadesh);


    }

    @Override
    public void onRefresh() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && !networkInfo.isConnected()) {
            showSnackMsg("ईन्टरनेट कनेक्सन छैन ।");
            return;
        }

        AboutFWDCApiCall aboutFWDCApiCall = new AboutFWDCApiCall();
        aboutFWDCApiCall.execute();
    }

    private void showSnackMsg(String s) {
//        Snackbar.make(vie, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
//                .setAction("Retry", null).show();

        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


    public class AboutFWDCApiCall extends AsyncTask<String, Void, String> {
        JSONArray data = null;


        String fwdc_desc_en = null, fwdc_title_en = null, fwdc_desc_np = null, fwdc_title_np = null, office_img = "";


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
            // TODO Auto-generated method stub

            String text1 = "";


            text1 = POST(UrlClass.URL_ABOUT_FWDC);


            if (isValidNewsResponse(text1)) {
                saveFWDCAPIResponse(text1);




            }


            return text1;

        }


        @Override
        protected void onPostExecute(String result) {

            if (isValidNewsResponse(result)){

                try {
                    setFWDCDesc();
                } catch (JSONException e) {
                    Log.e("shit", " " + e.toString());
                }

            }

            swipeRefreshLayout.setRefreshing(false);


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

    private boolean isValidNewsResponse(String response) {
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
}
