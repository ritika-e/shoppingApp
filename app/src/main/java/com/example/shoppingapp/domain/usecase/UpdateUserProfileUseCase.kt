package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.data.user.UserRepositoryImpl
import com.example.shoppingapp.domain.model.User
import com.example.shoppingapp.domain.repositories.UserRepository

class UpdateUserProfileUseCase(private val userRepository: UserRepository) {

    suspend fun execute(userId: String, mobileNumber: String, address: String): Result<Unit> {
        return try {
            // Call the repository to perform the update operation
            userRepository.updateUserProfile(userId, mobileNumber, address)
        } catch (e: Exception) {
            Result.failure(e) // Return failure result in case of error
        }
    }

    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            userRepository.getUserProfile(userId)
        } catch (e: Exception) {
            Result.failure(e) // Return failure in case of error
        }
    }
}