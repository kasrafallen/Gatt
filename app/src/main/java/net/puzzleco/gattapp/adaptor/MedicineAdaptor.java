package net.puzzleco.gattapp.adaptor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ReminderActivity;
import net.puzzleco.gattapp.model.MedicineModel;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppText;

import java.util.ArrayList;
import java.util.Random;

public class MedicineAdaptor extends RecyclerView.Adapter<MedicineAdaptor.ViewHolder> {

    private static final int HOUR_ID = +4484;
    private static final int NOTE_ID = +4845;
    private static final int NAME_ID = +2896;
    private static final int CLICKABLE_ID = +2586;
    private static final int MARKER_ID = +4171;
    private static final int OPTION_ID = +8717;

    public interface MedicineInterface {
        void delete(MedicineModel model);

        void edit(MedicineModel model);
    }

    private static final int[] COLORS_LIST
            = new int[]{Color.BLUE
            , Color.RED
            , Color.GRAY
            , Color.GREEN
            , Color.BLACK
            , Color.YELLOW
            , Color.DKGRAY
            , Color.CYAN
            , Color.MAGENTA
            , Color.LTGRAY};

    private ReminderActivity context;
    private float[] dimen;
    private MedicineInterface medicineInterface;
    private ArrayList<MedicineModel> list;
    private int cardView_size;
    private int margin;
    private int margin_side;
    private int margin_small;
    private int default_image_height;
    private int default_image_width;
    private int marker_size;
    private int option_size;
    private int line_size;

    public MedicineAdaptor(ReminderActivity context, float[] dimen, ArrayList<MedicineModel> list, MedicineInterface medicineInterface) {
        this.context = context;
        this.dimen = dimen;
        this.medicineInterface = medicineInterface;
        this.list = list;
        this.cardView_size = ViewUtil.toPx(110, context);
        this.margin = ViewUtil.toPx(18, context);
        this.margin_side = ViewUtil.toPx(25, context);
        this.margin_small = ViewUtil.toPx(9, context);
        this.marker_size = ViewUtil.toPx(18, context);
        this.option_size = ViewUtil.toPx(40, context);
        this.line_size = ViewUtil.toPx(1, context);
        this.default_image_width = ViewUtil.toPx(200, context);
        this.default_image_height = (int) (272f * default_image_width / 888f);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = createView(viewType);
        return new ViewHolder(view);
    }

