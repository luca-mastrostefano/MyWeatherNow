package com.example.myweathernow.database;

/**
 * Created by lucamastrostefano on 04/01/15.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private final DateFormat dateFormatter;
    private static DatabaseManager instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyWeatherNow";
    private static final String TABLE_SITES_NAME = "sites";
    private static final String NULL_VALUE = "null";

    private static final String KEY_ID = "id";
    private static final String KEY_PAGE_NAME = "page_name";
    private static final String KEY_URL = "url";
    private static final String KEY_SELECTOR = "selector";
    private static final String KEY_HASH = "hash";
    private static final String KEY_RUNNING_STATUS = "running_status";
    private static final String KEY_SITE_STATUS = "site_status";
    private static final String KEY_CREATION_DATE = "creation_date";
    private static final String KEY_LAST_CHECK = "last_check";
    private static final String KEY_SERVER_SYNCH = "server_synch";

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

    // Creating Tables
    @Override
    public void onCreate(final SQLiteDatabase db) {
        final String CREATE_SITES_TABLE = "CREATE TABLE " + DatabaseManager.TABLE_SITES_NAME + "(" + DatabaseManager.KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + DatabaseManager.KEY_SELECTOR + " TEXT," + DatabaseManager.KEY_PAGE_NAME + " TEXT,"
                + DatabaseManager.KEY_HASH + " TEXT," + DatabaseManager.KEY_URL + " TEXT," + DatabaseManager.KEY_RUNNING_STATUS + " TEXT,"
                + DatabaseManager.KEY_SITE_STATUS + " TEXT," + DatabaseManager.KEY_CREATION_DATE + " TEXT," + DatabaseManager.KEY_LAST_CHECK
                + " TEXT," + DatabaseManager.KEY_SERVER_SYNCH + " TEXT)";
        db.execSQL(CREATE_SITES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManager.TABLE_SITES_NAME);

        // Create tables again
        this.onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
/*
    // Store a site
    public long createSite(final Site site) {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = this.createValuesFromSite(site);
        // Inserting Row
        final long newID = db.insert(DatabaseManager.TABLE_SITES_NAME, null, values);
        // db.close(); // Closing database connection
        return newID;
    }

    // Getting single site
    public Site getSite(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();

        final String[] fields = new String[]{DatabaseManager.KEY_ID, DatabaseManager.KEY_PAGE_NAME, DatabaseManager.KEY_URL,
                DatabaseManager.KEY_SELECTOR, DatabaseManager.KEY_HASH, DatabaseManager.KEY_RUNNING_STATUS, DatabaseManager.KEY_SITE_STATUS,
                DatabaseManager.KEY_CREATION_DATE, DatabaseManager.KEY_LAST_CHECK, DatabaseManager.KEY_SERVER_SYNCH};
        final Cursor cursor = db.query(DatabaseManager.TABLE_SITES_NAME, fields, DatabaseManager.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return this.createSiteFromResult(cursor);
    }

    // Getting All Sites
    public List<Site> getAllSites() {
        final List<Site> siteList = new ArrayList<Site>();
        final String selectQuery = "SELECT * FROM " + DatabaseManager.TABLE_SITES_NAME + " ORDER BY " + DatabaseManager.KEY_ID + " DESC";

        final SQLiteDatabase db = this.getWritableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                final Site site = this.createSiteFromResult(cursor);
                siteList.add(site);
            } while (cursor.moveToNext());
        }

        return siteList;
    }

    // Updating single site
    public int updateSite(final Site site) throws SiteNotStoredException {
        final SQLiteDatabase db = this.getWritableDatabase();

        final ContentValues values = this.createValuesFromSite(site);

        return db.update(DatabaseManager.TABLE_SITES_NAME, values, DatabaseManager.KEY_ID + " = ?", new String[]{String.valueOf(site.getId())});
    }

    // Deleting single site
    public void deleteSites(final Site site) throws SiteNotStoredException {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseManager.TABLE_SITES_NAME, DatabaseManager.KEY_ID + " = ?", new String[]{String.valueOf(site.getId())});
        // db.close();
    }

    private Site createSiteFromResult(final Cursor cursor) {
        final Site site = new Site();
        try {
            site.setId(cursor.getLong(cursor.getColumnIndex(DatabaseManager.KEY_ID)));
            site.setPageName(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_PAGE_NAME)));
            site.setUrl(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_URL)));
            site.setSelector(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_SELECTOR)));
            site.setHash(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_HASH)));
            site.setRunningStatus(Site.RunningStatus.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_RUNNING_STATUS))));
            site.setSiteStatus(Site.SiteStatus.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_SITE_STATUS))));
            site.setCreationDate(this.dateFormatter.parse(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_CREATION_DATE))));
            final String lastCheck = cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_LAST_CHECK));
            if ((lastCheck == null) || lastCheck.equals(DatabaseManager.NULL_VALUE)) {
                site.setLastCheck(null);
            } else {
                site.setLastCheck(this.dateFormatter.parse(lastCheck));
            }
            site.setServerSynch(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DatabaseManager.KEY_SERVER_SYNCH))));
        } catch (final ParseException e) {
        }
        return site;
    }

    private ContentValues createValuesFromMeteo(final MeteoInfo meteo) {
        final ContentValues values = new ContentValues();
        values.put(DatabaseManager.KEY_PAGE_NAME, site.getPageName());
        values.put(DatabaseManager.KEY_URL, site.getUrl());
        values.put(DatabaseManager.KEY_SELECTOR, site.getSelector());
        values.put(DatabaseManager.KEY_HASH, site.getHash());
        values.put(DatabaseManager.KEY_RUNNING_STATUS, site.getRunningStatus().toString());
        values.put(DatabaseManager.KEY_SITE_STATUS, site.getSiteStatus().toString());
        values.put(DatabaseManager.KEY_CREATION_DATE, this.dateFormatter.format(site.getCreationDate()));
        if (site.getLastCheck() != null) {
            values.put(DatabaseManager.KEY_LAST_CHECK, this.dateFormatter.format(site.getLastCheck()));
        } else {
            values.put(DatabaseManager.KEY_LAST_CHECK, DatabaseManager.NULL_VALUE);
        }
        values.put(DatabaseManager.KEY_SERVER_SYNCH, Boolean.toString(site.isServerSynch()));
        return values;
    }
*/
}