package net.puzzleco.gattapp.adaptor;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ScanActivity;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppText;

import java.util.ArrayList;

public class DeviceAdaptor extends RecyclerView.Adapter<DeviceAdaptor.Holder> implements View.OnClickListener {
    private static final int CLICKABLE_ID = +4284;
    private static final int ADDRESS_ID = +47782;
    private static final int NAME_ID = +714878;
    private static final int LOGO_ID = +8482;

    private int margin;
    private ArrayList<BluetoothDevice> list;
    private ScanActivity context;
    private int image_size;
    private int item_size;
    private float radius;

    public DeviceAdaptor(ScanActivity context, ArrayList<BluetoothDevice> objects, float[] dimen) {
        this.context = context;
        this.list = objects;
        this.item_size = ViewUtil.toPx(65, context);
        this.image_size = ViewUtil.toPx(30, context);
        this.margin = ViewUtil.toPx(10, context);
        this.radius = ViewUtil.toPx(2, context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        BluetoothDevice device = list.get(position);

        holder.clickable.setTag(device);
        holder.name.setText("Device name: " + device.getName());
        holder.mac.setText("MAC address: " + device.getAddress());
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, -2);
        if (viewType == 0) {
            pp.setMargins(margin, margin, margin, margin);
        } else {
            pp.setMargins(margin, 0, margin, margin);
        }
        cardView.setLayoutParams(pp);
        cardView.setCardElevation(2);
        cardView.setRadius(radius);
        RelativeLayout layout = new RelativeLayout(context);
        CardView.LayoutParams p = new CardView.LayoutParams(-1, item_size);
        p.gravity = Gravity.CENTER;
        layout.setLayoutParams(p);

        layout.addView(logo());
        layout.addView(box());
        layout.addView(clickable());

        cardView.addView(layout);
        return new Holder(cardView);
    }

    private View box() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, -1);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.addRule(RelativeLayout.RIGHT_OF, LOGO_ID);
        p.rightMargin = margin;
        layout.setLayoutParams(p);
        layout.addView(space());
        layout.addView(tv(NAME_ID));
        layout.addView(space());
        layout.addView(tv(ADDRESS_ID));
        layout.addView(space());
        return layout;
    }

    private View space() {
        Space space = new Space(context);
        space.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
        return space;
    }

    private View clickable() {
        View view = new View(context);
        view.setId(CLICKABLE_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(2);
        }
        view.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        ViewUtil.setBackground(view, context);
        view.setOnClickListener(this);
        return view;
    }

    private View logo() {
        View view = new View(context);
        view.setId(LOGO_ID);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(image_size, image_size);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        p.leftMargin = margin;
        p.rightMargin = margin;
        view.setLayoutParams(p);
        view.setBackgroundResource(R.mipmap.ic_launcher);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() != null) {
            context.executeDevice((BluetoothDevice) view.getTag());
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        View clickable;
        AppText name;
        AppText mac;

        public Holder(View v) {
            super(v);
            clickable = v.findViewById(CLICKABLE_ID);
            name = (AppText) v.findViewById(NAME_ID);
            mac = (AppText) v.findViewById(ADDRESS_ID);
        }
    }

    private View tv(int id) {
        AppText textView = new AppText(context);
        textView.setId(id);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        textView.setLayoutParams(p);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView.setTextColor(Color.DKGRAY);
        return textView;
    }
}
