package net.puzzleco.gattapp.init;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.LaunchActivity;

public class InitLaunch {
    private LaunchActivity context;
    public View layout;

    public InitLaunch(LaunchActivity launchActivity) {
        this.context = launchActivity;
    }

    public View getView() {
        layout = new View(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.setBackgroundResource(R.color.theme);
        return layout;
    }

    public void runError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sorry").setMessage("This phone does not support Bluetooth Low Energy.").setCancelable(false)
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.finish();
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
