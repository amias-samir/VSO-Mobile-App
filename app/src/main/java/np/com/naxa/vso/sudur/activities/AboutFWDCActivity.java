package np.com.naxa.vso.sudur.activities;
/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language gfoverning permissions and
 * limitations under the License.
 */

import android.app.ProgressDialog;
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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;



import org.json.JSONArray;
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
import np.com.naxa.vso.sudur.fragment.AboutFWDCFragment;
import np.com.naxa.vso.sudur.fragment.CompletedProjectsFragment;
import np.com.naxa.vso.sudur.fragment.FutureProjectsFragment;
import np.com.naxa.vso.sudur.fragment.OnGoingProjectsFragment;
import np.com.naxa.vso.sudur.model.UrlClass;

public class AboutFWDCActivity extends AppCompatActivity {
    private static final String TAG = "AboutFWDCActivity";
    private SwipeRefreshLayout swipeContainer;


    private View coordinatorLayoutView;

    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    ProgressDialog mProgressDlg;
    public static final String MyPREFERENCES = "dev_activities";
    public static final String MyPREFERENCES1 = "fwdc_json";

    SharedPreferences sharedpreferences;
    SharedPreferences sharedpreferences1;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor1;

    private boolean setData;
    String jsonToSend = null;


    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_fwdc_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("विकास आयोग को बारेमा");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


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


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutFWDCFragment(), "परिचय");
        adapter.addFragment(new CompletedProjectsFragment(), "सम्पन्न \n परियोजना ");
        adapter.addFragment(new OnGoingProjectsFragment(), "हुदैगरेका \n परियोजना ");
        adapter.addFragment(new FutureProjectsFragment(), "भावि \n परियोजना ");
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
                if (sharedpreferences.getString("dev_activities", "").trim().isEmpty()) {



                        text = POST(UrlClass.URL_DEV_ACTIVITIES);
                        Log.e("MAIN_JSON", "" + text.toString());
                        editor.putString("dev_activities", text);
                        editor.commit();



                } else {

                    text = sharedpreferences.getString("dev_activities", "");
                    Log.e("dev_activities", "" + text.toString());

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
//            mProgressDlg.dismiss();
            if (result != null) {
                //Success

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

            try {
                if (sharedpreferences1.getString("fwdc_json", "").trim().isEmpty()) {
                    if (networkInfo != null && networkInfo.isConnected()) {
                        text1 = POST(UrlClass.URL_ABOUT_FWDC);
                        Log.e("fwdc_json", "" + text1.toString());
                        editor1.putString("fwdc_json", text1);
                        editor1.commit();
                    } else {
                        coordinatorLayoutView = findViewById(R.id.main_content);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }
                } else {
                    text1 = sharedpreferences1.getString("fwdc_json", "");
                    Log.e("fwdc_json", "" + text1.toString());

                }
                JSONObject jsonObj = new JSONObject(text1);
                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);

                    fwdc_desc_en = c.getString("desc_en");
                    fwdc_title_en = c.getString("title_en");

                    fwdc_desc_np = c.getString("desc_np");
                    fwdc_title_np = c.getString("title_np");
                    office_img = c.getString("office_photo");
                }


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return data.toString();
        }


        @Override
        protected void onPostExecute(String result) {


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







