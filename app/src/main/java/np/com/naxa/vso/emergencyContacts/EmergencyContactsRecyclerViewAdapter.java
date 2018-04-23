package np.com.naxa.vso.emergencyContacts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;

public class EmergencyContactsRecyclerViewAdapter extends BaseQuickAdapter<EmergencyContactsPojo, BaseViewHolder> {


    public EmergencyContactsRecyclerViewAdapter(@Nullable List<EmergencyContactsPojo> data) {
        super(R.layout.item_emergency_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmergencyContactsPojo item) {
        helper.setText(R.id.tv_contact_name, item.getName())
                .setText(R.id.tv_contact_number, item.getPhone())
                .addOnClickListener(R.id.ib_contact_call);
    }
}
