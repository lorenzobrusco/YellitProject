package unical.master.computerscience.yellit.utilities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionCheckUtils {

    /**
     * It used to check if all permissions are granted
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
}