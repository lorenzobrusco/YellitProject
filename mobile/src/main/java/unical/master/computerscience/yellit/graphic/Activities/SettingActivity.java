package unical.master.computerscience.yellit.graphic.Activities;

/**
 * Created by Lorenzo on 22/02/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.MainActivity;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Dialog.CustomDialogPrivacy;

/**
 * Created by Lorenzo on 07/09/2016.
 */
public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.privacy)
    LinearLayout privacy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        this.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildPrivacyDialog();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void buildPrivacyDialog() {
        final CustomDialogPrivacy customDialogPrivacy = new CustomDialogPrivacy(SettingActivity.this);
        customDialogPrivacy.show();
    }
}