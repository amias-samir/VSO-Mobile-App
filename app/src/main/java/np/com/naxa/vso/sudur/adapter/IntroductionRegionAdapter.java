package np.com.naxa.vso.sudur.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.sudur.model.IntroductionRegionModel;

/**
 * Created by Samir on 6/26/2017.
 */

public class IntroductionRegionAdapter extends RecyclerView.Adapter<IntroductionRegionAdapter.MyViewHolder> {

    private Context mContext;
    private List<IntroductionRegionModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, value;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.intro_item_title);
            value = (TextView) view.findViewById(R.id.intro_item_value);

        }
    }


    public IntroductionRegionAdapter(Context mContext, List<IntroductionRegionModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_introduction_region, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        IntroductionRegionModel album = albumList.get(position);
        holder.title.setText(album.getTitle_np());
        holder.value.setText(": " + album.getValue_np());

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }
}