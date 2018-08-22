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



import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.INGO_NGO_Model;

/**
 * Created by susan on 6/26/2017.
 */

public class NGO_INGO_DevelopmentList_Adapter extends RecyclerView.Adapter<NGO_INGO_DevelopmentList_Adapter.ContactViewHolder> {

    private List<INGO_NGO_Model> colorList;
    Context context;

    public NGO_INGO_DevelopmentList_Adapter(Context context, List<INGO_NGO_Model> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        INGO_NGO_Model ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
            contactViewHolder.orgTitle.setText(ci.getName());
//            contactViewHolder.rDistrict.setText(ci.getDi());
            contactViewHolder.orgEmail.setText(ci.getEmail());
//            contactViewHolder.rContact.setText(ci.get_number());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.ngo_ingo_items, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView orgTitle, orgEmail;
        private ImageView thumbImage;
        public ContactViewHolder(View v) {
            super(v);
            thumbImage = (ImageView) v.findViewById(R.id.img_thumbnail);
            orgTitle = (TextView) v.findViewById(R.id.org_title);
//            rDistrict = (TextView) v.findViewById(R.id.textView_district);
            orgEmail = (TextView) v.findViewById(R.id.org_email);
//            rContact = (TextView) v.findViewById(R.id.textView_contact);
        }
    }
}
