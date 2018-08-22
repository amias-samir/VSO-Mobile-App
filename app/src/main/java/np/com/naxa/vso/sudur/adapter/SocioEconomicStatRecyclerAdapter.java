package np.com.naxa.vso.sudur.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.SosioEconomicStatModel;

/**
 * Created by Samir on 11/21/2016.
 */

public class SocioEconomicStatRecyclerAdapter extends RecyclerView.Adapter<SocioEconomicStatRecyclerAdapter.ContactViewHolder> {

    private List<SosioEconomicStatModel> colorList;
    Context context;

    public SocioEconomicStatRecyclerAdapter(Context context, List<SosioEconomicStatModel> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        SosioEconomicStatModel ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
//        final boolean setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);
//
//        if (setData) {
        contactViewHolder.statName.setText(ci.getStat_name_np());

//            contactViewHolder.vName.setText(ci.title_np);
//            contactViewHolder.vDesc.setText(ci.description_np);
//
//
//        } else {

        //}

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view_stat_row, viewGroup, false);

        return new ContactViewHolder(itemView);
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView statName;

        public ContactViewHolder(View v) {
            super(v);
            statName = (TextView) v.findViewById(R.id.stat_title);


        }
    }

}
