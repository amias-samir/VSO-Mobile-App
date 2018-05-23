package np.com.naxa.vso.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.combinedentity.OpenAndCommon;
import np.com.naxa.vso.database.entity.OpenSpace;

/**
 * Created by samir on 4/23/2018.
 */

@Dao
public interface OpenSpaceDao {
    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from OpenSpace_table")
    LiveData<List<OpenSpace>> getAlphabetizedWords();

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    void insert(OpenSpace openSpace);

    @Insert
    void insert(OpenSpace... openSpace);

    @Query("DELETE FROM OpenSpace_table")
    void deleteAll();

    @Query("SELECT * FROM openspace_table "
            + "INNER JOIN commonplacesattrb ON commonplacesattrb.uid = openspace_table.fk_common_places")
    Flowable<List<OpenAndCommon>> getAllOpenSpacelist();

}
