package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.fragment.NagarpalikaFragment;

/**
 * Created by Nishon Tandukar on 26 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class NagarBudgetActivity extends AppCompatActivity {

    HamroSudurPaschimActivity.FragmentRefreshListener fragmentRefreshListener;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainerNagarpalik;


    public void setFragmentRefreshListener(HamroSudurPaschimActivity.FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagarpalika);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("स्थानीय बजेट कार्यक्रम");
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String EngDistrictName = bundle.getString("EngDistrictName");
            NagarpalikaFragment newFragment = new NagarpalikaFragment();
            newFragment.setDistrictToFilter(EngDistrictName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, newFragment, "id").commit();
        }


        if (bundle == null){
            Toast.makeText(this, "Sorry, Your Request Could Not Be Full Filled", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,NagarBudgetDistrict.class));
        }
    }
}
