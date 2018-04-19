package np.com.naxa.vso.home;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Random;

import np.com.naxa.vso.R;

public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {
    public SectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        MapDataCategory mapDataCategory = (MapDataCategory) item.t;
        switch (helper.getLayoutPosition() %
                2) {
            case 0:
                helper.setImageResource(R.id.iv, mapDataCategory.getImage());
                break;
            case 1:
                helper.setImageResource(R.id.iv, mapDataCategory.getImage());
                helper.addOnClickListener(R.id.card_view);
                break;

        }
        helper.setText(R.id.tv_title, mapDataCategory.getName());
        helper.setText(R.id.tv_subtitle, String.valueOf(new Random().nextInt(50) + 1));
    }
    @Override
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        helper.setText(R.id.header, item.header);




    }


}