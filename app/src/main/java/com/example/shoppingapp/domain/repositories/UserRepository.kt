package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.User

interface UserRepository {

    suspend fun updateUserProfile(userId: String, mobileNumber: String, address: String): Result<Unit>
    suspend fun getUserProfile(userId: String): Result<User>
}