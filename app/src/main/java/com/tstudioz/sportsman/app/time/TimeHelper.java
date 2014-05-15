package com.tstudioz.sportsman.app.time;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    /**
     * Converts given datetime to locale date.
     * @param context context
     * @param datetime datetime in text format
     * @return
     * @throws ParseException exception when parsing datetime
     */
    public static String convertDateToLocale(Context context, String datetime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(datetime);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }

    public static String convertTimeToLocale(Context context, String datetime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(datetime);
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(date);
    }

    /**
     * Converts integer values of date to String.
     * @return String datetime
     */
    public static String createTimestamp(int year, int month, int day, int hour, int minute, int second) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new GregorianCalendar(year, month, day, hour, minute, second).getTime());
    }

    /**
     * Returns an instance of Calendar parsed from String datetime.
     * @param datetime datetime in text format
     * @return instance of Calendar
     * @throws ParseException
     */
    public static Calendar getDateLocale(String datetime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(datetime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String getDateLocale(Context context, Calendar date) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(new GregorianCalendar(date.get(Calendar.YEAR),
                date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).getTime());
    }

    /**
     * Compares years, months and days of given dates.
     * @return true if equal, false otherwise
     */
    public static boolean equalDays(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
                && date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Converts miliseconds to user friendly time format.
     * @param miliseconds
     * @return time in format hh:mm:ss
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
