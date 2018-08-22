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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

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
import np.com.naxa.vso.sudur.Utils.ChartColor;
import np.com.naxa.vso.sudur.connection.ConnectonDetector;
import np.com.naxa.vso.sudur.fragment.Local_LevelRepresentativeFragment;
import np.com.naxa.vso.sudur.fragment.PoliticalPartiesFragment;
import np.com.naxa.vso.sudur.fragment.PolticianListFragment;
import np.com.naxa.vso.sudur.model.UrlClass;

/**
 * Created by Samir on 10/27/2016.
 */
public class RajnitikAwasthaActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View coordinatorLayoutView;

    private PieChart mChart;

    PieChart pieChart;

    ProgressDialog mProgressDlg;

    private static String stat_name = "राजनीतिक अवस्था";

    public static final String EXTRA_NAME = "cheese_name";

    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();

    public static final String MyPREFERENCES = "parties_chart_json";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private boolean setData;
    String jsonToSend = null;
    String party_name_np, rawseats;
    Float seat;

    //Samir
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    ConnectonDetector conectionDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rajnitik_awastha_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("राजनीतिक अवस्था");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

//connection detector
        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        //Samir
        //==============parties seat api call====================//
//        convertDataToJson();
//        PolticalPartiesSeat apiCall1 = new PolticalPartiesSeat();
//        apiCall1.execute();

        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setFocusableInTouchMode(true);
        scroll.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);


//=================================Pie Chart Draw==============================//

        pieChart = (PieChart) findViewById(R.id.chart1);

        pieChart.setCenterText( "निर्वाचित\n सिट संख्या");
        entries.add(new Entry(1, 0));
        entries.add(new Entry(7, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(9, 3));







        PieDataSet dataset = new PieDataSet(entries,"");
        labels.add("माओवादी");
        labels.add("एमाले");
        labels.add("फोरम लोकतान्त्रिक");
        labels.add("काँग्रेस");



        PieData data = new PieData(labels, dataset);
        dataset.setColors(ChartColor.COLORFUL_COLORS); //
        pieChart.setDescription("निर्वाचित राजनीतिक पार्टीहरु");
        pieChart.setData(data);
        pieChart.isDrawRoundedSlicesEnabled();
        pieChart.animateY(2000);

        pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image

        //===================================End Of PieChart=========================================//


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

        TextView rajnitik_bibaran = (TextView) findViewById(R.id.textView_rajnitik_bibaran);
        rajnitik_bibaran.setText("सुदूरपश्चिम बिकाश क्षेत्रमा क्रियाशिल राजनीतिक पार्टीहरू र राजनीतिक व्यक्तिहरुको विवरण निम्न अनुसार छन् ।");


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
//        final SharedPreferences wmbPreference = PreferenceManager
//                .getDefaultSharedPreferences(getActivity());

        adapter.addFragment(new PolticianListFragment(), "राजनीतिक \n व्यक्तिहरू");
        adapter.addFragment(new PoliticalPartiesFragment(), "राजनीतिक \n पार्टीहरु");
        adapter.addFragment(new Local_LevelRepresentativeFragment(), "स्थानिय तहका \n प्रतिनिधिहरु");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
//
//        }


    }


    //Poltical Parties Seat parsing
    public class PolticalPartiesSeat extends AsyncTask<String, Void, String> {
        JSONArray data = null;




        String fwdc_desc_en = null, fwdc_title_en = null, fwdc_desc_np = null, fwdc_title_np = null;
        String RawSeats, party_name_np;


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
            Log.e("SAMIR: ","inside on preexecute" );
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            Log.e("SAMIR: ","inside do in background" );

            String text1 = "";

            try {
                if (sharedpreferences.getString("parties_chart_json", "").trim().isEmpty()) {
                    Log.e("SAMIR: ","inside do in background try" );
                  //  if (networkInfo==null && networkInfo.isConnected()) {
                        text1 = POST(UrlClass.URL_PARTY_LIST);
                        Log.e("parties_chart_json", "" + text1.toString());
                        editor.putString("parties_chart_json", text1);
                        editor.commit();
                        Log.e("SAMIR: ","inside do in background try isconected" );
//                    } else {
//                        coordinatorLayoutView = findViewById(R.id.main_content);
//                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
//                                .setAction("Retry", null).show();
//                    }
                } else {
                    text1 = sharedpreferences.getString("parties_chart_json", "");
                    Log.e("parties_chart_json", "" + text1.toString());


                }
                JSONObject jsonObj = new JSONObject(text1);
                data = jsonObj.getJSONArray("data");

                Log.e("DATA", "" + data.toString());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);

                    RawSeats = c.getString("seats");
                    party_name_np = c.getString("sudur_political_party_name_np");

                    Log.e( "party_Name:", party_name_np );
                    Log.e( "party_seat:", RawSeats );


                    Integer seats = Integer.parseInt(RawSeats);

                    entries.add(new Entry(seats, i));
                    labels.add(party_name_np);

//                    fwdc_desc_np = c.getString("desc_np");
//                    fwdc_title_np = c.getString("title_np");
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
//                //Success
//                Log.e( "party_Name_onpost:", party_name_np );
//                Log.e( "party_seat_onpost:", RawSeats );
//                tvTitle.setText(fwdc_title_np);
//                tvDesc.setText(fwdc_desc_np);
//                swipeContainer.setRefreshing(false);

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


}
