package np.com.naxa.vso.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import np.com.naxa.vso.database.entity.CommonPlacesAttrb;


@Dao
public interface CommonPlacesAttrbDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from commonplacesattrb ORDER BY uid ASC")
    LiveData<List<CommonPlacesAttrb>> getAlphabetizedWords();

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    void insert(CommonPlacesAttrb commonPlacesAttrb);

    @Query("DELETE FROM commonplacesattrb")
    void deleteAll();
}
