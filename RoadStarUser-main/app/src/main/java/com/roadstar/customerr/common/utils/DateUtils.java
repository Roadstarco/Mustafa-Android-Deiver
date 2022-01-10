package com.roadstar.customerr.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;

import androidx.annotation.RequiresPermission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    // region DATE_FORMATS
    public static final String EDIT_PROFILE_FORMAT = "yy-mm-dd";
    public static final String DATE_FORMAT_5 = "MMM dd, yyyy";
    public static final String DATE_FORMAT_6 = "dd MMM";
    public static final String DATE_FORMAT_7 = "MM-dd-yyyy";
    public static final String HOUR_FORMAT_1 = "h:mm a";
    public static final String CALENDAR_DEFAULT_FORMAT = "EE MMM dd HH:mm:ss z yyyy";
    public static final String CAMERA_FORMAT = "yyyyMMdd_HHmmss";
    public static final String FILE_NAME = "yyyyMMdd_HHmmssSSS";
    public static final String UTC_TIME = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String UTC_TIME_MILLIS_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String UTC_TIME_MILLIS_2 = "yyyy-MM-dd'T'HH:mm:ss";

    // endregion

    public static final String EARNING_TIME_FORMAT = "MMM dd, yyyy";
    public static final String JOB_FORMAT = "dd MMM, h:mm a";
    public static final String CHAT_TIME_FORMAT = "dd MMM, hh:mm a";
    public static final String DATE_FORMAT_CARD = "MM-yyyy";

    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        return calendar;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate(String format) {
        Calendar c = getCalendar();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }
    public static String getCurrentTime(String format) {
        String currentTime = new SimpleDateFormat(HOUR_FORMAT_1, Locale.getDefault()).format(new Date());

        return currentTime;
    }


    @SuppressLint("SimpleDateFormat")
    public static Boolean isFreeTrialEnd(String date) {

        Calendar cCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(UTC_TIME_MILLIS_2, Locale.ENGLISH);

        try {
            Date accCreatedDate = df.parse(date);
            cCal.setTime(accCreatedDate);

            long msDiff = System.currentTimeMillis() - cCal.getTimeInMillis();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
            if (daysDiff>31) {
              return   true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String convertDate(long dateInMilliseconds, String dateFormat) {
        return (new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date(dateInMilliseconds)));
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        if (dateInMilliseconds == null)
            dateInMilliseconds = "0";
        if (dateInMilliseconds.isEmpty())
            dateInMilliseconds = "0";
        return (new SimpleDateFormat(dateFormat, Locale.getDefault()).format(new Date(dateStringToLong(dateInMilliseconds))));
    }

    public static long dateStringToLong(String dateInMilliseconds) {
        if (dateInMilliseconds == null)
            dateInMilliseconds = "0";
        if (dateInMilliseconds.isEmpty())
            dateInMilliseconds = "0";
        return (Long.parseLong(dateInMilliseconds.replaceAll("[^\\d.]", "")));
    }

    public static Calendar dateMilliSecondsToCalendar(long milliSeconds) {
        Calendar calendar = DateUtils.getCalendar();
        calendar.setTimeInMillis(milliSeconds);
        return calendar;
    }

    public static String oneFormatToAnother(String date, String oldFormat, String newFormat) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat(oldFormat,
                    Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat(newFormat,
                    Locale.getDefault());
            Date d = originalFormat.parse(date);
            return targetFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDate(String date, String format) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat(format, Locale.getDefault());
            return originalFormat.parse(date);
        } catch (Exception e) {
            Logger.caughtException(e);
            return null;
        }
    }

    public static boolean isFirstDateGreater(Date date1, Date date2) {
        long milliSeconds1 = date1.getTime();
        long milliSeconds2 = date2.getTime();
        return milliSeconds1 > milliSeconds2;
    }

    @RequiresPermission(Manifest.permission.WRITE_CALENDAR)
    @SuppressWarnings({"MissingPermission"})
    public static void addScheduleToCalendar(Context context, String calTitle, String calLocation, String description,
                                             int reminder, Calendar startTime, Calendar endTime) {
        long calID = 1;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startTime.getTimeInMillis());
        values.put(Events.DTEND, endTime.getTimeInMillis());
        values.put(Events.TITLE, calTitle);
        values.put(Events.EVENT_LOCATION, calLocation);
        values.put(Events.DESCRIPTION, description);
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = resolver.insert(Events.CONTENT_URI, values);
        if (reminder >= 0 && uri != null) {
            long eventID = Long.parseLong(uri.getLastPathSegment());
            ContentValues reminders = new ContentValues();
            reminders.put(Reminders.EVENT_ID, eventID);
            reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            reminders.put(Reminders.MINUTES, reminder);
            resolver.insert(Reminders.CONTENT_URI, reminders);
            Logger.info("addScheduleToCalendar", "LastPathSegment: " + uri.getLastPathSegment());
        }
    }

    @SuppressWarnings("WrongConstant")
    public static String howMuchTimePastFromNow(long milliSeconds) {
        if (milliSeconds < 0)
            return "N/A";

        long milliSecondsCurrent = DateUtils.getCalendar().getTimeInMillis();
        long diff = milliSecondsCurrent - milliSeconds;
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = hours / 30;
        long years = months / 12;

        Calendar systemCalendar = DateUtils.getCalendar();
        systemCalendar.setTimeInMillis(milliSeconds);
        Calendar current = DateUtils.getCalendar();
        current.setTimeInMillis(milliSecondsCurrent);

        if (systemCalendar.get(Calendar.DATE) + 1 == current.get(Calendar.DATE))
            return ("Yesterday at " + convertDate(milliSeconds, HOUR_FORMAT_1));
        if (years > 0) {
            return (convertDate(milliSeconds, DATE_FORMAT_5) + " at " + convertDate(milliSeconds, HOUR_FORMAT_1));
        } else if (months > 0) {
            // return (months + " Months Ago");
            return (convertDate(milliSeconds, DATE_FORMAT_5) + " at " + convertDate(milliSeconds, HOUR_FORMAT_1));
        } else if (days > 0) {
            // return (days + " Days Ago");
            return (convertDate(milliSeconds, DATE_FORMAT_5) + " at " + convertDate(milliSeconds, HOUR_FORMAT_1));
        } else if (hours > 0)
            return (hours + (hours == 1 ? " hour " : " hours ") + "ago");
        else if (minutes > 0)
            return (minutes + (minutes == 1 ? " minute " : " minutes ") + "ago");
        else if (seconds > 5)
            return (seconds + " seconds ago");
        else
            return ("Just now");
    }

    public static String getWeekFirstAndLastDates(Calendar curCalendar) {
        Calendar privateCalendar = Calendar.getInstance();
        privateCalendar.set(Calendar.WEEK_OF_YEAR, curCalendar.get(Calendar.WEEK_OF_YEAR));
        privateCalendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR));

        Date firstDayOfWeek, lastDayOfWeek;
        int dayOfWeek = privateCalendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            privateCalendar.add(Calendar.DATE, -1 * (dayOfWeek - Calendar.MONDAY) - 7);
            firstDayOfWeek = privateCalendar.getTime();
            privateCalendar.add(Calendar.DATE, 6);
            lastDayOfWeek = privateCalendar.getTime();
        } else {
            privateCalendar.add(Calendar.DATE, -1 * (dayOfWeek -
                    Calendar.MONDAY));
            firstDayOfWeek = privateCalendar.getTime();
            privateCalendar.add(Calendar.DATE, 6);
            lastDayOfWeek = privateCalendar.getTime();
        }
        String firstDay = DateUtils.convertDate(firstDayOfWeek.getTime(), DateUtils.EARNING_TIME_FORMAT);
        String lastDay = DateUtils.convertDate(lastDayOfWeek.getTime(), DateUtils.EARNING_TIME_FORMAT);
        String finalString = firstDay + " - " + lastDay;

        return finalString;
    }

    public static String[] getWeekDateArray(Calendar curCalendar) {
        String[] labels = new String[7];
        Calendar privateCalendar = Calendar.getInstance();
        privateCalendar.set(Calendar.WEEK_OF_YEAR, curCalendar.get(Calendar.WEEK_OF_YEAR));
        privateCalendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR));

        int dayOfWeek = privateCalendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY)
            privateCalendar.add(Calendar.DATE, -1 * (dayOfWeek - Calendar.MONDAY) - 7);
        else
            privateCalendar.add(Calendar.DATE, -1 * (dayOfWeek -
                    Calendar.MONDAY));

        for (int i = 0; i < 7; i++) {
            labels[i] = DateUtils.convertDate(privateCalendar.getTime().getTime(), "dd") + "\n" +
                    DateUtils.convertDate(privateCalendar.getTime().getTime(), "MMM");
            privateCalendar.add(Calendar.DATE, +1);
        }
        return labels;
    }

    public static int getDays(long seconds) {
        int day = (int) (seconds / (24 * 3600));
        return day;
    }

    public static long getHours(long seconds) {

        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;
        return hours;
    }

    public static long getMin(long seconds) {
        seconds %= 3600;
        long minutes = seconds / 60;

        return minutes;

    }
}
