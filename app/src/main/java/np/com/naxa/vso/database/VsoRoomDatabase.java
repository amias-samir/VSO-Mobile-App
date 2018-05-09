package np.com.naxa.vso.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import np.com.naxa.vso.database.dao.CommonPlacesAttrbDao;
import np.com.naxa.vso.database.dao.ContactDao;
import np.com.naxa.vso.database.dao.EducationalInstitutesDao;
import np.com.naxa.vso.database.dao.HospitalFacilitiesDao;
import np.com.naxa.vso.database.dao.OpenSpaceDao;
import np.com.naxa.vso.database.entity.CommonPlacesAttrb;
import np.com.naxa.vso.database.entity.Contact;
import np.com.naxa.vso.database.entity.EducationalInstitutes;
import np.com.naxa.vso.database.entity.HospitalFacilities;
import np.com.naxa.vso.database.entity.OpenSpace;

/**
 * Created by samir on 4/22/2018.
 */

@Database(entities = {Contact.class, OpenSpace.class, CommonPlacesAttrb.class, HospitalFacilities.class, EducationalInstitutes.class
}, version = 2, exportSchema = false)

public abstract class VsoRoomDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public abstract OpenSpaceDao openSpaceDao();
    public abstract CommonPlacesAttrbDao commonPlacesAttrbDao();
    public abstract HospitalFacilitiesDao hospitalFacilitiesDao();
    public abstract EducationalInstitutesDao educationalInstitutesDao();

    private static VsoRoomDatabase INSTANCE;

    public static VsoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VsoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VsoRoomDatabase.class, "vso_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ContactDao mContactDao;
        private final OpenSpaceDao mOpenSpaceDao;
        private final CommonPlacesAttrbDao mCommonPlacesAttrbDao;
        private final HospitalFacilitiesDao mHospitalFacilitiesDao;
        private final EducationalInstitutesDao mEducationalInstitutesDao;

        PopulateDbAsync(VsoRoomDatabase db) {
            mContactDao = db.contactDao();
            mOpenSpaceDao = db.openSpaceDao();
            mCommonPlacesAttrbDao = db.commonPlacesAttrbDao();
            mHospitalFacilitiesDao = db.hospitalFacilitiesDao();
            mEducationalInstitutesDao = db.educationalInstitutesDao();

        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//            mContactDao.deleteAll();
//            mOpenSpaceDao.deleteAll();
            mCommonPlacesAttrbDao.deleteAll();
            insertContact();
            return null;
        }

        private void insertContact() {
            Contact contact = new Contact("samir", "dangal", 40);
            mContactDao.insert(contact);
            contact = new Contact("nishon", "tandukar", 40);
            mContactDao.insert(contact);
        }
    }

}
