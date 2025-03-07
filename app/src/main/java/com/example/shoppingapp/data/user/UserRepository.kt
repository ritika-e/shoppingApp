package com.example.shoppingapp.data.user

import com.example.shoppingapp.utils.SharedPreferencesManager

class UserRepository(private val sharedPreferencesManager: SharedPreferencesManager) {

    fun getUserName(): String? {
        return sharedPreferencesManager.getUserName()
    }

    fun getUserRole(): String? {
        return sharedPreferencesManager.getUserRole()
    }

    fun saveUserData(userName: String, userRole: String) {
        sharedPreferencesManager.saveUserData(userName, userRole)
    }
}
