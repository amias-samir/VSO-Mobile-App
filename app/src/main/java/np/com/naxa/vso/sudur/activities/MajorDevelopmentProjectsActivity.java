package np.com.naxa.vso.sudur.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.adapter.MajorDevelopment_List_Adapter;
import np.com.naxa.vso.sudur.model.NewsAndEventsModel;

/**
 * Created by susan on 6/26/2017.
 */

public class MajorDevelopmentProjectsActivity extends AppCompatActivity {

    //Susan
    private SwipeRefreshLayout swipeContainer;

    //Susan
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    MajorDevelopment_List_Adapter ca;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    ProgressDialog mProgressDlg;
    public static List<NewsAndEventsModel> resultCur = new ArrayList<>();
    public static List<NewsAndEventsModel> filteredList = new ArrayList<>();
    public static final String MyPREFERENCES = "major_development_projects";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private boolean setData;
    String jsonToSend = null;
    private String date, time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.major_development_projects);

        initializeUI();

        //Susan
        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        recyclerView = (RecyclerView) findViewById(R.id.NewsList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        createList();
//        convertDataToJson();

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
//                    Drawer.closeDrawers();
                    int position = recyclerView.getChildPosition(child);
//                    Intent intent = new Intent(MajorDevelopmentProjectsActivity.this, MajorProjectDetailsActivity.class);
//                    intent.putExtra("project_title_np", resultCur.get(position).news_title_en);
//                    intent.putExtra("project_desc_np", resultCur.get(position).news_desc_en);
////                    intent.putExtra("news_date_np", resultCur.get(position).news_date_en);
//                    intent.putExtra("project_image", resultCur.get(position).mThumbnail);
//                    startActivity(intent);
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

        //Swipe Refresh Action
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainerNews);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (networkInfo != null && networkInfo.isConnected()) {

//                    editor.clear();
//                    editor.commit();
//
//                    refreshContent();
                    swipeContainer.setRefreshing(false);
                } else {
                    Snackbar.make(swipeContainer, "ईन्टरनेट कनेक्सन छैन । ", Snackbar.LENGTH_LONG)
                            .setAction("Retry", null).show();
                    swipeContainer.setRefreshing(false);
                }
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    private void refreshContent() {
        createList();
        fillTable();
    }

    private void initializeUI() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("मुख्य विकास परियोजनाहरु");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    private void createList() {


        resultCur.clear();


        NewsAndEventsModel newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("पश्चिम सेती जलविद्युत आयोजना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("पन्चेश्वर बहुउद्देश्यिय आयोजना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("रानी जमरा सिंचाई आयोजना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("सेती लोकमार्ग");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("मध्य पहाडि लोकमार्ग सुदूरपश्चिम खण्ड");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("महाकालिको चार लेनको पुल");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("दोधारा चाँदनी सुख्खा बन्दरगाह");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("क्षला औद्योगिक क्षेत्र");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("हुलाकी मार्ग (सुदूरपश्चिम खण्ड)");
        resultCur.add(newsAndEventsModel);


        fillTable();
    }

    public void fillTable() {
        filteredList = resultCur;
        ca = new MajorDevelopment_List_Adapter(this, filteredList);
        recyclerView.setAdapter(ca);
    }
}