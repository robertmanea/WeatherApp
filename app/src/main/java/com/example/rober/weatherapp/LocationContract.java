package com.example.rober.weatherapp;

import android.provider.BaseColumns;

/**
 * Created by rober on 06-Apr-17.
 */

public class LocationContract {

    public static final String DB_NAME = "com.example.rober.weatherapp.db";
    public static final int DB_VERSION = 1;

    public class LocationEntry implements BaseColumns {
        public static final String TABLE = "locations";

        public static final String COL_LOCATION_NAME = "location_name";
        public static final String COL_LOCATION_COUNTRY = "location_country";
        public static final String COL_LOCATION_ID = "location_id";
    }

}
