package np.com.naxa.vso.sudur.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.adapter.DistrictAdapter;
import np.com.naxa.vso.sudur.model.District;

/**
 * Created by Nishon Tandukar on 27 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class NagarBudgetDistrict extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_district_budget_nagar_activity)
    RecyclerView rvDistrictBudgetNagarActivity;


    ArrayList<String> ListWithUniqueDistrictString;
    ArrayList<District> ListWithUniqueDistrict;


    ArrayList<District> districts;
    private DistrictAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.district_budget_nagar_activity);
        ButterKnife.bind(this);
        setToolbar();


        ListWithUniqueDistrictString = new ArrayList<>();
        ListWithUniqueDistrict = new ArrayList<>();
        districts = new ArrayList<>();

        try {
            populateDistrictListAsync();
        } catch (JSONException e) {
            Log.d("Nishon", e.toString());
            e.printStackTrace();
        }
    }


    private void populateDistrictListAsync() throws JSONException {
        String MyPREFERENCES = "nagar_budget";
        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String text = sharedpreferences.getString("nagar_budget", "");
        JSONObject jsonObject = new JSONObject(text);
        final JSONArray data = jsonObject.getJSONArray("data");

        ListWithUniqueDistrictString.clear();
        ListWithUniqueDistrict.clear();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject row = data.getJSONObject(i);
                        String EngDistrictName = row.getString("district_name_en");
                        String NepDistrictName = row.getString("district_name_np");
                        Boolean listDoesNotCotainDistict = !ListWithUniqueDistrictString.contains(EngDistrictName);
                        if (listDoesNotCotainDistict) {
                            District district = new District(String.valueOf(i), EngDistrictName, NepDistrictName);
                            ListWithUniqueDistrictString.add(district.getEnName());
                            ListWithUniqueDistrict.add(district);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("Nishon", e.toString());
                    }

                }


                setupRecycleView();


            }
        };
        new Thread(runnable).start();
    }

    private void setupRecycleView() {

        adapter = new DistrictAdapter(this, ListWithUniqueDistrict);
        rvDistrictBudgetNagarActivity.setAdapter(adapter);
        rvDistrictBudgetNagarActivity.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new DistrictAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String DistrictNameEng = ListWithUniqueDistrict.get(position).getEnName();

                Intent toNagarBudgetAct = new Intent(NagarBudgetDistrict.this, NagarBudgetActivity.class);
                toNagarBudgetAct.putExtra("EngDistrictName", DistrictNameEng);
                startActivity(toNagarBudgetAct);



            }
        });
    }


    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("जिल्ला");
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }




}


