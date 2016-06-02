package com.aykuttasil.androidbasichelperlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.net.InetAddress;

import hugo.weaving.DebugLog;


/**
 * Created by aykutasil on 2.03.2016.
 */
public class InternetConnectionHelper {

    @DebugLog
    public static boolean checkConnection1(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    @DebugLog
    public static boolean checkConnection(Context con) {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @DebugLog
    public static Boolean getWifiIsConnected(Context mContext) {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //NetworkInfo mWifi = connManager.getNetworkInfo(connManager.getActiveNetwork());
        if (mWifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @DebugLog
    public static Boolean get3GIsConnected(Context mContext) {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (m3g.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @DebugLog
    public static String get3gStatus(Context context) {

        TelephonyManager telephonymanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        int cType = telephonymanager.getNetworkType();
        String cTypeString;
        switch (cType) {
            case 1:
                cTypeString = "GPRS";
                break;
            case 2:
                cTypeString = "EDGE";
                break;
            case 3:
                cTypeString = "UMTS";
                break;
            case 8:
                cTypeString = "HSDPA";
                break;
            case 9:
                cTypeString = "HSUPA";
                break;
            case 10:
                cTypeString = "HSPA";
                break;
            default:
                cTypeString = "unknown";
                break;
        }
        return cTypeString;
    }


    @DebugLog
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("reaktiftest.aktif.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }


}
