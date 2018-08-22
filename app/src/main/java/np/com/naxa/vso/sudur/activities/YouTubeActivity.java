package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.fragment.Fragment_YouTube;


/**
 * Created by ramaan on 5/31/2016.
 */
public class YouTubeActivity extends AppCompatActivity {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    Toolbar toolbar;
   // private YouTubePlayerView youTubeView;
    String videoCode, videoDesc_en, videoDesc_np, titleNp, titleEn;
    String newsTitle;
    TextView descText;

    String title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_player);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Introduction");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.accent));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.mipmap.ic_back_icon);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        titleEn = (String) bundle.get("VideoTitle_en");
        titleNp = (String) bundle.get("VideoTitle_np");
        videoCode = (String) bundle.get("VideoCode");
        videoDesc_en = (String) bundle.get("VideoDesc_En");
        videoDesc_np = (String) bundle.get("VideoDesc_Np");
        TextView title = (TextView) findViewById(R.id.textView_title);
        descText = (TextView) findViewById(R.id.textView_VideoDescription);

        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(YouTubeActivity.this);
        final boolean setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);
        String desc_title_nep = "वृष्तित विवरण";
        String desc_title_eng = "Description";
        TextView descTitle = (TextView) findViewById(R.id.youtube_description);

        if (setData) {

          //  mTitle.setText("भिडियो");
            title.setText(titleNp);
            descText.setText(videoDesc_np);
            descTitle.setText(desc_title_nep);

        } else {

          //  mTitle.setText("Video");
            title.setText(titleEn);
            descText.setText(videoDesc_en);
            descTitle.setText(desc_title_eng);
        }


        Fragment_YouTube f = Fragment_YouTube.newInstance(videoCode);
        getSupportFragmentManager().beginTransaction().replace(R.id.youtube_fragment, f).commit();
        getSupportActionBar().setTitle(titleEn);

    }

}