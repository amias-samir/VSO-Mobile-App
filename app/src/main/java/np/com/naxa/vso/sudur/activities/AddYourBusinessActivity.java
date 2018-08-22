package np.com.naxa.vso.sudur.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.District;
import np.com.naxa.vso.sudur.model.PlaceTypes;
import np.com.naxa.vso.sudur.model.StaticListOfCoordinates;
import np.com.naxa.vso.sudur.model.UrlClass;

public class AddYourBusinessActivity extends AppCompatActivity {

    private static final String TAG = "AddYourBusinessActivity";
    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    public static final String MyPREFERENCES = "add_business";

    public static SharedPreferences sharedpreferences;
    ArrayList<LatLng> listCf = new ArrayList<LatLng>();
    GPS_TRACKER_FOR_POINT gps;

    private boolean setData;

    private ArrayList<District> districtDetailsList;
    private ArrayList<PlaceTypes> typesDetailsList;
    private ArrayList<String> bussinessCategoryList, districtList;
    Spinner districtSpinner, bussinesCategorySpinner;
    Toolbar toolbar;
    String jsonToSend = null;
    private ProgressDialog formDownloadProgress;
    int CAMERA_PIC_REQUEST = 2;
    String dataSentStatus = "", imagePath, encodedImage = null, imageName = "no_photo";
    ImageButton photo, mapButton;
    ImageView previewImageSite;

    SharedPreferences placesTypeSharedPref;
    SharedPreferences.Editor placesTypeEditor;
    public static final String PLACESTYPES = "places_types";

    SharedPreferences districtSharedPref;
    SharedPreferences.Editor districtEditor;
    public static final String DISTRICTDATA = "district_data_for_form";

    //Susan
    private int MULTIPLE_PERMISSION_CODE = 22;

    double final_latitude;
    double final_longitude;
    int spinnerDist_Id, spinnerBusiness_Id;

    String business_name, business_address, business_description;
    ProgressDialog mProgressDlg;

    EditText etBusinessName, etBusinessAddress, etBusinessDesc;

    Button sendBtn;
    private String addBussinessSendJSON;
    private String bussinessType,districtName;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_business);

        districtSharedPref = getSharedPreferences(DISTRICTDATA, Context.MODE_PRIVATE);
        districtEditor = districtSharedPref.edit();

        placesTypeSharedPref = getSharedPreferences(PLACESTYPES, Context.MODE_PRIVATE);
        placesTypeEditor = placesTypeSharedPref.edit();

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        //Nishon
        intiInstances();

        //get spinner data
