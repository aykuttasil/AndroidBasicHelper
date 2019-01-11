package com.aykuttasil.androidbasichelperlib

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by aykutasil on 24.01.2017.
 */
object ManifestHelper {

    fun getMetadataInt(context: Context, key: String): Int {
        var value = getIntMetadata(context, key)
        if (value == null || value == 0) {
            value = 1
        }
        return value
    }

    fun getMetaDataString(context: Context, key: String): String {
        var value = getStringMetadata(context, key)
        if (value == null) {
            value = ""
        }
        return value
    }

    fun getMetaDataBoolean(context: Context, key: String): Boolean {
        return getBooleanMetadata(context, key)!!
    }

    private fun getStringMetadata(context: Context, key: String): String? {
        var value: String? = null
        val packageManager = context.packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            value = applicationInfo.metaData.getString(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    private fun getIntMetadata(context: Context, key: String): Int? {
        var value: Int? = null
        val packageManager = context.packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            value = applicationInfo.metaData.getInt(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    private fun getBooleanMetadata(context: Context, key: String): Boolean? {
        var flag: Boolean? = false
        val packageManager = context.packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            flag = applicationInfo.metaData.getBoolean(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return flag
    }
}