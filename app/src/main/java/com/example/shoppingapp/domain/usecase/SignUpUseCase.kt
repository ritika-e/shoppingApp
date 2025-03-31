package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository
import kotlin.Result


open class SignUpUseCase(private val userRepository: AuthRepository) {
   open suspend operator fun invoke(name:String, email: String, password: String, role: String): Result<String> {
        return userRepository.signUp(name, email, password, role)
    }
}

/*
class LoginUseCase(private val userRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = userRepository.login(email, password)
}*/
