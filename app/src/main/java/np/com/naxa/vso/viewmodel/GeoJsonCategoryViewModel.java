package np.com.naxa.vso.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import io.reactivex.Maybe;
import np.com.naxa.vso.database.databaserepository.GeoJsonCategoryRepository;
import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;

public class GeoJsonCategoryViewModel extends AndroidViewModel {
    private GeoJsonCategoryRepository mRepository;

    private LiveData<List<GeoJsonCategoryEntity>> mAllGeoJsonCategoryEntity;
    private Maybe<List<GeoJsonCategoryEntity>> mSpecificTypeGeoJsonCategoryEntity;

    public GeoJsonCategoryViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonCategoryRepository(application);

        mAllGeoJsonCategoryEntity = mRepository.getAllGeoJsonCategoryEntity();
    }
    
    public LiveData<List<GeoJsonCategoryEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity; }

    public Maybe<List<GeoJsonCategoryEntity>> getAllGeoJsonCategoryEntityByType(String category_type) {
        mSpecificTypeGeoJsonCategoryEntity = mRepository.getSpecificTypeListGeoJsonCategoryEntity(category_type);
        return mSpecificTypeGeoJsonCategoryEntity;
    }

    public void insert(GeoJsonCategoryEntity geoJsonCategoryEntity) {
        mRepository.insert(geoJsonCategoryEntity);

    }
}
