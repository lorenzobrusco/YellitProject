package unical.master.computerscience.yellit.graphic.Activities;

/**
 * Created by Lorenzo on 22/02/2017.
 */

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import petrov.kristiyan.colorpicker.ColorPicker;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Dialog.CustomDialogPrivacy;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.utilities.PrefManager;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * Created by Lorenzo on 07/09/2016.
 */
public class SettingActivity extends AppCompatActivity {

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String PREF_NAME = "yellit-pref";

    private static final String ISFIRSTTIMEPIN = "isFirstTime";


    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.privacy)
    protected LinearLayout privacy;

    @Bind(R.id.welcomePage)
    protected LinearLayout welcomePage;

    @Bind(R.id.EnableColorDisplay)
    protected Switch mColorModeSwitch;

    @Bind(R.id.enable_safe_mode_switch)
    protected Switch mSafeModeSwitch;

    @Bind(R.id.colors_layout)
    protected LinearLayout mColorsLinearLayout;

    @Bind(R.id.color_1_button_botton_bar)
    protected GridLayout mFitnessButtonHome;

    @Bind(R.id.color_2_button_botton_bar)
    protected GridLayout mProfileButtonHome;

    @Bind(R.id.color_3_button_botton_bar)
    protected GridLayout mHomeButtonHome;

    @Bind(R.id.color_4_button_botton_bar)
    protected GridLayout mNewPostButtonHome;

    @Bind(R.id.color_5_button_botton_bar)
    protected GridLayout mMenuButtonHome;

    @Bind(R.id.pin_safe_mode_layout)
    protected LinearLayout mPinSafeModeLinearLayout;

    private Animation mAnimationDown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        changeSystemBar(this, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mAnimationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        this.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildPrivacyDialog();
            }
        });
        this.mColorModeSwitch.setChecked(PrefManager.getInstace(getApplicationContext()).isColorMode());
        this.mColorsLinearLayout.setVisibility(this.mColorModeSwitch.isChecked() ? View.VISIBLE : View.GONE);
        this.mColorModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InfoManager.getInstance().setColorMode(isChecked);
                PrefManager.getInstace(getApplicationContext()).setColorMode(isChecked);
                mAnimationDown.reset();
                mColorsLinearLayout.clearAnimation();
                if (isChecked) {
                    mColorsLinearLayout.startAnimation(mAnimationDown);
                    mColorsLinearLayout.setVisibility(View.VISIBLE);
                } else
                    mColorsLinearLayout.setVisibility(View.GONE);
            }
        });
        this.mSafeModeSwitch.setChecked(PrefManager.getInstace(getApplicationContext()).isSafeMode());
        this.mSafeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InfoManager.getInstance().setColorMode(isChecked);
                PrefManager.getInstace(getApplicationContext()).setSafeMode(isChecked);
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

        this.mFitnessButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildColorPicker();
            }
        });
        this.mProfileButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildColorPicker();
            }
        });
        this.mHomeButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildColorPicker();
            }
        });
        this.mNewPostButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildColorPicker();
            }
        });
        this.mMenuButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildColorPicker();
            }
        });
        this.mPinSafeModeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mChangePinIntent = new Intent(getBaseContext(), ChangePinSafeModeActivity.class);
                mChangePinIntent.putExtra(ISFIRSTTIMEPIN, true);
                startActivity(mChangePinIntent);
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

    private void buildColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(SettingActivity.this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#3a7e30");
        colors.add("#b36000");
        colors.add("#30487b");
        colors.add("#af2925");
        colors.add("#3c2d75");
        colors.add("#82B926");
        colors.add("#a276eb");
        colors.add("#6a3ab2");
        colors.add("#666666");
        colors.add("#FFFF00");
        colors.add("#3C8D2F");
        colors.add("#FA9F00");
        colors.add("#FF0000");

        colorPicker.setColors(colors).setDefaultColorButton(Color.parseColor("#f84c44")).setColumns(5).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                Log.d("position", "" + position);// will be fired only when OK button was tapped
            }

            @Override
            public void onCancel() {

            }
        }).addListenerButton("newButton", new ColorPicker.OnButtonListener() {
            @Override
            public void onClick(View v, int position, int color) {
                Log.d("position", "" + position);
            }
        }).setRoundColorButton(true).show();
    }
}