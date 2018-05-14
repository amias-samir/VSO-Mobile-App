package np.com.naxa.vso.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import np.com.naxa.vso.database.databaserepository.CommonPlacesAttrbRepository;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;

/**
 * Created by samir on 5/08/2018.
 */

public class CommonPlacesAttribViewModel extends AndroidViewModel {
    private CommonPlacesAttrbRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<CommonPlacesAttrb>> mAllCommonPlacesAttrb;
    private List<CommonPlacesAttrb> placesAttrbList;

    public CommonPlacesAttribViewModel(Application application) {
        super(application);
        mRepository = new CommonPlacesAttrbRepository(application);
//        placesAttrbList = mRepository.getAllPlaces();
        mAllCommonPlacesAttrb = mRepository.getAllCommonPlacesAttrb();
    }

    //    contact
    public LiveData<List<CommonPlacesAttrb>> getmAllCommonPlacesAttrb() {
        return mAllCommonPlacesAttrb;
    }

    public Long insert(CommonPlacesAttrb commonPlacesAttrb) {
        long rowID = mRepository.insert(commonPlacesAttrb);
        return rowID;
    }

    public List<CommonPlacesAttrb> getAllCommonplaces() {
        return mRepository.getAllPlaces();
//        return placesAttrbList;
    }


}