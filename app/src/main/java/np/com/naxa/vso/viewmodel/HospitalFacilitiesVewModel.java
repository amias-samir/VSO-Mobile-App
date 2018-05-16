package np.com.naxa.vso.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import np.com.naxa.vso.database.databaserepository.HospitalFacilitiesRepository;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;


/**
 * Created by samir on 5/08/2018.
 */

public class HospitalFacilitiesVewModel extends AndroidViewModel {
    private HospitalFacilitiesRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<HospitalFacilities>> mAllHospitalFacilities;
    private LiveData<List<HospitalFacilities>> mAllFilteredHospitalFacilities;
    private LiveData<List<String>> mAllBedCapacityList;

    public HospitalFacilitiesVewModel(Application application) {
        super(application);
        mRepository = new HospitalFacilitiesRepository(application);

        mAllHospitalFacilities = mRepository.getAllHospitalFacilities();
        mAllBedCapacityList = mRepository.getmAllBedCapacityList();
    }
    public LiveData<List<HospitalFacilities>> getmAllHospitalFacilities() {
        return mAllHospitalFacilities;
    }

    public LiveData<List<String>> getmAllBedCapacityList() {
        return mAllBedCapacityList;
    }
    public void insert(HospitalFacilities hospitalFacilities) {
        Log.d("VIewholder", "insert: " + hospitalFacilities.getContact_no());
        mRepository.insert(hospitalFacilities);
    }


    public LiveData<List<HospitalFacilities>> getFilteredList(String ward, String hospital_type, String bedCapacity, String available_facilities,
                                                              String building_structure, String excavation_plans) {
        mAllFilteredHospitalFacilities = mRepository.getAllFilteredHospitalFacilities(ward, hospital_type, bedCapacity, available_facilities, building_structure, excavation_plans);

        return mAllFilteredHospitalFacilities;
    }


}