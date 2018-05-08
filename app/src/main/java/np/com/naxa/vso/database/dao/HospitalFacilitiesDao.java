package np.com.naxa.vso.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import np.com.naxa.vso.database.entity.HospitalFacilities;


/**
 * Created by samir on 5/08/2018.
 */

public interface HospitalFacilitiesDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from hospital_facilities ORDER BY hid ASC")
    LiveData<List<HospitalFacilities>> getFirstInsertedHospital();

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    void insert(HospitalFacilities hospitalFacilities);

    @Query("DELETE FROM hospital_facilities")
    void deleteAll();
}