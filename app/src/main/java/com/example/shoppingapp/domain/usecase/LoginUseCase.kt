package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser


open class LoginUseCase(private val authRepository: AuthRepository) {

    open  suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.login(email, password)
    }

    open suspend fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }

    open suspend fun logout() {
        authRepository.logout() // Firebase sign-out
    }

    open  suspend operator fun invoke(userId: String): String {
        return authRepository.getUserRole(userId)
    }

    open  suspend fun getUserName(userId: String): String? {
        return authRepository.getUserName(userId)
    }

}