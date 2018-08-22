package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.vso.R;

/**
 * Created by Nishon Tandukar on 26 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class BikashGatibitiActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frame_layout_introduction_bikash_kshetra)
    FrameLayout frameLayoutIntroductionBikashKshetra;
    @BindView(R.id.frame_layout_district_program)
    FrameLayout frameLayoutDistrictProgram;
    @BindView(R.id.frame_layout_local_budget)
    FrameLayout frameLayoutLocalBudget;
    @BindView(R.id.frame_layout_dev_agencies)
    FrameLayout frameLayoutDevAgencies;
    @BindView(R.id.frame_layout_proposed_development_projects)
    FrameLayout frameLayoutProposed;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private float posx;
    private float posy = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatibity_bikash);
        ButterKnife.bind(this);


        setToolbar();


    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("विकासका गतिविधिहरु");
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("x", (int) scrollView.getX());
        savedInstanceState.putInt("x", (int) scrollView.getY());

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {

        final int x = savedInstanceState.getInt("x");
        final int y = savedInstanceState.getInt("y");
        if (x != 0 && y != 0)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(x, y);
                }
            });
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @OnClick({R.id.frame_layout_introduction_bikash_kshetra, R.id.frame_layout_proposed_development_projects, R.id.frame_layout_district_program, R.id.frame_layout_local_budget, R.id.frame_layout_dev_agencies})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.frame_layout_introduction_bikash_kshetra:
                startActivity(new Intent(this, MajorDevelopmentProjectsActivity.class));

                break;
            case R.id.frame_layout_proposed_development_projects:
                startActivity(new Intent(this, ProposedDevelopmentProjectsActivity.class));

                break;
            case R.id.frame_layout_district_program:
                startActivity(new Intent(this, DistrictProgramActivity.class));
                break;
            case R.id.frame_layout_local_budget:
                startActivity(new Intent(this, NagarBudgetDistrict.class));
                break;
            case R.id.frame_layout_dev_agencies:
                startActivity(new Intent(this, DevelopmentINGOsOrganizationActivity.class));
                break;
        }
    }


}



