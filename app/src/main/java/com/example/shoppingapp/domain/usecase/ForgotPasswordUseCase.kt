package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository

class ResetPasswordUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String) {
         //authRepository.resetPassword(email)
    }
}