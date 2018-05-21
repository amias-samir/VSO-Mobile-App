package np.com.naxa.vso.database.databaserepository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.VsoRoomDatabase;
import np.com.naxa.vso.database.combinedentity.EducationAndCommon;
import np.com.naxa.vso.database.dao.EducationalInstitutesDao;
import np.com.naxa.vso.database.entity.EducationalInstitutes;


/**
 * Created by samir on 5/08/2018.
 */

public class EducationalInstitutesRepository {
    private EducationalInstitutesDao mEducationalInstitutesDao;
    private LiveData<List<EducationalInstitutes>> mAllEducationalInstitutes;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public EducationalInstitutesRepository(Application application) {
        VsoRoomDatabase db = VsoRoomDatabase.getDatabase(application);
        mEducationalInstitutesDao = db.educationalInstitutesDao();
        mAllEducationalInstitutes = mEducationalInstitutesDao.getFirstInsertedEducationalInstitutes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<EducationalInstitutes>> getAllEducationalInstitutes() {
        return mAllEducationalInstitutes;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (EducationalInstitutes educationalInstitutes) {
        Log.d("EducationalRepository", "insert: "+ educationalInstitutes.getContact_no());
        mEducationalInstitutesDao.insert(educationalInstitutes);
//        new EducationalInstitutesRepository.insertAsyncTask(mEducationalInstitutesDao).execute(educationalInstitutes);
    }

    public Flowable<List<EducationAndCommon>> getAll(){
        return mEducationalInstitutesDao.getAllDetail();
    }

    private static class insertAsyncTask extends AsyncTask<EducationalInstitutes, Void, Void> {

        private EducationalInstitutesDao mAsyncTaskDao;

        insertAsyncTask(EducationalInstitutesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EducationalInstitutes... params) {
            Log.d("EducationalRepository", "doInBackground: "+ params[0].getContact_no());
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
