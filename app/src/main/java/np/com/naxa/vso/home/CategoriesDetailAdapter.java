package np.com.naxa.vso.home;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.home.model.CategoriesDetail;
import np.com.naxa.vso.home.model.MapMarkerItem;

public class CategoriesDetailAdapter extends BaseQuickAdapter<HospitalAndCommon, BaseViewHolder> {

    public CategoriesDetailAdapter(int layoutResId, @Nullable List<HospitalAndCommon> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, HospitalAndCommon item) {
        helper.setText(R.id.tv_name,item.getCommonPlacesAttrb().getName())
                .setText(R.id.tv_location, "")
                .setText(R.id.tv_desciption,item.getHospitalFacilities().getType());
    }
}
