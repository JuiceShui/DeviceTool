package com.jojo.devicetool.util;

/**
 * Description: Jojo on 2019/3/26
 */

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


public class PermissionUtil {
    public static final int PERMISSION_REQUEST_CODE = 1;

    private static String TAG = "PermissionUtil";

    public static boolean permissionGranted(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false;
        else return true;
    }

    public static void checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < 23)
            return;
        if (permissionGranted(activity)) Log.d(TAG, "Permission granted!");
        if (!permissionGranted(activity)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        }
    }
}

