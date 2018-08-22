package np.com.naxa.vso.sudur.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;



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
import np.com.naxa.vso.sudur.fragment.CompletedProjectsFragment;
import np.com.naxa.vso.sudur.fragment.LocalAttractionFragment;
import np.com.naxa.vso.sudur.model.UrlClass;

public class HamroSudurPaschimActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SwipeRefreshLayout swipeContainer;
    private View coordinatorLayoutView;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    ProgressDialog mProgressDlg;
    public static final String MyPREFERENCES = "hamro_sudurpaschhim";
    public static final String MyPREFERENCES1 = "sudur_attract";
    public static final String NagarPalikaPREFERENCES1 = "nagar_budget";

    SharedPreferences sharedpreferences, sharedpreferences1, nagarpalikasharedpreferences;
    SharedPreferences.Editor editor, editor1, nagarpalikaeditor;
    private boolean setData;
    String jsonToSend = null;

    public static String dist_spinner_id = "0";

    private TabLayout tabLayout;
    private ViewPager viewPager;


    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamro_sudur_paschim);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("हाम्रो सुदुरपश्चिम");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        //SharedPreferences-DEV_ACTIVITIES
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        //SharedPreferences-DEV_ACTIVITIES
        sharedpreferences1 = this.getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        editor1 = sharedpreferences1.edit();
        //SharedPreferences-Nagarpalika Budget
        nagarpalikasharedpreferences = this.getSharedPreferences(NagarPalikaPREFERENCES1, Context.MODE_PRIVATE);
        nagarpalikaeditor = nagarpalikasharedpreferences.edit();

