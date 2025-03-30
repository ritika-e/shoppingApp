package com.example.shoppingapp.presentation.auth

import android.os.Looper
import androidx.lifecycle.Observer
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ForgetPasswordViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var forgetPasswordViewModel: ForgetPasswordViewModel
    private val resetPasswordUseCase: ResetPasswordUseCase = mock()

    @Before
    fun setup() {
        // Set the main dispatcher to the test dispatcher before running tests
        Dispatchers.setMain(testDispatcher)

        // Initialize your ViewModel
        forgetPasswordViewModel = ForgetPasswordViewModel(resetPasswordUseCase)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the tests
        Dispatchers.resetMain()
    }

    @Test
    fun testResetPasswordSuccess() = runTest {
        // Setup mock response for ResetPasswordUseCase
        val email = "test@example.com"

        // Mocking a successful password reset
        Mockito.`when`(resetPasswordUseCase.execute(email)).thenReturn(true)

        // Observe LiveData changes
        val resetResultObserver = mock<Observer<Boolean>>()
        forgetPasswordViewModel.resetResult.observeForever(resetResultObserver)

        val errorObserver = mock<Observer<String?>>()
        forgetPasswordViewModel.error.observeForever(errorObserver)

        // Trigger password reset
        forgetPasswordViewModel.resetPassword(email)

        // Ensure the coroutine is properly completed before verifying results
        testDispatcher.scheduler.advanceUntilIdle()  // Make sure that all the coroutines are finished

        Shadows.shadowOf(Looper.getMainLooper()).idle()

        // Verify reset result success
        verify(resetResultObserver).onChanged(true)

        // Verify error is not called
        verify(errorObserver, never()).onChanged(any())

        // Ensure that resetPasswordUseCase.execute() was called with the correct email
        verify(resetPasswordUseCase).execute(email)
    }

    @Test
    fun testResetPasswordFailure() = runTest {
        // Setup mock response for ResetPasswordUseCase
        val email = "test@example.com"

        // Mocking a failed password reset
        Mockito.`when`(resetPasswordUseCase.execute(email)).thenReturn(false)

        // Observe LiveData changes
        val resetResultObserver = mock<Observer<Boolean>>()
        forgetPasswordViewModel.resetResult.observeForever(resetResultObserver)

        val errorObserver = mock<Observer<String?>>()
        forgetPasswordViewModel.error.observeForever(errorObserver)

        // Trigger password reset
        forgetPasswordViewModel.resetPassword(email)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        Shadows.shadowOf(Looper.getMainLooper()).idle()


        // Verify reset result failure
        verify(resetResultObserver).onChanged(false)

        // Verify the error message is set
        verify(errorObserver).onChanged("Failed to send password reset email.")

        // Ensure that resetPasswordUseCase.execute() was called with the correct email
        verify(resetPasswordUseCase).execute(email)
    }

    @Test
    fun testResetPasswordException() = runTest {
        // Setup mock response for ResetPasswordUseCase that throws an unchecked exception
        val email = "test@example.com"

        // Mocking an exception in ResetPasswordUseCase (using RuntimeException instead of Exception)
        Mockito.`when`(resetPasswordUseCase.execute(email)).thenThrow(RuntimeException("Network error"))

        // Observe LiveData changes
        val resetResultObserver = mock<Observer<Boolean>>()
        forgetPasswordViewModel.resetResult.observeForever(resetResultObserver)

        val errorObserver = mock<Observer<String?>>()
        forgetPasswordViewModel.error.observeForever(errorObserver)

        // Trigger password reset
        forgetPasswordViewModel.resetPassword(email)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        Shadows.shadowOf(Looper.getMainLooper()).idle()


        // Verify reset result is not updated
        verify(resetResultObserver, never()).onChanged(any())

        // Verify the error message is set
        verify(errorObserver).onChanged("Error occurred: Network error")

        // Ensure that resetPasswordUseCase.execute() was called with the correct email
        verify(resetPasswordUseCase).execute(email)
    }

}
