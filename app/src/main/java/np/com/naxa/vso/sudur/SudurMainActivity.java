package np.com.naxa.vso.sudur;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.activities.AboutUsActivity;
import np.com.naxa.vso.sudur.activities.PrivacyPolicyActivity;
import np.com.naxa.vso.sudur.fragment.HomeFragment;
import np.com.naxa.vso.sudur.fragment.MapFragment;
import np.com.naxa.vso.sudur.fragment.NewsAndEventsFragment;
import np.com.naxa.vso.sudur.fragment.Video_Fragment;

public class SudurMainActivity extends AppCompatActivity {

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;

    String url = "naxa.com.np/privacy";


    private static final String TOOLBAR_COLOR = "#FF5252";

    Fragment fragment;

    android.app.FragmentTransaction fragmentTransaction;
    private static final String TAG = "SudurMainActivity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.video_list,
            R.drawable.newsfeed,
            R.drawable.ic_map,
    };
    ViewPagerAdapter adapter;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigation;

    private String[] mFragmentTitleList = {
            "गृह पृष्ठ"
            , "भिडियो"
            , "समाचार तथा कार्यक्रमहरू"
            , "नक्सा"
    };


//    private static class NavigationCallback extends CustomTabsCallback {
//        @Override
//        public void onNavigationEvent(int navigationEvent, Bundle extras) {
//            Log.w(TAG, "onNavigationEvent: Code = " + navigationEvent);
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sudur);
        initInstances();

        //to change page title on viewpager scroll
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(mFragmentTitleList[position]);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //-------------back button navigation--------------//
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });


    }


    //initiliaze the required UI elements
    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("गृह पृष्ठ");
//        toolbar.canShowOverflowMenu();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupNavDrawer();
        changeTabColor();


    }

    private void setupNavDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_list_activity);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_homepage_name, R.string.app_homepage_name);
        drawerLayout.addDrawerListener(drawerToggle);


        navigation = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigation.getHeaderView(0);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                 int id = menuItem.getItemId();

                selectDrawerItem(menuItem);

                return false;
            }
        });
    }



    //=========================================================custom chrome tab=======================================================//

//    @Override
//    protected void onDestroy() {
//        unbindCustomTabsService();
//        super.onDestroy();
//    }
//
//
//    private CustomTabsSession getSession() {
//        if (mClient == null) {
//            mCustomTabsSession = null;
//        } else if (mCustomTabsSession == null) {
//            mCustomTabsSession = mClient.newSession(new NavigationCallback());
//            SessionHelper.setCurrentSession(mCustomTabsSession);
//        }
//        return mCustomTabsSession;
//    }
//
//
//    private void bindCustomTabsService() {
//        if (mClient != null) return;
//        if (TextUtils.isEmpty(mPackageNameToBind)) {
//            mPackageNameToBind = CustomTabsHelper.getPackageNameToUse(this);
//            if (mPackageNameToBind == null) return;
//        }
//        mConnection = new ServiceConnection((ServiceConnectionCallback) this);
//        boolean ok = CustomTabsClient.bindCustomTabsService(this, mPackageNameToBind, mConnection);
//        if (ok) {
//            //mConnectButton.setEnabled(false);
//        } else {
//            mConnection = null;
//        }
//    }
//
//    private void unbindCustomTabsService() {
//        if (mConnection == null) return;
//        unbindService(mConnection);
//        mClient = null;
//        mCustomTabsSession = null;
//    }






    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
//        Fragment fragment = null;
//        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_item_home:
                Intent mainintent = new Intent(SudurMainActivity.this, SudurMainActivity.class);
                startActivity(mainintent);
                break;

            case R.id.nav_item_about_us:

                Intent aboutusintent = new Intent(SudurMainActivity.this, AboutUsActivity.class);
                startActivity(aboutusintent);
                break;

            case R.id.nav_item_privacy_policy:

                Intent privacyintent = new Intent(SudurMainActivity.this, PrivacyPolicyActivity.class);
                startActivity(privacyintent);
//                try{
//                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
//                    builder.setToolbarColor(Color.parseColor(TOOLBAR_COLOR)).setShowTitle(true);
//                    prepareMenuItems(builder);
//                    prepareActionButton(builder);
//                    builder.setStartAnimations(this, R.anim.move_right_in_activity, R.anim.move_left_out_activity);
//                    builder.setExitAnimations(this, R.anim.move_left_in_activity, R.anim.move_right_out_activity);
//                    builder.setCloseButtonIcon(
//                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_back_icon));
//                    CustomTabsIntent customTabsIntent = builder.build();
//                    CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
//                    customTabsIntent.launchUrl(this, Uri.parse(url));
//                }
//                catch (Exception e){
//
//                }

                break;
            default:
                Intent mainintent1 = new Intent(SudurMainActivity.this, SudurMainActivity.class);
                startActivity(mainintent1);
        }

//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();
//        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);
//        // Set action bar title
//        setTitle(menuItem.getTitle());
//        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }


    private void changeTabColor() {


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.accent), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.tabIconColor), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //binds the fragment and the icons together
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
//        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
//        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
//        tabLayout.getTabAt(6).setIcon(tabIcons[6]);
//        tabLayout.getTabAt(7).setIcon(tabIcons[7]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new Video_Fragment(), "Videos");
        adapter.addFragment(new NewsAndEventsFragment(), "News and Events");
        adapter.addFragment(new MapFragment(), "Map");
//        adapter.addFragment(new SociaEconomicFragment(), "Socio Economic Status");
//        adapter.addFragment(new MapFragment(), "Map");
//        adapter.addFragment(new FragmentNewsEvents(), "News and Events");
//        adapter.addFragment(new AddOfficeFragment(), "Add Office");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        //contains the fragments objects
        private final List<Fragment> mFragmentList = new ArrayList<>();
        //contains the list of title of the fragments
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

            return null;
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_report_problem_24dp)
                .setTitle("Exit From App")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SudurMainActivity.this.onBackPressed();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        } else {
                            finish();
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }


//    //==============================custom chrome tab  code starts here =================//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void prepareMenuItems(CustomTabsIntent.Builder builder) {
//        Intent menuIntent = new Intent();
//        menuIntent.setClass(getApplicationContext(), this.getClass());
//        // Optional animation configuration when the user clicks menu items.
//        Bundle menuBundle = ActivityOptions.makeCustomAnimation(this, android.R.anim.slide_in_left,
//                android.R.anim.slide_out_right).toBundle();
//        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, menuIntent, 0,
//                menuBundle);
//        builder.addMenuItem("Menu entry 1", pi);
//    }
//
//    private void prepareActionButton(CustomTabsIntent.Builder builder) {
//        // An example intent that sends an email.
//        Intent actionIntent = new Intent(Intent.ACTION_SEND);
//        actionIntent.setType("*/*");
//        actionIntent.putExtra(Intent.EXTRA_EMAIL, "example@example.com");
//        actionIntent.putExtra(Intent.EXTRA_SUBJECT, "example");
//        PendingIntent pi = PendingIntent.getActivity(this, 0, actionIntent, 0);
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share);
//        builder.setActionButton(icon, "send email", pi, true);
//    }
//    @Override
//    public void onServiceConnected(CustomTabsClient client) {
//
//
//    }
//
//    @Override
//    public void onServiceDisconnected() {
//
//
//
//    }

}
