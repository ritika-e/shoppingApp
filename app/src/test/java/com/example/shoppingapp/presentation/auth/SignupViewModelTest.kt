package com.example.shoppingapp.presentation.auth

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SignupViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var signupViewModel: SignupViewModel
    private val signUpUseCase: SignUpUseCase = mock()
    private val sharedPreferencesManager: SharedPreferencesManager = mock()

    @Before
    fun setup() {
        // Set the main dispatcher to the test dispatcher before running tests
        Dispatchers.setMain(testDispatcher)

        // Initialize your ViewModel
        signupViewModel = SignupViewModel(signUpUseCase, sharedPreferencesManager)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun testSignUpSuccess() = runTest {
        // Setup mock responses for signUpUseCase
        val name = "John Doe"
        val email = "john@example.com"
        val password = "password"
        val role = "customer"
        val userId = "12345"

        // Mocking successful signUpUseCase response
        Mockito.`when`(signUpUseCase(name, email, password, role)).thenReturn(Result.success(userId))

        // Observe LiveData changes
        val signUpStatusObserver = mock<Observer<Result<String>?>>()
        signupViewModel.signUpStatus.observeForever(signUpStatusObserver)

        val isSignedUpObserver = mock<Observer<Boolean>>()
        signupViewModel.isSignedUp.observeForever(isSignedUpObserver)

        // Trigger sign-up
        signupViewModel.signUp(name, email, password, role)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify sign-up success
        verify(signUpStatusObserver).onChanged(Result.success(userId))
        verify(isSignedUpObserver).onChanged(true)

        // Verify that sharedPreferencesManager.saveUserData() was called
        verify(sharedPreferencesManager).saveUserData(userId, name, role)
    }

    @Test
    fun testSignUpFailure() = runTest {
        // Setup mock responses for signUpUseCase
        val name = "John Doe"
        val email = "john@example.com"
        val password = "password"
        val role = "customer"

        // Mocking failed signUpUseCase response
        Mockito.`when`(signUpUseCase(name, email, password, role)).thenReturn(Result.failure(Exception("Sign-up failed")))

        // Observe LiveData changes
        val signUpStatusObserver = mock<Observer<Result<String>?>>()
        signupViewModel.signUpStatus.observeForever(signUpStatusObserver)

        val isSignedUpObserver = mock<Observer<Boolean>>()
        signupViewModel.isSignedUp.observeForever(isSignedUpObserver)

        // Trigger sign-up
        signupViewModel.signUp(name, email, password, role)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Use ArgumentCaptor to capture the argument passed to onChanged for signUpStatus
        val captor = argumentCaptor<Result<String>>()
        verify(signUpStatusObserver).onChanged(captor.capture())

        // Verify that the captured value is a failure with the expected exception message
        val result = captor.firstValue
        assert(result.isFailure) { "Expected failure result" }
        assert(result.exceptionOrNull()?.message == "Sign-up failed") { "Expected exception message to be 'Sign-up failed'" }

        // Verify that isSignedUp was not changed (i.e., still false)
        verify(isSignedUpObserver, never()).onChanged(true)

        // Ensure sharedPreferencesManager.saveUserData() was not called on failure
        verify(sharedPreferencesManager, never()).saveUserData(any(), any(), any())
    }
}
