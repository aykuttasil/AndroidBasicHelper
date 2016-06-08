package com.aykuttasil.androidbasichelperlib;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hugo.weaving.DebugLog;

public class SuperHelper {

    private static final String TAG = SuperHelper.class.getSimpleName();


    @DebugLog
    public static boolean deleteDirectoryContent(File path) {
        try {
            if (path.exists()) {
                File[] files = path.listFiles();
                if (files == null) {
                    return true;
                }
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    @DebugLog
    public static String getDeviceId(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonymanager.getDeviceId();
    }

    @DebugLog
    public static String getSimSerialNumber(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonymanager.getSimSerialNumber();
    }

    @DebugLog
    public static String getOperetorName(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonymanager.getNetworkOperatorName();
    }

    @DebugLog
    public static String getDeviceLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getDisplayName();
    }

    @DebugLog
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        if (manufacturer.equalsIgnoreCase("HTC")) {
            // make sure "HTC" is fully capitalized.
            return "HTC " + model;
        }
        return capitalize(manufacturer) + " " + model;
    }

    @DebugLog
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    @DebugLog
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    @DebugLog
    public static String getDeviceDate() {
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return sdfDateTime.format(new Date(System.currentTimeMillis()));
    }

    @DebugLog
    public static boolean validateTCKN(String TCKN) {
        try {
            Float.parseFloat(TCKN);
            if (TCKN.length() != 11) {
                return false;
            }
            if (TCKN.substring(0, 1).equals("0")) {
                return false;
            }

            int n1 = Integer.parseInt(TCKN.substring(0, 1));
            int n2 = Integer.parseInt(TCKN.substring(1, 2));
            int n3 = Integer.parseInt(TCKN.substring(2, 3));
            int n4 = Integer.parseInt(TCKN.substring(3, 4));
            int n5 = Integer.parseInt(TCKN.substring(4, 5));
            int n6 = Integer.parseInt(TCKN.substring(5, 6));
            int n7 = Integer.parseInt(TCKN.substring(6, 7));
            int n8 = Integer.parseInt(TCKN.substring(7, 8));
            int n9 = Integer.parseInt(TCKN.substring(8, 9));
            int n10 = Integer.parseInt(TCKN.substring(9, 10));
            int n11 = Integer.parseInt(TCKN.substring(10, 11));

            if ((((n1 + n3 + n5 + n7 + n9) * 7) - (n2 + n4 + n6 + n8)) % 10 != n10) {
                return false;
            }

            return (n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10) % 10 == n11;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * <h1>ReplaceFragmentBeginTransaction</h1>
     * <p>Activity içerisinde iken ve bu Activity içerisinde bulunan FragmentContainer a bir Fragment atama
     * yapılacak iken bu fonksiyon çağırılır.</p>
     *
     * @param activity
     * @param fragment
     * @param containerID
     * @param tag
     * @param isBackStack
     */
    @DebugLog
    public static void ReplaceFragmentBeginTransaction(AppCompatActivity activity, Fragment fragment, int containerID, String tag, boolean isBackStack) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(containerID, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * <h1>ReplaceFragmentBeginTransaction</h1>
     * <p>Fragment içerisinde iken ve bu Fragment içerisinde bulunan FragmentContainer a başka bir Fragment a atama
     * yapılacak iken bu fonksiyon çağırılır.</p>
     *
     * @param container
     * @param fragment
     * @param containerID
     * @param tag
     * @param isBackStack
     * @param popStackName
     */
    @DebugLog
    public static void ReplaceFragmentBeginTransaction(Fragment container, Fragment fragment, int containerID, String tag, boolean isBackStack, String popStackName) {
        FragmentTransaction fragmentTransaction = container.getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (isBackStack) {
            if (!popStackName.equals("")) {
                fragmentTransaction.addToBackStack(popStackName);
            } else {
                fragmentTransaction.addToBackStack(null);
            }
        }
        //fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.replace(containerID, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @DebugLog
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @DebugLog
    public static void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @DebugLog
    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setClickable(true);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }

    /*
    @DebugLog
    public static void ShowDatePickerViewClick(final EditText editText, final AppCompatActivity activity) {


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setThemeCustom(R.style.MyCustomBetterPickerTheme)
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                editText.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
                            }
                        })
                        .setFirstDayOfWeek(Calendar.MONDAY)
                                //.setPreselectedDate(towDaysAgo.getYear(), towDaysAgo.getMonthOfYear() - 1, towDaysAgo.getDayOfMonth())
                                //.setDateRange(minDate, null)
                        .setThemeDark(true);
                cdp.show(activity.getSupportFragmentManager(), "DPD");


            }
        });
    }
    */

    @DebugLog
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @DebugLog
    public static void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }

    @DebugLog
    public static String getFormatTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    @DebugLog
    public static String getFormatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    @DebugLog
    public static String getFormatTimeMinute(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    @DebugLog
    public static String getFormatTimeDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }


    @DebugLog
    public static boolean isServiceRunning(Activity activity, String serviceName) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            //ComponentName componentName = service.service;
            //service.service
//            if (service.flags == ActivityManager.RunningServiceInfo.FLAG_FOREGROUND) {
//                Log.i("isServiceRunning", String.valueOf(service.flags));
//                return true;
//            }

            //Log.i("isServiceRunningClsName", service.service.getClassName());
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /*
    public static List<ActivityManager.RunningServiceInfo> getRunningServices(int maxNum , Activity activity) {
         ActivityManager.RunningServiceInfo lst =(ActivityManager.RunningServiceInfo) activity.getSystemService(Context.ACTIVITY_SERVICE);

    }
    */

    @DebugLog
    public static String getStandartText(String text) {

        if (text == null) {
            return "";
        }
        return text.toLowerCase().replace("ü", "u").replace("ö", "o").replace("ı", "i").replace("ğ", "g").replace("ç", "c").replace("ş", "s");
    }


    // This method hides the system bars and resize the content
    private void hideSystemUI(AppCompatActivity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                // remove the following flag for version < API 19
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.abdullahbalta.androidlogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }

}
