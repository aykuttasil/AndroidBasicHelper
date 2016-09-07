package com.aykuttasil.androidbasichelperlib;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SuperHelper {

    private static final String TAG = SuperHelper.class.getSimpleName();

    public static String getSimState(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int state = telephonymanager.getSimState();

        switch (state) {
            case 0:
                return "Bilinmiyor";
            case 1:
                return "Sim Kart Yok";
            case 2:
                return "Pin Girilmesi Bekleniyor";
            case 3:
                return "Puk girilmesi bekleniyor";
            case 4:
                return "Pin(Network) girilmesi bekleniyor";
            case 5:
                return "Hazır";
            default:
                return "Bilinmiyor";
        }
    }

    public static String getBluetoothState(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return "YOK";
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                return "VAR - ENABLE";
            } else {
                return "VAR - DISABLE";
            }
        }


    }

    public static JSONArray getInstalledApps(Context mContext) {

        ArrayList<PackageInfo> res = new ArrayList<PackageInfo>();
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        JSONArray jsonarray = new JSONArray();


        for (ApplicationInfo app : apps) {
            // Uygulama sistem uygulamasi mi kontrol ediliyor.
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                // Diger sistem uygulamasi flagi
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                // Sistem uygulamasi olmayan uygulamalar asagidaki else'e
                // giriyor.
            } else {
                JSONObject obj = new JSONObject();
                //String description = (String)app.loadDescription(pm);
                String label = app.loadLabel(pm).toString();
                String packageName = app.packageName;
                String versionName = "";
                int versionNumber = 0;
                try {
                    versionName = pm.getPackageInfo(packageName, 0).versionName;
                    versionNumber = pm.getPackageInfo(packageName, 0).versionCode;
                } catch (PackageManager.NameNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                try {
                    //obj.put("description", description);
                    obj.put("label", label);
                    obj.put("packageName", packageName);
                    obj.put("verName", versionName);
                    obj.put("verCode", versionNumber);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonarray.put(obj);

            }
        }
        return jsonarray;

    }

    public static float getAvailableInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        return bytesAvailable / (1024.f * 1024.f);
    }

    public static float getTotalInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        return bytesAvailable / (1024.f * 1024.f);
    }

    public static float getAvailableSDStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        return bytesAvailable / (1024.f * 1024.f);
    }

    public static float getSDStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        return bytesAvailable / (1024.f * 1024.f);
    }

    public static long getLocalTime() {
        //DateFormat.getDateInstance().format(System.currentTimeMillis());
        return new Date(System.currentTimeMillis()).getTime();

    }

    public static boolean isDeviceRooted() {

        // get from build info
        String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Throwable e1) {
            // ignore
        }

        return false;
    }

    public static String getAccountName(Context mContext) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(mContext).getAccounts();
        String returnEmail = "";
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                if (account.type.equals("com.google"))
                    returnEmail = account.name;
                break;

            }
        }
        return returnEmail;
    }

    public static String generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getElapsedDays(Date date1, Date date2) {
        long duration = date1.getTime() - date2.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(duration);

        return days;

    }

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

    public static String getDeviceId(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonymanager.getDeviceId();
    }

    public static String getSimSerialNumber(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonymanager.getSimSerialNumber();
    }

    public static String getOperetorName(Context context) {
        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonymanager.getNetworkOperatorName();
    }

    public static String getDeviceLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getDisplayName();
    }

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

    public static String getDeviceDate() {
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return sdfDateTime.format(new Date(System.currentTimeMillis()));
    }

    public static boolean VerifyTCKN(String TCKN) {
        try {

            // TCKN'nin her hanesi rakamsal deger icerir.
            Float.parseFloat(TCKN);
            // TCKN 11 hanelidir.
            if (TCKN.length() != 11) {
                return false;
            }
            // TCKN 0 ile baslayamaz
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

            // 1. 3. 5. 7. ve 9. hanelerin toplamının 7 katından, 2. 4. 6. ve 8.
            // hanelerin toplamı cıkartıldıgında, elde edilen sonucun 10'a
            // bölümünden kalan, yani Mod10'u bize 10. haneyi verir.
            if ((((n1 + n3 + n5 + n7 + n9) * 7) - (n2 + n4 + n6 + n8)) % 10 != n10) {
                return false;
            }
            // 1. 2. 3. 4. 5. 6. 7. 8. 9. ve 10. hanelerin toplamindan elde
            // edilen sonucun 10'a bölümünden kalan, yani Mod10'u bize 11.
            // haneyi verir.
            return (n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10) % 10 == n11;

        } catch (Exception ex) {
            //Crashlytics.logException(ex);
            return false;
        }
    }

    public static void startWakeLock(Context context, int flags, String tag) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(flags, tag);
        wl.acquire();
    }

    public static void ReplaceFragmentBeginTransaction(AppCompatActivity activity, Fragment fragment, int containerID, boolean isBackStack) {

        /*
        FragmentManager akışını loglamak istiyorsan comment satırını aktifleştir.
         */
        //FragmentManager.enableDebugLogging(true);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        /*
        Eğer daha önce yüklenen bir fragment tekrar yüklenmeye çalışılıyor ise replace yapmak yerine popstack ile yükleme yapıyoruz.
         */
        Fragment alreadyFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());

        if (alreadyFragment == null) {
            if (isBackStack) {
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            fragmentTransaction.replace(containerID, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        } else {

            /*
            for (Fragment frg : fragmentManager.getFragments()) {
                if (frg != null) {
                }
            }
            */

            if (!alreadyFragment.isVisible()) {

                 /*
                 * {@link fragmentManager.popBackStackImmediate()} kullanarak popstack olup olmadığını yakalayabiliriz.
                 * Eğer fragment, uygulama ilk açıldığında yüklenen fragment ise popstack false olarak commit edileceği için
                 * popBackStackImmadiate = false döner.
                 * Bunun kontorülü yapıyoruz ve ilk yüklenen fragment herhangi bir butona basılarak tekrar yüklenmeye çalışılır ise
                 * isBackStack değişken kontrolü yaparak replace ediyoruz.
                 */
                boolean isPopStack = fragmentManager.popBackStackImmediate(alreadyFragment.getClass().getSimpleName(), 0);
                if (!isPopStack) {
                    if (isBackStack) {
                        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                    }
                    fragmentTransaction.replace(containerID, fragment, fragment.getClass().getSimpleName());
                    fragmentTransaction.commit();
                }
            }
        }
    }

    public static void removeAllPopStack(AppCompatActivity activity) {

        FragmentManager fm = activity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public static void ReplaceFragmentBeginTransaction(AppCompatActivity activity, Fragment fragment, int containerID, String tag, boolean isBackStack) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        if (isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        //fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.replace(containerID, fragment, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }

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

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setClickable(true);
        //editText.setBackgroundColor(Color.TRANSPARENT);
    }

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

    public static void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();

    }

    public static String getFormatTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static String getFormatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    public static String getFormatTimeMinute(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    public static String getFormatTimeHourMinute(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    public static String getFormatTimeDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        date.setTime(time);
        return simpleDateFormat.format(date);
    }

    public static void PhoneCall(Activity activity, String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        activity.startActivity(callIntent);
    }

    public static boolean isServiceRunning(Activity activity, String serviceName) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getStandartText(String text) {

        if (text == null) {
            return "";
        }
        return text.toLowerCase(Locale.getDefault()).replace("ü", "u").replace("ö", "o").replace("ı", "i").replace("ğ", "g").replace("ç", "c").replace("ş", "s");
    }

    public static String getCorrectPhoneNumber(String telNo) {
        String correctTel;

        if (telNo.length() >= 10) {
            String telItem = telNo;
            String firstChar = telItem.substring(0, 1);
            if (firstChar.equals("9")) {
                telItem = telItem.substring(1, telItem.length());
            }
            firstChar = telItem.substring(0, 1);
            if (!firstChar.equals("0")) {
                telItem = "0" + telItem;
            }
            correctTel = telItem;
        } else {
            correctTel = telNo.trim();
        }
        return correctTel;
    }

    public void printHashKey(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName,
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

    public static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setAlarmRepeating(Context context, long periodicTime, Class receiverClass, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), receiverClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        // setInexactRepeating() kullanıldığında sabit aralıklar verilmek zorundadır. Uygulamalar genelde bu şekilde periodic
        // servislerini çalıştırır. Android, düzenlenmiş diğer işlemlerle aynı anda periodic servisi çalıştıracağından
        // pil ömrü uzar.
        // -----
        // Eğer kendimiz zaman belirlemek istiyorsak (her 10 saniyede bir çalıştır gibi) setRepeating() kullanmamamız gerekir.
        // -----
        // Alarmı hemen çalıştırmaya başla ve her 15 dakikada bir tekrarla.
        /*alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.currentThreadTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent);*/

        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP);


        // AlarmManager.ELAPSED_REALTIME -> ELAPSED_REALTIME system başlangıcını baz alır. Eğer gerçek saat ile ilgili bir işlem yapılmıcak
        // ise bu parametre kullanılmalıdır.
        // RTC -> Cihazın local saatini baz alır. Örneğin perşembe saat 4 de yapılcak bi iş belirlemek istersen RTC kullanmalıyız.
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                periodicTime,
                pendingIntent);
    }

    public void stopAlarmRepeating(Context context, Class receiverClass, int requestCode) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), receiverClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public static String setRandomImage(Context context, ImageView imageView) {
        String randomUrl = "http://lorempixel.com/400/200";
        Glide.with(context)
                .load(randomUrl)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .fitCenter()
                .into(imageView);
        return randomUrl;
    }

    public static boolean validateIsEmpty(EditText... editTexts) {

        boolean flag = false;
        for (EditText editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                editText.setError("Boş bırakmayınız !");
                flag = true;
            } else {
                editText.setError(null);
            }
        }
        return flag;
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String getFolderSizeString(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }

        String value;
        long Filesize = size / 1024;//call function and convert bytes into Kb
        if (Filesize >= 1024)
            value = Filesize / 1024 + " Mb";
        else
            value = Filesize + " Kb";

        return value;
    }

    /**
     * Bitmap.CompressFormat can be PNG,JPEG or WEBP.
     * <p/>
     * quality goes from 1 to 100. (Percentage).
     * <p/>
     * dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir()
     * depending on where you want to save the image.
     */
    public static boolean saveBitmapToFile(File dir, String fileName, Bitmap bm, Bitmap.CompressFormat format, int quality) {

        File imageFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format, quality, fos);

            fos.close();

            return true;
        } catch (IOException e) {
            Log.e("app", e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }





    /*
    public static List<ActivityManager.RunningServiceInfo> getRunningServices(int maxNum , Activity activity) {
         ActivityManager.RunningServiceInfo lst =(ActivityManager.RunningServiceInfo) activity.getSystemService(Context.ACTIVITY_SERVICE);
    }
    */

        /*
    @DebugLog
    public static PhoneCallStateRequest getCallDetails(Context context, String GonderiNo) {
        PhoneCallStateRequest cr = new PhoneCallStateRequest();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        cursor.moveToFirst();

        ModelPersonel modelPersonel = DbManager.getPersonel();
        cr.setPkodu(modelPersonel.getPkodu());
        cr.setGonderiNo(GonderiNo);
        cr.setTelNo(cursor.getString(number));
        cr.setCallStatus(cursor.getInt(type));

        String callDate = cursor.getString(date);
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date callDayTime = new Date(Long.valueOf(callDate));

        cr.setCallDate(sdfDateTime.format(callDayTime));
        cr.setDuration(cursor.getInt(duration));

        String dir = null;
        int dircode = Integer.parseInt(cursor.getString(type));
        switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                cr.setCallStatusMsg("GİDEN");
                break;
            case CallLog.Calls.INCOMING_TYPE:
                cr.setCallStatusMsg("GELEN");
                break;
            case CallLog.Calls.MISSED_TYPE:
                cr.setCallStatusMsg("CEVAPSIZ");
                break;
        }

        cursor.close();
        return cr;
    }
    */
}
