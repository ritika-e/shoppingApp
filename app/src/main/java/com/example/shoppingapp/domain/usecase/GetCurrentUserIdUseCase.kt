package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository

class GetCurrentUserIdUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): String? {
        // Use the AuthRepository to get the current user's ID
        return authRepository.getCurrentUserId()
    }
}