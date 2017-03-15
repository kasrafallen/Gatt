package net.puzzleco.gattapp.init;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ReminderActivity;
import net.puzzleco.gattapp.adaptor.MedicineAdaptor;
import net.puzzleco.gattapp.instance.ReminderInstance;
import net.puzzleco.gattapp.model.MedicineModel;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppButton;
import net.puzzleco.gattapp.view.AppToolbar;
import net.puzzleco.gattapp.view.AppText;

import java.util.ArrayList;

public class InitReminder implements MedicineAdaptor.MedicineInterface {
    public final static int MODE_LIST = 1555;
    public final static int MODE_ADD = 1556;
    public final static int MODE_VIEW = 1557;

    private static final int MIN_MAX = 59;
    private static final int HOUR_MAX = 23;

    public int current_mode;

    private float[] dimen;
    private ReminderActivity context;
    private int header_size;
    private TextView tv;
    private RelativeLayout layout;
    private int margin;
    private AppButton check;
    private MedicineModel current_item;
    private RecyclerView view;

    public InitReminder(ReminderActivity medicineActivity) {
        this.context = medicineActivity;
        this.dimen = Util.getDimen(context);
        this.header_size = ViewUtil.getToolbarSize(context);
        this.margin = ViewUtil.toPx(20, context);
    }

