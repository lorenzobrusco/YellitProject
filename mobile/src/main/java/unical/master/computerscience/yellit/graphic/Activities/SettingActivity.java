package unical.master.computerscience.yellit.graphic.Activities;

/**
 * Created by Lorenzo on 22/02/2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Dialog.CustomDialogPrivacy;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.utilities.PrefManager;

/**
 * Created by Lorenzo on 07/09/2016.
 */
public class SettingActivity extends AppCompatActivity {

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String PREF_NAME = "androidhive-welcome";

    @Bind(R.id.privacy)
    LinearLayout privacy;
    @Bind(R.id.welcomePage)
    LinearLayout welcomePage;
    @Bind(R.id.EnableColorDisplay)
    Switch mColorModeSwitch;

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
        this.mColorModeSwitch.setChecked(new PrefManager(this).isColorMode());
        this.mColorModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InfoManager.getInstance().setColorMode(isChecked);
                new PrefManager(SettingActivity.this).setColorMode(isChecked);
            }
        });
        this.welcomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = SettingActivity.this.getSharedPreferences(PREF_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(IS_FIRST_TIME_LAUNCH, true);
                editor.commit();
                startActivity(new Intent(SettingActivity.this, WelcomeActivity.class));
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