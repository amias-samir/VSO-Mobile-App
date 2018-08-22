package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;


import np.com.naxa.vso.R;

public class NGODetailsActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "NGO_Detail_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingo_details);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_sudur.setBackgroundColor(getResources().getColor(R.color.nliveo_white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView News_Title = (TextView) findViewById(R.id.news_title);
        TextView News_Desc = (TextView) findViewById(R.id.news_desc);
        TextView News_date = (TextView) findViewById(R.id.news_date);

        ImageView News_Image = (ImageView) findViewById(R.id.backdrop);



        Intent i = getIntent();
        // Receiving the Data
        String title_np = i.getStringExtra("title_np");
        String desc_np = i.getStringExtra("desc_np");
        String date_np = i.getStringExtra("email_np");
//        String news_image = i.getStringExtra("image");
//
//        Log.e( "Image_Path", news_image );
//
//
//        if(news_image != null) {
//            Glide.with(getApplicationContext())
//                    .load(news_image)
//                    .thumbnail(0.5f)
//                    .override(256, 256)
//                    .into(News_Image);
//        }

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title_np);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.accent));

        // Displaying Received data
        News_Title.setText(title_np);
        News_Desc.setText(desc_np);

        News_date.setText(date_np);

    }
}
