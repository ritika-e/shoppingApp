package com.example.shoppingapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

    fun saveUserData(userId: String, userName: String, userRole: String) {
        val editor = sharedPreferences.edit()
        Log.e("User SharedPref", "User not Null => $userId $userName $userRole")
        editor.putString("user_id", userId)
        editor.putString("user_name", userName)
        editor.putString("user_role", userRole)
        editor.apply()
    }

    fun getUserData(): UserData {
        val userId = sharedPreferences.getString("user_id", null)
        val userName = sharedPreferences.getString("user_name", null)
        val userRole = sharedPreferences.getString("user_role", null)
        Log.e("User SharedPref", "getUserData 1=> $userId $userName $userRole")
        return UserData(
            userId = userId ?: "",
            userName = userName ?: "",
            userRole = userRole ?: ""
        )
    }

    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove("user_id")
        editor.remove("user_name")
        editor.remove("user_role")
        editor.clear()
        editor.apply()
        Log.e("User SharedPref", "User data removed")
    }

    // New method to check if user is logged in
    fun isLoggedIn(): Boolean {
        val userId = sharedPreferences.getString("user_id", null)
        return !userId.isNullOrEmpty() // If user_id exists, the user is logged in
    }

    data class UserData(val userId: String?, val userName: String?, val userRole: String?)
}
