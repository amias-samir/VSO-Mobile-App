package np.com.naxa.vso.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.home.model.CategoriesDetail;

public class CategoriesDetailAdapter extends BaseQuickAdapter<CategoriesDetail, BaseViewHolder> {

    public CategoriesDetailAdapter(int layoutResId, @Nullable List<CategoriesDetail> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CategoriesDetail item) {
        helper.setText(R.id.tv_name,item.getName())
                .setText(R.id.tv_location,item.getLocation())
                .setText(R.id.tv_desciption,item.getDesciption());
    }
}
