package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;


import java.util.ArrayList;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.Utils.ChartColor;

public class PieChartDetailsActivity extends AppCompatActivity {

    Toolbar toolbar;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);


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

       int val1 = Integer.parseInt(dist1);
//        Double val2 = Double.parseDouble(dist2);
//        Double val3 = Double.parseDouble(dist3);
//        Double val4 = Double.parseDouble(dist4);
//        Double val5 = Double.parseDouble(dist5);
//        Double val6 = Double.parseDouble(dist6);
//        Double val7 = Double.parseDouble(dist7);
//        Double val8 = Double.parseDouble(dist8);
//        Double val9 = Double.parseDouble(dist9);

        int val2 = Integer.parseInt(dist2);
        int val3 = Integer.parseInt(dist3);
        int val4 = Integer.parseInt(dist4);
        int val5 = Integer.parseInt(dist5);
        int val6 = Integer.parseInt(dist6);
        int val7 = Integer.parseInt(dist7);
        int val8 = Integer.parseInt(dist8);
        int val9 = Integer.parseInt(dist9);


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


        //=================================Pie Chart Draw==============================//

        pieChart = (PieChart) findViewById(R.id.chart);

        pieChart.setCenterText(stat_name);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(val1, 0));
        entries.add(new Entry(val2, 1));
        entries.add(new Entry(val3, 2));
        entries.add(new Entry(val4, 3));
        entries.add(new Entry(val5, 4));
        entries.add(new Entry(val6, 5));
        entries.add(new Entry(val7, 6));
        entries.add(new Entry(val8, 7));
        entries.add(new Entry(val9, 8));

        PieDataSet dataset = new PieDataSet(entries, stat_name);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("कैलाली");
        labels.add("आछाम");
        labels.add("बझांग");
        labels.add("बाजुरा");
        labels.add("कन्चनपुर");
        labels.add("डडेल्धुरा");
        labels.add("बैतडी");
        labels.add("दार्चुला");
        labels.add("डोटी");

        PieData data = new PieData(labels, dataset);
        dataset.setColors(ChartColor.COLORFUL_COLORS); //
        pieChart.setDescription("विवरण");
        pieChart.setData(data);

        pieChart.animateY(2000);

        pieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image

        //===================================End Of PieChart=========================================//

    }
}
