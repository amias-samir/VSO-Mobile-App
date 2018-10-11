package np.com.naxa.vso.firebase;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import np.com.naxa.vso.R;

public class MessageListAdapter extends BaseQuickAdapter<MessageHelper, BaseViewHolder> {

//    int messageCount = 0;

    public MessageListAdapter(int layoutResId, @Nullable List<MessageHelper> data) {
        super(layoutResId, data);

//        messageCount = data.size();
    }


    @Override
    protected void convert(final BaseViewHolder helper, final MessageHelper item) {

//        if(messageCount == 0){
//
//            final RelativeLayout relativeLayout = helper.getView(R.id.relative_reply);
//            relativeLayout.setVisibility(View.GONE);
//
//            final RelativeLayout relativeLayout1 = helper.getView(R.id.relative_send);
//            relativeLayout1.setVisibility(View.GONE);
//
//        }else {
            if(item.getMessage_type() == 0){
                helper.setText(R.id.question_txt, item.getMessage());
                helper.setText(R.id.message_date_send,item.getDate());
                helper.setText(R.id.message_time_send,item.getTime());

                final RelativeLayout relativeLayout = helper.getView(R.id.relative_reply);
                relativeLayout.setVisibility(View.GONE);

                final RelativeLayout relativeLayoutsend = helper.getView(R.id.relative_send);
                relativeLayoutsend.setVisibility(View.VISIBLE);

            }else if(item.getMessage_type() == 1){
                helper.setText(R.id.message_txt, item.getMessage());
                helper.setText(R.id.message_date,item.getDate());
                helper.setText(R.id.message_time,item.getTime());

                final RelativeLayout relativeLayout = helper.getView(R.id.relative_send);
                relativeLayout.setVisibility(View.GONE);

                final RelativeLayout relativeLayoutreply = helper.getView(R.id.relative_reply);
                relativeLayoutreply.setVisibility(View.VISIBLE);
            }
//        }





    }


}