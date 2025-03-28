package com.example.todoapp.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesUtil {
    private const val PREFS_NAME = "settings"
    private const val DARK_MODE_KEY = "dark_mode"

    fun saveDarkModeSetting(context: Context, isDarkMode: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(DARK_MODE_KEY, isDarkMode)
            apply()
        }
    }

    fun getDarkModeSetting(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(DARK_MODE_KEY, false)
    }
}