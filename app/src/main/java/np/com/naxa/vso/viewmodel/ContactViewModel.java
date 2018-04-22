package np.com.naxa.vso.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import np.com.naxa.vso.database.entity.Contact;
import np.com.naxa.vso.database.databaserepository.ContactRepository;

/**
 * Created by samir on 4/22/2018.
 */

public class ContactViewModel extends AndroidViewModel {

    private ContactRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Contact>> mAllContacts;

    public ContactViewModel (Application application) {
        super(application);
        mRepository = new ContactRepository(application);
        mAllContacts = mRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() { return mAllContacts; }

    public void insert(Contact contact) {
        Log.d("VIewholder", "insert: "+contact.getFirstName());
        mRepository.insert(contact); }
}