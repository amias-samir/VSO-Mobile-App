package np.com.naxa.vso.sudur.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.LocalAttractionModel;

/**
 * Created by Samir on 11/15/2016.
 */

public class LocalAttractionRecyclerAdapter extends RecyclerView.Adapter<LocalAttractionRecyclerAdapter.ContactViewHolder> implements View.OnClickListener {


    private List<LocalAttractionModel> placeList;
    Context context;

    public LocalAttractionRecyclerAdapter(Context context, List<LocalAttractionModel> cList) {
        this.placeList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        LocalAttractionModel ci = placeList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
//        final boolean setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);

//
//        if(setData){
        contactViewHolder.tvPlaceTitle.setText(ci.place_title_np);
        contactViewHolder.tvPlaceDesc.setText(ci.plaece_desc_np);
        contactViewHolder.tvPlaceDist.setText(ci.district_name_np);
        contactViewHolder.tvPlaceAddress.setText(ci.address_name_np);

        String Img_Thumb_Url = ci.mThumbnail;

        if (Img_Thumb_Url != null) {
            Glide.with(context)
                    .load(Img_Thumb_Url)
                    .thumbnail(0.5f)
                    .override(60, 60)
                    .into(contactViewHolder.imageViewThumb);
        }

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_attraction_row, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onClick(View v) {

    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvPlaceTitle, tvPlaceDesc, tvPlaceDist, tvPlaceAddress, tvThumbUrl;
        protected ImageView imageViewThumb, imageViewDetail;
        String img_Url;

        public ContactViewHolder(View v) {
            super(v);
            tvPlaceTitle = (TextView) v.findViewById(R.id.attraction_title);
            tvPlaceDesc = (TextView) v.findViewById(R.id.attraction_detail);
            tvPlaceDist = (TextView) v.findViewById(R.id.attraction_dist);
            tvPlaceAddress = (TextView) v.findViewById(R.id.attraction_address);
//            imageView = (ImageView) v.findViewById(R.id.project_imageView);
            imageViewThumb = (ImageView) v.findViewById(R.id.place_imageView);


        }
    }

}


