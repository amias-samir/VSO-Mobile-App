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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;


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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.UrlClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = "IntroductionActivity";

    private SwipeRefreshLayout swipeContainer;

    private View coordinatorLayoutView;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    private SliderLayout mDemoSlider;
    public static final String MyPREFERENCES = "intro_json";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private boolean setData;
    ProgressDialog mProgressDlg;

    String jsonToSend = null;

    //Here boundary is density form now
    TextView introTitle, introDes, introDesTitle, introPopulation, introArea, introDensity;

   public static String slider_1, slider_2, slider_3, slider_4, slider_5, slider_6;
   public static String slider_1_desc, slider_2_desc, slider_3_desc, slider_4_desc, slider_5_desc, slider_6_desc;

    public IntroductionActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("परिचय ");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (sharedpreferences.getString("intro_json", "").trim().isEmpty()) {
            if (networkInfo != null && networkInfo.isConnected()) {

                mProgressDlg = new ProgressDialog(this);
                mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
                mProgressDlg.setIndeterminate(false);
                //  mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                convertDataToJson();
                ApiCall apiCall = new ApiCall();
                apiCall.execute();
                slider_image();

            } else {
                coordinatorLayoutView = findViewById(R.id.main_content);
                Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();
            }
        }
        else {
            convertDataToJson();
            ApiCall apiCall = new ApiCall();
            apiCall.execute();
            slider_image();
        }



        introTitle = (TextView) findViewById(R.id.textView4);
        introDes = (TextView) findViewById(R.id.textViewintro);
        introPopulation = (TextView) findViewById(R.id.population_nametv);
        introArea = (TextView) findViewById(R.id.area_nametv);
        introDensity = (TextView) findViewById(R.id.density_nametv);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        //Susan
        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkInfo != null && networkInfo.isConnected()) {
                    editor.clear();
                    editor.commit();


                    finish();
                    startActivity(getIntent());
                    convertDataToJson();
                    ApiCall apiCall = new ApiCall();
                    apiCall.execute();
                    slider_image();

                    if (apiCall.equals(null)) {

                        // Your code to refresh the list here.
                        // Make sure you call swipeContainer.setRefreshing(false)
                        // once the network request has completed successfully.
                        swipeContainer.setRefreshing(false);
                        Log.d("DEBUG", "Fetch timeline error: ");
                    }
                } else {
                    coordinatorLayoutView = findViewById(R.id.main_content);
                    Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                            .setAction("Retry", null).show();
                }

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        slider_image();

    }


    public void slider_image (){

        try {
            Log.e(TAG, "slider_image: /n "+slider_1 +"/n"+slider_2 +" /n "+slider_3 +"/n"+slider_4 );
            Log.e(TAG, "slider_image_desc: /n "+slider_1_desc +"/n"+slider_2_desc +" /n "+slider_3_desc +"/n"+slider_4_desc );

            HashMap<String,String> url_maps = new HashMap<String, String>();
            url_maps.put(slider_1_desc, slider_1);
            url_maps.put(slider_2_desc, slider_2);
            url_maps.put(slider_3_desc, slider_3);
            url_maps.put(slider_4_desc, slider_4);
            url_maps.put(slider_5_desc, slider_5);
            url_maps.put(slider_6_desc, slider_6);

            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                mDemoSlider.addSlider(textSliderView);
            }

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.cancelLongPress();
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
            mDemoSlider.setPresetTransformer(0);


        } catch (RuntimeException e) {
            e.getStackTrace();
        }
    }


    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

//            ((SudurMainActivity) getActivity()).getSupportActionBar().setTitle("Introduction");
        //     ((SudurMainActivity) getActivity()).getToolbar().setBackgroundResource(R.color.colorPrimary);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((mProgressDlg != null) && mProgressDlg.isShowing())
            mProgressDlg.dismiss();
        mProgressDlg = null;
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

    public class ApiCall extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String intro_title_en, intro_title_np, intro_desc_en = null, intro_area_en = null, intro_boundary_en = null,
                intro_desc_np = null, intro_area_np = null, intro_boundary_np = null;

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
//            if (setData) {
//                mProgressDlg = new ProgressDialog(IntroductionActivity.this);
//                mProgressDlg.setMessage("कृपया पर्खनुहोस्...");
//                mProgressDlg.setIndeterminate(false);
//                mProgressDlg.show();
//
//            } else {
//
//                mProgressDlg = new ProgressDialog(IntroductionActivity.this);
//                mProgressDlg.setMessage("Loading please Wait...");
//                mProgressDlg.setIndeterminate(false);
//                mProgressDlg.show();
//
//            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String text = null;
            editor.clear();
            try {
                if (sharedpreferences.getString("intro_json", "").trim().isEmpty()) {

                    if (networkInfo != null && networkInfo.isConnected()) {
                        text = POST(UrlClass.URL_INTRODUCTION);
                        Log.e("MAIN_JSON", "" + text.toString());
                        editor.putString("intro_json", text);
                        editor.commit();
                    } else {
                        coordinatorLayoutView = findViewById(R.id.main_content);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }
                } else {
                    text = sharedpreferences.getString("intro_json", "");

                }
                JSONObject jsonObj = new JSONObject(text);
                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);

                    intro_title_en = c.getString("title_en");
                    intro_desc_en = c.getString("sudur_desc_en");
                    intro_area_en = c.getString("sudur_area_en");
                    intro_boundary_en = c.getString("sudur_boundary_en");

                    intro_title_np = c.getString("title_np");
                    intro_desc_np = c.getString("sudur_desc_np");
                    intro_area_np = c.getString("sudur_area_np");
                    intro_boundary_np = c.getString("sudur_boundary_np");

                    slider_1 = c.getString("slide_img_1");
                    slider_2 = c.getString("slide_img_2");
                    slider_3 = c.getString("slide_img_3");
                    slider_4 = c.getString("slide_img_4");
                    slider_5 = c.getString("slide_img_5");
                    slider_6 = c.getString("slide_img_6");

                    slider_1_desc = c.getString("img_1_desc_np");
                    slider_2_desc = c.getString("img_2_desc_np");
                    slider_3_desc = c.getString("img_3_desc_np");
                    slider_4_desc = c.getString("img_4_desc_np");
                    slider_5_desc = c.getString("img_5_desc_np");
                    slider_6_desc = c.getString("img_6_desc_np");

                    Log.e(TAG, "Slider Image Url"+ slider_1 );
                    Log.e(TAG, "Slider Image Url"+ slider_6 );
                    Log.e(TAG, "Slider Image Desc"+ slider_1_desc );
                    Log.e(TAG, "Slider Image Desc"+ slider_6_desc );



                }

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return data.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if ((mProgressDlg != null) && mProgressDlg.isShowing()) {
                mProgressDlg.dismiss();
            }

            if (result != null) {
                introTitle.setText(intro_title_np);
                introDes.setText(intro_desc_np);
                introPopulation.setText(intro_boundary_np);
                introArea.setText(intro_area_np);
                introDensity.setText(intro_boundary_np);
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

}
