package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository

open class ResetPasswordUseCase(private val authRepository: AuthRepository) {

  open suspend fun execute(email: String): Boolean {
       return authRepository.sendPasswordResetEmail(email)
   }
}