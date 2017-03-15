package net.puzzleco.gattapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.init.InitMedical;
import net.puzzleco.gattapp.instance.IDInstance;
import net.puzzleco.gattapp.model.IDModel;
import net.puzzleco.gattapp.util.ViewUtil;

public class MedicalActivity extends BaseCompatActivity implements View.OnClickListener {
    public IDModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, R.color.dark);
        setContentView(new InitMedical(this).getView());
        setResult(RESULT_OK);
    }

    @Override
    public void onClick(View view) {
        IDInstance.set(this, model);
        Toast.makeText(MedicalActivity.this, "Changes has been saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
