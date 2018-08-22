package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import np.com.naxa.vso.R;


public class NgoIngoDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "cheese_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_ingo_details);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_sudur.setBackgroundColor(getResources().getColor(R.color.nliveo_white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView tvSasthaName = (TextView) findViewById(R.id.sastha_name);
        TextView tvSathaType = (TextView) findViewById(R.id.sastha_type);
        TextView tvSasthaWork = (TextView) findViewById(R.id.sastha_work);
        TextView tvSasthaemail = (TextView) findViewById(R.id.sastha_email);
        TextView tvSasthaDesc = (TextView) findViewById(R.id.sastha_desc);


        ImageView News_Image = (ImageView) findViewById(R.id.backdrop);



        Intent i = getIntent();
        // Receiving the Data
        String sastha_name = i.getStringExtra("sastha_name_np");
        String sastha_type = i.getStringExtra("sastha_type_np");
        String sastha_work = i.getStringExtra("sastha_work_np");
        String sastha_email = i.getStringExtra("sastha_email");
        String sastha_desc = i.getStringExtra("sastha_desc_np");

//        Log.e( "News_Image_Path", news_image );
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
        collapsingToolbar.setTitle(sastha_name);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.accent));



        // Displaying Received data
            tvSasthaName.setText(sastha_name);
            tvSasthaWork.setText(sastha_work);
            tvSathaType.setText(sastha_type);
            tvSasthaemail.setText(sastha_email);
            tvSasthaDesc.setText(sastha_desc);



    }


}