//        SpinnerPopulatorTask spinnerPopulatorTask = new SpinnerPopulatorTask();
//        spinnerPopulatorTask.execute();

        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //Susan
        //Photo action
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        //Susan
        //custom get location dialog box
        mapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StaticListOfCoordinates.setList(listCf);
                startActivity(new Intent(AddYourBusinessActivity.this, MapPointActivity.class));
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    business_name = etBusinessName.getText().toString();
                    business_address = etBusinessAddress.getText().toString();
                    business_description = etBusinessDesc.getText().toString();

                    bussinessType = bussinesCategorySpinner.getSelectedItem().toString();
                    districtName = districtSpinner.getSelectedItem().toString();

                    if (!validate()) {
//                        onSignupFailed();
                        return;
                    }

                    if (networkInfo != null && networkInfo.isConnected()) {
                        mProgressDlg = new ProgressDialog(AddYourBusinessActivity.this);
                        mProgressDlg.setMessage("कृपया पर्खनुहोस...");
                        mProgressDlg.setIndeterminate(false);
                        mProgressDlg.setCancelable(true);
                        mProgressDlg.show();


                        convertDataToJsonFinal();
                        sendDatToserver();

                        Log.d("Ghost", addBussinessSendJSON);

                    } else {
                        final View coordinatorLayoutView = findViewById(R.id.activity_add_your_business);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन ।", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }

                } catch (Exception ex) {
                }
            }
        });

        //Nishon
        setToolbar();

        //Susan
        try {
            //First checking if the app is already having the permissHion
            if (isPermissionAllowed()) {
                //If permission is already having then showing the toast
//                Toast.makeText(AddYourBusinessActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
                //Existing the method with return
                return;
            } else {
                //If the app has not the permission then asking for the permission
                requestMultiplePermission();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.getMessage();
        }

        //Photo action
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
    }

    public boolean validate() {

        business_name = etBusinessName.getText().toString();
        business_address = etBusinessAddress.getText().toString();
        business_description = etBusinessDesc.getText().toString();

        boolean valid = true;
        if (business_name.isEmpty()) {
            etBusinessName.setError("कार्यलाय/व्यवसाय को नाम लेख्नुहोस्!");
            valid = false;
        } else {
            etBusinessName.setError(null);
        }

        if (business_address.isEmpty()) {
            etBusinessAddress.setError("र्कार्यलाय/व्यवसाय को ठेगाना लेख्नुहोस्।!");
            valid = false;
        } else {
            etBusinessAddress.setError(null);
        }

        if (business_description.isEmpty()) {
            etBusinessDesc.setError("कार्यालय/व्यवसाय को बारेमा लेख्नुहोस्!");
            valid = false;

        } else if (business_description.length() >= 200) {
            etBusinessDesc.setError("बढिमा ५० शब्द मात्र लेख्नुहोस्!");
            valid = false;

        } else {
            etBusinessDesc.setError(null);
        }
        return valid;
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Nishon
        if (MapPointActivity.lat == 0) {
            //lazy checking
            final_latitude = MapPointActivity.lat;
            final_longitude = MapPointActivity.lon;


        } else {
            final_latitude = MapPointActivity.lat;
            final_longitude = MapPointActivity.lon;
        }

    }

    private void intiInstances() {

        etBusinessName = (EditText) findViewById(R.id.textView_office_name);
        etBusinessAddress = (EditText) findViewById(R.id.textView_office_address);
        etBusinessDesc = (EditText) findViewById(R.id.textView_office_desc);
        districtSpinner = (Spinner) findViewById(R.id.district_list_spinner);
        bussinesCategorySpinner = (Spinner) findViewById(R.id.category_list_spinner);
        sendBtn = (Button) findViewById(R.id.btn_office_send);
        bussinessCategoryList = new ArrayList<>();
        districtDetailsList = new ArrayList<>();
        districtList = new ArrayList<>();
        typesDetailsList = new ArrayList<>();

        mapButton = (ImageButton) findViewById(R.id.map_button);
        photo = (ImageButton) findViewById(R.id.btn_take_office_photo);
        previewImageSite = (ImageView) findViewById(R.id.imgPreview);
        previewImageSite.setVisibility(View.GONE);
    }

    private void setToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("व्यवसाय सुचीकृत गर्नुहोस ");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    //intent request for camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);

                imagePath = filePath;
                addImage();
            }
        if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                previewImageSite.setVisibility(View.VISIBLE);
                previewImageSite.setImageBitmap(thumbnail);
                saveToExternalSorage(thumbnail);
                addImage();
            }
        }
    }

    private void saveToExternalSorage(Bitmap thumbnail) {
        // TODO Auto-generated method stub
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = calendar.getTimeInMillis();

        imageName = "business_" + timeInMillis;

        File file1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), imageName);

        if (file1.exists()) file1.delete();
        try {
            FileOutputStream out = new FileOutputStream(file1);
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public void addImage() {
        File file1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), imageName);
        String path = file1.toString();

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 1;
        options.inPurgeable = true;
        Bitmap bm = BitmapFactory.decodeFile(path, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // bitmap object
        byte[] byteImage_photo = baos.toByteArray();

        //generate base64 string of image
        encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
        Log.e("IMAGE STRING", "-" + encodedImage);

    }

