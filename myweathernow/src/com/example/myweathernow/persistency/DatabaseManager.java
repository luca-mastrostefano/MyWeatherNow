package com.example.myweathernow.persistency;

/**
 * Created by lucamastrostefano on 04/01/15.
 */

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.location.*;
import com.example.myweathernow.*;
import com.example.myweathernow.exception.*;

import java.text.*;
import java.util.*;

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
    private static final String KEY_TIMESTAMP = "loc_timestamp";
    private static final String KEY_PING = "loc_ping_number";

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
                        DatabaseManager.KEY_LONGITUDE + " REAL," +
                        DatabaseManager.KEY_TIMESTAMP + " TEXT," +
                        DatabaseManager.KEY_PING + " INTEGER)";

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
    public long addLocation(final MWNlocation location) {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = this.createValuesFromLocation(location);
        // Inserting Row
        final long newID = db.insert(DatabaseManager.TABLE_LOCATION_NAME, null, values);
        // db.close(); // Closing database connection
        return newID;
    }

    // Getting single location by id
    public MWNlocation getLocation(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();

        final String[] fields = new String[]{DatabaseManager.KEY_ID, DatabaseManager.KEY_LATITUDE, DatabaseManager.KEY_LONGITUDE,
                DatabaseManager.KEY_TIMESTAMP, DatabaseManager.KEY_PING};
        final Cursor cursor = db.query(DatabaseManager.TABLE_LOCATION_NAME, fields, DatabaseManager.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return this.createLocationFromResult(cursor);
    }

    // Getting All Location
    public List<MWNlocation> getAllLocation() {
        final List<MWNlocation> locationList = new ArrayList<MWNlocation>();
        // prende le location ordinate per ping decrescenti
        final String selectQuery = "SELECT * FROM " + DatabaseManager.TABLE_LOCATION_NAME + " ORDER BY " + DatabaseManager.KEY_PING + " DESC";

        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                final MWNlocation location = this.createLocationFromResult(cursor);
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        return locationList;
    }

    // Getting All Location with a ping number grater than a certain value
    public List<MWNlocation> getAllLocationByPingNumber(int ping) {
        final List<MWNlocation> locationList = new ArrayList<MWNlocation>();
        // prende le location ordinate per ping decrescenti
        final String selectQuery = "SELECT * FROM " + DatabaseManager.TABLE_LOCATION_NAME + " WHERE loc_ping_number >= " + ping + "ORDER BY " + DatabaseManager.KEY_PING + " DESC";

        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                final MWNlocation location = this.createLocationFromResult(cursor);
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        return locationList;
    }

    // Updating single location -  aggiorna la location dall'id
    public int updateLocation(final MWNlocation mwNlocation) throws LocationNotStoredException {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = this.createValuesFromLocation(mwNlocation);

        return db.update(DatabaseManager.TABLE_LOCATION_NAME, values, DatabaseManager.KEY_ID + " = ?", new String[]{String.valueOf(mwNlocation.getId())});
    }

    // Deleting single location - cancella la location con l'id
    public void deleteLocation(final MWNlocation mwNlocation) throws LocationNotStoredException {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseManager.TABLE_LOCATION_NAME, DatabaseManager.KEY_ID + " = ?", new String[]{String.valueOf(mwNlocation.getId())});
        // db.close();
    }

    // Deleting all location with ping number lesser than a certain value
    public void deleteLocationByPingNumber(final int ping) throws LocationNotStoredException {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseManager.TABLE_LOCATION_NAME, DatabaseManager.KEY_PING + " >= ?", new String[]{String.valueOf(ping)});
        // db.close();
    }

    // dato il risultato del db, crea un oggetto location
    private MWNlocation createLocationFromResult(final Cursor cursor) {
        final MWNlocation mwn_location = new MWNlocation();
        try {
            double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseManager.KEY_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseManager.KEY_LONGITUDE));
            Date date = this.dateFormatter.parse(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_TIMESTAMP)));
            long time = date.getTime();

            Location loc = new Location(LocationManager.NETWORK_PROVIDER);
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            loc.setTime(time);

            mwn_location.setId(cursor.getInt(cursor.getColumnIndex(DatabaseManager.KEY_ID)));
            mwn_location.setLocation(loc);
            mwn_location.setPing(cursor.getInt(cursor.getColumnIndex(DatabaseManager.KEY_PING)));
        } catch (final ParseException e) {
        }
        return mwn_location;
    }

    // data una location, crea i dati da inserire nel db
    private ContentValues createValuesFromLocation(final MWNlocation loc) {
        final ContentValues values = new ContentValues();
        values.put(DatabaseManager.KEY_LATITUDE, String.valueOf(loc.getLocation().getLatitude()));
        values.put(DatabaseManager.KEY_LONGITUDE, String.valueOf(loc.getLocation().getLongitude()));
        values.put(DatabaseManager.KEY_TIMESTAMP, this.dateFormatter.format(loc.getTimestamp()));
        values.put(DatabaseManager.KEY_PING, loc.getPing());
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