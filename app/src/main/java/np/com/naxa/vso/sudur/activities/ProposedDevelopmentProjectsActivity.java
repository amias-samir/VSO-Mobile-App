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

public class ProposedDevelopmentProjectsActivity extends AppCompatActivity {

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
    SharedPreferences.Editor editor ;

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

    private void initializeUI(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("प्रस्तावित विकास परियोजनाहरु");
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
        newsAndEventsModel.setNews_title_np("धनगढी – खुटिया – दिपायल – चैनपुर – ताक्लाकोट द्रुतमार्गको निर्माण सम्पन्न");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("महाकाली लोकमार्गको निर्माण सम्पन्न");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("सेती लोकमार्ग निर्माण सम्पन्न");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("मध्य पहाडी लोकमार्गको सेती – महाकाली खण्डको निर्माण तथा स्तरोन्नती ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("हुलाकी लोकमार्गको सेती – महाकाली खण्डको निर्माण तथा स्तरोन्नती");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("महेन्द्रनगर – दैजी – जोगबुडा – डडेल्धुरा सडक निर्माण सम्पन्न");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("पूर्बपश्चिम लोकमार्गको गड्डाचौकी – चिसापानी खण्ड ८ लेनको बनाउने");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("धनगढी विमानस्थललाइ क्षेत्रीय अन्तराष्ट्रिय विमानस्थलको रूपमा विकास र बिस्तार ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("कोल्टी, चैनपुर र गोकुलेश्वर विमानस्थलको स्तरोन्नती, विकास र संचालन");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("पश्चिम सेती जलविद्युत आयोजना निर्माण");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("पंचेश्वर बहु उद्देश्यीय जलविद्युत आयोजना निर्माण ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("सेती – कर्णाली बहु उद्देश्यीय जलाशययुक्त आयोजना निर्माण ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("चमेलिया, बुढीगंगा, कालंगा, लगायत निर्माणाधीन जलविद्युत आयोजना निर्माण सम्पन्न");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("तिब्बतको कैलाश मानसरोवर र यस क्षेत्रका चार धाम ९गोदावरी, परशुराम, मल्लिकार्जुन र बैजनाथ तथा सात शक्तिपीठहरु ९उग्रतारा, शैलेश्वरी, बडिमालिका, त्रिपुरासुन्दरी, निन्ग्लाशैनी, डीलाशैनी, मेलौली० हरु जोड्ने धार्मिक पर्यटकीय मार्ग निर्माण र स्तरोन्नती गर्ने");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("खप्तड राष्ट्रिय निकुन्जको मध्यवर्ती क्षेत्रमा पर्यटकीय चक्रपथ निर्माण");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("९ वटै जिल्लाका उपयुक्त पर्यटकीय स्थलहरुमा केवलकार लगायत अन्य पर्यटकीय पूर्वाधार स्थापना ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("कर्णाली चिसापानी देखि ब्रह्मदेवसम्मको चुरे क्षेत्रलाई संरक्षण र विकास गरी जंगल सफारी, हाइकिंग लगायत पर्यटकीय गतिबिधी संचालन");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("धनगढी, ताक्लाकोट, र दोधारा चाँदनीमा अत्याधुनिक भन्सार सुबिधा सहित ब्यबस्थित ब्यापार नाका स्थापना र संचालन");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("महाकाली नदीमा  दोधारा चाँदनी क्षेत्रमा सुक्खा बन्दरगाह सहित चार लेनको पक्कि पूल र पंचेश्वर, झुलाघांट र दार्चुलामा पक्कि पूल निर्माण ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("डडेल्धुरामा अत्याधुनिक र सुबिधा सम्पन्न रंगशाला र क्रिकेट मैदान निर्माण ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("कैलालीको गेटामा दशरथ चन्द स्वास्थ्य बिज्ञान प्रतिष्ठान तथा शिक्षण अस्पताल निर्माण ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("सुदूरपश्चिम बिश्वबिद्यालयको विकास, बिस्तार र स्तरोन्नती, सीपमुलक र रोजगार मैत्री प्राबिधिक र ब्यबसायिक अध्ययन केन्द्रहरुको स्थापना ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("नौ वटै जिल्ला सदरमुकामहरुमा बिशेषज्ञ सेवा सहितको सुबिधा सम्पन्न अस्पताल र ट्रमा सेन्टर निर्माण");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("कन्चनपुरको बेद्कोट नगरपालिकामा बृहद औद्योगिक क्षेत्र स्थापना र संचालन, कृषी यन्त्र, औजार, मलखाद, बिउ बिजन, बिषादी लगायत कृषी जन्य उद्योग स्थापनामा बिशेष छूटको ब्यबस्था");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("दिपायल र टीकापुरमा कृषि अध्ययन तथा अनुसन्धान संस्थान स्थापना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("अछाममा पशुबिज्ञान अध्ययन तथा अनुसन्धान संस्थान स्थापना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("डडेल्धुरामा बन बिज्ञान अध्ययन संथानको स्थापना ");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("बैतडी जिल्लामा रहेका फलाम लगायत खानी संचालन, अन्य क्षेत्रमा खानी तथा खनीज पदार्थको उपलब्धता सम्बन्धी अनुसन्धान");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("बझांग जिल्लामा जडीबुटी संकलन, प्रशोधन तथा आयुर्वेद अध्ययन केन्द्रको स्थापना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("दार्चुलामा बृहद घरेलु गलैंचा उद्योग स्थापना");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("सुदूरपश्चिम क्षेत्रको बिस्तृत भूउपयोग योजना तयारी तथा कार्यान्वयन");
        resultCur.add(newsAndEventsModel);

        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("उच्च पहाडी र हिमाली स्थानमा छरिएर रहेका, अन्य जोखिमयुक्त स्थानमा रहेका तथा विभिन्न विपद तथा विकास निर्माणको कारणले बिस्थापित भएका ब्यक्तीहरुको लागी एकीकृत बस्ती र सुरक्षित आवाशीय क्षेत्रको विकास");
        resultCur.add(newsAndEventsModel);

        //33
        newsAndEventsModel = new NewsAndEventsModel();
        newsAndEventsModel.setNews_title_np("सामाजिक आर्थिक रूपमा पिछडिएका विभिन्न बर्ग, क्षेत्र, समुदायका ब्यक्तीहरुको जीविकोपार्जन सहज बनाउन विभिन्न लक्षित कार्यक्रमहरु ");
        resultCur.add(newsAndEventsModel);

        fillTable();
    }
    public void fillTable() {
        filteredList = resultCur;
        ca = new MajorDevelopment_List_Adapter(this, filteredList);
        recyclerView.setAdapter(ca);
    }
}