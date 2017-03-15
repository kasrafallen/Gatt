package net.puzzleco.gattapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Util {

    private final static String WIDTH = "WIDTH";
    private final static String HEIGHT = "HEIGHT";
    private final static String MED_ID = "MED_ID";

    private static SharedPreferences getPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isDimen(Context context) {
        SharedPreferences preferences = getPreference(context);
        return preferences.contains(WIDTH) && preferences.contains(HEIGHT);
    }

    public static void setDimen(Context context, float[] floats) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putFloat(WIDTH, floats[0]);
        editor.putFloat(HEIGHT, floats[1]);
        editor.apply();
    }

    public static float[] getDimen(Context context) {
        SharedPreferences preferences = getPreference(context);
        return new float[]{preferences.getFloat(WIDTH, 0), preferences.getFloat(HEIGHT, 0)};
    }

    public static boolean getMedicalStatus(Context context) {
        return getPreference(context).getBoolean(MED_ID, false);
    }

    public static void setMedicalStatus(Context context, boolean isChecked) {
        getPreference(context).edit().putBoolean(MED_ID, isChecked).apply();
    }
}
