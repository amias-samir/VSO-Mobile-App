package np.com.naxa.vso.sudur.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import javax.net.ssl.HttpsURLConnection;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.SudurMainActivity;
import np.com.naxa.vso.sudur.model.UrlClass;
import np.com.naxa.vso.sudur.model.local.DatabaseHelper;
import np.com.naxa.vso.sudur.model.rest.ApiClient;
import np.com.naxa.vso.sudur.model.rest.ApiInterface;
import np.com.naxa.vso.sudur.model.rest.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Susan on 5/31/2016.
 */
public class SplashScreenActivity extends Activity {

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
//    private ProgressBar downloadProgressBar;



    private ProgressBar firstBar = null;
    private ProgressBar secondBar = null;
    private int downloadCount = 0;

    Context context = this;

    //    ===============================================================================================================//
//  ============================  About FWDC =================================== //
    public static final String FWDCDevPREFERENCES = "dev_activities";
    public static final String AboutFWDCPREFERENCES = "fwdc_json";
    SharedPreferences fwdcdevsharedpreferences;
    SharedPreferences aboutfwdcsharedpreferences;
    SharedPreferences.Editor fwdcdeveditor;
    SharedPreferences.Editor aboutfwdceditor;
    private boolean setData;
    String jsonToSend = null;
//===========================================  end of fwdc=====================================//

    //    =================================== Introduction ============================================//
    public static final String IntroPREFERENCES = "intro_json";
    SharedPreferences introsharedpreferences;
    SharedPreferences.Editor introeditor;

//    ================================== end of introduction =======================================//

    //================================================Socio Economic ===================================//
    public static final String SocioPREFERENCES = "socio_economic";
    SharedPreferences sociosharedpreferences;
    SharedPreferences.Editor socioeditor;
//    =================================== end of socio economic=====================================//

//    ================================== hamro sudur paschhim =========================================//

    public static final String SudurDevPREFERENCES = "hamro_sudurpaschhim";
    public static final String SudurAttractPREFERENCES = "sudur_attract";
    public static final String NagarPalikaPREFERENCES1 = "nagar_budget";

    SharedPreferences sudurdevsharedpreferences, sudurattractsharedpreferences, nagarpalikasharedpreferences;
    SharedPreferences.Editor sudurdeveditor, sudurattracteditor, nagarbudgeteditor;
//    ==================================== end of hamro sudur paschhim =================================//

    //   ====================================== poltical parties and poltician =================================//
    public static final String PolticianPREFERENCES = "PolticianData";
    SharedPreferences polticiansharedpreferences;
    SharedPreferences.Editor polticianeditor;

    public static final String PartiesPREFERENCES = "poltical_parties_json";
    SharedPreferences partiessharedpreferences;
    SharedPreferences.Editor partieseditor;
//    ================================== end of poltical prties and poltician ================================//

    //=========================================NGO/INGO ==========================================================//
    public static final String NgoIngoPREFERENCES = "development_ingo_ngo";
    SharedPreferences ngosharedpreferences;
    SharedPreferences.Editor ngoeditor;
//=========================================end of NGO/INGO ==========================================================//


    //========================================= Gaupalika Representative ==============================================//
    public static final String gaunPREFERENCES = "gaunpalika_representative";
    SharedPreferences gaunsharedpreferences;
    SharedPreferences.Editor gauneditor;
    //========================================= end of gaupalika representative ========================================//

    //========================================= Nagarpalika Representative ==============================================//
    public static final String nagarPREFERENCES = "nagarpalika_representative";
    SharedPreferences magarsharedpreferences;
    SharedPreferences.Editor nagareditor;
    //========================================= end of gaupalika representative ========================================//

//========================================================================================================================//

    TextView textView1, textView2, textView3, textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_sudur);

        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        textView3 = (TextView) findViewById(R.id.textView3);
        Typeface face3 = Typeface.createFromAsset(getAssets(), "font/roboto_thin.ttf");
        textView3.setTypeface(face3);

        textView4 = (TextView) findViewById(R.id.textView4);
        Typeface face4 = Typeface.createFromAsset(getAssets(), "font/roboto_thin.ttf");
        textView4.setTypeface(face4);

        firstBar = (ProgressBar) findViewById(R.id.firstBar);
        secondBar = (ProgressBar) findViewById(R.id.secondBar);



