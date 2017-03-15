package net.puzzleco.gattapp.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import net.puzzleco.gattapp.R;

public class ViewUtil {

    public static void setBackground(View view, Context context) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }

    public static void setBar(Activity context, @ColorRes int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = context.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(context, color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int toPx(int i, Context context) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static int getToolbarSize(Context context) {
        return ViewUtil.toPx(56, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Drawable createButtonRipple(Context context) {
        int ripple_color = context.getResources().getColor(R.color.black);
        ShapeDrawable mask = new ShapeDrawable();
        mask.setShape(new OvalShape());
        return new RippleDrawable(new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{}
                        },
                new int[]
                        {
                                ripple_color,
                                ripple_color
                        }
        ), null, mask);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setBackgroundRipple(View view, Context context) {
        int ripple_color = context.getResources().getColor(R.color.white);
        ShapeDrawable shape = new ShapeDrawable(new OvalShape());
        view.setBackground(new RippleDrawable(new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{}
                        },
                new int[]
                        {
                                ripple_color,
                                ripple_color
                        })
                , null, shape));
    }
}
