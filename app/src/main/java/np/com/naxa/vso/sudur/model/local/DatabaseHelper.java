package np.com.naxa.vso.sudur.model.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "fwdc.db";

    public static final String TABLE_BUSINESS_PLACES = "BUSINESS";

    private static final String KEY_BUSINESS_ID = "business_id";
    private static final String KEY_BUSINESS_NAME = "business_name";
    private static final String KEY_BUSINESS_TYPE = "business_type";
    private static final String KEY_BUSINESS_LAT = "business_lat";
    private static final String KEY_BUSINESS_LON = "business_lon";
    private static final String KEY_BUSINESS_PHOTO = "business_photo";
    private static final String KEY_BUSINESS_DESC = "business_desc";
    private static final String KEY_BUSINESS_ADDRS = "business_addrs";
    private static String KEY_LAST_MODIFIED_DATE_TIME = "last_modified_data_time";

    private static DatabaseHelper databaseHelper;


    public static DatabaseHelper getInstance(Context context) {


        if (databaseHelper != null) {
            return databaseHelper;
        }

        databaseHelper = new DatabaseHelper(context);
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BUSINESS_PLACES + "("
                + KEY_BUSINESS_ID + " INTEGER PRIMARY KEY,"
                + KEY_BUSINESS_NAME + " TEXT,"
                + KEY_BUSINESS_TYPE + " TEXT,"
                + KEY_BUSINESS_LAT + " TEXT,"
                + KEY_BUSINESS_LON + " TEXT,"
                + KEY_BUSINESS_PHOTO + " TEXT,"
                + KEY_BUSINESS_DESC + " TEXT,"
                + KEY_BUSINESS_ADDRS + " TEXT,"
                + KEY_LAST_MODIFIED_DATE_TIME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS_PLACES);
        onCreate(db);
    }

    public ArrayList<String> getBusinessCategories() {

        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(true, TABLE_BUSINESS_PLACES, new String[]{KEY_BUSINESS_TYPE}, null, null, KEY_BUSINESS_TYPE, null, null, null);

        try {

            cursor.moveToFirst();

            do {

                categories.add(cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_TYPE)));

            } while (cursor.moveToNext());

        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {

        } finally {
            cursor.close();
            db.close();
        }


        return categories;
    }


    public String getLastSyncDate(String tableName) {


        SQLiteDatabase db = this.getWritableDatabase();
        String datetime;
        String minimumDateTime = "0";

        Cursor cursor = db.query(tableName, new String[]{KEY_LAST_MODIFIED_DATE_TIME}
                , null, null, null, null, KEY_LAST_MODIFIED_DATE_TIME + " DESC ", "1");


        try {
            cursor.moveToFirst();
            datetime = cursor.getString(cursor.getColumnIndex(KEY_LAST_MODIFIED_DATE_TIME));
        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {
            datetime = minimumDateTime;
        } finally {
            cursor.close();
            db.close();
        }

        return datetime;
    }


    public ArrayList<Bussiness> getBusinessFromTypes(String type) {

        ArrayList<Bussiness> bussinesseslist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BUSINESS_PLACES, null, KEY_BUSINESS_TYPE + "=?", new String[]{type}, null, null, null, null);


        try {

            cursor.moveToFirst();

            do {
                Bussiness bussiness = new Bussiness();

                bussiness.setBusinessId(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_ID)));

                bussiness.setBusinessName(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_NAME)));

                bussiness.setBusinessAddress(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_ADDRS)));

                bussiness.setBusinessDescription(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_DESC)));

                bussiness.setLatitude(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_LAT)));

                bussiness.setLongitude(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_LON)));


                bussiness.setPhotoPath(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_PHOTO)));

                bussiness.setBusinessType(
                        cursor.getString(cursor.getColumnIndex(KEY_BUSINESS_TYPE)));

                bussiness.setLastModifiedDate(
                        cursor.getString(cursor.getColumnIndex(KEY_LAST_MODIFIED_DATE_TIME)));


                bussinesseslist.add(bussiness);

            } while (cursor.moveToNext());

        } catch (CursorIndexOutOfBoundsException | NullPointerException e) {

        } finally {
            cursor.close();
            db.close();
        }


        return bussinesseslist;

    }

    public void saveSyncedBussinesses(List<Bussiness> bussinesses) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (Bussiness bussiness : bussinesses) {


            if (bussiness.getIsDeleted() == 1) {
                db.delete(TABLE_BUSINESS_PLACES, KEY_BUSINESS_ID + "=?", new String[]{bussiness.getBusinessId()});
                continue;
            }


            values.put(KEY_BUSINESS_ID, bussiness.getBusinessId());
            values.put(KEY_BUSINESS_NAME, bussiness.getBusinessName());
            values.put(KEY_BUSINESS_ADDRS, bussiness.getBusinessAddress());

            values.put(KEY_BUSINESS_DESC, bussiness.getBusinessDescription());
            values.put(KEY_BUSINESS_LAT, bussiness.getLatitude());
            values.put(KEY_BUSINESS_LON, bussiness.getLongitude());
            values.put(KEY_BUSINESS_PHOTO, bussiness.getPhotoPath());
            values.put(KEY_BUSINESS_TYPE, bussiness.getBusinessType());

            values.put(KEY_LAST_MODIFIED_DATE_TIME, bussiness.getLastModifiedDate());
            db.replace(TABLE_BUSINESS_PLACES, null, values);

        }

        db.close();
    }


    public void clearTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }
}
