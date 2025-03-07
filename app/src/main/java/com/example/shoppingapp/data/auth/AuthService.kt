package com.example.shoppingapp.data.auth

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthService(private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
        private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun signUp(email: String, password: String,role: String): Boolean {
        try {
            // Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid ?: return false

            // Save user to Firestore
            firestore.collection("users")
                .document(userId).set(mapOf(
                    "email" to email,
                    "role" to role
                )).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun loginUser(email: String,password: String): AuthResult{
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    fun isUserLoggedIn():Boolean{
       // return FirebaseAuth.getInstance().currentUser != null
        return auth.currentUser != null
    }

    fun getCurrentUserId():String?{
        return auth.currentUser?.uid
    }

    fun logoutUser(){
        auth.signOut()
    }

    suspend fun resetPassword(email: String) {
          try {
            auth.sendPasswordResetEmail(email).await()
         } catch (e: FirebaseAuthException) {
            throw Exception("Failed to send reset password email: ${e.message}")
         }
    }
}