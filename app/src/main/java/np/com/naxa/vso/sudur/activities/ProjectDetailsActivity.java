package np.com.naxa.vso.sudur.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import np.com.naxa.vso.R;

public class ProjectDetailsActivity extends AppCompatActivity {

    String project_image;

    public static final String EXTRA_NAME = "cheese_name";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_sudur.setBackgroundColor(getResources().getColor(R.color.nliveo_white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        TextView Project_Title = (TextView) findViewById(R.id.textView_project_title);
        TextView Project_Desc = (TextView) findViewById(R.id.textView_project_desc);
        TextView Project_District = (TextView) findViewById(R.id.textView_district_name);
        TextView Project_Contractor = (TextView) findViewById(R.id.textView_project_contractor);
        TextView Project_Budget = (TextView) findViewById(R.id.textView_project_budget);

        ImageView Project_Image = (ImageView) findViewById(R.id.backdrop);



        Intent i = getIntent();
        // Receiving the Data
        String title_np = i.getStringExtra("project_title_np");
        String desc_np = i.getStringExtra("project_desc_np");

        String district_np = i.getStringExtra("project_district_np");
        String contractor_np = i.getStringExtra("project_contractor_np");
        String budget_np = i.getStringExtra("project_budget_np");
        String project_image = i.getStringExtra("project_image");





        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(title_np);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.accent));



        // Displaying Received data
        Project_Title.setText(title_np);
        Project_Desc.setText(desc_np);

        Project_District.setText(": "+district_np);
        Project_Contractor.setText(": "+contractor_np);
        Project_Budget.setText(": "+budget_np);

        if(project_image != null) {
            Glide.with(getApplicationContext())
                    .load(project_image)
                    .thumbnail(0.5f)
                    .override(256, 256)
                    .into(Project_Image);
        }
    }



}

