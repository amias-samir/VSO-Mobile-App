package np.com.naxa.vso.sudur.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.Constants;
import np.com.naxa.vso.sudur.model.Project_Status_Model;

/**
 * Created by Samir on 10/24/2016.
 */

public class Project_Status_Adapter extends RecyclerView.Adapter<Project_Status_Adapter.ViewHolder> {
    List<Project_Status_Model> mItems;
    Activity activity;
    String[] colorcode = {
            "#ffffff", "#ffffff",
            "#ffffff", "#ffffff",
            "#ffffff", "#ffffff"};

    public Project_Status_Adapter(Context context, Activity activity) {
        super();
        mItems = new ArrayList<Project_Status_Model>();
        Project_Status_Model species = new Project_Status_Model();
        String[] titleGrid;

        this.activity = activity;
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
        final boolean setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);

//        if (setData) {
//            titleGrid = Constants.menuNepali;
//        } else {
        titleGrid = Constants.statusEnglish;
        // }

        species = new Project_Status_Model();
        species.setName(titleGrid[0]);
        species.setThumbnail(R.drawable.bayalpata_hospital_completed1);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[1]);
        species.setThumbnail(R.drawable.mahakali_irrigation_ongoing);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[2]);
        species.setThumbnail(R.drawable.envisoned_projects);
        mItems.add(species);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_project_status, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Project_Status_Model nature = mItems.get(i);


//        Resources r = activity.getResources();
//        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
//        //this change height of rcv
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
//        int width = displaymetrics.widthPixels;
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.height = (height - px) / 3; //height recycleviewer
//
//        viewHolder.cardViewGridView.setLayoutParams(params);
        viewHolder.tvspecies.setText(nature.getName());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
//        viewHolder.cardViewGridView.setCardBackgroundColor(Color.parseColor(colorcode[i]));
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvspecies;
        public CardView cardViewGridView;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView) itemView.findViewById(R.id.tv_species);
            cardViewGridView = (CardView) itemView.findViewById(R.id.cardview_grd);
        }
    }
}