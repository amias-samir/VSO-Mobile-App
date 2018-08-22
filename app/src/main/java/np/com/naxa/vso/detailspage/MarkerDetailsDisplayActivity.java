package np.com.naxa.vso.detailspage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;
import np.com.naxa.vso.utils.QueryBuildWithSplitter;

public class MarkerDetailsDisplayActivity extends AppCompatActivity {
    private static final String TAG = "MarkerDetailsDisplay";

    String jsonString;
    List<MarkerDetailsKeyValue> markerDetailsKeyValueListCommn = new ArrayList<MarkerDetailsKeyValue>();
    List<MarkerDetailsKeyValue> markerDetailsKeyValueListSpecf = new ArrayList<MarkerDetailsKeyValue>();

    List<MarkerDetailsKeyValue> markerDetailsKeyValueWholeList = new ArrayList<MarkerDetailsKeyValue>();
    @BindView(R.id.recylcer_view_item_detail)
    RecyclerView recylcerViewItemDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_details_display);
        ButterKnife.bind(this);

        setupToolBar();
        setupListRecycler();

        getIntentData();
        parseReceivedJson(jsonString);

        getFullListToDisplay();

    }

    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_general);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            if (intent.hasExtra("data")) {
                Bundle extras = intent.getExtras();
                jsonString = extras.getString("data");
                Log.d(TAG, "onCreate: after Clicked data received" + jsonString);
                parseCommonJson(new JSONObject(jsonString));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListRecycler() {
        MarkerDetailedDisplayAdapter markerDetailedDisplayAdapter = new MarkerDetailedDisplayAdapter(R.layout.marker_detailed_info_display_layout, null);
        recylcerViewItemDetail.setLayoutManager(new LinearLayoutManager(this));
        recylcerViewItemDetail.setAdapter(markerDetailedDisplayAdapter);

    }


    private void parseReceivedJson(String JSON) {
        try {
            JSONObject jsonObject = new JSONObject(JSON);
            if (jsonObject.has("commonPlacesAttrb")) {
                JSONObject jsonCommonObject = new JSONObject(jsonObject.getString("commonPlacesAttrb"));
                parseCommonJson(jsonCommonObject);
            }

            if (jsonObject.has("hospitalFacilities")) {
                JSONObject jsonHospitalObject = new JSONObject(jsonObject.getString("hospitalFacilities"));
                parseHospitalJson(jsonHospitalObject);
            }

            if (jsonObject.has("openSpace")) {
                JSONObject jsonOpenObject = new JSONObject(jsonObject.getString("openSpace"));
                parseOpenJson(jsonOpenObject);
            }

            if (jsonObject.has("educationalInstitutes")) {
                JSONObject jsonEducationObject = new JSONObject(jsonObject.getString("educationalInstitutes"));
                parseEducationJson(jsonEducationObject);
            }

            if (jsonObject.has("wardDetailsModel")) {
                JSONObject jsonEducationObject = new JSONObject(jsonObject.getString("wardDetailsModel"));
                parseWardDetailsJson(jsonEducationObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseCommonJson(JSONObject jsonObject) {
        Log.d(TAG, "parseCommonJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListCommn = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void parseHospitalJson(JSONObject jsonObject) {
        Log.d(TAG, "parseHospitalJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));

    }

    private void parseOpenJson(JSONObject jsonObject) {
        Log.d(TAG, "parseOpenJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void parseEducationJson(JSONObject jsonObject) {
        Log.d(TAG, "parseEducationJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void parseWardDetailsJson(JSONObject jsonObject) {
        Log.d(TAG, "parseEducationJson: " + jsonObject.toString());
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListSpecf = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(jsonObject.toString()));
    }

    private void getFullListToDisplay() {
        markerDetailsKeyValueWholeList.clear();
        markerDetailsKeyValueWholeList.addAll(markerDetailsKeyValueListCommn);
        markerDetailsKeyValueWholeList.addAll(markerDetailsKeyValueListSpecf);

        ((MarkerDetailedDisplayAdapter) recylcerViewItemDetail.getAdapter()).replaceData(markerDetailsKeyValueWholeList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch_to_english:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    LocaleChanger.setLocale(new Locale("en", "US"));
                    ActivityRecreationHelper.recreate(MarkerDetailsDisplayActivity.this, true);
                }
                return true;
            case R.id.action_switch_to_nepali:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    LocaleChanger.setLocale(new Locale("ne", "NP"));
                    ActivityRecreationHelper.recreate(MarkerDetailsDisplayActivity.this, true);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}
