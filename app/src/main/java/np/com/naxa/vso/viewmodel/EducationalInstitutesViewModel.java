package np.com.naxa.vso.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import np.com.naxa.vso.database.databaserepository.EducationalInstitutesRepository;
import np.com.naxa.vso.database.entity.EducationalInstitutes;

/**
 * Created by samir on 5/08/2018.
 */

public class EducationalInstitutesViewModel  extends AndroidViewModel {
    private EducationalInstitutesRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<EducationalInstitutes>> mAllEducationalInstitutes;

    public EducationalInstitutesViewModel(Application application) {
        super(application);
        mRepository = new EducationalInstitutesRepository(application);

        mAllEducationalInstitutes = mRepository.getAllEducationalInstitutes();
    }
    //    contact
    public LiveData<List<EducationalInstitutes>> getmAllCommonPlacesAttrb() { return mAllEducationalInstitutes; }

    public void insert(EducationalInstitutes educationalInstitutes) {
        Log.d("VIewholder", "insert: "+educationalInstitutes.getContact_no());
        mRepository.insert(educationalInstitutes); }



}