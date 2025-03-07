package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.domain.repositories.AuthRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class RegisterUserUseCaseTest {

    private lateinit var registerUserUseCase: RegisterUserUseCase
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        authRepository = mock()  // Mocking AuthRepository
        registerUserUseCase = RegisterUserUseCase(authRepository)
    }
@Test
    fun `test successful user registration`() = runBlocking {
        // Arrange: Mock the repository to return successfully
        `when`(authRepository.registerUser(any(), any(), any(), any())).thenReturn(Unit)

        // Act: Call the execute method
        val result = registerUserUseCase.execute("John", "john@example.com", "password", "user")

        // Assert: Verify the result is Success
        assertEquals(Result.Success, result)

        // Verify that the registerUser method was called once
        verify(authRepository).registerUser("John", "john@example.com", "password", "user")
    }

    @Test(expected = Exception::class)
    fun `test registerUserUseCase failure`(): Unit = runBlocking {
        // Arrange: Simulate an exception being thrown by the repository
        Mockito.doThrow(Exception("Registration failed")).`when`(authRepository).registerUser("John", "john@example.com", "password", "customer")

        // Act: Call the method which will throw the exception
        registerUserUseCase.execute("John", "john@example.com", "password", "customer")
    }


    @Test
    fun `test failed user registration with unknown error`() = runBlocking {
        // Arrange: Mock the repository to throw an unchecked exception (RuntimeException)
        `when`(authRepository.registerUser(any(), any(), any(), any())).thenThrow(RuntimeException())

        // Act: Call the execute method
        val result = registerUserUseCase.execute("John", "john@example.com", "password", "user")

        // Assert: Verify the result is Failure with a generic message
        assertEquals(Result.Failure("Unknown error"), result)

        // Verify that the registerUser method was called once
        verify(authRepository).registerUser("John", "john@example.com", "password", "user")
    }

}