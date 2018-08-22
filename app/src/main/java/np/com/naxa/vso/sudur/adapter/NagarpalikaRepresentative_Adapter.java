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
 * Created by susan on 6/26/2017.
 */

public class NagarpalikaRepresentative_Adapter extends RecyclerView.Adapter<NagarpalikaRepresentative_Adapter.ContactViewHolder> {

    private List<Local_Level_Representative_Model> colorList;
    Context context;

    private int[] flag = {
            R.drawable.flag_1
            , R.drawable.flag_2
            , R.drawable.flag_3
            , R.drawable.flag_4
    };

    public NagarpalikaRepresentative_Adapter(Context context, List<Local_Level_Representative_Model> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(NagarpalikaRepresentative_Adapter.ContactViewHolder contactViewHolder, int i) {
        Local_Level_Representative_Model ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
        contactViewHolder.rNameHead.setText(": " + ci.get_palika_head_name_np());
        contactViewHolder.rPalikaNameHead.setText(": " + ci.get_palika_name_np());
        contactViewHolder.rNameSubHead.setText(": " + ci.get_palika_subhead_name_np());
        contactViewHolder.rPalikaNameSubHead.setText(": " + ci.get_palika_name_np());
//        contactViewHolder.thumbImage.setImageResource(flag[i]);
    }

    @Override
    public NagarpalikaRepresentative_Adapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.nagarpalika_representative_items, viewGroup, false);

        return new NagarpalikaRepresentative_Adapter.ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView rNameHead, rNameSubHead, rPalikaNameHead, rPalikaNameSubHead, rPalikaPostHead, rPalikaPostSubHead;
        private ImageView thumbImage;

        public ContactViewHolder(View v) {
            super(v);
//            thumbImage = (ImageView) v.findViewById(R.id.img_thumbnail);
            rNameHead = (TextView) v.findViewById(R.id.textView_representative_headname);
            rPalikaNameHead = (TextView) v.findViewById(R.id.textView_palika_name_head);
            rPalikaPostHead = (TextView) v.findViewById(R.id.textView_head_post);

            rNameSubHead = (TextView) v.findViewById(R.id.textView_representative_subhead_name);
            rPalikaNameSubHead = (TextView) v.findViewById(R.id.textView_palika_name_subhead);
            rPalikaPostSubHead = (TextView) v.findViewById(R.id.textView_subhead_post);
//            rContact = (TextView) v.findViewById(R.id.textView_contact);
        }
    }
}
