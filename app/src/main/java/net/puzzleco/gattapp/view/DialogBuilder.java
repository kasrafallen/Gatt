package net.puzzleco.gattapp.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;

import net.puzzleco.gattapp.R;

public class DialogBuilder {

    private Context context;
    private float[] dimen;
    private AlertDialog dialog;

    public DialogBuilder(Context activity, float[] dimen) {
        this.context = activity;
        this.dimen = dimen;
    }

    public void scanDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true).setView(getDialogView());
        dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    private View getDialogView() {
        return null;
    }
}
