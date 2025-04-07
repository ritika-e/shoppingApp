package com.example.shoppingapp.data.user

import android.util.Log
import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.model.User
import com.example.shoppingapp.domain.repositories.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val firebaseFirestore: FirebaseFirestore) : UserRepository {


    override suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            // Perform the query to find the user by userId field
            val querySnapshot = firebaseFirestore.collection("users")
                .whereEqualTo("userId", userId) // Search for the user based on the userId field
                .get()
                .await()

            // Check if the query result is empty
            if (querySnapshot.isEmpty) {
                Log.d("UserRepository", "No user found for ID: $userId")
                Result.failure(Exception("User not found"))
            } else {
                // Log if the document exists and return the user data
                val user = querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
                Log.d("UserRepository", "User found: ${user?.userId}")
                if (user != null) {
                    Result.success(user) // Return the user if found
                } else {
                    Result.failure(Exception("Failed to map user data"))
                }
            }
        } catch (e: Exception) {
            // Log any errors that occur during the fetch operation
            Log.e("UserRepository", "Error fetching user with ID: $userId - ${e.message}")
            Result.failure(e) // Return failure in case of error
        }
    }

    override suspend fun updateUserProfile(userId: String, mobileNumber: String, address: String): Result<Unit> {
        return try {
            // First, get the document reference for the specific user
            val userRef = firebaseFirestore.collection("users").document(userId)

            // Prepare the map with the fields to be updated
            val updates = hashMapOf<String, Any>(
                "mobileNumber" to mobileNumber,
                "address" to address
            )

            // Perform the update operation on the user document
            userRef.update(updates).await()

            // Return success if the update is successful
            Log.d("UserRepository", "User profile updated successfully for userId: $userId")
            Result.success(Unit) // Return success with empty result (Unit)

        } catch (e: Exception) {
            // Log the error if the update fails
            Log.e("UserRepository", "Error updating user profile for userId: $userId - ${e.message}")
            Result.failure(e) // Return failure with the exception
        }
    }
}

