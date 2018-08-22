package np.com.naxa.vso.sudur.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.activities.AskForHelpActivity;
import np.com.naxa.vso.sudur.activities.BikashGatibitiActivity;
import np.com.naxa.vso.sudur.activities.BusinessPlacesMapActivity;
import np.com.naxa.vso.sudur.activities.HamroSudurPaschimActivity;
import np.com.naxa.vso.sudur.activities.IntroductionUpdateActivity;
import np.com.naxa.vso.sudur.activities.RajnitikAwasthaActivity;
import np.com.naxa.vso.sudur.activities.SocioEconomicActivity;
import np.com.naxa.vso.sudur.adapter.GridSpacingItemDecorator;
import np.com.naxa.vso.sudur.adapter.HomeList_Adapter;


/**
 * Created by Samir on 3/29/2016.
 */
public class HomeFragment extends Fragment {
    String title_english;



    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    MyReceiver r;

    private SharedPreferences wmbPreference;
    private boolean setData;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_layout,null);
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
            HomeFragment.this.refresh();
        }
    }

    //
    @Override
    public void onDestroy() {
        super.onDestroy();
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
       // if (setData) {
        //    ((SudurMainActivity) getActivity()).getSupportActionBar().setTitle(title_nepali);
        //    ((SudurMainActivity) getActivity()).getToolbar().setBackgroundResource(R.color.colorPrimary);

       // }else{
         //   ((SudurMainActivity) getActivity()).getSupportActionBar().setTitle(title_english);
         //   ((SudurMainActivity) getActivity()).getToolbar().setBackgroundResource(R.color.colorPrimary);

       // }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Calling the RecyclerView

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_grid_home);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spanCount = 1;
        int spacing = 5;
        boolean includeEdge = true;
        mRecyclerView.addItemDecoration(new GridSpacingItemDecorator(spanCount, spacing, includeEdge));

        mAdapter = new HomeList_Adapter(getActivity(), getActivity());
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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

    private void callFragment(int position) {

        Fragment mFragment = null;
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        switch (position) {
            case 0:
                Intent intent1 = new Intent(getActivity(), IntroductionUpdateActivity.class);
                startActivity(intent1);
                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), BikashGatibitiActivity.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(getActivity(), HamroSudurPaschimActivity.class);
                startActivity(intent3);
                break;
            case 3:
                Intent intent7 = new Intent(getActivity(), SocioEconomicActivity.class);
                startActivity(intent7);
                break;
            case 4:
                Intent intent6 = new Intent(getActivity(), RajnitikAwasthaActivity.class);
                startActivity(intent6);
                break;

            case 5:
                Intent intent5 = new Intent(getActivity(), BusinessPlacesMapActivity.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent4 = new Intent(getActivity(), AskForHelpActivity.class);
                startActivity(intent4);
                break;


        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().addToBackStack("home").replace(R.id.containerView, mFragment).commit();
        }
    }
}
