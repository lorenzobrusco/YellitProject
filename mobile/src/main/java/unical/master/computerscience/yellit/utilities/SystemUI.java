package unical.master.computerscience.yellit.utilities;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import unical.master.computerscience.yellit.R;

/**
 * Created by Lorenzo on 02/05/2017.
 */

public class SystemUI {

    /**
     * Changes the System Bar Theme.
     */
    public static void changeSystemBar(final Activity pActivity, final boolean pIsDark){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setSystemBarTheme(pActivity, pIsDark);
        } else {
            pActivity.setTheme(R.style.AppThemeL);
        }
    }

    /**
     * Changes the System Bar Theme.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static final void setSystemBarTheme(final Activity pActivity, final boolean pIsDark) {
        // Fetch the current flags.
        final int lFlags = pActivity.getWindow().getDecorView().getSystemUiVisibility();
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        pActivity.getWindow().getDecorView().setSystemUiVisibility(pIsDark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
    }

}
