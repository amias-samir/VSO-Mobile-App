package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import np.com.naxa.vso.R;

public class AttractionDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "cheese_name";
    private FloatingActionButton FABrouteToAttraction;
    private String lat;
    private String lon;

//    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_details);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_sudur.setBackgroundColor(getResources().getColor(R.color.nliveo_white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView Place_Title = (TextView) findViewById(R.id.attraction_title);
        TextView Place_Desc = (TextView) findViewById(R.id.attraction_detail);
        TextView Place_dist = (TextView) findViewById(R.id.attraction_dist);
        TextView Place_Address = (TextView) findViewById(R.id.attraction_address);

        FABrouteToAttraction = (FloatingActionButton) findViewById(R.id.fab_direction);


        ImageView Place_Image = (ImageView) findViewById(R.id.backdrop);


        Intent i = getIntent();
        // Receiving the Data
        String title_np = i.getStringExtra("place_title_np");
        String desc_np = i.getStringExtra("plaece_desc_np");
        lat = i.getStringExtra("place_lat");
        lon = i.getStringExtra("place_lon");


        String dist_np = i.getStringExtra("project_district_np");
        String address_np;


        address_np = i.getStringExtra("address_name_np");
        dist_np = i.getStringExtra("project_district_np");


        String place_image = i.getStringExtra("place_image");


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title_np);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.accent));


        // Displaying Received data
        Place_Title.setText(title_np);
        Place_Desc.setText(desc_np);
        Place_dist.setText(dist_np);
        Place_Address.setText(address_np);


        if (place_image != null) {
            Glide.with(getApplicationContext())
                    .load(place_image).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Place_Image);
        }


        HideFABIfNoLocation();

        FABrouteToAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat + "," + lon));
                startActivity(intent);
            }
        });

    }


    private void HideFABIfNoLocation() {
        boolean hideFAB = false;

        if (lat == null || lat.trim().length() == 0) {
            hideFAB = true;
        }

        if (lon == null || lon.trim().length() == 0) {
            hideFAB = true;
        }

        if (hideFAB) {
            FABrouteToAttraction.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }
}

