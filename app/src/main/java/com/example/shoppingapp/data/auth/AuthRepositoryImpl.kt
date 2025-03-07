package com.example.shoppingapp.data.auth

//Firebase setup
import android.util.Log
import com.example.shoppingapp.domain.model.User
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val fireBaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
    ) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        role: String
    ): Result<String> {
        return try {
            // Firebase Authentication
            val result = fireBaseAuth.createUserWithEmailAndPassword(email, password).await()

            // Create user object
            val user = result.user
            val userId = user?.uid ?: throw Exception("Unknown Error")
            // Store the user data in Firestore
            storeUserInFirestore(userId, name, email, role)

            Result.success(result.user?.uid ?: "Unknown Error")
        } catch (e: Exception) {
            Result.failure(e)
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

    /*override suspend fun getUserData(userId: String): Result<User> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val userId = document.getString("userId") ?: "No Name"
                val userName = document.getString("name") ?: "No Name"
                val role = document.getString("role") ?: "No Role"
                val email = document.getString("email") ?: "No Email"
                Result.success(User(userId,userName,email, role))
            } else {
                Result.failure(Exception("No such user"))
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
*/
    /* override suspend fun login(email: String, password: String): Result<String> {
        return try {
            // Sign in the user with email and password
            val authResult = fireBaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid  // Get the user ID after successful login

            if (userId != null) {
                Result.success(userId)  // Return user ID on success
            } else {
                Result.failure(Exception("Authentication failed"))  // If no user ID, return failure
            }
        } catch (e: Exception) {
            Result.failure(e)  // Return failure in case of an error
        }
    }*/




    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            // Sign in the user with email and password
            val authResult = fireBaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Authentication failed")

            // Return the UID of the authenticated user
            Result.success(uid)
         } catch (e: Exception) {
            Result.failure(e)  // Return failure result in case of error
        }
    }

    override suspend fun resetPassword(email: String) {
        try {
            fireBaseAuth.sendPasswordResetEmail(email).await()
        } catch (e: FirebaseAuthException) {
            throw Exception("Failed to send reset password email: ${e.message}")
        }
    }

    override suspend fun getUserName(): String? {
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

    override suspend fun getUserRole(userId: String): String? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            return document.getString("role")
        } catch (e: Exception) {
            null // Or handle error if needed
        }
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


    /*override suspend fun signUp(email: String, password: String, role: String): Boolean {
        return authService.signUp(email, password, role)
    }*/
    /* override suspend fun signUp(user: User, password: String): Result<String> {
         return try {
             val authResult = fireBaseAuth.createUserWithEmailAndPassword(user.email, password).await()
             val uid = authResult.user?.uid ?: throw Exception("Authentication failed")
             firestore.collection("users").document(uid).set(user).await()
             Result.success(uid)
         } catch (e: Exception) {
             Result.failure(e)
         }
     }
*/

    /*override suspend fun registerUser(name:String,email:String, password:String,role:String){
        authService.registerUser(name,email, password,role)
    }*/


    /* override suspend fun loginUser(email: String,password: String){
        authService.loginUser(email, password)
    }

    override fun getCurrentUserId(): String? {
        return authService.getCurrentUserId() // Get current user ID from Firebase
    }
    override fun isUserLoggedIn(): Boolean {
        return authService.isUserLoggedIn() // Check if the user is logged in
    }

  override  fun logoutUser() = authService.logoutUser()

    override suspend fun resetPassword(email: String) {
        authService.resetPassword(email)
    }*/


    /*override suspend fun getUserRole(userId: String): String? {
        return try {
            // Fetch the user document from Firestore
            val userDoc = firestore.collection("users").document(userId).get().await()
            Log.e("UserRepository 40", "User document does not exist for userId: $userId")
            // Check if the document exists
            if (userDoc.exists()) {
                Log.e("UserRepository", " IF")
                // Get the role field from the document
                userDoc.getString("role")
            } else {
                Log.e("UserRepository", "User document does not exist for userId: $userId")
                null // If document doesn't exist, return null
            }
        } catch (e: Exception) {
            // Handle any exceptions that may occur during the query
            Log.e("UserRepository", "Error fetching user role: ${e.message}")
            null
        }
    }*/


   /* override suspend fun getUserNameAndRole(): Pair<String?, String?>? {
        val currentUserId = fireBaseAuth.getCurrentUserId()

        if (currentUserId != null) {
            try {
                val userDoc = .collection("users").document(currentUserId).get().await()
                Log.e("Firebase Data", "" + userDoc.id)
                if (userDoc.exists()) {
                    val name = userDoc.getString("name")
                    val role = userDoc.getString("role")
                    Log.e("Firebase Data", "" + name)
                    Log.e("Firebase Data", "" + role)
                    return Pair(name, role) // Return both name and role as a Pair
                }
            } catch (e: Exception) {
                // Handle any errors
                Log.e("UserRepository", "Error fetching user data", e)
            }
        }
        return null // Return null if user is not found or there is an error
    }*/



