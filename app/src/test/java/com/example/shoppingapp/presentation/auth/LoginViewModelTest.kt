package com.example.shoppingapp.presentation.auth

import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)  // Disable manifest processing for testing purposes
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var loginViewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mock()
    private val sharedPreferencesManager: SharedPreferencesManager = mock()

    @Before
    fun setup() {
        // Set the main dispatcher to the test dispatcher before running tests
        Dispatchers.setMain(testDispatcher)

        // Initialize your ViewModel
        loginViewModel = LoginViewModel(loginUseCase, sharedPreferencesManager)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun testLoginSuccess() = runTest {
        // Setup mock responses for loginUseCase
        val email = "test@example.com"
        val password = "password"
        val userId = "12345"
        val userRole = "admin"
        val userName = "John Doe"

        // Mocking successful loginUseCase response
        Mockito.`when`(loginUseCase(email, password)).thenReturn(Result.success(userId))
        Mockito.`when`(loginUseCase.invoke(userId)).thenReturn(userRole)
        Mockito.`when`(loginUseCase.getUserName(userId)).thenReturn(userName)

        // Observe LiveData changes
        val loginStatusObserver = mock<Observer<Result<String>?>>()
        loginViewModel.loginStatus.observeForever(loginStatusObserver)

        // Trigger login
        loginViewModel.login(email, password)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify login success
        verify(loginStatusObserver).onChanged(Result.success(userId))

        // Verify that sharedPreferencesManager.saveUserData() was called
        verify(sharedPreferencesManager).saveUserData(userId, userName, userRole)
    }

    @Test
    fun testLoginFailure() = runTest {
        // Setup mock responses for loginUseCase
        val email = "test@example.com"
        val password = "wrongpassword"

        // Mocking failed loginUseCase response
        Mockito.`when`(loginUseCase(email, password)).thenReturn(Result.failure(Exception("Login failed")))

        // Observe LiveData changes
        val loginStatusObserver = mock<Observer<Result<String>?>>()
        loginViewModel.loginStatus.observeForever(loginStatusObserver)

        // Trigger login
        loginViewModel.login(email, password)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Use ArgumentCaptor to capture the argument passed to onChanged
        val captor = argumentCaptor<Result<String>>()
        verify(loginStatusObserver).onChanged(captor.capture())

        // Verify that the captured value is a failure with the expected exception message
        val result = captor.firstValue
        assert(result.isFailure) { "Expected failure result" }
        assert(result.exceptionOrNull()?.message == "Login failed") { "Expected exception message to be 'Login failed'" }


        // Verify login failure
      //  verify(loginStatusObserver).onChanged(Result.failure(Exception("Login failed")))
    }
}
