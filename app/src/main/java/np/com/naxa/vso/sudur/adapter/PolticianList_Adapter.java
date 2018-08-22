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
import np.com.naxa.vso.sudur.model.Poltician_List_Model;

/**
 * Created by User on 11/6/2016.
 */

public class PolticianList_Adapter extends RecyclerView.Adapter<PolticianList_Adapter.ContactViewHolder> {

    private List<Poltician_List_Model> colorList;
    Context context;

    public PolticianList_Adapter(Context context, List<Poltician_List_Model> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(PolticianList_Adapter.ContactViewHolder contactViewHolder, int i) {
        Poltician_List_Model ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);

        String Img_Thumb_Url = ci.mThumbnail;

        if(!Img_Thumb_Url.equals("") && Img_Thumb_Url != null) {
            contactViewHolder.pName.setText(ci.poltician_name_np);
            contactViewHolder.pParty.setText(ci.poltical_party_name_np);
            contactViewHolder.pContact.setText(ci.poltician_contact_no_np);
            contactViewHolder.pElection_area.setText(ci.poltician_election_area_np);
            Glide.with(context)
                    .load(Img_Thumb_Url)
                    .thumbnail(0.5f)
                    .override(90, 90)
                    .into(contactViewHolder.imageView);
        }
        else {
            contactViewHolder.pName.setText(ci.poltician_name_np);
            contactViewHolder.pParty.setText(ci.poltical_party_name_np);
            contactViewHolder.pContact.setText(ci.poltician_contact_no_np);
            contactViewHolder.pElection_area.setText(ci.poltician_election_area_np);
            Glide.with(context)
                    .load(R.drawable.ic_potitician)
                    .thumbnail(0.5f)
                    .override(90, 90)
                    .into(contactViewHolder.imageView);
        }

    }

    @Override
    public PolticianList_Adapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_poltician_list, viewGroup, false);

        return new PolticianList_Adapter.ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView pName, pParty, pContact, pElection_area;
        protected ImageView imageView;

        public ContactViewHolder(View v) {
            super(v);
            pName = (TextView) v.findViewById(R.id.textView_poltician_name);
            pParty = (TextView) v.findViewById(R.id.textView_poltical_party);
            pContact = (TextView) v.findViewById(R.id.textView_poltician_contact);
            pElection_area = (TextView) v.findViewById(R.id.textView_poltician_election_area);
            imageView = (ImageView) v.findViewById(R.id.img_thumbnail);

        }
    }

}
