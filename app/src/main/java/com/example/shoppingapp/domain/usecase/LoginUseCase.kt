package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser


class LoginUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.login(email, password)
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    suspend fun logout() {
        authRepository.logout() // Firebase sign-out
    }

    suspend operator fun invoke(userId: String): String {
        return authRepository.getUserRole(userId)
    }

    suspend fun getUserName(userId: String): String? {
        return authRepository.getUserName(userId)
    }

}