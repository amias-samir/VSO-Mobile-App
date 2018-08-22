package np.com.naxa.vso.sudur.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;



import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.fragment.DevStatusSudurPaschhimFragment;

/**
 * Created by Nishon Tandukar on 26 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class DistrictProgramActivity extends AppCompatActivity {

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
        getSupportActionBar().setTitle("जिल्ला स्थित कार्यक्रम");
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        if (savedInstanceState == null) {
            Fragment newFragment = new DevStatusSudurPaschhimFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, newFragment, "id").commit();
        }
    }
}
