package np.com.naxa.vso.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.databaserepository.GeoJsonListEntityRepository;
import np.com.naxa.vso.database.entity.GeoJsonListEntity;

public class GeoJsonListViewModel extends AndroidViewModel {
    private GeoJsonListEntityRepository mRepository;

    private LiveData<List<GeoJsonListEntity>> mAllGeoJsonListEntity;
    private LiveData<GeoJsonListEntity> mSpecifiedGeoJsonEntity;

    public GeoJsonListViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonListEntityRepository(application);

        mAllGeoJsonListEntity = mRepository.getAllGeoJsonListEntity();
    }

    public LiveData<List<GeoJsonListEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonListEntity;
    }

    public LiveData<GeoJsonListEntity> getmSpecificGeoJsonEntity(String category_table) {
        mSpecifiedGeoJsonEntity = mRepository.getSpecificGeoJsonEntity(category_table);
        return mSpecifiedGeoJsonEntity;
    }

    public void insert(GeoJsonListEntity geoJsonListEntity) {
        mRepository.insert(geoJsonListEntity);

    }
}
