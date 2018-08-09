package np.com.naxa.vso.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;

@Dao
public interface GeoJsonCategoryDao {

    @Query("SELECT * from GeoJsonCategoryEntity ORDER BY id ASC")
    LiveData<List<GeoJsonCategoryEntity>> getGeoJsonCategoryList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GeoJsonCategoryEntity geoJsonCategoryEntity);

    @Query("DELETE FROM GeoJsonCategoryEntity")
    void deleteAll();
}
