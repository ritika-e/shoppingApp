package com.example.shoppingapp.domain.repositories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class AuthRepositoryTest {

    // Mock the AuthRepository
    private val authRepository: AuthRepository = mockk()

    @Test
    fun `test registerUser calls the repository method`() = runBlocking {
        // Given
        val name = "John"
        val email = "john@example.com"
        val password = "password123"
        val role = "user"

        // Mocking the registerUser method to do nothing (suspend function)
        coEvery { authRepository.registerUser(name, email, password, role) } returns Unit

        // When
        authRepository.registerUser(name, email, password, role)

        // Then
        // Verifying that the registerUser method was called
        coVerify { authRepository.registerUser(name, email, password, role) }
    }

    @Test
    fun `test isUserLoggedIn returns true when user is logged in`() = runBlocking {
        // Given
        coEvery { authRepository.isUserLoggedIn() } returns true

        // When
        val result = authRepository.isUserLoggedIn()

        // Then
        assertTrue(result)
    }

    @Test
    fun `test isUserLoggedIn returns false when user is not logged in`() = runBlocking {
        // Given
        coEvery { authRepository.isUserLoggedIn() } returns false

        // When
        val result = authRepository.isUserLoggedIn()

        // Then
        assertFalse(result)
    }

    @Test
    fun `test getCurrentUserId returns non-null user id when logged in`() = runBlocking {
        // Given
        val userId = "user123"
        coEvery { authRepository.getCurrentUserId() } returns userId

        // When
        val result = authRepository.getCurrentUserId()

        // Then
        assertEquals(userId, result)
    }

    @Test
    fun `test getCurrentUserId returns null when no user is logged in`() = runBlocking {
        // Given
        coEvery { authRepository.getCurrentUserId() } returns null

        // When
        val result = authRepository.getCurrentUserId()

        // Then
        assertEquals(null, result)
    }
}
