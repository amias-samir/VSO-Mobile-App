package np.com.naxa.vso.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.vso.database.combinedentity.EducationAndCommon;
import np.com.naxa.vso.database.combinedentity.HospitalAndCommon;
import np.com.naxa.vso.database.entity.EducationalInstitutes;

/**
 * Created by samir on 5/08/2018.
 */

@Dao
public interface EducationalInstitutesDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from educational_institutes ORDER BY eid ASC")
    LiveData<List<EducationalInstitutes>> getFirstInsertedEducationalInstitutes();

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    void insert(EducationalInstitutes educationalInstitutes);

    @Query("DELETE FROM educational_institutes")
    void deleteAll();

    @Query("SELECT * FROM educational_institutes "
            + "INNER JOIN commonplacesattrb ON commonplacesattrb.uid = educational_institutes.fk_common_places")
    Flowable<List<EducationAndCommon>> getAllDetail();
}