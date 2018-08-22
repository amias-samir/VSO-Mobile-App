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
import android.widget.TextView;

import com.naxa.nepal.sudurpaschimanchal.R;
import com.naxa.nepal.sudurpaschimanchal.model.Constants;
import com.naxa.nepal.sudurpaschimanchal.model.Project_Status_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samir on 3/14/2017.
 */

public class AboutFWDC_Project_List_Adapter extends RecyclerView.Adapter<AboutFWDC_Project_List_Adapter.ViewHolder> {
    List<Project_Status_Model> mItems;
    Activity activity;
    String[] colorcode = {
            "#000000", "#000000",
            "#000000", "#000000",
            "#000000", "#000000",
            "#000000", "#000000",
            "#000000"};

    public AboutFWDC_Project_List_Adapter(Context context, Activity activity) {
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
        titleGrid = Constants.districtListEnglish;
        // }

        species = new Project_Status_Model();
        species.setName(titleGrid[0]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[1]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[2]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[3]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[4]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[5]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[6]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[7]);
        mItems.add(species);

        species = new Project_Status_Model();
        species.setName(titleGrid[8]);
        mItems.add(species);
    }

    @Override
    public AboutFWDC_Project_List_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_about_fwdc_project_list, viewGroup, false);
       ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AboutFWDC_Project_List_Adapter.ViewHolder viewHolder, int i) {
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
//        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
//        viewHolder.cardViewGridView.setCardBackgroundColor(Color.parseColor(colorcode[i]));
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        public ImageView imgThumbnail;
        public TextView tvspecies;
        public CardView cardViewGridView;

        public ViewHolder(View itemView) {
            super(itemView);
//            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView) itemView.findViewById(R.id.tv_species);
            cardViewGridView = (CardView) itemView.findViewById(R.id.cardview_grd);
        }
    }
}
