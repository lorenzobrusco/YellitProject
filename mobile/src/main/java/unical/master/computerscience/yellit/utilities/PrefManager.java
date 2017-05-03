package unical.master.computerscience.yellit.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lorenzo on 13/08/2016.
 */
public class PrefManager {

    /**
     * shared pref mode
     */
    private static int PRIVATE_MODE = 0;

    /**
     * Shared preferences file name
     */
    private static final String PREF_NAME = "yellit-pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_COLOR_MODE = "isColorModes";
    private static final String USER = "user";

    public static void setFirstTimeLaunch(final Context context, boolean isFirstTime) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(final Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public static void setColorMode(final Context context, boolean isColorMode) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_COLOR_MODE, isColorMode);
        editor.apply();
    }

    public static boolean isColorMode(final Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(IS_COLOR_MODE, true);
    }


    public static void setUser(final Context context, final String user) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(IS_COLOR_MODE, user);
        editor.apply();
    }

    public static String getUser(final Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getString(USER, null);
    }

}