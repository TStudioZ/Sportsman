package com.tstudioz.sportsman.app.database;

import android.provider.BaseColumns;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public final class WaypointContract {
    public static abstract class WaypointEntry implements BaseColumns {
        public static final String TABLE_NAME = "waypoint";
        public static final String COLUMN_NAME_WORKOUT_ID = "workout_id";
        public static final String COLUMN_NAME_DATETIME = "datetime";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";

    }
}
