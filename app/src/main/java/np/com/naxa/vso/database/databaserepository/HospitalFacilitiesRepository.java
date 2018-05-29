package np.com.naxa.vso.database.databaserepository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.VsoRoomDatabase;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.dao.HospitalFacilitiesDao;
import np.com.naxa.vso.database.entity.HospitalFacilities;


/**
 * Created by samir on 5/08/2018.
 */

public class HospitalFacilitiesRepository {

    private HospitalFacilitiesDao mHospitalFacilitiesDao;
    private LiveData<List<HospitalFacilities>> mAllHospitalFacilities;
    private LiveData<List<HospitalAndCommon>> mAllFilteredHospitalFacilities;
    private LiveData<List<String>> mAllDistinctValuesList;

    private LiveData<List<String>> mAllDistinctTypeList;
    private LiveData<List<String>> mAllDistinctStructureTypeList;
    private LiveData<List<String>> mAllDistinctBedCapacityList;
    private LiveData<List<String>> mAllDistinctEvacuationPlanList;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public HospitalFacilitiesRepository(Application application) {
        VsoRoomDatabase db = VsoRoomDatabase.getDatabase(application);
        mHospitalFacilitiesDao = db.hospitalFacilitiesDao();
        mAllHospitalFacilities = mHospitalFacilitiesDao.getFirstInsertedHospital();
        mAllDistinctTypeList = mHospitalFacilitiesDao.getDistinctTypeList();

    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<HospitalFacilities>> getAllHospitalFacilities() {
        return mAllHospitalFacilities;
    }

    public LiveData<List<String>> getmAllBedCapacityList() {
        mAllDistinctBedCapacityList = mHospitalFacilitiesDao.getDistinctBedCapacityList();

        return mAllDistinctBedCapacityList;
    }

    public LiveData<List<String>> getmAllTypeList() {
        mAllDistinctTypeList = mHospitalFacilitiesDao.getDistinctTypeList();
        return mAllDistinctTypeList;
    }

    public LiveData<List<String>> getmAllStructureTypeList() {
        mAllDistinctStructureTypeList = mHospitalFacilitiesDao.getDistinctStructureTypeList();

        return mAllDistinctStructureTypeList;
    }

    public LiveData<List<String>> getmAllEvacuationPlanList() {
        mAllDistinctEvacuationPlanList = mHospitalFacilitiesDao.getDistinctEvacuationPlanList();

        return mAllDistinctEvacuationPlanList;
    }


    public LiveData<List<String>> getDistinctValuesFromColumn(String columnName) {
        Log.d("repo", "getDistinctValuesFromColumn: " + columnName);
        mAllDistinctValuesList = mHospitalFacilitiesDao.getDistinctValuesFromColumn(columnName);
//        Log.d("", "getDistinctValuesFromColumn: value "+mAllDistinctValuesList.getValue().get(1));
        return mAllDistinctValuesList;
    }


    public LiveData<List<HospitalAndCommon>> getAllFilteredHospitalFacilities(String ward, String hospital_type, String bedCapacity,
                                                                              String building_structure, String available_facilities, String excavation_plans,
                                                                              String medicine_stock, String blood_stock, String disaster_plan, String first_aid,
                                                                              String earthquake_resilient) {

        mAllFilteredHospitalFacilities = mHospitalFacilitiesDao.getAllFilteredList(ward, hospital_type, bedCapacity, building_structure,
                available_facilities, excavation_plans, medicine_stock, blood_stock, disaster_plan, first_aid, earthquake_resilient);
        return mAllFilteredHospitalFacilities;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(HospitalFacilities hospitalFacilities) {
        Log.d("HospitalRepository", "insert: " + hospitalFacilities.getContact_Number());
        mHospitalFacilitiesDao.insert(hospitalFacilities);
    }

    public Flowable<List<HospitalAndCommon>> getAllDetail() {
        return mHospitalFacilitiesDao.getAllHospitalDetailList();
    }


}
