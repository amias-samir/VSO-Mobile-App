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
import np.com.naxa.vso.sudur.model.NagarpalikaBudget_Model;

/**
 * Created by Samir on 6/1/2017.
 */

public class NagarpalikaBudget_Adapter extends RecyclerView.Adapter<NagarpalikaBudget_Adapter.ContactViewHolder> {

    private List<NagarpalikaBudget_Model> colorList;
    Context context;

    public NagarpalikaBudget_Adapter(Context context, List<NagarpalikaBudget_Model> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(NagarpalikaBudget_Adapter.ContactViewHolder contactViewHolder, int i) {
        NagarpalikaBudget_Model ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
//        final boolean setData = wmbPreference.getBoolean("SET_ENGLISH_ON", true);
//
//        if (setData) {
        contactViewHolder.tvNagarName.setText(ci.getNagar_title_np());
        contactViewHolder.tvNagarBudget.setText("रू. "+ci.getNagar_budget_amount_en());;

//        } else {

        //}

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview_nagarpalika_budget, viewGroup, false);

        return new ContactViewHolder(itemView);
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvNagarName, tvNagarBudget;

        public ContactViewHolder(View v) {
            super(v);
            tvNagarName = (TextView) v.findViewById(R.id.nagarpalika_name);
            tvNagarBudget = (TextView) v.findViewById(R.id.nagarpalika_budget);


        }
    }

}
