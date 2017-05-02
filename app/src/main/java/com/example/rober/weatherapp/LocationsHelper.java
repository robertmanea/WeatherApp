package com.example.rober.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by rober on 06-Apr-17.
 */

public class LocationsHelper extends SQLiteOpenHelper {

    public LocationsHelper(Context context) {
        super(context, LocationContract.DB_NAME, null, LocationContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE IF NOT EXISTS " + LocationContract.LocationEntry.TABLE + " (" +
                             LocationContract.LocationEntry.COL_LOCATION_NAME + " VARCHAR, " +
                             LocationContract.LocationEntry.COL_LOCATION_COUNTRY + " VARCHAR, " +
                             LocationContract.LocationEntry.COL_LOCATION_ID + " INT(10))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationContract.LocationEntry.TABLE);
        onCreate(db);
    }

    public void deleteDatabase(){
        this.deleteLocationsDB();
    }

    public void deleteLocationsDB() {
        String deleteScript = "delete from " + LocationContract.LocationEntry.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);
    }

    public void insert(String locationName, String locationCountry, int locationId){

        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "SELECT * FROM " + LocationContract.LocationEntry.TABLE + " WHERE " +
                             LocationContract.LocationEntry.COL_LOCATION_NAME + "='" + locationName + "' AND " +
                             LocationContract.LocationEntry.COL_LOCATION_COUNTRY + "='" + locationCountry+"'";

        Cursor c = db.rawQuery(queryString, null);

        if(c==null || c.getCount()<=0) {
            ContentValues values = new ContentValues();
            values.put(LocationContract.LocationEntry.COL_LOCATION_NAME, locationName);
            values.put(LocationContract.LocationEntry.COL_LOCATION_COUNTRY, locationCountry);
            values.put(LocationContract.LocationEntry.COL_LOCATION_ID, locationId);

            db.insertWithOnConflict(LocationContract.LocationEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.close();
    }

    public void read(){
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            String queryString = "SELECT * FROM " + LocationContract.LocationEntry.TABLE;

            Cursor c = db.rawQuery(queryString, null);

            int locationNameIndex = c.getColumnIndex(LocationContract.LocationEntry.COL_LOCATION_NAME);
            int locationCountryIndex = c.getColumnIndex(LocationContract.LocationEntry.COL_LOCATION_COUNTRY);
            int locationIdIndex = c.getColumnIndex(LocationContract.LocationEntry.COL_LOCATION_ID);

            if (c != null && c.moveToFirst()){
                do {
                    Location location = new Location(c.getString(locationNameIndex),c.getString(locationCountryIndex),c.getInt(locationIdIndex));
                    MainActivity.locationList.add(location);
                } while(c.moveToNext());
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
