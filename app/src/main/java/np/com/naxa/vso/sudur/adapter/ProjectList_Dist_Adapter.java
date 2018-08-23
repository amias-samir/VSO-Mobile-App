package np.com.naxa.vso.sudur.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import np.com.naxa.vso.sudur.model.Project_List_Dist_Model;

/**
 * Created by Samir on 10/4/2016.
 */
public class ProjectList_Dist_Adapter extends RecyclerView.Adapter<ProjectList_Dist_Adapter.ViewHolder> {
    List<Project_List_Dist_Model> mItems;
    Activity activity;
    String[] colorcode = {
            "#ffffff", "#ffffff",
            "#ffffff", "#ffffff",
            "#ffffff", "#ffffff"};

    public ProjectList_Dist_Adapter(Context context, Activity activity) {
        super();
        mItems = new ArrayList<Project_List_Dist_Model>();
        Project_List_Dist_Model species = new Project_List_Dist_Model();
        String[] titleDistrictList;

        this.activity = activity;
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
        final boolean setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);

//        if (setData) {
//            titleGrid = Constants.menuNepali;
//        } else {
        titleDistrictList = Constants.districtListEnglish;
        // }

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[0]);
       // species.setThumbnail(R.drawable.khaptad_religious);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[1]);
        //species.setThumbnail(R.drawable.deve3);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[2]);
        //species.setThumbnail(R.drawable.historical);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[3]);
       // species.setThumbnail(R.drawable.nature);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[4]);
        // species.setThumbnail(R.drawable.nature);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[5]);
        // species.setThumbnail(R.drawable.nature);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[6]);
        // species.setThumbnail(R.drawable.nature);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[7]);
        // species.setThumbnail(R.drawable.nature);
        mItems.add(species);

        species = new Project_List_Dist_Model();
        species.setName(titleDistrictList[8]);
        // species.setThumbnail(R.drawable.nature);
        mItems.add(species);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_district_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Project_List_Dist_Model nature = mItems.get(i);


        Resources r = activity.getResources();
       // int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
        //this change height of rcv

       // DisplayMetrics displaymetrics = new DisplayMetrics();
       // activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


       // int height = displaymetrics.heightPixels;
        //int width = displaymetrics.widthPixels;

      // RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

      //  params.height = (height - px) / 3; //height recycleviewer

       // viewHolder.cardViewDistrictListView.setLayoutParams(params);

        viewHolder.tvspecies.setText(nature.getName());
       // viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
      //  viewHolder.cardViewDistrictListView.setCardBackgroundColor(Color.parseColor(colorcode[i]));
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvspecies;
        public CardView cardViewDistrictListView;

        public ViewHolder(View itemView) {
            super(itemView);
           // imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView) itemView.findViewById(R.id.tv_district_list);
            cardViewDistrictListView = (CardView) itemView.findViewById(R.id.cardview_district_list);
        }
    }
}

