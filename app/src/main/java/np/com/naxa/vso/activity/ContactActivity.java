package np.com.naxa.vso.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.R;
import np.com.naxa.vso.database.entity.Contact;
import np.com.naxa.vso.viewmodel.ContactViewModel;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "ContactActivity";

    private ContactViewModel mContactViewModel;

    List<Contact> contactList = new ArrayList<Contact>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        // Get a new or existing ViewModel from the ViewModelProvider.
        mContactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mContactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts) {
                // Update the cached copy of the words in the adapter.
//                adapter.setWords(words);
                Log.d(TAG, "onChanged: insert " +"one more new data inserted");
            }
        });

        saveViaRoom();

    }

    public void saveViaRoom(){
        Contact contact = new Contact();
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, "saveViaRoom: insert inserting "+i);
            contact.setFirstName( "Name_" + i);
            contact.setLastName("Lastname_" + i);
            contact.setAge(41 + i);
            mContactViewModel.insert(contact);
        }



    }
}
