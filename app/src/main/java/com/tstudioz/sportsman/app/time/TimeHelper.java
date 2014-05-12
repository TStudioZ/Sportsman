package com.tstudioz.sportsman.app.time;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static String convertToLocale(Context context, String timestamp) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(timestamp);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }

    public static String createTimestamp(int year, int month, int day, int hour, int minute, int second) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new GregorianCalendar(year, month, day, hour, minute, second).getTime());
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
