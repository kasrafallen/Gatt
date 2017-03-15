package net.puzzleco.gattapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AppText extends TextView {

    public AppText(Context context) {
        super(context);
//        ViewUtil.set(this, context);
    }

    public AppText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