    private View createView(int viewType) {
        if (viewType != -2) {
            CardView view = new CardView(context);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, cardView_size);
            if (viewType == 0) {
                p.setMargins(margin_side, margin, margin_side, margin_small);
            } else {
                p.setMargins(margin_side, margin_small, margin_side, margin_small);
            }
            view.setLayoutParams(p);
            view.setCardElevation(3);
            view.setRadius(1);

            RelativeLayout layout = new RelativeLayout(context);
            CardView.LayoutParams pp = new CardView.LayoutParams(-1, -1);
            layout.setLayoutParams(pp);
            layout.addView(marker());
            layout.addView(detail());

            view.addView(layout);
            view.addView(clickable());
            view.addView(option());
            return view;
        } else {
            RelativeLayout layout = new RelativeLayout(context);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams((int) dimen[0], -2);
            p.topMargin = (int) (dimen[1] / 3);
            layout.setLayoutParams(p);

            View image = new View(context);
            RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(default_image_width, default_image_height);
            pp.addRule(RelativeLayout.CENTER_IN_PARENT);
            image.setLayoutParams(pp);
            image.setBackgroundResource(R.mipmap.default_reminder);

            layout.addView(image);
            return layout;
        }
    }

    private View option() {
        View button = new View(context);
        button.setId(OPTION_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setElevation(2);
        }
        CardView.LayoutParams p = new CardView.LayoutParams(option_size, option_size);
        p.gravity = Gravity.RIGHT | Gravity.TOP;
        button.setLayoutParams(p);
        button.setBackgroundResource(R.drawable.ic_navigation_more_vert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() == null) {
                    return;
                }
                showOptions((MedicineModel) view.getTag(), view);
            }
        });
        return button;
    }

    private View detail() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, -2);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.addRule(RelativeLayout.RIGHT_OF, MARKER_ID);
        p.setMargins(margin_side, 0, option_size, 0);
        layout.setLayoutParams(p);

        layout.addView(text(NAME_ID));
        layout.addView(line());
        layout.addView(text(NOTE_ID));
        return layout;
    }

    private View line() {
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, line_size));
        view.setBackgroundColor(Color.LTGRAY);
        return view;
    }

    private View text(int id) {

        if (id == NAME_ID) {
            AppText text = new AppText(context);
            text.setId(NAME_ID);
            LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-2, -2);
            pp.bottomMargin = margin_small;
            pp.gravity = Gravity.LEFT;
            text.setLayoutParams(pp);
            text.setTextColor(context.getResources().getColor(R.color.theme));
            text.setTextSize(1, 20);
            text.setSingleLine();
            text.setGravity(Gravity.LEFT);
            text.setTypeface(text.getTypeface(), Typeface.BOLD);
            return text;
        }

        RelativeLayout layout = new RelativeLayout(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = margin_small;
        layout.setLayoutParams(p);

        AppText text1 = new AppText(context);
        text1.setId(NOTE_ID);
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(-2, -2);
        pp.addRule(RelativeLayout.CENTER_VERTICAL);
        pp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        text1.setLayoutParams(pp);
        text1.setTextColor(Color.GRAY);
        text1.setTextSize(1, 14);
        text1.setGravity(Gravity.LEFT);
        text1.setSingleLine();

        AppText text = new AppText(context);
        text.setId(HOUR_ID);
        RelativeLayout.LayoutParams ppp = new RelativeLayout.LayoutParams(-2, -2);
        ppp.addRule(RelativeLayout.CENTER_VERTICAL);
        ppp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ppp.addRule(RelativeLayout.RIGHT_OF, NOTE_ID);
        text.setLayoutParams(ppp);
        text.setTextColor(context.getResources().getColor(R.color.theme));
        text.setTextSize(1, 13);
        text.setGravity(Gravity.RIGHT);
        text.setSingleLine();

        layout.addView(text1);
        layout.addView(text);
        return layout;
    }

    private View marker() {
        View view = new View(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(4);
        }
        view.setId(MARKER_ID);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(marker_size, -1);
        p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view.setLayoutParams(p);
        return view;
    }

    private View clickable() {
        View view = new View(context);
        view.setId(CLICKABLE_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(1);
        }
        ViewUtil.setBackground(view, context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicineInterface.edit((MedicineModel) view.getTag());
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MedicineModel model = list.get(position);
        if (model == null) {
            return;
        }

        holder.name.setText(model.getName());
        holder.note.setText(model.getNote());
        holder.hour.setText("Every " + cast(model.getHour()) + ":" + cast(model.getMinute()));
        holder.clickable.setTag(model);
        holder.option.setTag(model);
        holder.marker.setBackgroundColor(getColor());
    }

    private int getColor() {
        return COLORS_LIST[new Random().nextInt(COLORS_LIST.length)];
    }

    private String cast(int number) {
        String cast = String.valueOf(number);
        if (cast.length() == 1) {
            cast = "0" + cast;
        }
        return cast;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 1 && list.get(0) == null) {
            return -2;
        }
        if (position == 0) {
            return 0;
        } else {
            return -1;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppText note;
        AppText hour;
        AppText name;
        View clickable;
        View option;
        View marker;

        public ViewHolder(View itemView) {
            super(itemView);
            this.hour = (AppText) itemView.findViewById(HOUR_ID);
            this.name = (AppText) itemView.findViewById(NAME_ID);
            this.note = (AppText) itemView.findViewById(NOTE_ID);
            this.option = itemView.findViewById(OPTION_ID);
            this.clickable = itemView.findViewById(CLICKABLE_ID);
            this.marker = itemView.findViewById(MARKER_ID);
        }
    }

    private void showOptions(final MedicineModel tag, View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setGravity(Gravity.BOTTOM);
        popupMenu.inflate(R.menu.main);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit) {
                    medicineInterface.edit(tag);
                } else if (item.getItemId() == R.id.delete) {
                    medicineInterface.delete(tag);
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