//    private String[] cleanDistictData() {
//        for (int i = 0; i < districtDetailsList.size(); i++) {
//            districtList.add(districtDetailsList.get(i).getNpName());
//        }
//
//        String[] district = new String[districtList.size()];
//        districtList.toArray(district);
//
//        return district;
//    }
//
//    private String[] cleanPlacesTypesData() {
//        for (int i = 0; i < typesDetailsList.size(); i++) {
//            bussinessCategoryList.add(typesDetailsList.get(i).getNpName());
//        }
//
//        String[] types = new String[bussinessCategoryList.size()];
//        bussinessCategoryList.toArray(types);
//
//
//        String[] mockTypes = new String[]{"होटल व्यवसाय", "खुद्रा व्यापार", "होलसेल पसल", "औषधि व्यवसाय", " स्वास्थ्य केन्द्र", "शैक्षिक संस्था", "परामर्श सेवा केन्द्र", "निर्माण व्यवसाय कार्यालय"};
//
//        return mockTypes;
//    }
//
//    //called after the data is downloaded
//    private void setSpinnerData() {
//        Log.d("Nishon", districtList.size() + "");
//        if (districtList != null) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style, cleanDistictData());
//            districtSpinner.setAdapter(adapter);
//
//
//            // Spinner click listener
//            districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.jet));
//                    //
//                    spinnerDist_Id = position + 1;
//
//                    Log.e("SpiDistId: ", String.valueOf(position + 1));
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//        }
//
//        if (typesDetailsList != null) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style, cleanPlacesTypesData());
//            bussinesCategorySpinner.setAdapter(adapter);
//            // Spinner click listener
//            bussinesCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.jet));
//                    //
//                    // spinnerBusiness_Id = position + 1;
//                    //mocking
//                    spinnerBusiness_Id = 1;
//
//                    Log.e("SpinBisId: ", String.valueOf(position + 1));
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//        }
//    }
//
//
//    public void districtJsonParser(String rawJSON) {
//        Log.d(TAG, "Populating places  types spinner " + !districtSharedPref.getString(DISTRICTDATA, "").trim().isEmpty());
//        JSONArray dataArray = null;
//        try {
//            JSONObject jsonObj = new JSONObject(rawJSON);
//            dataArray = jsonObj.getJSONArray("data");
//
//            if (dataArray.getJSONObject(0).has("district_id")) {
//                //if data is valid put it
//                districtEditor.putString(DISTRICTDATA, rawJSON);
//                districtEditor.commit();
//            }
//        } catch (Exception e) {
//            Log.e(TAG, " passing district response" + e.toString());
//        }
//        try {
//            if (!districtSharedPref.getString(DISTRICTDATA, "").trim().isEmpty()) {
//                String districtCache = districtSharedPref.getString(DISTRICTDATA, "").trim();
//                Log.d(TAG, districtCache);
//                JSONObject districtObj = new JSONObject(districtCache);
//                dataArray = districtObj.getJSONArray("data");
//                for (int i = 0; i < dataArray.length(); i++) {
//                    JSONObject data = dataArray.getJSONObject(i);
//                    District district = new District(data.getString("district_id"), data.getString("district_name_en"), data.getString("district_name_np"));
//                    districtDetailsList.add(district);
//                }
//            }
//        } catch (Exception e) {
//            Log.e("Nishon", e.toString());
//        }
//    }
//
//    public void placesTypeJSONParser(String rawJSON) {
//
//        try {
//            JSONObject jsonObj = new JSONObject(rawJSON);
//            JSONArray dataArray = jsonObj.getJSONArray("data");
//
//            if (dataArray.getJSONObject(0).has("place_type_id")) {
//                //if data is valid put it
//
//
//                placesTypeEditor.putString(PLACESTYPES, rawJSON);
//                placesTypeEditor.commit();
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//
//        if (!placesTypeSharedPref.getString(PLACESTYPES, "").trim().isEmpty()) {
//
//            String districtCache = placesTypeSharedPref.getString(PLACESTYPES, "").trim();
//
//
//            try {
//
//                JSONObject jsonObj = new JSONObject(districtCache);
//                JSONArray dataArray = null;
//                dataArray = jsonObj.getJSONArray("data");
//
//
//                for (int i = 0; i < dataArray.length(); i++) {
//
//                    JSONObject data = dataArray.getJSONObject(i);
//
//                    PlaceTypes placeTypes = new PlaceTypes(data.getString("place_type_id"), data.getString("place_type_name_en"), data.getString("place_type_name_np"));
//                    typesDetailsList.add(placeTypes);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public void convertDataToJson() {
        try {

            JSONObject header = new JSONObject();

            header.put("token", "bf5d483811");
            jsonToSend = header.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public class SpinnerPopulatorTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            formDownloadProgress = new ProgressDialog(AddYourBusinessActivity.this);
//            formDownloadProgress.setMessage("कृपया पर्खनुहोस्...");
//            formDownloadProgress.setIndeterminate(false);
//            formDownloadProgress.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            String text = null;
//            text = POST(UrlClass.URL_DISTRICT_LIST);
//
//            Log.e("MAIN_JSON", "" + text.toString());
//
//            return text.toString();
//        }
//
//
//        private void showLog(String msg) {
//            Log.d(TAG, msg);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//
//            if (result.isEmpty()) {
//                districtJsonParser("");
//            } else {
//                districtJsonParser(result);
//            }
//
//            PlacesTypesSpinnerTask placesTypesSpinnerTask = new PlacesTypesSpinnerTask();
//            placesTypesSpinnerTask.execute();
//
//        }
//
//        public String POST(String myurl) {
//
//            URL url;
//            String response = "";
//            try {
//                url = new URL(myurl);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//
//                convertDataToJson();
//                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("data", jsonToSend);
//                String query = builder.build().getEncodedQuery();
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//                    String line;
//                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    while ((line = br.readLine()) != null) {
//                        response += line;
//                    }
//                } else {
//                    response = "";
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Log.e("Nishon", " resposnce returned " + response);
//            return response;
//        }
//    }
//
//    public class PlacesTypesSpinnerTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            String text = null;
//            text = POST(UrlClass.URL_PLACES_TYPE_LIST);
//            Log.e("MAIN_JSON", "" + text.toString());
//
//            return text.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            formDownloadProgress.dismiss();
//            if (result != null) {
//                placesTypeJSONParser(result);
//                setSpinnerData();
//            }
//        }
//
//        public String POST(String myurl) {
//
//            URL url;
//            String response = "";
//            try {
//                url = new URL(myurl);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(15000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//
//
//                OutputStream os = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//
//                convertDataToJson();
//                Uri.Builder builder = new Uri.Builder()
//                        .appendQueryParameter("data", jsonToSend);
//                String query = builder.build().getEncodedQuery();
//                writer.write(query);
//                writer.flush();
//                writer.close();
//                os.close();
//                int responseCode = conn.getResponseCode();
//
//                if (responseCode == HttpsURLConnection.HTTP_OK) {
//                    String line;
//                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                    while ((line = br.readLine()) != null) {
//                        response += line;
//                    }
//                } else {
//                    response = "";
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//    }

    /**
     * @Author Susan
     * Final json data send background process
     **/

    public void convertDataToJsonFinal() {
        try {

            JSONObject header = new JSONObject();
            header.put("token", "bf5d483811");
            header.put("place_name_en", business_name);
            header.put("place_type_id", bussinessType);
            header.put("place_district_id", districtName);
            header.put("place_address_en", business_address);
            header.put("place_lat", final_latitude);
            header.put("place_lon", final_longitude);
            header.put("place_desc_en", business_description);
            header.put("message_photo", encodedImage);
            addBussinessSendJSON = header.toString();


            Log.e("Ghost", encodedImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendDatToserver() {

        if (addBussinessSendJSON.length() > 0) {
            Log.e("Inside Send", "BeforeSending " + addBussinessSendJSON);

            RestApii restApii = new RestApii();
            restApii.execute();
        }
    }


    //same for all
    private class RestApii extends AsyncTask<String, Void, String> {
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
            String urll = UrlClass.URL_SEND_ADD_BUSINESS_DATA;

            String text = null;

            try {
                text = POST(urll);
                JSONObject jsonObject = new JSONObject(text);
                dataSentStatus = jsonObject.getString("status");
                Log.e("tagg", "Before parsing");
                Log.e("error", "" + text);


            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result != null) {

                //{"code":1,"status":200,"data":"ok"}
//                Toast.makeText(getApplicationContext(), dataSentStatus + "", Toast.LENGTH_SHORT).show();
                if (dataSentStatus.equals("200")) {
                    mProgressDlg.dismiss();

                    Toast.makeText(getApplicationContext(), getString(R.string.form_sent_np), Toast.LENGTH_SHORT).show();

                    previewImageSite.setVisibility(View.GONE);
                    etBusinessName.setText("");
                    etBusinessAddress.setText("");
                    etBusinessDesc.setText("");
                }
                if (!dataSentStatus.equals("200")) {
                    mProgressDlg.dismiss();

                    Toast.makeText(getApplicationContext(), getString(R.string.form_not_sent_np), Toast.LENGTH_SHORT).show();

                    previewImageSite.setVisibility(View.VISIBLE);
                    etBusinessName.setText(business_name);
                    etBusinessAddress.setText(business_address);
                    etBusinessDesc.setText(business_description);
                }

            }

        }

        //post url part code
        public String POST(String urll) {
            InputStream inputStream = null;
            String result = "";
            URL url;

            try {
                url = new URL(urll);

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
                        .appendQueryParameter("data", addBussinessSendJSON);

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
                        result += line;
                    }

                } else {
                    result = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

    }

    @Override
    public void onBackPressed() {

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(this);
        showDialog.setContentView(R.layout.close_dialog_english);
        final Button yes = (Button) showDialog.findViewById(R.id.buttonYes);
        final Button no = (Button) showDialog.findViewById(R.id.buttonNo);

        showDialog.setTitle("साबधान !!!");
        showDialog.setCancelable(false);
        showDialog.show();
        showDialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog.dismiss();
            }
        });
    }

    /**
     * @return Susan Permissions: Camera, Storage, Location, Internet, etc.
     */
    //We are calling this method to check the permission status
    private boolean isPermissionAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestMultiplePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, MULTIPLE_PERMISSION_CODE);

    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MULTIPLE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }
}
