package com.sevendesign.planitprom.utils;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

/**
 * Created by mib on 30.08.13.
 */
public class CalendarUtils {
	public static void addNoteToCalendar(Activity activity, long departMillis, String title) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
			addToCalendarAPI14(activity, departMillis, title);
        } else {
			addToCalendarOldAPI(activity, departMillis, title);
        }

    }

    /* silent putting note for API higher then 14 */
	private static void addToCalendarAPI14(Activity activity, long departMillis, String noteTitle) {
        // getting calendar id
        Cursor cursor = activity.getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"), new String[]{"_id", "calendar_displayName"}, null, null, null);
        cursor.moveToFirst();
        String calendarNames[] = new String[cursor.getCount()];
        int [] calendarId = new int[cursor.getCount()];
        for (int i = 0; i < calendarNames.length; i++) {
            calendarId[i] = cursor.getInt(0);
            calendarNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }

        // putting note to calendar
        if (calendarId.length > 0) {
            TimeZone tz = TimeZone.getDefault();
            String timeZoneString = tz.getID();

            ContentResolver contentResolver = activity.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(CalendarContract.Events.CALENDAR_ID, calendarId[0]); // XXX pick)
            cv.put(CalendarContract.Events.TITLE, noteTitle);
            cv.put(CalendarContract.Events.DTSTART, departMillis);
			cv.put(CalendarContract.Events.DTEND, departMillis);
            cv.put(CalendarContract.Events.EVENT_TIMEZONE, timeZoneString);
            //cv.put(CalendarContract.Events.HAS_ALARM, true);
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, cv);
        }
    }

    /* silent putting note to calendar for API less then 14 */
	private static void addToCalendarOldAPI(Activity activity, long departMillis, String noteTitle) {
        // getting calendar id
        Cursor cursor = activity.getContentResolver().query(Uri.parse("content://com.android.calendar/calendars"), new String[]{"_id", "displayname"}, null, null, null);
        cursor.moveToFirst();
        String calendarNames[] = new String[cursor.getCount()];
        int [] calendarId = new int[cursor.getCount()];
        for (int i = 0; i < calendarNames.length; i++) {
            calendarId[i] = cursor.getInt(0);
            calendarNames[i] = cursor.getString(1);
            cursor.moveToNext();
        }

        // putting note to calendar
        if (calendarId.length > 0) {
            ContentValues cv = new ContentValues();
            cv.put("calendar_id", calendarId[0]);
            cv.put("title", noteTitle);
            //cv.put("description", "Wedding Party");
            //cv.put("eventLocation", "New York");
            //cv.put("beginTime", cal.getTimeInMillis());
            cv.put("dtstart", departMillis);
            //cv.put("endTime", returnMillis);
//            cv.put("dtend", returnMillis);
            cv.put("allDay", 1);   // 0 for false, 1 for true
            //cv.put("hasAlarm", 1); // has alarm
            Uri eventsUri = Uri.parse("content://com.android.calendar/events");
            // event is added successfully
            activity.getContentResolver().insert(eventsUri, cv);
            cursor.close();
        }
    }

    public static boolean weHaveThisTime(int month, int week, int day, Calendar departDate) {
        Calendar curTime = Calendar.getInstance();
        curTime.roll(Calendar.MONTH, month);
        curTime.roll(Calendar.WEEK_OF_YEAR, week);
        curTime.roll(Calendar.DAY_OF_YEAR, day);

        if((departDate.getTimeInMillis() - curTime.getTimeInMillis()) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
