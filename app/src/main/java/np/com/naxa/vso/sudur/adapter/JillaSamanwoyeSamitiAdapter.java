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
import np.com.naxa.vso.sudur.model.Local_Level_Representative_Model;

/**
 * Created by samir on 6/26/2017.
 */

public class JillaSamanwoyeSamitiAdapter extends RecyclerView.Adapter<JillaSamanwoyeSamitiAdapter.ContactViewHolder> {

    private List<Local_Level_Representative_Model> colorList;
    Context context;

    private int[] flag = {
            R.drawable.flag_1
            , R.drawable.flag_2
            , R.drawable.flag_3
            , R.drawable.flag_4
    };

    public JillaSamanwoyeSamitiAdapter(Context context, List<Local_Level_Representative_Model> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(JillaSamanwoyeSamitiAdapter.ContactViewHolder contactViewHolder, int i) {
        Local_Level_Representative_Model ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
        contactViewHolder.rNameHead.setText(": " + ci.get_palika_head_name_np());
//        contactViewHolder.rHeadContactNo.setText(": " + ci.get_head_contact_no_np());
        contactViewHolder.rNameSubHead.setText(": " + ci.get_palika_subhead_name_np());
//        contactViewHolder.rSubHeadContactNo.setText(": " + ci.get_subhead_contact_no_np());

//        contactViewHolder.thumbImage.setImageResource(flag[i]);
    }

    @Override
    public JillaSamanwoyeSamitiAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_jilla_samanwoye_samiti, viewGroup, false);

        return new JillaSamanwoyeSamitiAdapter.ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView rNameHead, rNameSubHead, rHeadContactNo, rSubHeadContactNo, rPalikaPostHead, rPalikaPostSubHead;
        private ImageView thumbImage;

        public ContactViewHolder(View v) {
            super(v);
//            thumbImage = (ImageView) v.findViewById(R.id.img_thumbnail);
            rNameHead = (TextView) v.findViewById(R.id.textView_representative_headname);
            rHeadContactNo = (TextView) v.findViewById(R.id.textView_contact_no_head);
//            rPalikaNameHead = (TextView) v.findViewById(R.id.textView_palika_name_head);
            rPalikaPostHead = (TextView) v.findViewById(R.id.textView_head_post);

            rNameSubHead = (TextView) v.findViewById(R.id.textView_representative_subhead_name);
            rSubHeadContactNo = (TextView) v.findViewById(R.id.textView_contact_no_subhead);
//            rPalikaNameSubHead = (TextView) v.findViewById(R.id.textView_palika_name_subhead);
            rPalikaPostSubHead = (TextView) v.findViewById(R.id.textView_subhead_post);
//            rContact = (TextView) v.findViewById(R.id.textView_contact);
        }
    }
}
