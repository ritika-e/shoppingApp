package com.example.shoppingapp.domain.usecase

import android.util.Log
import com.example.shoppingapp.data.user.UserRepository
import com.example.shoppingapp.domain.model.User
import com.example.shoppingapp.domain.repositories.AuthRepository

class UserUseCase(private val authRepository: AuthRepository) {

    /*suspend fun execute(userId: String): Result<User> {
        return authRepository.getUserData(userId)
    }*/

    suspend fun execute(): Result<Pair<String?, String?>> {
        return try {
            // Call the repository method
            val result = authRepository.getUserNameAndRole()

            // Check if the result is null or not
            if (result != null) {
                Result.success(result)
            } else {
                Result.failure(Exception("Failed to get user name and role"))
            }
        } catch (e: Exception) {
            Log.e("getUserNameAndRole Error", "Exception in getUserNameAndRole: ${e.message}")

            Result.failure(e)
        }
    }
}