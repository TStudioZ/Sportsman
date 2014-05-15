package com.tstudioz.sportsman.app.training;

import com.tstudioz.sportsman.app.R;

/**
 * Created by Tomáš Zahálka on 10. 5. 2014.
 */
public final class Mood {
    public static int getDrawableID(int moodID) {
        switch (moodID) {
            case 1:
                return R.drawable.worse;
            case 2:
                return R.drawable.middle;
            default:
                return R.drawable.best;
        }
    }
}
