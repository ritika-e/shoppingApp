package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository


class LoginUseCase(private val userRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = userRepository.login(email, password)

   /* suspend fun execute(email:String,password:String) {
        userRepository.login(email, password)

        val userNameAndRole = userRepository.getUserNameAndRole()
        if (userNameAndRole != null) {
            val (name, role) = userNameAndRole

        } else {
            throw Exception("Unable to fetch user details (name, role)")
        }
    }*/
}



/*
class LoginUseCase(private val authRepository: AuthRepository) {
    suspend fun login(email: String, password: String): Result<Boolean> {
        return authRepository.login(email, password)
    }

    suspend fun logout(): Result<Boolean> {
        return authRepository.logout()
    }
}*/
