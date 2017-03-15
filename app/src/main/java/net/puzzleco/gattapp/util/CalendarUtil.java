package net.puzzleco.gattapp.util;

import java.util.Calendar;
import java.util.Locale;

public class CalendarUtil {

    public static String getDay() {
        return Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
    }

    public static String getMonth() {
        return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
    }

    public static String getDate() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " ";
    }

    public static CharSequence getBirth(int[] birth) {
        return birth[0] + " - " + birth[1] + " - " + birth[2];
    }
}
