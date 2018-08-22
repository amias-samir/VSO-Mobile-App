package np.com.naxa.vso.sudur.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.District;

/**
 * Created by Nishon Tandukar on 27 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */


public class DistrictAdapter extends
        RecyclerView.Adapter<DistrictAdapter.ViewHolder> {


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        District contact = mdistricts.get(position);
        TextView textView = viewHolder.districtTextView;
        textView.setText(contact.getNpName());

    }

    @Override
    public int getItemCount() {
        return mdistricts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView districtTextView;


        public ViewHolder(final View itemView) {

            super(itemView);

            districtTextView = (TextView) itemView.findViewById(R.id.tv_district_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    private List<District> mdistricts;

    private Context mContext;

    public DistrictAdapter(Context context, List<District> districts) {
        mdistricts = districts;
        mContext = context;
    }


    private Context getContext() {
        return mContext;
    }


    @Override
    public DistrictAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.district_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
