package np.com.naxa.vso.firebase;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import np.com.naxa.vso.R;
import np.com.naxa.vso.activity.ReportActivity;
import np.com.naxa.vso.viewmodel.MessageHelperViewModel;

public class MessageActivity extends AppCompatActivity {

    Toolbar toolbar;
    @BindView(R.id.textViewNoMessage)
    TextView textViewNoMessage;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;

    MessageHelperViewModel messageHelperViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        if (VSOFirebaseMessagingService.notification.equals(true)) {
            Intent intent = new Intent(MessageActivity.this, MessageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


        messageHelperViewModel = ViewModelProviders.of(this).get(MessageHelperViewModel.class);

        initToolbar();

        setupListRecycler();

        getAndSetDataToAdapter();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_general);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(MessageActivity.this.getResources().getString(R.string.messageActivity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void setupListRecycler() {
        MessageListAdapter messageListAdapter = new MessageListAdapter(R.layout.inbox_list_row, null);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(messageListAdapter);
    }


    private void getAndSetDataToAdapter(){

        List<MessageHelper> messageHelperList = new ArrayList<MessageHelper>();

        messageHelperViewModel.getAllMessageList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<MessageHelper>>() {
                    @Override
                    public void onNext(List<MessageHelper> messageHelpers) {
                        Log.i("firebase", "onNext: message list size"+ messageHelpers.size());

                        messageHelperList.addAll(messageHelpers);
                        ((MessageListAdapter) myRecyclerView.getAdapter()).replaceData(messageHelperList);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
