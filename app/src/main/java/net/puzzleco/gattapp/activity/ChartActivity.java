package net.puzzleco.gattapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.init.InitChart;
import net.puzzleco.gattapp.util.ViewUtil;

public class ChartActivity extends BaseCompatActivity {

    public final static String BODY_temperature = "Body Temperature";
    public final static String BLOOD_pressure = "Blood Pressure";
    public final static String SPO2 = "Pulse Oximeter";
    public final static String RR = "Respiratory rate";
    public final static String HR = "Heart rate";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, getColor(getIntent().getAction()));
        InitChart initChart = new InitChart(this, getIntent().getAction());
        setContentView(initChart.getView());
        initChart.setData();
    }

    private int getColor(String action) {
        switch (action) {
            case BODY_temperature:
                return R.color.dark_5;
            case BLOOD_pressure:
                return R.color.dark_3;
            case SPO2:
                return R.color.dark_7;
            case RR:
                return R.color.dark_11;
            default:
                return R.color.dark_9;
        }
    }
}
