package com.example.shoppingapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPreferencesManager {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getSharedPreferences(): SharedPreferences? {
        return sharedPreferences
    }

    fun saveUserData(userName: String, userRole: String) {
        sharedPreferences?.edit()?.apply {
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_ROLE, userRole)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }


    fun getUserName(): String? {
         return sharedPreferences?.getString(KEY_USER_NAME, null)
    }

    fun getUserRole(): String? {
        return sharedPreferences?.getString(KEY_USER_ROLE, null)
    }
    fun getUserId(): String? {
        val userId = sharedPreferences?.getString("KEY_USER_ID", null)
        Log.d("SharedPreferencesManager getUserId", "User ID return: $userId") // Log the user ID
        return userId
     }

    fun saveUserId(userId: String) {
        sharedPreferences?.edit()?.apply {
            putString(KEY_USER_ID, userId)  // Save user ID
            putBoolean(KEY_IS_LOGGED_IN, true)  // Mark user as logged in
            apply()
        }
        Log.d("SharedPreferencesManager saveUserId", "User ID saved: $userId")  // Debug log
    }


    fun isLoggedIn(): Boolean {
        return getSharedPreferences()?.getBoolean(KEY_IS_LOGGED_IN, false) ?: false
    }

    fun clearUserData() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
