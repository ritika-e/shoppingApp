package com.example.shoppingapp.data.auth

//Firebase setup
import android.util.Log
import com.example.shoppingapp.domain.model.User
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

open class AuthRepositoryImpl(private val fireBaseAuth: FirebaseAuth,
                              private val firestore: FirebaseFirestore
    ) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        role: String
    ): Result<String> {
        return try {
             val result = fireBaseAuth.createUserWithEmailAndPassword(email, password).await()
            // Create user object
            val user = result.user
            val userId = user?.uid ?: throw Exception("Unknown Error: User not created")

             try {
                storeUserInFirestore(userId, name, email, role)
            } catch (e: FirebaseFirestoreException) {
                 val errorMessage = e.message
                 Log.e("FirestoreError", "Error storing user data in Firestore: ${e.message}")
                return Result.failure(Exception("Error storing user data. Please try again. $errorMessage"))
            }
            // Return the UID of the user if successfully created
            Result.success(userId)

        } catch (e: FirebaseAuthUserCollisionException) {
            val errorMessage = e.message
             Log.e("FirebaseAuthError", "User already exists with this email: ${e.message}")
            return Result.failure(Exception("An account with this email already exists. Please try a different one."))
        } catch (e: FirebaseAuthWeakPasswordException) {
            val errorMessage = e.message
            Log.e("FirebaseAuthError", "Weak password: ${e.message}")
            return Result.failure(Exception("The password is too weak. Please choose a stronger password."))
        } catch (e: FirebaseAuthException) {
            // Handle other Firebase authentication errors (e.g., invalid email, network issues)
            val errorMessage = e.message
            Log.e("FirebaseAuthError", "Authentication error: ${e.message}")
            return Result.failure(Exception("Authentication failed. $errorMessage"))
        } catch (e: Exception) {
            // Catch any unexpected errors
            val errorMessage = e.message
            Log.e("SignUpError", "Unexpected error: ${e.message}")
            return Result.failure(Exception("An unexpected error occurred. Please try again later. $errorMessage"))
        }
    }

    // Store user data in Firestore
    private suspend fun storeUserInFirestore(
        userId: String,
        name: String,
        email: String,
        role: String
    ) {
        val user = User(userId, name, email, role)  // Simple user data with name and email
        val userRef = firestore.collection("users").document(userId)

        try {
            // Add the user to Firestore
            userRef.set(user).await()
        } catch (e: Exception) {
            throw Exception("Failed to store user in Firestore: ${e.message}")
        }
    }


     override suspend fun login(email: String, password: String): Result<String> {

        return try {
            // Sign in the user with email and password
            val authResult = fireBaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            if (user == null) {
                Log.e("LoginError", "User object is null. Authentication failed.")
                throw Exception("Authentication failed: User object is null.")
            }
            // Return the UID of the authenticated user
            val uid = user.uid
            val userName = user.displayName ?: "Unnamed user"
            Result.success(uid)
         } catch (e: FirebaseAuthInvalidUserException) {
            // User not found or invalid email/password combination
            Log.e("FirebaseAuthError", "Invalid user: ${e.message}")
            Result.failure(Exception("Invalid email or password. Please try again."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Invalid credentials, password might be wrong
            val errorMessage = e.message
            Log.e("FirebaseAuthError", "Invalid credentials: ${e.message}")
            Result.failure(Exception("Invalid credentials $errorMessage"))
        } catch (e: FirebaseAuthException) {
            // Other Firebase Authentication errors (e.g., network issues, service errors)
            val errorCode = e.errorCode
            val errorMessage = e.message
            Log.e("FirebaseAuthError", "Error code: $errorCode, Message: $errorMessage")
            Result.failure(Exception("Login failed: $errorMessage"))
        } catch (e: Exception) {
            // Catch any other unexpected exceptions
            val errorMessage = e.message
//            Log.e("LoginError", "Unexpected error: ${e.message}")
            Result.failure(Exception("$errorMessage"))
        }
    }
   /*
   WORKING
   override suspend fun login(email: String, password: String): Result<FirebaseUser> {
       return withContext(Dispatchers.IO){
           try {
               val authResult = fireBaseAuth.signInWithEmailAndPassword(email, password).await()
               authResult.user?.let {
                   Result.success(it) // Success, return FirebaseUser
               } ?: Result.failure(Exception("User not found"))
           } catch (e: FirebaseAuthInvalidCredentialsException) {
               // Invalid credentials error
               Result.failure(Exception("Invalid credentials. Please check your password."))
           } catch (e: FirebaseAuthInvalidUserException) {
               // Invalid user error
               Result.failure(Exception("No account found with this email."))
           } catch (e: Exception) {
               // General exception handler
               Result.failure(Exception("Authentication failed. Please try again later."))
           }
   }

   }
*/
    override suspend fun getCurrentUser(): FirebaseUser? {
        return fireBaseAuth.currentUser
    }

    override suspend fun logout() {
        fireBaseAuth.signOut()
    }


   override suspend fun sendPasswordResetEmail(email: String): Boolean {
       return try {
           fireBaseAuth.sendPasswordResetEmail(email).await()
           true
       } catch (e: Exception) {
           false
       }
   }



    override suspend fun getUserName(userId: String): String? {
        val currentUser = fireBaseAuth.currentUser
        return currentUser?.let {
            try {
                val document = firestore.collection("users")
                    .document(it.uid)
                    .get()
                    .await()

                return document.getString("name")
            } catch (e: Exception) {
                null // Or handle error if needed
            }
        }

    }

    override suspend fun getUserRole(userId: String): String {
        val userDocument = firestore
            .collection("users")
            .document(userId)
            .get()
            .await()

        // Get role from Firestore document (assuming the field is named 'role')
        return userDocument.getString("role") ?: "customer" // Default role is "customer" if not found
    }

    override suspend fun getUserData(userId: String): Result<User> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUserId(): String? {
        return fireBaseAuth.currentUser?.uid
    }

    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun logoutUser() {
        fireBaseAuth.signOut()
    }

    override suspend fun getUserNameAndRole(): Pair<String?, String?>? {
        val currentUser = fireBaseAuth.currentUser
        return currentUser?.let {
            try {
                val document = firestore.collection("users")
                    .document(it.uid)
                    .get()
                    .await()

                val name = document.getString("name")
                val role = document.getString("role")
                return Pair(name, role)
            } catch (e: Exception) {
                null // Or handle error if needed
            }
        }
    }
}
