package net.puzzleco.gattapp.init;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.MainActivity;
import net.puzzleco.gattapp.activity.MedicalActivity;
import net.puzzleco.gattapp.activity.ReminderActivity;
import net.puzzleco.gattapp.service.AppService;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppText;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitDrawer implements View.OnClickListener {
    private static final int MAIN = +824784;
    private static final int HEALTH = +824783;
    private static final int MEDICINE = +824782;
    private static final int ID = +824781;
    private static final int ABOUT = +824785;
    private static final int HELP = +824786;

    private static final int LOGO = +222;
    private static final int SWITCH = +800;

    private MainActivity context;
    private DrawerLayout drawerLayout;
    private SwitchCompat compat;

    private float[] dimen;
    private int drawer_size;
    private int header_size;
    private int field_size;
    private int logo_size;
    private int margin;

    public InitDrawer(MainActivity context, float[] dimen, DrawerLayout layout) {
        this.context = context;
        this.dimen = dimen;
        this.drawerLayout = layout;
        this.drawer_size = ViewUtil.toPx(250, context);
        this.header_size = ViewUtil.toPx(150, context);
        this.field_size = ViewUtil.toPx(50, context);
        this.logo_size = ViewUtil.toPx(20, context);
        this.margin = ViewUtil.toPx(9, context);
    }

    public View getDrawer() {
        FrameLayout layout = new FrameLayout(context);
        layout.setBackgroundColor(Color.WHITE);
        DrawerLayout.LayoutParams p = new DrawerLayout.LayoutParams(drawer_size, -1);
        p.gravity = Gravity.LEFT;
        layout.setLayoutParams(p);

        ScrollView view = new ScrollView(context);
        view.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);

        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.addView(header());
        box.addView(field(MAIN));
//        box.addView(field(HEALTH));
        box.addView(field(MEDICINE));
        box.addView(field(ID));
        box.addView(field(ABOUT));
        box.addView(field(HELP));

        view.addView(box);
        layout.addView(view);
        return layout;
    }

    private View field(int id) {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setId(id);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, field_size);
        layout.setLayoutParams(p);
        ViewUtil.setBackground(layout, context);
        layout.setOnClickListener(this);
        if (id == ID) {
            layout.addView(switchButton());
        }
        layout.addView(logo(id));
        layout.addView(text(id));
        return layout;
    }

    private View switchButton() {
        compat = new SwitchCompat(context);
        compat.setId(SWITCH);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-2, -2);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.rightMargin = margin;
        compat.setLayoutParams(p);
        compat.setChecked(Util.getMedicalStatus(context));
        compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Util.setMedicalStatus(context, isChecked);
                startService();
            }
        });
        return compat;
    }

    private void startService() {
        Intent service = new Intent(context, AppService.class);
        context.startService(service);
    }

    private View text(int id) {
        AppText text = new AppText(context);
        text.setSingleLine();
        text.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, -1);
        p.addRule(RelativeLayout.RIGHT_OF, +(LOGO + id));
        if (id == ID) {
            p.addRule(RelativeLayout.LEFT_OF, SWITCH);
        }
        p.setMargins(margin, 0, margin, 0);
        text.setLayoutParams(p);
        text.setTextColor(Color.DKGRAY);
        text.setTextSize(1, 13);
        text.setTypeface(text.getTypeface(), Typeface.BOLD);
        switch (id) {
            case HEALTH:
                text.setText("Health Devices");
                break;
            case ID:
                text.setText("Medical ID");
                break;
            case MAIN:
                text.setText("Main Page");
                break;
            case MEDICINE:
                text.setText("Medicine reminder");
                break;
            case ABOUT:
                text.setText("About us");
                break;
            case HELP:
                text.setText("Help");
                break;
        }
        return text;
    }

    private View logo(int id) {
        View view = new View(context);
        view.setId(+(LOGO + id));
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(logo_size, logo_size);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        p.leftMargin = margin;
        view.setLayoutParams(p);
        switch (id) {
            case HEALTH:
                view.setBackgroundResource(R.mipmap.health);
                break;
            case ID:
                view.setBackgroundResource(R.mipmap.id);
                break;
            case MAIN:
                view.setBackgroundResource(R.mipmap.main);
                break;
            case MEDICINE:
                view.setBackgroundResource(R.mipmap.med_reminder);
                break;
            case ABOUT:
                view.setBackgroundResource(R.mipmap.about_us);
                break;
            case HELP:
                view.setBackgroundResource(R.mipmap.help);
                break;
        }
        return view;
    }

    private View header() {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setBackgroundResource(R.color.theme);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-1, header_size));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.setElevation(2);
        }

        LinearLayout box = new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(-1, -2);
        pp.addRule(RelativeLayout.CENTER_VERTICAL);
        box.setLayoutParams(pp);

        CircleImageView view = new CircleImageView(context);
        LinearLayout.LayoutParams p;
        if (header_size > drawer_size) {
            p = new LinearLayout.LayoutParams(drawer_size / 2, drawer_size / 2);
        } else {
            p = new LinearLayout.LayoutParams(header_size / 2, header_size / 2);
        }
        p.gravity = Gravity.CENTER_HORIZONTAL;
        view.setLayoutParams(p);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(R.mipmap.main_tes);

        AppText tv = new AppText(context);
        LinearLayout.LayoutParams ppp = new LinearLayout.LayoutParams(-1, -2);
        ppp.topMargin = ViewUtil.toPx(10, context);
        tv.setLayoutParams(ppp);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(1, 14);
        tv.setText("Admin Test");

        box.addView(view);
        box.addView(tv);
        layout.addView(box);
        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case HEALTH:
                break;
            case ID:
                Intent intent = new Intent(context, MedicalActivity.class);
                context.startActivity(intent);
                break;
            case MAIN:
                break;
            case MEDICINE:
                Intent intent1 = new Intent(context, ReminderActivity.class);
                context.startActivity(intent1);
                break;
            case ABOUT:
                break;
            case HELP:
                break;
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void changeSwitch() {
        if (compat != null)
            compat.setChecked(Util.getMedicalStatus(context));
    }
}
