package np.com.naxa.vso.home;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.home.model.CategoriesDetail;
import np.com.naxa.vso.home.model.MapMarkerItem;

public class CategoriesDetailAdapter extends BaseQuickAdapter<MapMarkerItem, BaseViewHolder> {

    public CategoriesDetailAdapter(int layoutResId, @Nullable List<MapMarkerItem> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MapMarkerItem item) {
        helper.setText(R.id.tv_name,item.getTitle())
                .setText(R.id.tv_location,item.getTitle())
                .setText(R.id.tv_desciption,item.getTitle());
    }
}
