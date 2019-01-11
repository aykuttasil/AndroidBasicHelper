package com.aykuttasil.androidbasichelperlib

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefsHelper {

    private var mContext: Context? = null
    var preference: SharedPreferences? = null
        private set

    val prefEditor: SharedPreferences.Editor
        get() = preference!!.edit()

    private constructor(context: Context) {
        mContext = context
        preference = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private constructor(context: Context, pref_name: String) {
        mContext = context
        preference = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE)
    }

    fun writeString(key: String, value: String) {
        prefEditor.putString(key, value).commit()
    }

    fun readString(key: String): String? {
        return preference!!.getString(key, DEFAULT_STRING_VALUE)
    }

    fun writeInt(key: String, value: Int) {
        prefEditor.putInt(key, value).commit()
    }

    fun readInt(key: String): Int {
        return preference!!.getInt(key, DEFAULT_INT_VALUE)
    }

    fun writeBool(key: String, value: Boolean) {
        prefEditor.putBoolean(key, value).commit()
    }

    fun readBool(key: String): Boolean {
        return preference!!.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
    }

    fun clearAllPreference() {
        preference!!.edit().clear().apply()
    }

    /*
    public class DateTimePreference {
        private final SharedPreferences preferences;
        private final String key;
        private final DateTime defaultValue;

        public DateTimePreference(@NonNull SharedPreferences preferences, @NonNull String key) {
            this(preferences, key, null);
        }

        public DateTimePreference(@NonNull SharedPreferences preferences, @NonNull String key, @Nullable DateTime defaultValue) {
            this.preferences = preferences;
            this.key = key;
            this.defaultValue = defaultValue;
        }

        @Nullable
        public DateTime get() {
            final long millis = preferences.getLong(key, defaultValue != null ? defaultValue.getMillis() : -1);
            return millis == -1 ? null : new DateTime(millis);
        }

        public boolean isSet() {
            return preferences.contains(key);
        }

        public void set(@Nullable DateTime value) {
            preferences.edit().putLong(key, value != null ? value.getMillis() : -1).apply();
        }

        public void delete() {
            preferences.edit().remove(key).apply();
        }
    }
    */


    /*
    class EnumPreference<E : Enum<E>>(private val preferences: SharedPreferences,
                                      private val clazz: Class<E>,
                                      private val key: String,
                                      private val defaultValue: E) {

        val isSet: Boolean
            get() = preferences.contains(key)

        fun get(): E {
            val string = preferences.getString(key, null)
            return if (string != null) E.valueOf<E>(clazz, string) else defaultValue
        }

        fun set(value: E?) {
            preferences.edit().putString(key, value?.name).apply()
        }

        fun delete() {
            preferences.edit().remove(key).apply()
        }
    }
    */

    class StringPreference @JvmOverloads constructor(private val preferences: SharedPreferences, private val key: String, private val defaultValue: String? = null) {

        val isSet: Boolean
            get() = preferences.contains(key)

        fun get(): String? {
            return preferences.getString(key, defaultValue)
        }

        fun set(value: String?) {
            preferences.edit().putString(key, value).apply()
        }

        fun delete() {
            preferences.edit().remove(key).apply()
        }
    }

    class IntPreference @JvmOverloads constructor(private val preferences: SharedPreferences, private val key: String, private val defaultValue: Int = 0) {

        val isSet: Boolean
            get() = preferences.contains(key)

        fun get(): Int {
            return preferences.getInt(key, defaultValue)
        }

        fun set(value: Int) {
            preferences.edit().putInt(key, value).apply()
        }

        fun delete() {
            preferences.edit().remove(key).apply()
        }
    }

    class BooleanPreference @JvmOverloads constructor(private val preferences: SharedPreferences, private val key: String, private val defaultValue: Boolean = false) {

        val isSet: Boolean
            get() = preferences.contains(key)

        fun get(): Boolean {
            return preferences.getBoolean(key, defaultValue)
        }

        fun set(value: Boolean) {
            preferences.edit().putBoolean(key, value).apply()
        }

        fun delete() {
            preferences.edit().remove(key).apply()
        }
    }

    companion object {

        private val DEFAULT_STRING_VALUE: String? = null
        private val DEFAULT_INT_VALUE = -100
        private val DEFAULT_BOOLEAN_VALUE = false

        @JvmStatic
        fun init(context: Context): PrefsHelper {
            return PrefsHelper(context)
        }

        @JvmStatic
        fun init(context: Context, name: String): PrefsHelper {
            return PrefsHelper(context, name)
        }

        @JvmStatic
        fun getDefaultPreference(context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        @JvmStatic
        fun writePrefString(context: Context, key: String, value: String) {
            init(context).prefEditor.putString(key, value).commit()
        }

        @JvmStatic
        fun readPrefString(context: Context, key: String): String? {
            return init(context).preference!!.getString(key, DEFAULT_STRING_VALUE)
        }

        @JvmStatic
        fun writePrefInt(context: Context, key: String, value: Int) {
            init(context).prefEditor.putInt(key, value).commit()
        }

        @JvmStatic
        fun readPrefInt(context: Context, key: String): Int {
            return init(context).preference!!.getInt(key, DEFAULT_INT_VALUE)
        }

        @JvmStatic
        fun writePrefBool(context: Context, key: String, value: Boolean) {
            init(context).prefEditor.putBoolean(key, value).commit()
        }

        @JvmStatic
        fun readPrefBool(context: Context, key: String): Boolean {
            return init(context).preference!!.getBoolean(key, DEFAULT_BOOLEAN_VALUE)
        }

        @JvmStatic
        fun clearPreference(context: Context) {
            init(context).preference!!.edit().clear().apply()
        }
    }

}
