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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.UrlClass;

public class AskForHelpActivity extends AppCompatActivity {

    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    private static final String TAG = "AskForHelpActivity";

    Context context = this;

    private Button sendButton;
    private EditText etUserName, etAddress, etContact, etMessage;

    String message_sender_name;
    String message_sender_address;
    String message_sender_cnt_no;
    String message;

    int CAMERA_PIC_REQUEST = 2;
    String dataSentStatus = "", imagePath, encodedImage = null, imageName = "no_photo";
    FloatingActionButton photo;
    ImageView previewImageSite;


    ProgressDialog mProgressDlg;

    String jsonToSend = null;

    private SharedPreferences wmbPreference;
    private boolean setData;

    //Susan
    private int MULTIPLE_PERMISSION_CODE = 22;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion_help_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_sudur.setBackgroundColor(getResources().getColor(R.color.nliveo_white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("सुझाब/सहयोग");
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.accent));

        etUserName = (EditText) findViewById(R.id.reporter_name);
        etAddress = (EditText) findViewById(R.id.tv_get_locatoin);
        etContact = (EditText) findViewById(R.id.reporter_contact_no);
        etMessage = (EditText) findViewById(R.id.create_description);
        sendButton = (Button) findViewById(R.id.send_btn);

        photo = (FloatingActionButton) findViewById(R.id.fab);
        previewImageSite = (ImageView) findViewById(R.id.backdrop);
//        previewImageSite.setVisibility(View.GONE);

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        //Photo action
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        // add click listener to Button "POST"
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    message_sender_name = etUserName.getText().toString();
                    message_sender_address = etAddress.getText().toString();
                    message_sender_cnt_no = etContact.getText().toString();
                    message = etMessage.getText().toString();

                    if (!validate()) {
//                        onSignupFailed();
                        return;
                    }

                    if (networkInfo != null && networkInfo.isConnected()) {
                        mProgressDlg = new ProgressDialog(context);
                        mProgressDlg.setMessage("Please wait...");
                        mProgressDlg.setIndeterminate(false);
                        mProgressDlg.setCancelable(true);
                        mProgressDlg.show();

                        convertDataToJson();

                    } else {
                        final View coordinatorLayoutView = findViewById(R.id.main_content);
                        Snackbar.make(coordinatorLayoutView, "ईन्टरनेट कनेक्सन छैन ।", Snackbar.LENGTH_LONG)
                                .setAction("Retry", null).show();
                    }

                } catch (Exception ex) {
                }

            }
        });

        try {
            //First checking if the app is already having the permission
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

        message_sender_name = etUserName.getText().toString();
        message_sender_address = etAddress.getText().toString();
        message_sender_cnt_no = etContact.getText().toString();
        message = etMessage.getText().toString();

        boolean valid = true;


        if (message_sender_name.isEmpty()) {
//            if (setData) {
            etUserName.setError("तपाईंको नाम लेख्नुहोस्!");
            valid = false;
//            } else {
//                etUserName.setError("Username field is empty !");
//                valid = false;
//            }

        } else {
            etUserName.setError(null);
        }

        if (message_sender_address.isEmpty()) {

//            if (setData) {
            etAddress.setError("तपाईंको ठेगाना लेख्नुहोस्।!");
            valid = false;
//            } else {
//                etAddress.setError("Address field is empty !");
//                valid = false;
//            }

        } else {
            etAddress.setError(null);
        }

        if (message_sender_cnt_no.isEmpty()) {
//            if (setData) {
            etContact.setError("तपाईंको मोबाईल नम्बर लेख्नुहोस्!");
            valid = false;
//            } else {
//                etContact.setError("Contact field id empty or number not complete!");
//                valid = false;
//            }

        } else if (message_sender_cnt_no.length() < 9 || message_sender_cnt_no.length() > 15) {
//            if (setData) {
            etContact.setError("१० अङ्क को मोबाईल नम्बर लेख्नुहोस्!");
            valid = false;
//            } else {
//                etContact.setError("Inter 10 digit number!");
//                valid = false;
//            }

        } else {
            etContact.setError(null);
        }

        if (message.isEmpty()) {
//            if (setData) {
            etMessage.setError("तपाईंको सन्देश लेख्नुहोस् !");
            valid = false;
//            } else {
//                etMessage.setError("Message field is empty !");
//                valid = false;
//            }

        } else {
            etMessage.setError(null);
        }
        return valid;
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

        imageName = "ask_" + timeInMillis;

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

    // data convert
    public void convertDataToJson() {
        //function in the activity that corresponds to the layout button

        try {

            JSONObject header = new JSONObject();

            header.put("token", "bf5d483811");
            header.put("message_sender_name", message_sender_name);
            header.put("message_sender_address", message_sender_address);
            header.put("message_sender_cnt_no", message_sender_cnt_no);
            header.put("message", message);
            header.put("message_photo", encodedImage);


            jsonToSend = header.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendDatToserver();
    }

    public void sendDatToserver() {

        if (jsonToSend.length() > 0) {

            RestApii restApii = new RestApii();
            restApii.execute();
        }
    }


    private class RestApii extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String text = null;
            text = POST(UrlClass.URL_PUBLIC_MESSAGE);
            Log.d(TAG, "RAW resposne"+text);

            return text.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            mProgressDlg.dismiss();

            Log.d(TAG, "on post resposne"+result);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                dataSentStatus = jsonObject.getString("code");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (dataSentStatus.equals("1")){
                Toast.makeText(context, "तपाईंको सुझाव रेकर्ड गरिएको छ ।", Toast.LENGTH_SHORT).show();
                previewImageSite.setVisibility(View.GONE);

                etUserName.setText("");
                etAddress.setText("");
                etContact.setText("");
                etMessage.setText("");

            }
        }


        public String POST(String urll) {
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

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog showDialog = new Dialog(context);
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
     *
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
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
