package com.aykuttasil.androidbasichelperlib

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PowerManager
import android.os.StatFs
import android.os.SystemClock
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.PhoneUtils
import com.blankj.utilcode.util.Utils
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SuperHelper {

    fun printHashKey(context: Context, packageName: String) {
        try {
            val info = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("KeyHash:", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.d("KeyHash:", e.toString())
        }
    }

    fun setAlarmRepeating(context: Context, periodicTime: Long, receiverClass: Class<*>, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context.applicationContext, receiverClass)
        val pendingIntent = PendingIntent.getBroadcast(context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)


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
            pendingIntent)
    }

    /**
     * setInexactRepeating() kullanıldığında sabit aralıklar verilmek zorundadır. Uygulamalar genelde bu şekilde periodic
     * servislerini çalıştırır. Android, düzenlenmiş diğer işlemlerle aynı anda periodic servisi çalıştıracağından
     * pil ömrü uzar.
     * -----
     * Eğer kendimiz zaman belirlemek istiyorsak (her 10 saniyede bir çalıştır gibi) setRepeating() kullanmamamız gerekir.
     * -----
     * Alarmı hemen çalıştırmaya başla ve her 15 dakikada bir tekrarla.
     * //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
     * //SystemClock.currentThreadTimeMillis(),
     * //AlarmManager.INTERVAL_FIFTEEN_MINUTES,
     * //pendingIntent);
     *
     *
     * //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP);
     *
     *
     * AlarmManager.ELAPSED_REALTIME -> ELAPSED_REALTIME system başlangıcını baz alır. Eğer gerçek saat ile ilgili bir işlem yapılmıcak
     * ise bu parametre kullanılmalıdır.
     * RTC -> Cihazın local saatini baz alır. Örneğin perşembe saat 4 de yapılcak bi iş belirlemek istersen RTC kullanmalıyız.
     *
     * @param context
     * @param broadcastReceiverClass
     * @param isInExect
     * @param periodicTime
     * @param requestCode
     */
    fun setAlarmRepeating(context: Context, broadcastReceiverClass: Class<*>, isInExact: Boolean, periodicTime: Long, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, broadcastReceiverClass)
        val pendingIntent = PendingIntent.getBroadcast(context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        if (isInExact) {
            if (periodicTime != AlarmManager.INTERVAL_FIFTEEN_MINUTES &&
                periodicTime != AlarmManager.INTERVAL_HALF_HOUR &&
                periodicTime != AlarmManager.INTERVAL_HOUR &&
                periodicTime != AlarmManager.INTERVAL_HALF_DAY &&
                periodicTime != AlarmManager.INTERVAL_DAY) {

                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    AlarmManager.INTERVAL_HALF_HOUR,
                    pendingIntent)
            } else {
                try {
                    throw Exception("AlarmManager.INTERVAL* değerlerinden birini girmelisiniz.")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                periodicTime,
                pendingIntent)
        }
    }

    fun stopAlarmRepeating(context: Context, receiverClass: Class<*>, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, receiverClass)
        val pendingIntent = PendingIntent.getBroadcast(context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private val TAG = SuperHelper::class.java.simpleName

        fun getSimState(context: Context): String {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val state = telephonyManager.simState
            return when (state) {
                0 -> "Bilinmiyor"
                1 -> "Sim Kart Yok"
                2 -> "Pin Girilmesi Bekleniyor"
                3 -> "Puk girilmesi bekleniyor"
                4 -> "Pin(Network) girilmesi bekleniyor"
                5 -> "Hazır"
                else -> "Bilinmiyor"
            }
        }

        @RequiresPermission(value = Manifest.permission.BLUETOOTH)
        fun getBluetoothState(context: Context): String {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            return if (mBluetoothAdapter == null) {
                "YOK"
            } else {
                if (mBluetoothAdapter.isEnabled) {
                    "VAR - ENABLE"
                } else {
                    "VAR - DISABLE"
                }
            }
        }

        fun getInstalledApps(mContext: Context): JSONArray {
            val res = ArrayList<PackageInfo>()
            val pm = mContext.packageManager
            val apps = pm.getInstalledApplications(0)
            val jsonarray = JSONArray()

            for (app in apps) {
                // Uygulama sistem uygulamasi mi kontrol ediliyor.
                if (app.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 1) {
                    // Diger sistem uygulamasi flagi
                } else if (app.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
                    // Sistem uygulamasi olmayan uygulamalar asagidaki else'e
                    // giriyor.
                } else {
                    val obj = JSONObject()
                    //String description = (String)app.loadDescription(pm);
                    val label = app.loadLabel(pm).toString()
                    val packageName = app.packageName
                    var versionName = ""
                    var versionNumber = 0
                    try {
                        versionName = pm.getPackageInfo(packageName, 0).versionName
                        versionNumber = pm.getPackageInfo(packageName, 0).versionCode
                    } catch (e1: PackageManager.NameNotFoundException) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace()
                    }

                    try {
                        //obj.put("description", description);
                        obj.put("label", label)
                        obj.put("packageName", packageName)
                        obj.put("verName", versionName)
                        obj.put("verCode", versionNumber)
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }

                    jsonarray.put(obj)

                }
            }
            return jsonarray
        }

        val availableInternalMemorySize: Float
            get() {
                val stat = StatFs(Environment.getDataDirectory().path)
                val bytesAvailable = stat.blockSize.toLong() * stat.availableBlocks.toLong()
                return bytesAvailable / (1024f * 1024f)
            }

        val totalInternalMemorySize: Float
            get() {
                val stat = StatFs(Environment.getDataDirectory().path)
                val bytesAvailable = stat.blockSize.toLong() * stat.blockCount.toLong()
                return bytesAvailable / (1024f * 1024f)
            }

        val availableSDStorage: Float
            get() {
                val stat = StatFs(Environment.getExternalStorageDirectory().path)
                val bytesAvailable = stat.blockSize.toLong() * stat.availableBlocks.toLong()
                return bytesAvailable / (1024f * 1024f)
            }

        val sdStorage: Float
            get() {
                val stat = StatFs(Environment.getExternalStorageDirectory().path)
                val bytesAvailable = stat.blockSize.toLong() * stat.blockCount.toLong()
                return bytesAvailable / (1024f * 1024f)
            }

        //DateFormat.getDateInstance().format(System.currentTimeMillis());
        val localTime: Long
            get() = Date(System.currentTimeMillis()).time

        fun isDeviceRooted(context: Context): Boolean {
            return DeviceUtils.isDeviceRooted()

            //        // get from build info
            //        String buildTags = Build.TAGS;
            //        if (buildTags != null && buildTags.contains("test-keys")) {
            //            return true;
            //        }
            //
            //        // check if /system/app/Superuser.apk is present
            //        try {
            //            File file = new File("/system/app/Superuser.apk");
            //            if (file.exists()) {
            //                return true;
            //            }
            //        } catch (Throwable e1) {
            //            // ignore
            //        }
            //
            //        return false;
        }

        @RequiresPermission(value = Manifest.permission.GET_ACCOUNTS)
        fun getAccountName(context: Context): String {
            val emailPattern = Patterns.EMAIL_ADDRESS // API level 8+
            val accounts: Array<Account>
            try {
                accounts = AccountManager.get(context).accounts
            } catch (e: Exception) {
                throw e
            }

            var returnEmail = ""
            for (account in accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    if (account.type == "com.google")
                        returnEmail = account.name
                    break
                }
            }
            return returnEmail
        }

        fun generateRandom(length: Int): String {
            val random = Random()
            val digits = CharArray(length)
            digits[0] = (random.nextInt(9) + '1'.toInt()).toChar()
            for (i in 1 until length) {
                digits[i] = (random.nextInt(10) + '0'.toInt()).toChar()
            }
            return String(digits)
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        fun getElapsedDays(date1: Date, date2: Date): Long {
            val duration = date1.time - date2.time
            val days = TimeUnit.MILLISECONDS.toDays(duration)

            return days
        }

        fun deleteDirectoryContent(path: File): Boolean {
            try {
                if (path.exists()) {
                    val files = path.listFiles() ?: return true
                    for (i in files.indices) {
                        files[i].delete()
                    }
                }
                return true
            } catch (ex: Exception) {
                return false
            }

        }

        fun getDeviceId(application: Application): String {
            Utils.init(application)
            return PhoneUtils.getIMEI()
        }

        @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
        fun getSimSerialNumber(context: Context): String {
            val telephonymanager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonymanager.simSerialNumber
        }

        fun getOperetorName(context: Context): String {
            return NetworkUtils.getNetworkOperatorName()
        }

        fun getDeviceLanguage(context: Context): String {
            return context.resources.configuration.locale.displayName
        }

        // make sure "HTC" is fully capitalized.
        val deviceName: String?
            get() {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                if (model.startsWith(manufacturer)) {
                    return capitalize(model)
                }
                return if (manufacturer.equals("HTC", ignoreCase = true)) {
                    "HTC $model"
                } else capitalize(manufacturer) + " " + model
            }

        private fun capitalize(str: String): String? {
            if (TextUtils.isEmpty(str)) {
                return str
            }
            val arr = str.toCharArray()
            var capitalizeNext = true
            var phrase = ""
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase += Character.toUpperCase(c)
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true
                }
                phrase += c
            }
            return phrase
        }

        fun isAppIsInBackground(context: Context): Boolean {
            //AppUtils.isAppForeground(context)

            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.runningAppProcesses
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo.packageName == context.packageName) {
                    isInBackground = false
                }
            }

            return isInBackground
        }

        val deviceDate: String
            get() {
                val sdfDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                return sdfDateTime.format(Date(System.currentTimeMillis()))
            }

        fun VerifyTCKN(TCKN: String): Boolean {
            try {
                // TCKN'nin her hanesi rakamsal deger icerir.
                java.lang.Float.parseFloat(TCKN)
                // TCKN 11 hanelidir.
                if (TCKN.length != 11) {
                    return false
                }
                // TCKN 0 ile baslayamaz
                if (TCKN.substring(0, 1) == "0") {
                    return false
                }

                val n1 = Integer.parseInt(TCKN.substring(0, 1))
                val n2 = Integer.parseInt(TCKN.substring(1, 2))
                val n3 = Integer.parseInt(TCKN.substring(2, 3))
                val n4 = Integer.parseInt(TCKN.substring(3, 4))
                val n5 = Integer.parseInt(TCKN.substring(4, 5))
                val n6 = Integer.parseInt(TCKN.substring(5, 6))
                val n7 = Integer.parseInt(TCKN.substring(6, 7))
                val n8 = Integer.parseInt(TCKN.substring(7, 8))
                val n9 = Integer.parseInt(TCKN.substring(8, 9))
                val n10 = Integer.parseInt(TCKN.substring(9, 10))
                val n11 = Integer.parseInt(TCKN.substring(10, 11))

                // 1. 3. 5. 7. ve 9. hanelerin toplamının 7 katından, 2. 4. 6. ve 8.
                // hanelerin toplamı cıkartıldıgında, elde edilen sonucun 10'a
                // bölümünden kalan, yani Mod10'u bize 10. haneyi verir.
                //if ((((n1 + n3 + n5 + n7 + n9) * 7) - (n2 + n4 + n6 + n8)) % 10 != n10) {
                //    return false;
                //}


                // 1. 2. 3. 4. 5. 6. 7. 8. 9. ve 10. hanelerin toplamindan elde
                // edilen sonucun 10'a bölümünden kalan, yani Mod10'u bize 11.
                // haneyi verir.
                return (n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10) % 10 == n11

            } catch (ex: Exception) {
                //Crashlytics.logException(ex);
                return false
            }

        }

        fun startWakeLock(context: Context, flags: Int, tag: String) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wl = pm.newWakeLock(flags, tag)
            wl.acquire(10 * 60 * 1000L /*10 minutes*/)
        }

        fun ReplaceFragmentBeginTransaction(activity: AppCompatActivity, fragment: Fragment, containerID: Int, isBackStack: Boolean) {
            /*
        FragmentManager akışını loglamak istiyorsan comment satırını aktifleştir.
         */
            //FragmentManager.enableDebugLogging(true);
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            /*
        Eğer daha önce yüklenen bir fragment tekrar yüklenmeye çalışılıyor ise replace yapmak yerine popstack ile yükleme yapıyoruz.
         */
            val alreadyFragment = fragmentManager.findFragmentByTag(fragment.javaClass.simpleName)

            if (alreadyFragment == null) {
                if (isBackStack) {
                    fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
                }
                fragmentTransaction.replace(containerID, fragment, fragment.javaClass.simpleName)
                fragmentTransaction.commit()
            } else {

                /*
            for (Fragment frg : fragmentManager.getFragments()) {
                if (frg != null) {
                }
            }
            */

                if (!alreadyFragment.isVisible) {

                    /*
                 * {@link fragmentManager.popBackStackImmediate()} kullanarak popstack olup olmadığını yakalayabiliriz.
                 * Eğer fragment, uygulama ilk açıldığında yüklenen fragment ise popstack false olarak commit edileceği için
                 * popBackStackImmadiate = false döner.
                 * Bunun kontorülü yapıyoruz ve ilk yüklenen fragment herhangi bir butona basılarak tekrar yüklenmeye çalışılır ise
                 * isBackStack değişken kontrolü yaparak replace ediyoruz.
                 */
                    val isPopStack = fragmentManager.popBackStackImmediate(alreadyFragment.javaClass.simpleName, 0)
                    if (!isPopStack) {
                        if (isBackStack) {
                            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
                        }
                        fragmentTransaction.replace(containerID, fragment, fragment.javaClass.simpleName)
                        fragmentTransaction.commit()
                    }
                }
            }
        }

        fun removeAllPopStack(activity: AppCompatActivity) {
            val fm = activity.supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
        }

        fun ReplaceFragmentBeginTransaction(activity: AppCompatActivity, fragment: Fragment, containerID: Int, tag: String, isBackStack: Boolean) {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            if (isBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            //fragmentTransaction.commitAllowingStateLoss();
            fragmentTransaction.replace(containerID, fragment, tag)
            fragmentTransaction.commitAllowingStateLoss()
        }

        fun ReplaceFragmentBeginTransaction(container: Fragment, fragment: Fragment, containerID: Int, tag: String, isBackStack: Boolean, popStackName: String) {
            val fragmentTransaction = container.childFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            if (isBackStack) {
                if (popStackName != "") {
                    fragmentTransaction.addToBackStack(popStackName)
                } else {
                    fragmentTransaction.addToBackStack(null)
                }
            }
            //fragmentTransaction.commitAllowingStateLoss();
            fragmentTransaction.replace(containerID, fragment, tag)
            fragmentTransaction.commitAllowingStateLoss()
        }

        fun hideSoftKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showSoftKeyboard(context: Context, view: View) {
            if (view.requestFocus()) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        fun disableEditText(editText: EditText) {
            editText.isFocusable = false
            editText.isEnabled = false
            editText.isCursorVisible = false
            editText.keyListener = null
            editText.isClickable = true
            //editText.setBackgroundColor(Color.TRANSPARENT);
        }

        fun drawableToBitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }

        fun DeleteRecursive(fileOrDirectory: File) {
            if (fileOrDirectory.isDirectory)
                for (child in fileOrDirectory.listFiles())
                    DeleteRecursive(child)

            fileOrDirectory.delete()

        }

        val formatTime: String
            get() {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                val date = Date()
                return simpleDateFormat.format(date)
            }

        fun getFormatTime(time: Long): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val date = Date()
            date.time = time
            return simpleDateFormat.format(date)
        }

        fun getFormatTimeMinute(time: Long): String {
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            val date = Date()
            date.time = time
            return simpleDateFormat.format(date)
        }

        fun getFormatTimeHourMinute(time: Long): String {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.US)
            val date = Date()
            date.time = time
            return simpleDateFormat.format(date)
        }

        fun getFormatTimeDate(time: Long): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = Date()
            date.time = time

            return simpleDateFormat.format(date)
        }

        @RequiresPermission(value = Manifest.permission.CALL_PHONE)
        fun PhoneCall(activity: Activity, phoneNumber: String) {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            if (checkPermission(activity, Manifest.permission.CALL_PHONE)) {
                activity.startActivity(callIntent)
            }
        }

        fun isServiceRunning(activity: Activity, serviceName: String): Boolean {
            val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceName == service.service.className) {
                    return true
                }
            }
            return false
        }

        fun getStandartText(text: String?): String {
            return text?.toLowerCase(Locale("TR"))?.replace("ü", "u")?.replace("ö", "o")?.replace("ı", "i")?.replace("ğ", "g")?.replace("ç", "c")?.replace("ş", "s")
                ?: ""
        }

        fun getCorrectPhoneNumber(telNo: String): String {
            var correctTel: String

            if (telNo.length >= 10) {
                var telItem = telNo

                var firstChar = telItem.substring(0, 1)
                if (firstChar == "9") {
                    telItem = telItem.substring(1)
                }

                firstChar = telItem.substring(0, 1)
                if (firstChar != "0") {
                    telItem = "0$telItem"
                }
                correctTel = telItem
            } else {
                correctTel = telNo.trim { it <= ' ' }
            }

            while (correctTel.contains(" ")) {
                val part1 = correctTel.substring(0, correctTel.indexOf(" "))
                val part2 = correctTel.substring(correctTel.indexOf(" ") + 1)
                correctTel = part1 + part2
            }
            return correctTel
        }

        fun checkPermission(context: Context, permission: String): Boolean {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }

        /*
    public static String setRandomImage(Context context, ImageView imageView) {

        String randomUrl = "http://lorempixel.com/400/200";

        Glide.with(context)
                .load(randomUrl)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .fitCenter()
                .into(imageView);

        return randomUrl;
    }
    */

        fun validateIsEmpty(vararg editTexts: EditText): Boolean {
            var flag = false
            for (editText in editTexts) {
                if (editText.text.toString().isEmpty()) {
                    editText.error = "Boş bırakmayınız!"
                    flag = true
                } else {
                    editText.error = null
                }
            }
            return flag
        }

        fun getFolderSize(f: File): Long {
            var size: Long = 0
            if (f.isDirectory) {
                for (file in f.listFiles()) {
                    size += getFolderSize(file)
                }
            } else {
                size = f.length()
            }
            return size
        }

        fun getFolderSizeString(f: File): String {
            var size: Long = 0

            if (f.isDirectory) {
                for (file in f.listFiles()) {
                    size += getFolderSize(file)
                }
            } else {
                size = f.length()
            }

            val value: String
            val fileSize = size / 1024//call function and convert bytes into Kb
            value = if (fileSize >= 1024)
                (fileSize / 1024).toString() + " Mb"
            else
                fileSize.toString() + " Kb"

            return value
        }

        /**
         * Bitmap.CompressFormat can be PNG,JPEG or WEBP.
         *
         *
         * quality goes from 1 to 100. (Percentage).
         *
         *
         * dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir()
         * depending on where you want to save the image.
         */
        fun saveBitmapToFile(dir: File, fileName: String, bm: Bitmap, format: Bitmap.CompressFormat, quality: Int): Boolean {
            val imageFile = File(dir, fileName)

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(imageFile)
                bm.compress(format, quality, fos)
                fos.close()
                return true
            } catch (e: IOException) {
                Log.e("app", e.message)
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }

                }
            }

            return false
        }

        /**
         * Belirtilen dosya daha önce cihaza indirilmiş mi?
         * Download klasörü kontrol edilir.
         *
         * @param checkFileName
         * @return
         */
        fun isAlreadyDownloadFile(checkFileName: String): Boolean {
            val appPath = (Environment.getExternalStorageDirectory().toString() + "/"
                + Environment.DIRECTORY_DOWNLOADS + "/"
                + checkFileName)

            return File(appPath).exists()
        }

        fun ShowDatePickerViewClick(editText: EditText, activity: AppCompatActivity) {
            editText.setOnClickListener {
                val cdp = CalendarDatePickerDialogFragment()
                    .setThemeCustom(R.style.MyCustomBetterPickerTheme)
                    .setOnDateSetListener { dialog, year, monthOfYear, dayOfMonth -> editText.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1).toString() + "/" + year.toString()) }
                    .setFirstDayOfWeek(Calendar.MONDAY)
                    //.setPreselectedDate(towDaysAgo.getYear(), towDaysAgo.getMonthOfYear() - 1, towDaysAgo.getDayOfMonth())
                    //.setDateRange(minDate, null)
                    .setThemeDark()
                cdp.show(activity.supportFragmentManager, "DPD")
            }
        }
    }

}
