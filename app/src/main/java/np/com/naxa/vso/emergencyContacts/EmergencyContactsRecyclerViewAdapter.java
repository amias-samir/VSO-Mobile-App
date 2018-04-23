package np.com.naxa.vso.emergencyContacts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Collections;
import java.util.List;

import np.com.naxa.vso.R;

public class EmergencyContactsRecyclerViewAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final int TYPE_0_HEADING = 0;
    private static final int TYPE_1_DETAIL = 1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public EmergencyContactsRecyclerViewAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_0_HEADING, R.layout.item_emergency_contact_heading);
        addItemType(TYPE_1_DETAIL, R.layout.item_emergency_contacts_detail);
    }


    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_0_HEADING:

                break;
            case TYPE_1_DETAIL:

                break;
        }
    }
}