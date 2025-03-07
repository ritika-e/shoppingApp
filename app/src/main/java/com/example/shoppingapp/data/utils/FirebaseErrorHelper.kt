package com.example.shoppingapp.data.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.io.IOException

// Utility class or a helper function for Firebase-specific exception handling
object FirebaseErrorHelper {
    fun handleFirebaseAuthError(exception: Throwable): String {

        Log.e("FirebaseErrorHelper", "Handling exception: ${exception::class.java.simpleName}")

        return when (exception) {
            is FirebaseAuthUserCollisionException -> "The email address is already in use. Please try logging in."
            is FirebaseAuthWeakPasswordException -> "The password is too weak. Please choose a stronger password."
            is FirebaseAuthInvalidCredentialsException -> "The email address is invalid. Please enter a valid email."
            is FirebaseAuthException -> "Signup failed due to an authentication error. Please try again."
            is IOException -> "Network error. Please check your connection and try again."
            else -> "Signup failed. Please try again later."
        }
    }
}
