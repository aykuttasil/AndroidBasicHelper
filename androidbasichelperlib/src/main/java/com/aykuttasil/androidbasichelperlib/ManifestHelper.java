package com.aykuttasil.androidbasichelperlib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by aykutasil on 24.01.2017.
 */
public class ManifestHelper {

    public ManifestHelper() {
    }

    public static int getMetadataInt(Context context, String key) {

        Integer value = getIntMetadata(context, key);

        if (value == null || value == 0) {
            value = 1;
        }

        return value;
    }

    public static String getMetaDataString(Context context, String key) {

        String value = getStringMetadata(context, key);

        if (value == null) {
            value = "";
        }

        return value;
    }

    public static boolean getMetaDataBoolean(Context context, String key) {

        return getBooleanMetadata(context, key);
    }

    private static String getStringMetadata(Context context, String key) {

        String value = null;

        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = applicationInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    private static Integer getIntMetadata(Context context, String key) {

        Integer value = null;

        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = applicationInfo.metaData.getInt(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    private static Boolean getBooleanMetadata(Context context, String key) {

        Boolean flag = false;

        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            flag = applicationInfo.metaData.getBoolean(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
