package net.puzzleco.gattapp.instance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import net.puzzleco.gattapp.model.MedicineModel;
import net.puzzleco.gattapp.util.ReminderUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

public class ReminderInstance {

    private final static String PREF_REMINDER = "PREF_REMINDER";

    public static ArrayList<MedicineModel> getAll(Context context) {
        ArrayList<MedicineModel> list = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = preferences.getString(PREF_REMINDER, null);
        if (string != null) {
            MedicineModel[] array = new Gson().fromJson(string, MedicineModel[].class);
            for (MedicineModel model : array) {
                list.add(model);
            }
        }
        Collections.sort(list, new Comparator<MedicineModel>() {
            @Override
            public int compare(MedicineModel model, MedicineModel t1) {
                return t1.getDate().compareToIgnoreCase(model.getDate());
            }
        });
        return list;
    }

    public static void put(Context context, MedicineModel medicineModel) {
        if (medicineModel == null) {
            return;
        }

        medicineModel.setId(new Random().nextLong());
        medicineModel.setDate(new Date().toString());

        ArrayList<MedicineModel> list = getAll(context);

        Collections.sort(list, new Comparator<MedicineModel>() {
            @Override
            public int compare(MedicineModel model, MedicineModel t1) {
                return Double.compare(model.getIndex(), t1.getIndex());
            }
        });
        if (list.size() <= 0) {
            medicineModel.setIndex(5);
        } else {
            medicineModel.setIndex(list.get(list.size() - 1).getIndex() + 1);
        }

        list.add(medicineModel);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_REMINDER, new Gson().toJson(list)).apply();

        ReminderUtil.put(medicineModel, context);
    }

    public static void remove(Context context, MedicineModel item) {
        if (item == null) {
            return;
        }
        ArrayList<MedicineModel> list = getAll(context);
        ArrayList<MedicineModel> copy = new ArrayList<>(list);
        for (MedicineModel model : list) {
            if (model.getId() == item.getId()) {
                copy.remove(list.indexOf(model));
            }
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_REMINDER, new Gson().toJson(copy)).apply();

        ReminderUtil.cancel(item, context);
    }

    public static void edit(Context context, MedicineModel item) {
        if (item == null) {
            return;
        }

        ArrayList<MedicineModel> list = getAll(context);
        for (MedicineModel model : list) {
            if (model.getId() == item.getId()) {
                ReminderUtil.cancel(model, context);

                model.setHour(item.getHour());
                model.setMinute(item.getMinute());
                model.setName(item.getName());
                model.setNote(item.getNote());
                break;
            }
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_REMINDER, new Gson().toJson(list)).apply();

        ReminderUtil.put(item, context);
    }
}
