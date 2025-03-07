package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.User

interface AuthRepository {

    suspend fun signUp(name: String,email: String, password: String, role: String): Result<String>
    suspend fun login(email: String, password: String): Result<String>
     // suspend fun registerUser(name:String,email: String, password: String,role:String)
  /* suspend fun signUp(email: String, password: String, role: String): Boolean
    suspend fun loginUser(email: String, password: String)
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    fun logoutUser()
    suspend fun resetPassword(email: String)
    suspend fun getUserName() : String?
    suspend fun getUserRole(userId:String):String?

    suspend fun getUserNameAndRole(): Pair<String?, String?>?*/

    suspend fun resetPassword(email: String)
    suspend fun getUserName() : String?
    suspend fun getUserRole(userId:String):String?
    suspend fun getUserData(userId: String): Result<User>
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    fun logoutUser()
    suspend fun getUserNameAndRole(): Pair<String?, String?>?
}