/*
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.district_list_spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("सबै जिल्ला");
        categories.add("कैलाली");
        categories.add("आछाम");
        categories.add("बझांग");
        categories.add("बाजुरा");
        categories.add("कन्चनपुर");
        categories.add("डडेल्धुरा");
        categories.add("बैतडी");
        categories.add("दार्चुला");
        categories.add("डोटी");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

*/

        if (sharedpreferences1.getString("sudur_attract", "").trim().isEmpty()) {
            if (networkInfo != null && networkInfo.isConnected()) {

                mProgressDlg = new ProgressDialog(HamroSudurPaschimActivity.this);
                mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
                mProgressDlg.setIndeterminate(false);
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                //=============DEVELOPEMENT ACTIVITIES APICALL==================//
                convertDataToJson();
                ApiCall apiCall = new ApiCall();
                apiCall.execute();


                //=============local attraction PLACES APICALL==================//
                convertDataToJson();
                AttrractionApiCall attractionapiCall = new AttrractionApiCall();
                attractionapiCall.execute();

                //   Nagarpalika Budget Api call
                convertDataToJson();
                NagarpalikaBudgetApiCall nagarpalikaBudgetApiCall = new NagarpalikaBudgetApiCall();
                nagarpalikaBudgetApiCall.execute();

            } else {
                coordinatorLayoutView = findViewById(R.id.main_content);
                Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
            }

        }
        else {
            //=============DEVELOPEMENT ACTIVITIES APICALL==================//
            convertDataToJson();
            ApiCall apiCall = new ApiCall();
            apiCall.execute();

            convertDataToJson();
            //=============RELIGIOUS PLACES APICALL==================//
            AttrractionApiCall attractionapiCall = new AttrractionApiCall();
            attractionapiCall.execute();

            //   Nagarpalika Budget Api call
            convertDataToJson();
            NagarpalikaBudgetApiCall nagarpalikaBudgetApiCall = new NagarpalikaBudgetApiCall();
            nagarpalikaBudgetApiCall.execute();
        }




        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkInfo != null && networkInfo.isConnected()) {
                    editor.clear();
                    editor.commit();

                    editor1.clear();
                    editor1.commit();

                    mProgressDlg = new ProgressDialog(HamroSudurPaschimActivity.this);
                    mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
                    mProgressDlg.setIndeterminate(false);
                    mProgressDlg.setCancelable(false);
                    mProgressDlg.show();

                    convertDataToJson();
                    ApiCall apiCall = new ApiCall();
                    apiCall.execute();

                    convertDataToJson();
                    AttrractionApiCall attractionapiCall = new AttrractionApiCall();
                    attractionapiCall.execute();

                    convertDataToJson();
                    NagarpalikaBudgetApiCall nagarpalikaBudgetApiCall = new NagarpalikaBudgetApiCall();
                    nagarpalikaBudgetApiCall.execute();

                    viewPager.clearFocus();
                    tabLayout.removeAllTabs();
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.setupWithViewPager(viewPager);
                        }
                    });

                    //Nishon
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.all_project_fragment_root);
                    if (currentFragment instanceof CompletedProjectsFragment) {
                        Log.i("Nishon", "i am in");


                        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                        fragTransaction.detach(currentFragment);
                        fragTransaction.attach(currentFragment);
                        fragTransaction.commit();
                    }

                    if (apiCall.equals(null)) {
                        swipeContainer.setRefreshing(false);
                        Log.d("DEBUG", "Fetch timeline error: ");
                    }
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocalAttractionFragment(), "स्थानिय \n आकर्षण");
//        adapter.addFragment(new NagarpalikaFragment(), "बजेट");
//        adapter.addFragment(new DevStatusSudurPaschhimFragment(), "बिकाश \n गतिविधिहरु");

        viewPager.setAdapter(adapter);

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
            // TODO Auto-generated method stub
            String text = "";

            try {
                if (sharedpreferences.getString("hamro_sudurpaschhim", "").trim().isEmpty()) {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        text = POST(UrlClass.URL_SUDURPASCHHIM_DEV_ACT);

                        Log.e("hamro_sudurpaschhim", "" + text.toString());

                        editor.putString("hamro_sudurpaschhim", text);
                        editor.commit();
                    } else {
                        coordinatorLayoutView = findViewById(R.id.main_content);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }

                } else {

                    text = sharedpreferences.getString("hamro_sudurpaschhim", "");
                    Log.e("hamro_sudurpaschhim", "" + text.toString());

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

            if (result != null) {
                //Success
//                swipeContainer.setRefreshing(false);
            } else {
                coordinatorLayoutView = findViewById(R.id.main_content);
                Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
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

    public class NagarpalikaBudgetApiCall extends AsyncTask<String, Void, String> {
        JSONArray data1 = null;

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

            try {
                if (nagarpalikasharedpreferences.getString("nagar_budget", "").trim().isEmpty()) {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        text1 = POST(UrlClass.URL_NAGARPALIKA_BUDGET);

                        Log.e("nagar_budget", "" + text1.toString());
                        nagarpalikaeditor.putString("nagar_budget", text1);
                        nagarpalikaeditor.commit();
                    } else {
                        coordinatorLayoutView = findViewById(R.id.main_content);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }

                } else {

                    text1 = sharedpreferences1.getString("nagar_budget", "");
                    Log.e("nagar_budget", "" + text1.toString());
                }

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text1.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            //Log.e("ONPOSTEXECUTE", "ONPOST");
//            if (mProgressDlg!= null && mProgressDlg.isShowing()){
//                mProgressDlg.dismiss();
//            }
//            if (swipeContainer != null && swipeContainer.isRefreshing() ){
//                swipeContainer.setRefreshing(false);
//            }
            if (result != null) {

                //Success

            }
//            else {
//                coordinatorLayoutView = findViewById(R.id.main_content);
//                Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
//                        .setAction("Retry", null).show();
//            }

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

    public class AttrractionApiCall extends AsyncTask<String, Void, String> {
        JSONArray data1 = null;
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
            // TODO Auto-generated method stub
            String text1 = "";

            try {
                if (sharedpreferences1.getString("sudur_attract", "").trim().isEmpty()) {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        text1 = POST(UrlClass.URL_SUDURPASCHHIM_LOCAL_ATTR);

                        Log.e("sudur_attract", "" + text1.toString());
                        editor1.putString("sudur_attract", text1);
                        editor1.commit();
                    } else {
                        coordinatorLayoutView = findViewById(R.id.main_content);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }

                } else {

                    text1 = sharedpreferences1.getString("sudur_attract", "");
                    Log.e("sudur_attract", "" + text1.toString());
                }

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text1.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (mProgressDlg!= null && mProgressDlg.isShowing()){
                mProgressDlg.dismiss();
            }
            if (swipeContainer != null && swipeContainer.isRefreshing() ){
                swipeContainer.setRefreshing(false);
            }
            if (result != null) {

                //Success

            } else {
                coordinatorLayoutView = findViewById(R.id.main_content);
                Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
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

    //======================spinner item selection========================//
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.nliveo_black));
        String item = parent.getItemAtPosition(position).toString();

        if (item == "कैलाली") {
            dist_spinner_id = "1";
        } else if (item == "आछाम") {
            dist_spinner_id = "2";
        } else if (item == "बझांग") {
            dist_spinner_id = "3";
        } else if (item == "बाजुरा") {
            dist_spinner_id = "4";
        } else if (item == "कन्चनपुर") {
            dist_spinner_id = "5";
        } else if (item == "डडेल्धुरा") {
            dist_spinner_id = "6";
        } else if (item == "बैतडी") {
            dist_spinner_id = "7";
        } else if (item == "दार्चुला") {
            dist_spinner_id = "8";
        } else if (item == "डोटी") {
            dist_spinner_id = "9";
        } else if (item == "सबै जिल्ला") {
            dist_spinner_id = "0";
        }

        if(getFragmentRefreshListener()!=null){
            getFragmentRefreshListener().onRefresh();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }


}
