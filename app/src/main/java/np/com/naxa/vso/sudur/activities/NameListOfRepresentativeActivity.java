package np.com.naxa.vso.sudur.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;



import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.fragment.Gaunpalika_RepresentativeFragment;
import np.com.naxa.vso.sudur.fragment.JillaSamanwoyeSamitiFrgment;
import np.com.naxa.vso.sudur.fragment.Nagarpalika_RepresentativeFragment;

/**
 * Created by susan on 6/26/2017.
 */

public class NameListOfRepresentativeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    public static String district_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_list_of_local_representative);

        //Call initialize UI method
        initializeUI();

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void initializeUI() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("स्थानिय तहका प्रतिनिधि");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Intent intent = getIntent();
        district_name = intent.getStringExtra("district_np");

    }

    //method to setup View Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Nagarpalika_RepresentativeFragment(), "नगरपालिका \n प्रतिनिधिहरु");
        adapter.addFragment(new Gaunpalika_RepresentativeFragment(), "गाउँपालिका \n प्रतिनिधिहरु");
        adapter.addFragment(new JillaSamanwoyeSamitiFrgment(), "जिल्ला समन्वय \n समिति");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    //View Pager Adapter Class extends Fragment Pager Adapter
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
