package unical.master.computerscience.yellit.graphic.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;

import static unical.master.computerscience.yellit.utilities.SystemUI.changeSystemBar;

/**
 * Activity that allow us to change the  pin in the safemode if
 * we don't use the fingerprint
 */
public class ChangePinSafeModeActivity extends AppCompatActivity {

    private static final String ISFIRSTTIME = "isFirstTime";
    private boolean isFirstTime;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.old_pin_safe_mode_layout)
    protected LinearLayout mOldPinLinearLayout;
    @Bind(R.id.input_old_pin)
    protected EditText mOldPinEditText;
    @Bind(R.id.input_new_pin)
    protected EditText mNewPinEditText;
    @Bind(R.id.input_repeat_new_pin)
    protected EditText mRepeatNewPinEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeSystemBar(this, false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_safe_mode);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        isFirstTime = getIntent().getBooleanExtra(ISFIRSTTIME, true);
        mOldPinLinearLayout.setVisibility((isFirstTime) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changepinmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
