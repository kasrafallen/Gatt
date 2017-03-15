package net.puzzleco.gattapp.adaptor;

import net.puzzleco.gattapp.activity.SignalActivity;
import net.puzzleco.gattapp.view.spark.SparkAdapter;

import java.util.ArrayList;

public class SignalAdaptor extends SparkAdapter {
    private float[] dimen;
    private SignalActivity context;
    private String mode;

    private Float[] y;

    public SignalAdaptor(float[] dimen, String mode, SignalActivity context) {
        this.context = context;
        this.dimen = dimen;
        this.mode = mode;
        this.y = new Float[]{};
    }

    @Override
    public int getCount() {
        return y.length;
    }

    @Override
    public Object getItem(int index) {
        return y[index];
    }

    @Override
    public float getY(int index) {
        return y[index];
    }

    public void setData(ArrayList<Float> list) {
        this.y = list.toArray(new Float[list.size()]);
    }

    public void setData(Float[] list) {
        this.y = list;
    }
}
