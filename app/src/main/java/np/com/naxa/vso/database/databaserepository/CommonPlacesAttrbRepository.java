package np.com.naxa.vso.database.databaserepository;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import np.com.naxa.vso.database.VsoRoomDatabase;
import np.com.naxa.vso.database.dao.CommonPlacesAttrbDao;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;

/**
 * Created by samir on 5/08/2018.
 */

public class CommonPlacesAttrbRepository {

    private static final String TAG = "CommonPlacesAttrb";

    public static long rowID;

    public static ArrayList<Long> pID = new ArrayList<>();

    private CommonPlacesAttrbDao mCommonPlacesAttrbDao;
    private LiveData<List<CommonPlacesAttrb>> mAllCommonPlacesAttrb;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public CommonPlacesAttrbRepository(Application application) {
        VsoRoomDatabase db = VsoRoomDatabase.getDatabase(application);
        mCommonPlacesAttrbDao = db.commonPlacesAttrbDao();
        mAllCommonPlacesAttrb = mCommonPlacesAttrbDao.getFirstInsertedCommonPlaces();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<CommonPlacesAttrb>> getAllCommonPlacesAttrb() {
        return mAllCommonPlacesAttrb;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (CommonPlacesAttrb commonPlacesAttrb) {
        Log.d("CommonPlacesRepository", "insert: "+ commonPlacesAttrb.getName());
        new CommonPlacesAttrbRepository.insertAsyncTask(mCommonPlacesAttrbDao).execute(commonPlacesAttrb);
    }

    private static class insertAsyncTask extends AsyncTask<CommonPlacesAttrb, Void, Void> {

        private CommonPlacesAttrbDao mAsyncTaskDao;

        insertAsyncTask(CommonPlacesAttrbDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CommonPlacesAttrb... params) {
            Log.d("ContactRepository", "doInBackground: "+ params[0].getName());
            rowID = mAsyncTaskDao.insert(params[0]);
            pID.add(rowID);

            Log.d(TAG, "saveHospitalData: id " + rowID);
//
//            pID.add(mAsyncTaskDao.insert(params[0]));

       return null;
        }
    }
}
