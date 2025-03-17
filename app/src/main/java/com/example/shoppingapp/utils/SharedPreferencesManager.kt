package com.example.shoppingapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveUserData(userId: String, userName: String, userRole: String) {
        val editor = sharedPreferences.edit()
        Log.e("User","USer not Null => $userId $userName $userRole")
        editor.putString("user_id", userId)
        editor.putString("user_name", userName)
        editor.putString("user_role", userRole)
        editor.apply()
    }

    fun getUserData(): UserData {
        val userId = sharedPreferences.getString("user_id", null)
        val userName = sharedPreferences.getString("user_name", null)
        val userRole = sharedPreferences.getString("user_role", null)
        return UserData(userId, userName, userRole)
    }
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.clear() // Clear all data
        editor.apply()
    }


    data class UserData(val userId: String?, val userName: String?, val userRole: String?)
}