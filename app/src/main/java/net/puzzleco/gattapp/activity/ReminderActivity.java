package net.puzzleco.gattapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.init.InitReminder;
import net.puzzleco.gattapp.util.ViewUtil;

public class ReminderActivity extends BaseCompatActivity {
    public InitReminder initMedicine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, R.color.dark);
        initMedicine = new InitReminder(this);
        setContentView(initMedicine.getView());
        initMedicine.setView(InitReminder.MODE_LIST, null);
    }

    @Override
    public void onBackPressed() {
        if (initMedicine.current_mode == InitReminder.MODE_LIST) {
            super.onBackPressed();
        } else {
            initMedicine.setView(InitReminder.MODE_LIST, null);
        }
    }
}
