package np.com.naxa.vso.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.databaserepository.MessageHelperRepository;
import np.com.naxa.vso.firebase.MessageHelper;

public class MessageHelperViewModel extends AndroidViewModel {
    private MessageHelperRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private Flowable<List<MessageHelper>> mAllMessageList;

    public MessageHelperViewModel(Application application) {
        super(application);
        mRepository = new MessageHelperRepository(application);

        mAllMessageList = mRepository.getAllMessageList();
    }
    //    contact
    public Flowable<List<MessageHelper>> getAllMessageList() { return mAllMessageList; }

    public void insert(MessageHelper messageHelper) {
        Log.d("VIewholder", "insert: "+messageHelper.getMessage());
        mRepository.insert(messageHelper); }
}