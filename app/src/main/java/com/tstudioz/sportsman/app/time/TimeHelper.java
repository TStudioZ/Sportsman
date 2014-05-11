package com.tstudioz.sportsman.app.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tomáš Zahálka on 11. 5. 2014.
 */
public class TimeHelper {

    /**
     * Returns curent time and date in database friendly format.
     * @return current timestamp
     */
    public static String getCurrentTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * Converts miliseconds to user friendly time format.
     * @param miliseconds
     * @return
     */
    public static String getTime(long miliseconds) {
        long seconds = miliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        String secs = seconds > 9 ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
        String mins = minutes > 9 ? String.valueOf(minutes) :  "0" + String.valueOf(minutes);
        String hrs = hours > 9 ? String.valueOf(hours) : "0" + String.valueOf(hours);
        return hrs + ":" + mins + ":" + secs;
    }
}
