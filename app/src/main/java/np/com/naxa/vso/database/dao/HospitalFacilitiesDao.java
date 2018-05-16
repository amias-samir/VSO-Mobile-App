package np.com.naxa.vso.database.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import np.com.naxa.vso.database.entity.HospitalFacilities;
import retrofit2.http.Url;


/**
 * Created by samir on 5/08/2018.
 */

@Dao
public interface HospitalFacilitiesDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from hospital_facilities ORDER BY hid ASC")
    LiveData<List<HospitalFacilities>> getFirstInsertedHospital();

    @Query("SELECT DISTINCT number_of_bed from hospital_facilities ")
    LiveData<List<String>> getBedCapacityList();

    @Query("SELECT * FROM hospital_facilities WHERE number_of_bed LIKE :ward OR structure LIKE :hospital_type OR number_of_bed LIKE :bedCapacity" +
            " AND structure LIKE :building_structure OR structure LIKE :available_facilities OR structure LIKE :excavation_plans")
    public abstract LiveData<List<HospitalFacilities>> getAllFilteredList(String ward, String hospital_type,  String bedCapacity, String building_structure,
                                                                          String available_facilities, String excavation_plans);

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HospitalFacilities hospitalFacilities);

    @Query("DELETE FROM hospital_facilities")
    void deleteAll();


}