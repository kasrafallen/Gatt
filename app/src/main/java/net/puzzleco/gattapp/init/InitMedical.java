package net.puzzleco.gattapp.init;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.MedicalActivity;
import net.puzzleco.gattapp.instance.IDInstance;
import net.puzzleco.gattapp.model.IDModel;
import net.puzzleco.gattapp.util.CalendarUtil;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppButton;
import net.puzzleco.gattapp.view.AppText;
import net.puzzleco.gattapp.view.AppToolbar;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class InitMedical {

    private static final int LOGO_ID = +5520;
    private static final int NAME = +4871872;
    private static final int EMERGENCY = +2444444;

    private static final int SEX = +848727;
    private static final int BLOOD = +848728;
    private static final int DATE = +4824999;

    private static final int WEIGHT = +88717;
    private static final int HEIGHT = +88727;

    private static final int MED_CON = +999500;
    private static final int MED_NOT = +51488;
    private static final int ALL_RE = +4287287;
    private static final int MEDIC = +844855;

    private MedicalActivity context;
    private int header_size;
    private int image_size;
    private int logo_size;
    private int margin;
    private boolean isEditable;

    public InitMedical(MedicalActivity medicalActivity) {
        this.context = medicalActivity;
        this.header_size = ViewUtil.getToolbarSize(context);
        this.margin = ViewUtil.toPx(11, context);
        this.logo_size = ViewUtil.toPx(20, context);
        this.image_size = ViewUtil.toPx(100, context);
        this.isEditable = context.getIntent().getAction() == null;
        context.model = IDInstance.get(context);
    }

    public View getView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.color.white);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.addView(toolbar());
        layout.addView(scroll());
        return layout;
    }

    private View toolbar() {
        AppToolbar toolbar = new AppToolbar(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, header_size);
        toolbar.setLayoutParams(p);
        toolbar.setBackgroundResource(R.color.dark_3);
        toolbar.addView(navigation());
        if (isEditable) {
            toolbar.addView(check());
        }
        toolbar.addView(title());
        return toolbar;
    }

    private View title() {
        AppText tv = new AppText(context);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(-2, -2);
        p.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        p.leftMargin = ViewUtil.toPx(8, context);
        tv.setTextSize(1, 18);
        tv.setTextColor(Color.WHITE);
        tv.setText("Medical ID");
        tv.setLayoutParams(p);
        return tv;
    }

    private View check() {
        AppButton button = new AppButton(context);
        button.setBackgroundResource(R.drawable.ic_navigation_check);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(header_size, header_size);
        p.gravity = Gravity.RIGHT;
        button.setLayoutParams(p);
        button.setOnClickListener(context);
        return button.getView();
    }

    private View navigation() {
        AppButton button = new AppButton(context);
        button.setBackgroundResource(R.drawable.ic_navigation);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(header_size, header_size);
        p.gravity = Gravity.LEFT;
        button.setLayoutParams(p);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });
        return button.getView();
    }

    private View scroll() {
        NestedScrollView scrollView = new NestedScrollView(context);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(image());
        layout.addView(nameField(NAME));
        layout.addView(spinner(SEX));
        layout.addView(spinner(BLOOD));
        layout.addView(date(DATE));
        layout.addView(detail(HEIGHT));
        layout.addView(detail(WEIGHT));
        layout.addView(donor());
        layout.addView(desc(MED_CON));
        layout.addView(desc(MED_NOT));
        layout.addView(desc(ALL_RE));
        layout.addView(desc(MEDIC));
        layout.addView(nameField(EMERGENCY));

        scrollView.addView(layout);
        return scrollView;
    }

    private View date(int date) {
        RelativeLayout layout = getField(R.mipmap.date);

        AppText text = new AppText(context);
        text.setId(date);
        text.setTextColor(Color.DKGRAY);
        text.setSingleLine();
        text.setTextSize(1, 13);
        text.setText("Date of Birth :   ");
        if (context.model.getBirth() != null) {
            text.append(CalendarUtil.getBirth(context.model.getBirth()));
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, ViewUtil.toPx(50, context));
        params.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        text.setLayoutParams(params);
        text.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        ViewUtil.setBackground(text, context);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditable) {
                    showBirthPicker();
                }
            }
        });

        View view = new View(context);
        view.setBackgroundColor(Color.GRAY);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, ViewUtil.toPx(1, context));
        p.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        p.addRule(RelativeLayout.BELOW, date);
        p.setMargins(margin / 3, 0, margin / 3, 0);
        view.setLayoutParams(p);

        layout.addView(text);
        layout.addView(view);
        return layout;
    }

    private View desc(int id) {
        RelativeLayout layout;
        if (id == MED_CON) {
            layout = getField(R.mipmap.condition);
        } else {
            layout = getField(R.color.transparent);
        }

        LinearLayout box = new LinearLayout(context);
        box.setId(+800855);
        box.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
        params.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        box.setLayoutParams(params);
        setBoxItems(box, id);

        layout.addView(box);
        return layout;
    }

    private View donor() {
        RelativeLayout layout = getField(R.mipmap.orgon);

        RelativeLayout box = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, ViewUtil.toPx(50, context));
        params.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        params.bottomMargin = margin;
        box.setLayoutParams(params);
        ViewUtil.setBackground(box, context);

        AppText text = new AppText(context);
        text.setSingleLine();
        text.setTextColor(Color.DKGRAY);
        text.setTextSize(1, 13);
        text.setText("Organ donor");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        text.setLayoutParams(lp);

        final AppCompatCheckBox checkBox = new AppCompatCheckBox(context);
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(-2, -2);
        pp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        pp.addRule(RelativeLayout.CENTER_VERTICAL);
        checkBox.setLayoutParams(pp);
        checkBox.setFocusableInTouchMode(false);
        checkBox.setFocusable(false);
        checkBox.setClickable(false);
        checkBox.setChecked(context.model.isDonor());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                context.model.setDonor(b);
            }
        });

        View view = new View(context);
        view.setBackgroundColor(Color.GRAY);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, ViewUtil.toPx(1, context));
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        p.setMargins(margin / 3, 0, margin / 3, 0);
        view.setLayoutParams(p);

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditable) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            }
        });

        box.addView(checkBox);
        box.addView(text);
        box.addView(view);
        layout.addView(box);
        return layout;
    }

    private View detail(final int id) {
        RelativeLayout layout;
        if (id == WEIGHT) {
            layout = getField(R.mipmap.weight);
        } else {
            layout = getField(R.mipmap.height);
        }

        EditText text = getEditText(true, id);
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (id == WEIGHT) {
            if (context.model.getWeight() != null)
                text.setText("" + context.model.getWeight());
        } else {
            if (context.model.getHeight() != null)
                text.setText("" + context.model.getHeight());
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
                if (id == WEIGHT) {
                    context.model.setWeight(editable.toString());
                } else {
                    context.model.setHeight(editable.toString());
                }
            }
        });

        AppText tv = new AppText(context);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-2, -2);
        p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setLayoutParams(p);
        tv.setTextColor(Color.GRAY);
        tv.setTextSize(1, 13);

        if (id == WEIGHT) {
            text.setHint("Weight");
            tv.setText("kg");
        } else {
            text.setHint("Height");
            tv.setText("cm");
        }

        layout.addView(text);
        layout.addView(tv);

        set(text);
        return layout;
    }

    private View spinner(final int id) {
        RelativeLayout layout;
        switch (id) {
            case SEX:
                layout = getField(R.mipmap.sex);
                break;
            default:
                layout = getField(R.mipmap.blood_type);
                break;
        }

        final Spinner spinner = new Spinner(context);
        spinner.setId(id);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        params.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        spinner.setLayoutParams(params);
        spinner.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        ArrayAdapter<String> spinnerData = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, getList(id));
        spinner.setAdapter(spinnerData);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (id == SEX) {
                    context.model.setSex(i);
                } else {
                    context.model.setBlood(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (id == SEX) {
            spinner.setSelection(context.model.getSex());
        } else {
            spinner.setSelection(context.model.getBlood());
        }

        View view = new View(context);
        view.setBackgroundColor(Color.GRAY);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, ViewUtil.toPx(1, context));
        p.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        p.addRule(RelativeLayout.BELOW, id);
        p.setMargins(margin / 3, 0, margin / 3, 0);
        view.setLayoutParams(p);

        layout.addView(spinner);
        layout.addView(view);

        set(spinner);

        spinner.setPadding(0, 0, 0, 0);
        return layout;
    }

    private String[] getList(int id) {
        if (id == BLOOD) {
            return IDModel.BLOOD_ARRAY;
        } else {
            return IDModel.SEX_ARRAY;
        }
    }

    private RelativeLayout getField(int resource) {
        RelativeLayout layout = new RelativeLayout(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(margin, 0, margin, 0);
        layout.setLayoutParams(p);

        View view = new View(context);
        view.setId(LOGO_ID);
        view.setBackgroundResource(resource);
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(logo_size, logo_size);
        pp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        pp.addRule(RelativeLayout.CENTER_VERTICAL);
        pp.rightMargin = margin;
        view.setLayoutParams(pp);

        layout.addView(view);
        return layout;
    }

    private View nameField(final int id) {
        RelativeLayout layout;
        EditText text = getEditText(true, id);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (id == NAME) {
                    context.model.setName(editable.toString());
                } else {
                    context.model.setEmergency(editable.toString());
                }
            }
        });
        if (id == NAME) {
            layout = getField(R.mipmap.name);
            text.setHint("Name");
            if (context.model.getName() != null)
                text.setText(context.model.getName());
        } else {
            layout = getField(R.mipmap.emergency);
            text.setHint("Emergency contact");
            if (context.model.getEmergency() != null)
                text.setText(context.model.getEmergency());
        }
        set(text);
        layout.addView(text);
        return layout;
    }

    private EditText getEditText(boolean isSingleLine, int id) {
        EditText text = new EditText(context);
        text.setId(id);
        text.setSingleLine(isSingleLine);
        text.setEllipsize(TextUtils.TruncateAt.END);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, ViewUtil.toPx(50, context));
        params.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(0, margin / 2, 0, margin / 2);
        text.setLayoutParams(params);
        text.setTextColor(Color.DKGRAY);
        text.setTextSize(1, 13);
        text.setHintTextColor(Color.GRAY);
        text.setPadding(text.getPaddingLeft(), 10 * text.getPaddingTop() / 9, text.getPaddingRight(), 10 * text.getPaddingBottom() / 9);
        return text;
    }

    private View image() {
        CircleImageView imageView = new CircleImageView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(image_size, image_size);
        p.gravity = Gravity.CENTER_HORIZONTAL;
        p.setMargins(0, margin, 0, margin);
        imageView.setLayoutParams(p);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.main_tes);
        return imageView;
    }

    private void setBoxItems(LinearLayout box, final int id) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 0, 0, (int) (1f * margin / 2f));

        AppText text = new AppText(context);
        text.setSingleLine();
        text.setGravity(Gravity.LEFT);
        text.setTextColor(Color.GRAY);
        text.setTextSize(1, 13);

        EditText desc = new EditText(context);
        desc.setEllipsize(TextUtils.TruncateAt.END);
        desc.setSingleLine(false);
        desc.setTextColor(Color.DKGRAY);
        desc.setTextSize(1, 12);
        desc.setHintTextColor(Color.GRAY);
        desc.setGravity(Gravity.LEFT);

        switch (id) {
            case MED_CON:
                text.setText("Medical Conditions");
                desc.setHint("Describe your medical conditions\n" +
                        "(e.g. hemophilia,diabet)");
                if (context.model.getConditions() != null) {
                    desc.setText(context.model.getConditions());
                }
                break;
            case MED_NOT:
                text.setText("Medical Notes");
                desc.setHint("Type any special instructions or other\n" +
                        "information");
                if (context.model.getNotes() != null) {
                    desc.setText(context.model.getNotes());
                }
                break;
            case MEDIC:
                text.setText("Medications");
                desc.setHint("List the medications you are taking\n" +
                        "(name,dosage,frequency)");
                if (context.model.getMedications() != null) {
                    desc.setText(context.model.getMedications());
                }
                break;
            case ALL_RE:
                text.setText("Allergies & Reactions");
                desc.setHint("Type your allergies and their associated\n" +
                        "symptoms");
                if (context.model.getAllergies() != null) {
                    desc.setText(context.model.getAllergies());
                }
                break;
        }

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                switch (id) {
                    case MED_CON:
                        context.model.setConditions(editable.toString());
                        break;
                    case MED_NOT:
                        context.model.setNotes(editable.toString());
                        break;
                    case MEDIC:
                        context.model.setMedications(editable.toString());
                        break;
                    case ALL_RE:
                        context.model.setAllergies(editable.toString());
                        break;
                }
            }
        });

        box.addView(text, p);
        box.addView(desc, p);

        set(desc);
    }

    private void showBirthPicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                AppText text = (AppText) context.findViewById(DATE);
                text.setText("Date of Birth :   ");
                int data[] = new int[]{dayOfMonth, monthOfYear + 1, year};
                text.append(CalendarUtil.getBirth(data));
                context.model.setBirth(data);
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    private void set(View view) {
        if (!isEditable) {
            view.setLongClickable(false);
            view.setFocusableInTouchMode(false);
            view.setFocusable(false);
            view.setClickable(false);

            if (view instanceof Spinner) {
                view.setEnabled(false);
            }
        }
    }
}
