package np.com.naxa.vso.database.databaserepository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import np.com.naxa.vso.database.VsoRoomDatabase;
import np.com.naxa.vso.database.dao.HospitalFacilitiesDao;
import np.com.naxa.vso.database.entity.HospitalFacilities;


/**
 * Created by samir on 5/08/2018.
 */

public class HospitalFacilitiesRepository {

    private HospitalFacilitiesDao mHospitalFacilitiesDao;
    private LiveData<List<HospitalFacilities>> mAllHospitalFacilities;
    private LiveData<List<HospitalFacilities>> mAllFilteredHospitalFacilities;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public HospitalFacilitiesRepository(Application application) {
        VsoRoomDatabase db = VsoRoomDatabase.getDatabase(application);
        mHospitalFacilitiesDao = db.hospitalFacilitiesDao();
        mAllHospitalFacilities = mHospitalFacilitiesDao.getFirstInsertedHospital();
//        mAllFilteredHospitalFacilities = mHospitalFacilitiesDao.getAllFilteredList("", "");
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<HospitalFacilities>> getAllHospitalFacilities() {
        return mAllHospitalFacilities;
    }

    public LiveData<List<HospitalFacilities>> getAllFilteredHospitalFacilities(String ward, String hospital_type, String bed_capacity,
                                                                               String building_structure, String available_facilities, String excavation_plans) {

        mAllFilteredHospitalFacilities = mHospitalFacilitiesDao.getAllFilteredList(ward, hospital_type, bed_capacity, building_structure, available_facilities, excavation_plans);
        return mAllFilteredHospitalFacilities;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(HospitalFacilities hospitalFacilities) {
        Log.d("HospitalRepository", "insert: " + hospitalFacilities.getContact_no());
        mHospitalFacilitiesDao.insert(hospitalFacilities);
//        new HospitalFacilitiesRepository.insertAsyncTask(mHospitalFacilitiesDao).execute(hospitalFacilities);
    }

    private static class insertAsyncTask extends AsyncTask<HospitalFacilities, Void, Void> {

        private HospitalFacilitiesDao mAsyncTaskDao;

        insertAsyncTask(HospitalFacilitiesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final HospitalFacilities... params) {
            Log.d("HospitalRepository", "doInBackground: " + params[0].getContact_no());
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
