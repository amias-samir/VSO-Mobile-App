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
import np.com.naxa.vso.sudur.model.District;

/**
 * Created by susan on 6/26/2017.
 */

public class DistrictList_Adapter extends RecyclerView.Adapter<DistrictList_Adapter.ContactViewHolder> {

    private List<District> colorList;
    Context context;

    public DistrictList_Adapter(Context context, List<District> cList) {
        this.colorList = cList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    @Override
    public void onBindViewHolder(DistrictList_Adapter.ContactViewHolder contactViewHolder, int i) {
        District ci = colorList.get(i);
        final SharedPreferences wmbPreference = PreferenceManager
                .getDefaultSharedPreferences(context);
            contactViewHolder.pName.setText(ci.getNpName());
    }

    @Override
    public DistrictList_Adapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.district_items, viewGroup, false);

        return new DistrictList_Adapter.ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView pName;

        public ContactViewHolder(View v) {
            super(v);
            pName = (TextView) v.findViewById(R.id.tv_district_name);
        }
    }
}