//================================== shared preferences initialization ==============================================//

        //SharedPreferences-DEV_ACTIVITIES
        fwdcdevsharedpreferences = this.getSharedPreferences(FWDCDevPREFERENCES, Context.MODE_PRIVATE);
        fwdcdeveditor = fwdcdevsharedpreferences.edit();

        //SharedPreferences-About_FWDC
        aboutfwdcsharedpreferences = this.getSharedPreferences(AboutFWDCPREFERENCES, Context.MODE_PRIVATE);
        aboutfwdceditor = aboutfwdcsharedpreferences.edit();

        //introduction
        introsharedpreferences = getSharedPreferences(IntroPREFERENCES, Context.MODE_PRIVATE);
        introeditor = introsharedpreferences.edit();

        // socio economic status
        sociosharedpreferences = this.getSharedPreferences(SocioPREFERENCES, Context.MODE_PRIVATE);
        socioeditor = sociosharedpreferences.edit();

        //SharedPreferences-Sudur paschhim DEV_ACTIVITIES
        sudurdevsharedpreferences = this.getSharedPreferences(SudurDevPREFERENCES, Context.MODE_PRIVATE);
        sudurdeveditor = sudurdevsharedpreferences.edit();

        //SharedPreferences-sudur paschhim local attraction
        sudurattractsharedpreferences = this.getSharedPreferences(SudurAttractPREFERENCES, Context.MODE_PRIVATE);
        sudurattracteditor = sudurattractsharedpreferences.edit();

        //SharedPreferences-sudur paschhim Nagarpalika Budget
        nagarpalikasharedpreferences = this.getSharedPreferences(NagarPalikaPREFERENCES1, Context.MODE_PRIVATE);
        nagarbudgeteditor = nagarpalikasharedpreferences.edit();

        // poltician list
        polticiansharedpreferences = this.getSharedPreferences(PolticianPREFERENCES, Context.MODE_PRIVATE);
        polticianeditor = polticiansharedpreferences.edit();

        // parties list
        partiessharedpreferences = this.getSharedPreferences(PartiesPREFERENCES, Context.MODE_PRIVATE);
        partieseditor = partiessharedpreferences.edit();

        // NGO/INGO
        ngosharedpreferences = this.getSharedPreferences(NgoIngoPREFERENCES, Context.MODE_PRIVATE);
        ngoeditor = ngosharedpreferences.edit();

        // gaunpalika representative
        gaunsharedpreferences = this.getSharedPreferences(gaunPREFERENCES, Context.MODE_PRIVATE);
        gauneditor = gaunsharedpreferences.edit();

        // Nagarpalika Representative
        nagarpalikasharedpreferences = this.getSharedPreferences(nagarPREFERENCES, Context.MODE_PRIVATE);
        nagareditor = nagarpalikasharedpreferences.edit();

//        logActivityVisible();





//======================================================== end of shared preferences initialization ========================//
        try {

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splash_background);
            Animation relativeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            relativeLayout.startAnimation(relativeAnim);

            TextView textView3 = (TextView) findViewById(R.id.textView3);
            Animation textViewAnimate3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slow);
            textView3.startAnimation(textViewAnimate3);


            if (networkInfo != null && networkInfo.isConnected()) {
                firstBar.setVisibility(View.VISIBLE);
                firstBar.setMax(12);
                secondBar.setVisibility(View.GONE);
                firstBar.setSecondaryProgress(downloadCount + 1);

//                            if (downloadCount <= 9) {


                //==============about fwdc dev call====================//
                convertDataToJson();
                FWDCDEVApiCall fwdcdevApiCall = new FWDCDEVApiCall();
                fwdcdevApiCall.execute();


                //==============about fwdc call====================//
                convertDataToJson();
                AboutFWDCApiCall aboutFWDCApiCall = new AboutFWDCApiCall();
                aboutFWDCApiCall.execute();


                //=========== introduction Api call ==============//
                convertDataToJson();
                IntroApiCall introapiCall = new IntroApiCall();
                introapiCall.execute();

                //socio economc status
                convertDataToJson();
                SocioEconomicService restApi = new SocioEconomicService();
                restApi.execute();

                //============= Sudur DEVELOPEMENT ACTIVITIES APICALL==================//
                convertDataToJson();
                SudurDevApiCall sudurDevApiCall = new SudurDevApiCall();
                sudurDevApiCall.execute();

                //=============local attraction PLACES APICALL==================//
                convertDataToJson();
                AttrractionApiCall attractionapiCall = new AttrractionApiCall();
                attractionapiCall.execute();

                //   Nagarpalika Budget Api call
                convertDataToJson();
                NagarpalikaBudgetApiCall nagarpalikaBudgetApiCall = new NagarpalikaBudgetApiCall();
                nagarpalikaBudgetApiCall.execute();


                //==================Poltical parties ===================//
                convertDataToJson();
                PoticalPartiesListService poticalPartiesListService = new PoticalPartiesListService();
                poticalPartiesListService.execute();

                //================= polticial list ========================//
                convertDataToJson();
                PoticianListService poticianListService = new PoticianListService();
                poticianListService.execute();

                //================= polticial list ========================//
                convertDataToJson();
                GaunRepresentativeListService gaunRepresentativeListService = new GaunRepresentativeListService();
                gaunRepresentativeListService.execute();

                //================= polticial list ========================//
                convertDataToJson();
                NagarRepresentativeListService nagarRepresentativeListService = new NagarRepresentativeListService();
                nagarRepresentativeListService.execute();

                //====================NGO/INGO=================================//
                convertDataToJson();
                DevelopmentINGOAPI ngoIngoApi = new DevelopmentINGOAPI();
                ngoIngoApi.execute();



                fetchBussinesFromServer(
                        DatabaseHelper.getInstance(
                                getApplicationContext()).getLastSyncDate(DatabaseHelper.TABLE_BUSINESS_PLACES));



//                        }

            } else {

                Thread pause = new Thread() {
                    public void run() {
                        try {

                            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splash_background);
                            Animation relativeAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                            relativeLayout.startAnimation(relativeAnim);

                            TextView textView3 = (TextView) findViewById(R.id.textView3);
                            Animation textViewAnimate3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slow);
                            textView3.startAnimation(textViewAnimate3);

                            sleep(3000);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            Intent stuff = new Intent(SplashScreenActivity.this, SudurMainActivity.class);
                            startActivity(stuff);
                        }
                        finish();
                    }
                };
                pause.start();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
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


    //About fwdc dev
    public class FWDCDEVApiCall extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";
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
//            String text = "";

            try {
                text = POST(UrlClass.URL_DEV_ACTIVITIES);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
//            mProgressDlg.dismiss();
            if (result != null && !result.equals("")) {
                //Success
                Log.e("FWDCDEV ACTIVITIES", "Splash" + text.toString());
                fwdcdeveditor.putString("dev_activities", text);
                fwdcdeveditor.commit();

                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);

            }
