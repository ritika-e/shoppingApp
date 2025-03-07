package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import kotlin.test.assertFailsWith

class LoginUserUseCaseTest {

   /* private lateinit var authRepository: AuthRepository
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setup() {
        // Initialize the mock repository and use case before each test
        authRepository = mock()
        loginUserUseCase = LoginUserUseCase(authRepository)
    }

    @Test
    fun `test successful login`() = runBlocking {
        // Arrange: Mock the repository to return nothing (Unit) for successful login
        `when`(authRepository.loginUser("john@example.com", "password123")).thenReturn(Unit)

        // Act: Call the execute method for login
        loginUserUseCase.execute("john@example.com", "password123")

        // Assert: Verify that the loginUser method was called with the correct arguments
        verify(authRepository).loginUser("john@example.com", "password123")
    }

    @Test
    fun `test failed login due to exception`() = runBlocking {
        // Arrange: Mock the repository to throw an exception when loginUser is called
        `when`(authRepository.loginUser("john@example.com", "password123")).thenThrow(RuntimeException("Login failed"))

        // Act & Assert: Verify that the exception is thrown when calling the use case
        assertFailsWith<RuntimeException> {
            loginUserUseCase.execute("john@example.com", "password123")
        }

        // Verify that the loginUser method was called once
        verify(authRepository).loginUser("john@example.com", "password123")
    }

    @Test
    fun `test login with invalid email`() = runBlocking {
        // Arrange: Mock the repository to throw an exception for invalid email
        `when`(authRepository.loginUser("invalid_email", "password123")).thenThrow(RuntimeException("Invalid email"))

        // Act & Assert: Verify that the exception is thrown when calling the use case
        assertFailsWith<RuntimeException> {
            loginUserUseCase.execute("invalid_email", "password123")
        }

        // Verify that the loginUser method was called once
        verify(authRepository).loginUser("invalid_email", "password123")
    }

    @Test
    fun `test login with empty email`() = runBlocking {
        // Arrange: Mock the repository to throw an exception for empty email
        `when`(authRepository.loginUser("", "password123")).thenThrow(IllegalArgumentException("Email cannot be empty"))

        // Act & Assert: Verify that the exception is thrown for empty email
        assertFailsWith<IllegalArgumentException> {
            loginUserUseCase.execute("", "password123")
        }

        // Verify that the loginUser method was called once
        verify(authRepository).loginUser("", "password123")
    }*/
}
