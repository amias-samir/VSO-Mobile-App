package np.com.naxa.vso.database.databaserepository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import np.com.naxa.vso.database.VsoRoomDatabase;
import np.com.naxa.vso.database.dao.ContactDao;
import np.com.naxa.vso.database.dao.GeoJsonCategoryDao;
import np.com.naxa.vso.database.entity.Contact;
import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;

public class GeoJsonCategoryRepository {

    private GeoJsonCategoryDao mGeoJsonCategoryDao;
    private LiveData<List<GeoJsonCategoryEntity>> mAllGeoJsonCategoryEntity;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public GeoJsonCategoryRepository(Application application) {
        VsoRoomDatabase db = VsoRoomDatabase.getDatabase(application);
        mGeoJsonCategoryDao = db.geoJsonCategoryDao();
        mAllGeoJsonCategoryEntity = mGeoJsonCategoryDao.getGeoJsonCategoryList();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<GeoJsonCategoryEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (GeoJsonCategoryEntity geoJsonCategoryEntity) {
        Observable.just(geoJsonCategoryEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<GeoJsonCategoryEntity>() {
            @Override
            public void onNext(GeoJsonCategoryEntity geoJsonCategoryEntity) {
                Log.d("GeoJsonCategoryEntity", "insert: "+ geoJsonCategoryEntity.getCategoryName());
                mGeoJsonCategoryDao.insert(geoJsonCategoryEntity);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }
}
