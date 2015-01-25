package com.example.myweathernow.persistency;

/**
 * Created by lucamastrostefano on 04/01/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.example.myweathernow.background_check.LocationsDetector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DatabaseManager extends SQLiteOpenHelper {

    private final DateFormat dateFormatter;
    private static DatabaseManager instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyWeatherNow";
    private static final String TABLE_LOCATION_NAME = "MWN_location";
    private static final String NULL_VALUE = "null";

    // campi della tabella MWN_location
    private static final String KEY_ID = "loc_id";
    private static final String KEY_LATITUDE = "loc_latitude";
    private static final String KEY_LONGITUDE = "loc_longitude";

    public static DatabaseManager getInstance(final Context context) {
        if (DatabaseManager.instance == null) {
            DatabaseManager.instance = new DatabaseManager(context);
        }
        return DatabaseManager.instance;
    }

    private DatabaseManager(final Context context) {
        super(context, DatabaseManager.DATABASE_NAME, null, DatabaseManager.DATABASE_VERSION);
        this.dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }

    // Creating Tables - called when Db is created for first time
    @Override
    public void onCreate(final SQLiteDatabase db) {
        // tabella location
        final String CREATE_LOCATION_TABLE =
                "CREATE TABLE " + DatabaseManager.TABLE_LOCATION_NAME + "(" +
                        DatabaseManager.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        DatabaseManager.KEY_LATITUDE + " REAL," +
                        DatabaseManager.KEY_LONGITUDE + " REAL)";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManager.TABLE_LOCATION_NAME);
        // Create tables again
        this.onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Store a location
    public long addLocation(final Location location) {
        Log.d("DatabaseManager" , "Saving location in the DB");
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = this.createValuesFromLocation(location);
        // Inserting Row
        final long newID = db.insert(DatabaseManager.TABLE_LOCATION_NAME, null, values);
        // db.close(); // Closing database connection
        return newID;
    }
/*
    // Getting single location by id
    public Location getLocation(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();

        final String[] fields = new String[]{DatabaseManager.KEY_ID, DatabaseManager.KEY_LATITUDE, DatabaseManager.KEY_LONGITUDE};
        final Cursor cursor = db.query(DatabaseManager.TABLE_LOCATION_NAME, fields, DatabaseManager.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return this.createLocationFromResult(cursor);
    }
*/
    // Getting All Location
    public boolean existNearKnownLocation(Location location) {
        Log.d("DatabaseManager" , "Find near locations");
        // prende le location ordinate per ping decrescenti
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        final String selectQuery = "SELECT * FROM " + DatabaseManager.TABLE_LOCATION_NAME;

        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                final Location loc = this.createLocationFromResult(cursor);
                if(loc.distanceTo(location) <= LocationsDetector.DISTANCE_THREASHOLD){
                    Log.d("DatabaseManager" , "Near to " + loc.getLatitude() + "; " + loc.getLongitude());
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

/*
    // Deleting single location - cancella la location con l'id
    public void deleteLocation(final Location location) throws LocationNotStoredException {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseManager.TABLE_LOCATION_NAME, DatabaseManager.KEY_ID + " = ?", new String[]{String.valueOf(location.getId())});
        // db.close();
    }
*/


    // dato il risultato del db, crea un oggetto location
    private Location createLocationFromResult(final Cursor cursor) {
        double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseManager.KEY_LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseManager.KEY_LONGITUDE));
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    // data una location, crea i dati da inserire nel db
    private ContentValues createValuesFromLocation(final Location location) {
        final ContentValues values = new ContentValues();
        double lat = location.getLatitude();
        String latitude = String.valueOf(lat);
        values.put(DatabaseManager.KEY_LATITUDE, latitude);
        values.put(DatabaseManager.KEY_LONGITUDE, String.valueOf(location.getLongitude()));
        return values;
    }

    //    private ContentValues createValuesFromMeteo(final WeatherInfo meteo) {
//        final ContentValues values = new ContentValues();
//        values.put(DatabaseManager.KEY_LATITUDE, site.getPageName());
//        values.put(DatabaseManager.KEY_URL, site.getUrl());
//        values.put(DatabaseManager.KEY_SELECTOR, site.getSelector());
//        values.put(DatabaseManager.KEY_HASH, site.getHash());
//        values.put(DatabaseManager.KEY_RUNNING_STATUS, site.getRunningStatus().toString());
//        values.put(DatabaseManager.KEY_SITE_STATUS, site.getSiteStatus().toString());
//        values.put(DatabaseManager.KEY_CREATION_DATE, this.dateFormatter.format(site.getCreationDate()));
//        if (site.getLastCheck() != null) {
//            values.put(DatabaseManager.KEY_LAST_CHECK, this.dateFormatter.format(site.getLastCheck()));
//        } else {
//            values.put(DatabaseManager.KEY_LAST_CHECK, DatabaseManager.NULL_VALUE);
//        }
//        values.put(DatabaseManager.KEY_SERVER_SYNCH, Boolean.toString(site.isServerSynch()));
//        return values;
//    }

}