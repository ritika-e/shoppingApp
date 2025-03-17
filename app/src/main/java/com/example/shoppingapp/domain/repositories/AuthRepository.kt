package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun signUp(name: String,email: String, password: String, role: String): Result<String>
    suspend fun login(email: String, password: String): Result<String>
    suspend fun sendPasswordResetEmail(email: String):Boolean
    suspend fun getUserName(userId: String) : String?
  //  suspend fun getUserRole(userId:String):String?
    suspend fun getUserData(userId: String): Result<User>
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
    fun logoutUser()
    suspend fun logout()
    suspend fun getUserRole(userId: String): String
    suspend fun getUserNameAndRole(): Pair<String?, String?>?
    suspend fun getCurrentUser(): FirebaseUser?
}