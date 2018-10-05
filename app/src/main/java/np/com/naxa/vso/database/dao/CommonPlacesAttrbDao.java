package np.com.naxa.vso.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.GeoJsonCategoryEntity;

/**
 * Created by samir on 5/08/2018.
 */

@Dao
public interface CommonPlacesAttrbDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from commonplacesattrb ORDER BY uid ASC")
    Flowable<List<CommonPlacesAttrb>> getFirstInsertedCommonPlaces();

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CommonPlacesAttrb commonPlacesAttrb);

    @Insert
    Long insertWithReturnID(CommonPlacesAttrb commonPlacesAttrb);

    @Query("DELETE FROM commonplacesattrb")
    void deleteAll();

    @Query("SELECT * from commonplacesattrb WHERE name LIKE '%'||:value||'%'")
    Flowable<List<CommonPlacesAttrb>> getPlacesContaining(String value);

    @Query("SELECT * from commonplacesattrb WHERE places_type =:type")
    Flowable<List<CommonPlacesAttrb>> getPlaceByType(String type);

}
