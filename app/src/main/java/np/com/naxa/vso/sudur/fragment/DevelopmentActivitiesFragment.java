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
import np.com.naxa.vso.sudur.adapter.ProjectList_Dist_Adapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class DevelopmentActivitiesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String title_english;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    MyReceiver r;

    private SharedPreferences wmbPreference;
    private boolean setData;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Grid_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalAttractionFragment newInstance(String param1, String param2) {
        LocalAttractionFragment fragment = new LocalAttractionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DevelopmentActivitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_development_activities,null);
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
            DevelopmentActivitiesFragment.this.refresh();
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

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_list_development_activities_district);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spanCount = 2;
        int spacing = 5;
        boolean includeEdge = true;

        // mRecyclerView.addItemDecoration(new GridSpacingItemDecorator(spanCount, spacing, includeEdge));

        mAdapter = new ProjectList_Dist_Adapter(getActivity(), getActivity());
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
//                    callFragment(position);
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
//    private void callFragment(int position) {
//
//        Fragment mFragment = null;
//        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
//        String title_english = null;
//        String title_nepali = null;
//        int color = R.color.colorPrimary;
////        connectonDetector = new ConnectonDettor.isConnectedToInternet();
//        switch (position) {
//            case 0:
//                Intent intent1 = new Intent(getActivity(), DevelopmentProjectListActivity.class);
//                startActivity(intent1);
//                // title_nepali = getResources().getString(R.string.good_construction_practices_np);
////                title_english = "Introduction";
////                color = R.color.colorPrimary;
//                break;
//
//
//            case 1:
//                Intent intent2 = new Intent(getActivity(), DevelopmentProjectListActivity.class);
//                startActivity(intent2);
//                //  title_nepali = getResources().getString(R.string.worksite_safety_np);
////                title_english = "About FWDC";
////                color = R.color.colorPrimary;
//                break;
//            case 2:
//                Intent intent3 = new Intent(getActivity(), DevelopmentProjectListActivity.class);
//                startActivity(intent3);
//                // title_nepali = getResources().getString(R.string.dudbc_catalogue_np);
////                title_english = "Socio Economic Status";
////                color = R.color.colorPrimary;
//                break;
//
//        }
//
//        if (mFragment != null) {
//            mFragmentManager.beginTransaction().addToBackStack("home").replace(R.id.containerView, mFragment).commit();
//        }
//    }
}