    public View getView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.setBackgroundResource(R.color.base);
        layout.addView(toolbar());
        layout.addView(box());
        return layout;
    }

    private View box() {
        layout = new RelativeLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
        return layout;
    }

    private View toolbar() {
        AppToolbar toolbar = new AppToolbar(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, header_size);
        toolbar.setLayoutParams(p);
        toolbar.setBackgroundResource(R.color.white);
        toolbar.addView(navigation());
        toolbar.addView(check());
        toolbar.addView(title());
        return toolbar;
    }

    private View check() {
        check = new AppButton(context);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(header_size, header_size);
        p.gravity = Gravity.RIGHT;
        check.setLayoutParams(p);
        check.setBackgroundResource(R.drawable.ic_content_clear);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(current_item);
            }
        });
        return check.getView();
    }

    private View title() {
        tv = new TextView(context);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(-2, -2);
        p.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        p.leftMargin = ViewUtil.toPx(8, context);
        tv.setLayoutParams(p);
        tv.setTextSize(1, 18);
        tv.setSingleLine();
        tv.setTextColor(context.getResources().getColor(R.color.theme));
        return tv;
    }

    private View navigation() {
        AppButton button = new AppButton(context);
        button.setBackgroundResource(R.drawable.ic_hardware_keyboard_backspace);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(header_size, header_size);
        p.gravity = Gravity.LEFT;
        button.setLayoutParams(p);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_mode == MODE_LIST) {
                    context.finish();
                } else {
                    setView(MODE_LIST, null);
                }
            }
        });
        return button.getView();
    }

    public void setView(int mode, MedicineModel item) {
        if (mode == current_mode) {
            return;
        }
        current_item = item;
        current_mode = mode;
        if (layout.getChildCount() == 0) {
            addView(mode, item);
        } else {
            animateView(false, item, mode);
        }
    }

    private void addView(int mode, MedicineModel item) {
        if (mode == MODE_LIST) {
            tv.setText("Medicine Reminder");
            check.getView().setVisibility(View.GONE);
            layout.addView(list());
            layout.addView(fab());
        } else {
            if (mode == MODE_ADD) {
                tv.setText("New Reminder");
                check.getView().setVisibility(View.GONE);
            } else {
                tv.setText(item.getName());
                check.getView().setVisibility(View.VISIBLE);
            }
            layout.addView(item(item, mode));
        }
    }

    private void animateView(final boolean flag, final MedicineModel item, final int mode) {
        AlphaAnimation alphaAnimation;
        if (flag) {
            addView(mode, item);
            alphaAnimation = new AlphaAnimation(0, 1);
        } else {
            alphaAnimation = new AlphaAnimation(1, 0);
        }
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setFillAfter(true);
        layout.startAnimation(alphaAnimation);
        if (!flag) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout.removeAllViews();
                    animateView(true, item, mode);
                }
            }, 300);
        }
    }

    private View fab() {
        final FloatingActionButton fab = new FloatingActionButton(context);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-2, -2);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            p.setMargins(margin, margin, margin, margin);
            fab.setElevation(6);
        }
        fab.setLayoutParams(p);
        fab.setBackgroundColor(context.getResources().getColor(R.color.theme));
        fab.setImageResource(R.drawable.ic_content_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView(MODE_ADD, new MedicineModel());
            }
        });
        return fab;
    }

    private View list() {
        view = new RecyclerView(context);
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setHasFixedSize(true);
        view.setAdapter(new MedicineAdaptor(context, dimen, getList(), this));
        return view;
    }

    private ArrayList<MedicineModel> getList() {
        ArrayList<MedicineModel> list = ReminderInstance.getAll(context);
        if (list.size() == 0) {
            list.add(null);
        }
        return list;
    }

    private View item(MedicineModel item, int mode) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, -1);
        p.setMargins(margin * 2, 0, margin * 2, 0);
        layout.setLayoutParams(p);
        layout.addView(space());
        layout.addView(editText(item, 0));
        layout.addView(space());
        layout.addView(editText(item, 1));
        layout.addView(space());
        layout.addView(text());
        layout.addView(picker(item));
        layout.addView(space());
        layout.addView(button(item, mode));
        layout.addView(space());
        return layout;
    }

    private View picker(MedicineModel model) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        layout.addView(pick(true, model));
        layout.addView(sep());
        layout.addView(pick(false, model));
        return layout;
    }

    private View sep() {
        AppText text = new AppText(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-2, -2);
        p.gravity = Gravity.CENTER_VERTICAL;
        text.setLayoutParams(p);
        text.setTextColor(Color.GRAY);
        text.setTextSize(1, 25);
        text.setText("    :    ");
        text.setSingleLine();
        return text;
    }

    private View pick(final boolean isHour, MedicineModel model) {
        final NumberPicker picker = new NumberPicker(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-2, -2, 1f);
        p.gravity = Gravity.CENTER_VERTICAL;
        picker.setLayoutParams(p);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setEnabled(true);
        if (isHour) {
            picker.setMinValue(0);
            picker.setMaxValue(HOUR_MAX);
            picker.setValue(model.getHour());
        } else {
            picker.setMinValue(0);
            picker.setMaxValue(MIN_MAX);
            picker.setValue(model.getMinute());
        }
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                if (i < 10) {
                    return "0" + i;
                }
                return String.valueOf(i);
            }
        });
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d("TAG", "onValueChange() returned: " + i + "   " + i1);
                if (isHour) {
                    current_item.setHour(i1);
                } else {
                    current_item.setMinute(i1);
                }
            }
        });
        return picker;
    }

    private View text() {
        AppText text = new AppText(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = margin;
        text.setLayoutParams(p);
        text.setTextColor(Color.GRAY);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setSingleLine();
        text.setTextSize(1, 13);
        text.setText("Frequency in hours");
        return text;
    }

    private View editText(MedicineModel item, final int i) {
        EditText text = new EditText(context);
        text.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        text.setEllipsize(TextUtils.TruncateAt.END);
        text.setSingleLine();
        text.setTextColor(context.getResources().getColor(R.color.theme_fade));
        text.setHintTextColor(Color.GRAY);
        if (i == 0) {
            text.setHint("Medicine Name");
            if (item != null) {
                text.setText(item.getName());
            }
        } else {
            text.setHint("Notes");
            if (item != null) {
                text.setText(item.getNote());
            }
        }
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (i == 0) {
                    current_item.setName(editable.toString());
                } else {
                    current_item.setNote(editable.toString());
                }
            }
        });
        return text;
    }

    private View space() {
        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
        return space;
    }

    private View button(final MedicineModel item, int mode) {
        Button button = new Button(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, ViewUtil.toPx(60, context));
        p.setMargins(margin, 0, margin, 0);
        button.setLayoutParams(p);
        button.setTextColor(Color.WHITE);
        button.setTextSize(1, 14);
        if (mode == MODE_ADD) {
            button.setText("Create");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getHour() == 0 && item.getMinute() < 30) {
                        Toast.makeText(context, "Minimum of minutes is 30", Toast.LENGTH_SHORT).show();
//                        TODO REMOVE IT
                        ReminderInstance.put(context, item);
                        setView(MODE_LIST, null);
                    } else {
                        ReminderInstance.put(context, item);
                        setView(MODE_LIST, null);
                    }
                }
            });
        } else {
            button.setText("Save");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getHour() == 0 && item.getMinute() < 30) {
                        Toast.makeText(context, "Minimum is 30 minutes", Toast.LENGTH_SHORT).show();
//                        TODO REMOVE IT
//                        ReminderInstance.edit(context, item);
//                        setView(MODE_LIST, null);
                    } else {
                        ReminderInstance.edit(context, item);
                        setView(MODE_LIST, null);
                        Toast.makeText(context, "Changes has been saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        button.setAllCaps(false);
        button.setBackgroundResource(R.drawable.button_box);
        return button;
    }

    @Override
    public void delete(MedicineModel model) {
        ReminderInstance.remove(context, model);
        if (current_mode != MODE_LIST) {
            setView(MODE_LIST, null);
        } else {
            updateView();
        }
    }

    private void updateView() {
        if (view != null)
            view.setAdapter(new MedicineAdaptor(context, dimen, getList(), this));
    }

    @Override
    public void edit(MedicineModel model) {
        setView(InitReminder.MODE_VIEW, model);
    }
}
