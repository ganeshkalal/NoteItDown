package com.gkalal.demonote.utils

import android.content.Context

object SharedPrefUtil {

    const val THEME_MODE = "theme_mode"

    fun getPrefInt(context: Context, key: String): Int {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return prefs.getInt(key, 0)
    }

    fun savePrefInt(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        prefs.edit().putInt(key, value).apply()
    }

    fun getPrefBoolean(context: Context, key: String): Boolean {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }

    fun savePrefBoolean(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getPrefLong(context: Context, key: String): Long {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return prefs.getLong(key, 0)
    }

    fun savePrefLong(context: Context, key: String, value: Long) {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        prefs.edit().putLong(key, value).apply()
    }

    fun getPrefString(context: Context, key: String): String {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

    fun savePrefString(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    fun clearSharedPref(context: Context) {
        val prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
