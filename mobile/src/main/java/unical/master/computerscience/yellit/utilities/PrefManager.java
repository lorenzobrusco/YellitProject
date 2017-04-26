package unical.master.computerscience.yellit.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lorenzo on 13/08/2016.
 */
public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "yellit-pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_COLOR_MODE = "isColorMode";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setColorMode(boolean isColorMode) {
        editor.putBoolean(IS_COLOR_MODE, isColorMode);
        editor.commit();
    }

    public boolean isColorMode(){
        return pref.getBoolean(IS_COLOR_MODE, true);
    }



}