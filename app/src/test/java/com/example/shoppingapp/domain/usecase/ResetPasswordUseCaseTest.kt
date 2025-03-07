package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ResetPasswordUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var resetPasswordUseCase: ResetPasswordUseCase

    @Before
    fun setup() {
        // Initialize the mock repository and use case before each test
        authRepository = mock()
        resetPasswordUseCase = ResetPasswordUseCase(authRepository)
    }

    @Test
    fun `test successful password reset`() = runBlocking {
        // Arrange: Mock the repository to return nothing (Unit) for successful password reset
        `when`(authRepository.resetPassword("john@example.com")).thenReturn(Unit)

        // Act: Call the execute method for password reset
        resetPasswordUseCase.execute("john@example.com")

        // Assert: Verify that the resetPassword method was called with the correct argument
        verify(authRepository).resetPassword("john@example.com")
    }

    @Test
    fun `test failed password reset due to exception`() = runBlocking {
        // Arrange: Mock the repository to throw an exception when resetPassword is called
        `when`(authRepository.resetPassword("john@example.com")).thenThrow(RuntimeException("Password reset failed"))

        // Act & Assert: Verify that the exception is thrown when calling the use case
        assertFailsWith<RuntimeException> {
            resetPasswordUseCase.execute("john@example.com")
        }

        // Verify that the resetPassword method was called once
        verify(authRepository).resetPassword("john@example.com")
    }

    @Test
    fun `test reset password with empty email`() = runBlocking {
        // Arrange: Mock the repository to throw an exception for empty email
        `when`(authRepository.resetPassword("")).thenThrow(IllegalArgumentException("Email cannot be empty"))

        // Act & Assert: Verify that the exception is thrown for empty email
        assertFailsWith<IllegalArgumentException> {
            resetPasswordUseCase.execute("")
        }

        // Verify that the resetPassword method was called once
        verify(authRepository).resetPassword("")
    }

    @Test
    fun `test reset password with invalid email format`() = runBlocking {
        // Arrange: Mock the repository to throw an exception for invalid email format
        `when`(authRepository.resetPassword("invalid-email")).thenThrow(IllegalArgumentException("Invalid email format"))

        // Act & Assert: Verify that the exception is thrown for invalid email format
        assertFailsWith<IllegalArgumentException> {
            resetPasswordUseCase.execute("invalid-email")
        }

        // Verify that the resetPassword method was called once
        verify(authRepository).resetPassword("invalid-email")
    }
}