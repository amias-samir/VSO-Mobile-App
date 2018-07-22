package np.com.naxa.vso.home;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.hospitalfilter.SortedHospitalItem;

public class CategoryListAdapter extends BaseQuickAdapter<CommonPlacesAttrb, BaseViewHolder> {

    public CategoryListAdapter(int layoutResId, @Nullable List<CommonPlacesAttrb> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CommonPlacesAttrb item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_location, item.getAddress())
                .setText(R.id.tv_desciption, item.getRemarks());
    }
}