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
import np.com.naxa.vso.sudur.model.PolticalParties_List_Model;

/**
 * Created by Samir on 11/6/2016.
 */

public class PolticalParties_List_Adapter extends RecyclerView.Adapter<PolticalParties_List_Adapter.ContactViewHolder> {

    private List<PolticalParties_List_Model> colorList;
    Context context;

    public PolticalParties_List_Adapter(Context context, List<PolticalParties_List_Model> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(PolticalParties_List_Adapter.ContactViewHolder contactViewHolder, int i) {
        PolticalParties_List_Model ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);

        String Img_Thumb_Url = ci.mThumbnail;

        if(!Img_Thumb_Url.equals("") && Img_Thumb_Url != null) {
            Glide.with(context)
                    .load(Img_Thumb_Url)
                    .thumbnail(0.5f)
                    .override(90, 90)
                    .into(contactViewHolder.imageView);
            contactViewHolder.pName.setText(ci.poltical_party_name_np);
            contactViewHolder.imageViewError.setVisibility(View.GONE);
        }else {
            contactViewHolder.imageViewError.setVisibility(View.VISIBLE);
            contactViewHolder.pName.setText(ci.poltical_party_name_np);
        }

    }

    @Override
    public PolticalParties_List_Adapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_poltical_parties_list, viewGroup, false);

        return new PolticalParties_List_Adapter.ContactViewHolder(itemView);
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView pName, pParty, pContact, pElection_area;
        protected ImageView imageView, imageViewError;

        public ContactViewHolder(View v) {
            super(v);
            pName = (TextView) v.findViewById(R.id.textView_poltical_party_name);
//            pParty = (TextView) v.findViewById(R.id.textView_poltical_party);
//            pContact = (TextView) v.findViewById(R.id.textView_poltician_contact);
//            pElection_area = (TextView) v.findViewById(R.id.textView_poltician_election_area);
            imageView = (ImageView) v.findViewById(R.id.img_thumbnail);
            imageViewError = (ImageView) v.findViewById(R.id.image_error);

        }
    }

}
