package com.tstudioz.sportsman.app.database;

import android.provider.BaseColumns;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public final class WorkoutContract {
    public static abstract class WorkoutEntry implements BaseColumns {
        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_SPORT_ID = "sport_id";
        public static final String COLUMN_NAME_MOOD_ID = "mood_id";
        public static final String COLUMN_NAME_DATETIME = "date_and_time";

    }
}
