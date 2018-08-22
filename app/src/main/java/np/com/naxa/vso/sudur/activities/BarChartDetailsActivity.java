package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import java.util.ArrayList;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.Utils.ChartColor;

public class BarChartDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    Float val1,val2,val3,val4,val5,val6,val7,val8,val9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart_details);


        Intent i = getIntent();
        // Receiving the Data
        String stat_name = i.getStringExtra("stat_name");
        String dist1 = i.getStringExtra("dist1_value");
        String dist2 = i.getStringExtra("dist2_value");
        String dist3 = i.getStringExtra("dist3_value");
        String dist4 = i.getStringExtra("dist4_value");
        String dist5 = i.getStringExtra("dist5_value");
        String dist6 = i.getStringExtra("dist6_value");
        String dist7 = i.getStringExtra("dist7_value");
        String dist8 = i.getStringExtra("dist8_value");
        String dist9 = i.getStringExtra("dist9_value");

         val1 = Float.parseFloat(dist1);
         val2 = Float.parseFloat(dist2);
         val3 = Float.parseFloat(dist3);
         val4 = Float.parseFloat(dist4);
         val5 = Float.parseFloat(dist5);
         val6 = Float.parseFloat(dist6);
         val7 = Float.parseFloat(dist7);
         val8 = Float.parseFloat(dist8);
         val9 = Float.parseFloat(dist9);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(stat_name);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //=================================Bar Chart Draw==============================//

        chart = (BarChart) findViewById(R.id.chart);
        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();

        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, stat_name);

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(ChartColor.COLORFUL_COLORS);

        chart.setData(BARDATA);
        chart.setDescription("विवरण");
        chart.animateY(3000);






        chart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image

        //===================================End Of Bar Chart=========================================//


    }

    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(val1, 0));
        BARENTRY.add(new BarEntry(val2, 1));
        BARENTRY.add(new BarEntry(val3, 2));
        BARENTRY.add(new BarEntry(val4, 3));
        BARENTRY.add(new BarEntry(val5, 4));
        BARENTRY.add(new BarEntry(val6, 5));
        BARENTRY.add(new BarEntry(val7, 6));
        BARENTRY.add(new BarEntry(val8, 7));
        BARENTRY.add(new BarEntry(val9, 8));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("कैलाली");
        BarEntryLabels.add("आछाम");
        BarEntryLabels.add("बझांग");
        BarEntryLabels.add("बाजुरा");
        BarEntryLabels.add("कन्चनपुर");
        BarEntryLabels.add("डडेल्धुरा");
        BarEntryLabels.add("बैतडी");
        BarEntryLabels.add("दार्चुला");
        BarEntryLabels.add("डोटी");

    }
}
