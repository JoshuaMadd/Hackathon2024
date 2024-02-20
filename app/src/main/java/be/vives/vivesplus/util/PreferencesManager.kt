/**
 * Created by Cantaert Jordy
 * 11 feb 2020
 *
 * Last edited by Cantaert Jordy
 * 14 feb 2020
 */


package be.vives.vivesplus.util

import android.content.Context

class PreferencesManager {

    private val PREFS_NAME = "VivesEventsPrefs"
    private val KEY_PREFIX = "+@:vivesevent:"

    fun writeStringToPreferences(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putString("$KEY_PREFIX$key", value)
        editor.apply()
    }

    fun getStringFromPreferences(context: Context, key: String): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getString("$KEY_PREFIX$key", "")
    }

    fun writeIntToPreferences(context: Context, key: String, value: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putInt("$KEY_PREFIX$key", value)
        editor.apply()
    }

    fun getIntFromPreferences(context: Context, key: String): Int? {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getInt("$KEY_PREFIX$key", -1)
    }

    fun writeBoolToPreferences(context: Context, key: String, value: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putBoolean("$KEY_PREFIX$key", value)
        editor.apply()
    }

    fun getBoolFromPreferences(context: Context, key: String): Boolean? {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getBoolean("$KEY_PREFIX$key", false)
    }

    fun writeStringSetToPreferences(context: Context, key: String, value: MutableSet<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val editor = prefs.edit()
        editor.putStringSet("$KEY_PREFIX$key", value)
        editor.apply()
    }

    fun getStringSetFromPreferences(context: Context, key: String): MutableSet<String>? {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        return prefs.getStringSet("$KEY_PREFIX$key", null)
    }
}