//            else {
//                restartActivity();
//            }

        }

//        private void counter(){
//            cur++
//                    if (cur == total){
//                        //itent
//                    }
//        }

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

    //About fwdc
    public class AboutFWDCApiCall extends AsyncTask<String, Void, String> {
        String text1 = "";
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

//            String text1 = "";

            try {
                text1 = POST(UrlClass.URL_ABOUT_FWDC);



            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text1;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");

            if (result != null && !result.equals("")) {
//                //Success
                Log.e("ABOUT FWDC ", "Splash" + text1.toString());
                aboutfwdceditor.putString("fwdc_json", text1);
                aboutfwdceditor.commit();

                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);


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

    //Intro Apicall
    public class IntroApiCall extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = null;

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

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

//            String text = null;
//            introeditor.clear();
            try {

                text = POST(UrlClass.URL_INTRODUCTION);



            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (result != null && !result.equals("")) {
//                success
                Log.e("INTRODUCTION FWDC :", "Splash" + text.toString());
                introeditor.putString("intro_json", text);
                introeditor.commit();

                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);

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

    //socio economic status
    private class SocioEconomicService extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";


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
//            String text = "";

            try {
                text = POST(UrlClass.URL_SOCIO_ECONOMIC);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");

            if (result != null && !result.equals("")) {
//                success
                Log.e("FWDC SOCIO ECONOMIC :  ", "Splash" + text.toString());
                socioeditor.putString("socio_economic", text);
                socioeditor.commit();


                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);

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

    // sudur dev
    public class SudurDevApiCall extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";

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
//            String text = "";

            try {
                text = POST(UrlClass.URL_SUDURPASCHHIM_DEV_ACT);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");

            if (result != null && !result.equals("")) {
                //Success
                Log.e("SUDURPASCHHIM DEV : ", "Splash" + text.toString());

                sudurdeveditor.putString("hamro_sudurpaschhim", text);
                sudurdeveditor.commit();


                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);
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

    //nagarpalika budget
    public class NagarpalikaBudgetApiCall extends AsyncTask<String, Void, String> {
        JSONArray data1 = null;
        String text1 = "";


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
//            String text1 = "";

            try {
                text1 = POST(UrlClass.URL_NAGARPALIKA_BUDGET);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text1;
        }

        @Override
        protected void onPostExecute(String result) {

            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (result != null && !result.equals("")) {

                //Success
                Log.e("nagar_budget", "Splash" + text1.toString());
                nagarbudgeteditor.putString("nagar_budget", text1);
                nagarbudgeteditor.commit();


                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);


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

    // sudur attract
    public class AttrractionApiCall extends AsyncTask<String, Void, String> {
        JSONArray data1 = null;
        String text1 = "";

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
//            String text1 = "";

            try {
                text1 = POST(UrlClass.URL_SUDURPASCHHIM_LOCAL_ATTR);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text1;
        }

        @Override
        protected void onPostExecute(String result) {

            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (result != null && !result.equals("")) {

                //Success
                Log.e("SUDUR LOCAL ATTRACTION", "Splash" + text1.toString());
                sudurattracteditor.putString("sudur_attract", text1);
                sudurattracteditor.commit();


                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);


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

    // poltical parties
    private class PoticalPartiesListService extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";


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
//            String text = "";

            try {

                text = POST(UrlClass.URL_PARTY_LIST);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result != null && !result.equals("")) {
//                success
                Log.e("POLTICAL PARTIES ", "Splash" + text.toString());
                partieseditor.putString("poltical_parties_json", text);
                partieseditor.commit();


                downloadCount++;
                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);

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

    // poltician list
    private class PoticianListService extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";


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
//            String text = "";

            try {
                text = POST(UrlClass.URL_POLTICIAN_LIST);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (result != null && !result.equals("")) {
//                success
                Log.e("POLTICIAN LIST :  ", "Splash" + text.toString());
                polticianeditor.putString("PolticianData", text);
                polticianeditor.commit();


                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);
//                startMainActivity(downloadCount);

//                Log.e("ProgressBar", "end " + downloadCount);

            }
//            else {
//                restartActivity();
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


    // NagarPalika representative list
    private class NagarRepresentativeListService extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";


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
//            String text = "";

            try {
                text = POST(UrlClass.URL_NAGARPALIKA_REPRESENTATIVE);



            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (result != null && !result.equals("")) {
//                success
                Log.e("Nagar Represent LIST: ", "Splash" + text.toString());
                nagareditor.putString("nagarpalika_representative", text);
                nagareditor.commit();
                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);
//                startMainActivity(downloadCount);

//                Log.e("ProgressBar", "end " + downloadCount);

            }
//            else {
//                restartActivity();
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

    // Gaunpalika representative list
    private class GaunRepresentativeListService extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";


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
//            String text = "";

            try {
                text = POST(UrlClass.URL_GAUNPALIKA_REPRESENTATIVE);

            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            //Log.e("ONPOSTEXECUTE", "ONPOST");
            if (result != null && !result.equals("")) {
//                success
                Log.e("Gaun Represent LIST: ", "Splash" + text.toString());
                gauneditor.putString("gaunpalika_representative", text);
                gauneditor.commit();


                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
                firstBar.setSecondaryProgress(downloadCount + 1);
//                startMainActivity(downloadCount);

//                Log.e("ProgressBar", "end " + downloadCount);

            }
//            else {
//                restartActivity();
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


    //  NGO/INGO
    private class DevelopmentINGOAPI extends AsyncTask<String, Void, String> {
        JSONArray data = null;
        String text = "";


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
//            String text = "";

            try {
                text = POST(UrlClass.URL_INGO_NGO_DEVELOPMENT);

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
            if (result != null && !result.equals("")) {
//                success
                ngoeditor = ngosharedpreferences.edit();
                ngoeditor.putString("development_ingo_ngo", text);
                Log.e("NGO/INGO LIST :  ", "Splash" + text.toString());
                ngoeditor.commit();

                downloadCount++;
                firstBar.setProgress(downloadCount);
                //Set the second progress bar value
//                firstBar.setSecondaryProgress(downloadCount + 1);
                startMainActivity(downloadCount);

                Log.e("ProgressBar", "end " + downloadCount);

            } else {
                restartActivity();
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


    public void startMainActivity(int count) {
        if (count == 13) {
            Intent stuff = new Intent(SplashScreenActivity.this, SudurMainActivity.class);
            startActivity(stuff);
        } else {
            restartActivity();
        }
    }


    private void fetchBussinesFromServer(String lastSyncDateTime) {


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Data> call = apiService.getMenu(lastSyncDateTime);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                handleResponse(response);
            }

            private void handleResponse(Response<Data> response) {
                if (response.code() != 200 || response.body().getData() == null) {
                    return;
                }


                Data data = response.body();
                DatabaseHelper.getInstance(getApplicationContext()).saveSyncedBussinesses(data.getData());

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {


            }
        });

    }

    public void restartActivity() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
        showDialog.setContentView(R.layout.restart_download_popup);
        final Button yes = (Button) showDialog.findViewById(R.id.buttonYes);
        final Button no = (Button) showDialog.findViewById(R.id.buttonNo);

        showDialog.setTitle("Connection Error");
        showDialog.setCancelable(false);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
                Intent intent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
                startActivity(intent);
//                                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
                Intent intent = new Intent(SplashScreenActivity.this, SudurMainActivity.class);
                startActivity(intent);
            }
        });
    }


}
