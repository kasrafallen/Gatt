package net.puzzleco.gattapp.instance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import net.puzzleco.gattapp.model.IDModel;

public class IDInstance {
    private final static String PREF_ID = "PREF_ID";

    public static IDModel get(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = preferences.getString(PREF_ID, null);
        if (string == null) {
            return new IDModel();
        }
        return new Gson().fromJson(string, IDModel.class);
    }

    public static void set(Context context, IDModel model) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_ID, new Gson().toJson(model)).apply();
    }
}
