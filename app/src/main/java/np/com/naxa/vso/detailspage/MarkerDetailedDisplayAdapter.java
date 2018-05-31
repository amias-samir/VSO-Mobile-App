package np.com.naxa.vso.detailspage;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;

public class MarkerDetailedDisplayAdapter extends BaseQuickAdapter<MarkerDetailsKeyValue, BaseViewHolder> {

    public MarkerDetailedDisplayAdapter(int layoutResId, @Nullable List<MarkerDetailsKeyValue> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MarkerDetailsKeyValue item) {
        helper.setText(R.id.tv_key_data,item.getKey())
                .setText(R.id.tv_value_data, ": "+item.getValue());
    }
}
