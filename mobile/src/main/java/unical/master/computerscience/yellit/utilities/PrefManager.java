package unical.master.computerscience.yellit.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lorenzo on 13/08/2016.
 */
public class PrefManager {

    /**
     * Singleton pattern
     */
    private static PrefManager mPrefManager;

    /**
     * share pref
     */
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    /**
     * shared pref mode
     */
    private static int PRIVATE_MODE = 0;

    /**
     * Shared preferences file name
     */
    private static final String PREF_NAME = "yellit-pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_COLOR_MODE = "isColorMode";
    private static final String IS_SAFE_MODE = "isSafeMode";
    private static final String USER = "user";

    private PrefManager(final Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public static PrefManager getInstace(final Context context) {
        if (mPrefManager == null)
            mPrefManager = new PrefManager(context);
        return mPrefManager;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        mEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        mEditor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return mPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setColorMode(boolean isColorMode) {
        mEditor.putBoolean(IS_COLOR_MODE, isColorMode);
        mEditor.commit();
    }

    public boolean isColorMode() {
        return mPreferences.getBoolean(IS_COLOR_MODE, false);
    }

    public void setSafeMode(boolean isSafeMode) {
        mEditor.putBoolean(IS_SAFE_MODE, isSafeMode);
        mEditor.commit();
    }

    public boolean isSafeMode() {
        return mPreferences.getBoolean(IS_SAFE_MODE, true);
    }

    public void setUser(final String user) {
        mEditor.putString(USER, user);
        mEditor.commit();
    }

    public String getUser() {
        return mPreferences.getString(USER, null);
    }

}