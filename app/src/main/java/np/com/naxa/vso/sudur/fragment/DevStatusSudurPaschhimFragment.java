package np.com.naxa.vso.sudur.fragment;


import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.Utils.SessionHelper;
import np.com.naxa.vso.sudur.adapter.AboutFWDC_Project_List_Adapter;
import np.com.naxa.vso.sudur.adapter.GridSpacingItemDecorator;
import np.com.naxa.vso.sudur.shared.CustomTabsHelper;
import np.com.naxa.vso.sudur.shared.ServiceConnection;
import np.com.naxa.vso.sudur.shared.ServiceConnectionCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevStatusSudurPaschhimFragment extends Fragment {


    public static String TAG = "DevStatusSudurPaschhim";
    public static String pdf_downloadLINK;
    private static final String TOOLBAR_COLOR = "#ffffff";


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    MyReceiver r;

    private SharedPreferences wmbPreference;
    private boolean setData;

    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private String mPackageNameToBind;

    private static class NavigationCallback extends CustomTabsCallback {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            Log.w(TAG, "onNavigationEvent: Code = " + navigationEvent);
        }
    }


    public DevStatusSudurPaschhimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_project_status, container, false);
        wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);


        return rootView;


    }
    public void refresh() {
        //yout code in refresh.
        Log.i("Refresh", "YES");
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            DevStatusSudurPaschhimFragment.this.refresh();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    @Override
    public void onResume() {
        super.onResume();

        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r,
                new IntentFilter("HOME_REFRESH"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Calling the RecyclerView

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_grid_status);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spanCount = 1;
        int spacing = 5;
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecorator(spanCount, spacing, includeEdge));

        mAdapter = new AboutFWDC_Project_List_Adapter(getActivity(), getActivity());
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildPosition(child);
                    callFragment(position);
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void callFragment(int position) {

        Fragment mFragment = null;
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();

//        switch (position) {
//            case 0:
//                Intent intent1 = new Intent(getActivity(), FWDRCompletedProjectsActivity.class);
//                startActivity(intent1);
//                break;
//            case 1:
//                Intent intent2 = new Intent(getActivity(), FWDROngoingProjectActivity.class);
//                startActivity(intent2);
//                break;
//            case 2:
//                Intent intent3 = new Intent(getActivity(), FWDRFutureProjectsActivity.class);
//                startActivity(intent3);
//                break;
//        }
        switch (position) {
            case 0:
//                Intent intent1 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent1.putExtra("url", "ddckailali.gov.np/wp-content/uploads/2016/08/DDC-book-73_74.pdf");
//                startActivity(intent1);
//      "http://docs.google.com/gview?embedded=true&url="+

                pdf_downloadLINK ="https://drive.google.com/file/d/0BzQ9rVbqzg-dTWxMWEFTZklxR28/view?usp=sharing";
                chromeInitialization();


                break;
            case 1:
//                Intent intent2 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent2.putExtra("url", "");
//                startActivity(intent2);

                pdf_downloadLINK ="";
                if(!pdf_downloadLINK.equals("")){
                    chromeInitialization();
                }
                else {
                    Toast.makeText(getActivity(), "कुनै जानकारी भेटिएन", Toast.LENGTH_SHORT).show();
                }


                break;
            case 2:
//                Intent intent3 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent3.putExtra("url", "http://ddcbajhang.gov.np/wp-content/uploads/2016/03/073-74-ddc-counciel-56.pdf");
//                startActivity(intent3);

                pdf_downloadLINK = "https://drive.google.com/file/d/0BzQ9rVbqzg-dcmYya0VmSXRwUUk/view?usp=sharing";
                chromeInitialization();

                break;

            case 3:
//                Intent intent4 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent4.putExtra("url", "http://ddcbajura.gov.np/wp-content/uploads/2016/08/Setting-ADDP-2073-74-2.pdf");
//                startActivity(intent4);
                pdf_downloadLINK = "https://drive.google.com/file/d/0BzQ9rVbqzg-dUFdXV1lEQWw4cTQ/view?usp=sharing";
                chromeInitialization();
                break;
            case 4:
//                Intent intent5 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent5.putExtra("url", "http://ddckanchanpur.gov.np/wp-content/uploads/2016/04/%E0%A4%9C%E0%A4%BF%E0%A4%B2%E0%A5%8D%E0%A4%B2%E0%A4%BE-%E0%A4%B5%E0%A4%BF%E0%A4%95%E0%A4%BE%E0%A4%B8-%E0%A4%AF%E0%A5%8B%E0%A4%9C%E0%A4%A8%E0%A4%BE-%E0%A5%A8%E0%A5%A6%E0%A5%AD%E0%A5%A9-%E0%A5%AD%E0%A5%AA.pdf");
//                startActivity(intent5);
                pdf_downloadLINK = "https://drive.google.com/file/d/0BzQ9rVbqzg-ddGprY0JEQzZyenM/view?usp=sharing";
                chromeInitialization();


                break;
            case 5:
//                Intent intent6 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent6.putExtra("url", "http://ddcdadeldhura.gov.np/wp-content/uploads/2016/12/%E0%A4%A8%E0%A4%BF%E0%A4%B0%E0%A5%8D%E0%A4%B5%E0%A4%BE%E0%A4%9A%E0%A4%A8-%E0%A4%95%E0%A5%8D%E0%A4%B7%E0%A5%87%E0%A4%A4%E0%A5%8D%E0%A4%B0-%E0%A4%AA%E0%A5%82%E0%A4%B0%E0%A5%8D%E0%A4%B5%E0%A4%BE%E0%A4%A7%E0%A4%BE%E0%A4%B0-%E0%A4%B5%E0%A4%BF%E0%A4%B6%E0%A5%87%E0%A4%B7-%E0%A4%95%E0%A4%BE%E0%A4%B0%E0%A5%8D%E0%A4%AF%E0%A4%95%E0%A5%8D%E0%A4%B0%E0%A4%AE-%E0%A5%A8%E0%A5%A6%E0%A5%AD%E0%A5%A9-%E0%A5%A6%E0%A5%AD%E0%A5%AA-%E0%A4%95%E0%A4%BE-%E0%A4%AF%E0%A5%8B%E0%A4%9C%E0%A4%A8%E0%A4%BE%E0%A4%B9%E0%A4%B0%E0%A5%81.pdf");
//                startActivity(intent6);
                pdf_downloadLINK = "https://drive.google.com/file/d/0BzQ9rVbqzg-dMDBfeDNVTS0xYm8/view?usp=sharing";
                chromeInitialization();
                break;

            case 6:
//                Intent intent7 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent7.putExtra("url", "");
//                startActivity(intent7);
                pdf_downloadLINK ="";
                if(!pdf_downloadLINK.equals("")){
                    chromeInitialization();
                }
                else {
                    Toast.makeText(getActivity(), "कुनै जानकारी भेटिएन", Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:
//                Intent intent8 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent8.putExtra("url", "http://ddcdarchula.gov.np/wp-content/uploads/2016/06/DDP-2072_73.pdf");
//                startActivity(intent8);
                pdf_downloadLINK = "https://drive.google.com/file/d/0BzQ9rVbqzg-ddGtocFpqWTJyNUU/view?usp=sharing";
                chromeInitialization();
                break;
            case 8:
//                Intent intent9 = new Intent(AboutFWDCActivity.this, AboutFWDCpdfDetailsWebActivity.class);
//                intent9.putExtra("url", "http://ddcdoti.gov.np/wp-content/uploads/2015/11/1.District-Development-Plan-2072-073-DDC.doc");
//                startActivity(intent9);
                pdf_downloadLINK = "https://drive.google.com/file/d/0BzQ9rVbqzg-dRlNDVnhsM01qd00/view?usp=sharing";
                chromeInitialization();
                break;
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().addToBackStack("home").replace(R.id.containerView, mFragment).commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void chromeInitialization(){

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(Color.parseColor(TOOLBAR_COLOR)).setShowTitle(true);
        prepareMenuItems(builder);
        prepareActionButton(builder);
        builder.setStartAnimations(getActivity(), R.anim.move_right_in_activity, R.anim.move_left_out_activity);
        builder.setExitAnimations(getActivity(), R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_back_icon));
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(getActivity(), customTabsIntent.intent);
        customTabsIntent.launchUrl(getActivity(), Uri.parse(pdf_downloadLINK));


        Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_downloadLINK));
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(
                activityIntent, PackageManager.MATCH_ALL);
        List<String> packagesSupportingCustomTabs = new ArrayList<>();
        for (ResolveInfo info : resolvedActivityList) {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction("android.support.customtabs.action.CustomTabsService");
            serviceIntent.setPackage(info.activityInfo.packageName);
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName);
            }
        }

    }

    @Override
    public void onDestroy() {
        unbindCustomTabsService();
        super.onDestroy();
    }


    private CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new DevStatusSudurPaschhimFragment.NavigationCallback());
            SessionHelper.setCurrentSession(mCustomTabsSession);
        }
        return mCustomTabsSession;
    }

    private void bindCustomTabsService() {
        if (mClient != null) return;
        if (TextUtils.isEmpty(mPackageNameToBind)) {
            mPackageNameToBind = CustomTabsHelper.getPackageNameToUse(getActivity());
            if (mPackageNameToBind == null) return;
        }
        mConnection = new ServiceConnection((ServiceConnectionCallback) this);
        boolean ok = CustomTabsClient.bindCustomTabsService(getActivity(), mPackageNameToBind, mConnection);
        if (ok) {
            //mConnectButton.setEnabled(false);
        } else {
            mConnection = null;
        }
    }

    private void unbindCustomTabsService() {
        if (mConnection == null) return;
        getActivity().unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void prepareMenuItems(CustomTabsIntent.Builder builder) {
        Intent menuIntent = new Intent();
        menuIntent.setClass(getActivity(), this.getClass());
        // Optional animation configuration when the user clicks menu items.
        Bundle menuBundle = ActivityOptions.makeCustomAnimation(getActivity(), android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).toBundle();
        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, menuIntent, 0,
                menuBundle);
        builder.addMenuItem("Menu entry 1", pi);
    }

    private void prepareActionButton(CustomTabsIntent.Builder builder) {
        // An example intent that sends an email.
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("*/*");
        actionIntent.putExtra(Intent.EXTRA_EMAIL, "example@example.com");
        actionIntent.putExtra(Intent.EXTRA_SUBJECT, "example");
        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, actionIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share);
        builder.setActionButton(icon, "send email", pi, true);
    }
    //    @Override
    public void onServiceConnected(CustomTabsClient client) {
//        mLaunchButton.setEnabled(true);

    }

    //    @Override
    public void onServiceDisconnected() {
//        mLaunchButton.setEnabled(false);


    }



}
