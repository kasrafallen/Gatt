package net.puzzleco.gattapp.data;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.Gravity;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ChartActivity;
import net.puzzleco.gattapp.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartData {

    private int position;
    private String mode;
    private ChartActivity context;
    private float[] dimen;

    public ChartData(ChartActivity context, int position, String mode, float[] dimen) {
        this.context = context;
        this.position = position;
        this.mode = mode;
        this.dimen = dimen;
    }

    public LineChartView getChart() {
        LineChartView chart = new LineChartView(context);
        CardView.LayoutParams p = new CardView.LayoutParams(-1, -1);
        p.gravity = Gravity.CENTER;
        int margin = ViewUtil.toPx(5, context);
        p.setMargins(margin, margin, margin, margin);
        chart.setLayoutParams(p);
        chart.setZoomEnabled(false);
        chart.setLineChartData(getData());
        return chart;
    }

    private LineChartData getData() {
        LineChartData data = new LineChartData();
        data.setAxisXBottom(bottomAxis());
        data.setAxisYLeft(leftAxis());
        data.setLines(getLines());
        return data;
    }

    private List<Line> getLines() {
        List<Line> lines = new ArrayList<>();
        switch (mode) {
            case ChartActivity.BLOOD_pressure:
                lines.add(line(context.getResources().getColor(R.color.dark_5), true));
                lines.add(line(context.getResources().getColor(R.color.dark_3), false));
                break;
            case ChartActivity.BODY_temperature:
                lines.add(line(context.getResources().getColor(R.color.dark_5), true));
                break;
            case ChartActivity.RR:
                lines.add(line(context.getResources().getColor(R.color.dark_11), true));
                break;
            case ChartActivity.HR:
                lines.add(line(context.getResources().getColor(R.color.dark_9), true));
                break;
            default:
                lines.add(line(context.getResources().getColor(R.color.dark_7), true));
                break;
        }
        return lines;
    }

    private Line line(int color, boolean flag) {
        Line line = new Line();
        line.setColor(color);
        line.setCubic(false);
        line.setPointRadius(5);
        line.setHasPoints(true);
        line.setHasLabels(false);
        line.setValues(getValues(flag));
        return line;
    }

    private List<PointValue> getValues(boolean isFirst) {
        switch (mode) {
            case ChartActivity.BODY_temperature: {
                ArrayList<PointValue> pointValues = new ArrayList<>();
                pointValues.add(new PointValue(0, 150));
                pointValues.add(new PointValue(1, 160));
                pointValues.add(new PointValue(2, 170));
                pointValues.add(new PointValue(3, 140));
                pointValues.add(new PointValue(4, 145));
                pointValues.add(new PointValue(5, 170));
                pointValues.add(new PointValue(6, 130));
                pointValues.add(new PointValue(7, 140));
                pointValues.add(new PointValue(8, 190));
                pointValues.add(new PointValue(9, 110));
                pointValues.add(new PointValue(10, 130));
                return pointValues;
            }
            case ChartActivity.BLOOD_pressure:
                if (isFirst) {
                    ArrayList<PointValue> pointValues = new ArrayList<>();
                    pointValues.add(new PointValue(0, 150));
                    pointValues.add(new PointValue(1, 160));
                    pointValues.add(new PointValue(2, 170));
                    pointValues.add(new PointValue(3, 140));
                    pointValues.add(new PointValue(4, 145));
                    pointValues.add(new PointValue(5, 170));
                    pointValues.add(new PointValue(6, 130));
                    pointValues.add(new PointValue(7, 140));
                    pointValues.add(new PointValue(8, 190));
                    pointValues.add(new PointValue(9, 110));
                    pointValues.add(new PointValue(10, 130));
                    return pointValues;
                } else {
                    ArrayList<PointValue> pointValues = new ArrayList<>();
                    pointValues.add(new PointValue(0, 110));
                    pointValues.add(new PointValue(1, 90));
                    pointValues.add(new PointValue(2, 80));
                    pointValues.add(new PointValue(3, 70));
                    pointValues.add(new PointValue(4, 100));
                    pointValues.add(new PointValue(5, 70));
                    pointValues.add(new PointValue(6, 90));
                    pointValues.add(new PointValue(7, 60));
                    pointValues.add(new PointValue(8, 110));
                    pointValues.add(new PointValue(9, 90));
                    pointValues.add(new PointValue(10, 80));
                    return pointValues;
                }
            default: {
                ArrayList<PointValue> pointValues = new ArrayList<>();
                pointValues.add(new PointValue(0, 150));
                pointValues.add(new PointValue(1, 160));
                pointValues.add(new PointValue(2, 170));
                pointValues.add(new PointValue(3, 140));
                pointValues.add(new PointValue(4, 145));
                pointValues.add(new PointValue(5, 170));
                pointValues.add(new PointValue(6, 130));
                pointValues.add(new PointValue(7, 140));
                pointValues.add(new PointValue(8, 190));
                pointValues.add(new PointValue(9, 110));
                pointValues.add(new PointValue(10, 130));
                return pointValues;
            }
        }
    }

    private Axis leftAxis() {
        Axis axis = new Axis();
        axis.setHasSeparationLine(true);
        axis.setTextColor(Color.GRAY);
        axis.setTextSize(10);
        ArrayList<AxisValue> values = new ArrayList<>();
        values.add(new AxisValue(20));
        values.add(new AxisValue(40));
        values.add(new AxisValue(60));
        values.add(new AxisValue(80));
        values.add(new AxisValue(100));
        values.add(new AxisValue(120));
        values.add(new AxisValue(140));
        values.add(new AxisValue(160));
        values.add(new AxisValue(180));
        values.add(new AxisValue(200));
        values.add(new AxisValue(220));
        axis.setValues(values);
        return axis;
    }

    private Axis bottomAxis() {
        Axis axis = new Axis();
        axis.setHasSeparationLine(true);
        axis.setTextColor(Color.GRAY);
        axis.setTextSize(10);
        ArrayList<AxisValue> values = new ArrayList<>();
        switch (position) {
            case 0:
                values.add(new AxisValue(0, new char[]{'0', '0'}));
                values.add(new AxisValue(3));
                values.add(new AxisValue(6));
                values.add(new AxisValue(9));
                values.add(new AxisValue(12, new char[]{'1', '2', 'P', 'M'}));
                values.add(new AxisValue(15));
                values.add(new AxisValue(18));
                values.add(new AxisValue(21));
                values.add(new AxisValue(23));
                break;
            case 1:
                values.add(new AxisValue(1, new char[]{}));
                values.add(new AxisValue(2, new char[]{}));
                values.add(new AxisValue(3, new char[]{}));
                values.add(new AxisValue(4, new char[]{}));
                values.add(new AxisValue(5, new char[]{}));
                values.add(new AxisValue(6, new char[]{}));
                values.add(new AxisValue(7, new char[]{}));
                break;
            case 2:
                values.add(new AxisValue(1, new char[]{}));
                values.add(new AxisValue(2, new char[]{}));
                values.add(new AxisValue(3, new char[]{}));
                values.add(new AxisValue(4, new char[]{}));
                values.add(new AxisValue(5, new char[]{}));
                values.add(new AxisValue(6, new char[]{}));
                values.add(new AxisValue(7, new char[]{}));
                break;
            default:
                values.add(new AxisValue(1, new char[]{'J', 'a', 'n'}));
                values.add(new AxisValue(2, new char[]{'F', 'e', 'b'}));
                values.add(new AxisValue(3, new char[]{'M', 'a', 'r'}));
                values.add(new AxisValue(4, new char[]{'A', 'p', 'r'}));
                values.add(new AxisValue(5, new char[]{'M', 'a', 'y'}));
                values.add(new AxisValue(6, new char[]{'J', 'u', 'n'}));
                values.add(new AxisValue(7, new char[]{'J', 'u', 'l'}));
                values.add(new AxisValue(8, new char[]{'A', 'u', 'g'}));
                values.add(new AxisValue(9, new char[]{'S', 'e', 'p'}));
                values.add(new AxisValue(10, new char[]{'O', 'c', 't'}));
                values.add(new AxisValue(11, new char[]{'N', 'o', 'v'}));
                values.add(new AxisValue(12, new char[]{'D', 'e', 'c'}));
                break;
        }
        axis.setValues(values);
        return axis;
    }
}
