package np.com.naxa.vso.detailspage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import np.com.naxa.vso.R;

public class MarkerDetailsDisplayActivity extends AppCompatActivity {
    private static final String TAG = "MarkerDetailsDisplay";

    String jsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_details_display);

        setupToolBar();
        getIntentData();

        parseReceivedJson(jsonString);
    }

    private void setupToolBar() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_header);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }

    private void getIntentData (){
        try {
            Intent intent = getIntent();
            if(intent.hasExtra("data")) {
                Bundle extras = intent.getExtras();
                jsonString = extras.getString("data");
                Log.d(TAG, "onCreate: after Clicked data received"+jsonString);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parseReceivedJson(String JSON){

    }
}
