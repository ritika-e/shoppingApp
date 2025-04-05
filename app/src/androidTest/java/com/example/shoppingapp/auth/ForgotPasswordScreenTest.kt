package com.example.shoppingapp.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.shoppingapp.presentation.auth.ForgetPasswordViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import com.example.shoppingapp.presentation.auth.ForgotPasswordScreen
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ForgotPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: ForgetPasswordViewModel

    @Before
    fun setup() {
        mockViewModel = mockk()
        // Setup the mock ViewModel
        every { mockViewModel.resetResult } returns MutableLiveData(true) // Mocking resetResult LiveData
        every { mockViewModel.isLoading } returns MutableLiveData(false) // Mocking isLoading property (no parentheses)
    }

    //PASSED
    @Test
    fun testEmailInputFieldIsDisplayed() {
        // Set the content for the test (No need to mock again, it's already done in setup)
        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        // Ensure Compose has finished rendering
        composeTestRule.waitForIdle()

        // Step 1: Ensure the email input field is displayed (checks for the placeholder text)
        composeTestRule.onNodeWithText("Enter your email") // Use label or placeholder text
            .assertIsDisplayed()

        // Step 2: Perform text input on the editable text field (EditableText node)
        composeTestRule.onNodeWithTag("emailInputField") // Use the test tag directly for the editable field
            .performTextInput("test@example.com")

        // Step 3: Assert that the entered text is correct in the EditableText node
        composeTestRule.onNodeWithText("test@example.com") // Assert directly on the editable text
            .assertIsDisplayed() // Ensures the editable text is displayed correctly
    }


    //PASSED
    @Test
    fun testEmailValidationForEmptyInput() {
        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        // Perform a click on the reset button without entering email
        composeTestRule.onNodeWithText("Reset password").performClick()

        // Check that the error dialog appears
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email cannot be empty").assertIsDisplayed()
    }

    //PASSED
    @Test
    fun testEmailValidationForInvalidFormat() {
        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        // Enter an invalid email
        composeTestRule.onNodeWithText("Enter your email").performTextInput("invalid-email")

        // Click the reset button
        composeTestRule.onNodeWithText("Reset password").performClick()

        // Verify that the error dialog appears for invalid email format
        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Invalid email format").assertIsDisplayed()
    }

    /* @SuppressLint("CheckResult")
     @Test
     fun testSuccessfulPasswordReset() {
         // Mock the LiveData with relaxed = true to handle internal methods like isInitialized() automatically
         val mockResetResult = mockk<LiveData<Boolean?>>(relaxed = true)

         // Mock the 'value' property of LiveData to simulate a successful password reset
         every { mockResetResult.value } returns true

         // Mock the ViewModel's resetPassword method to simulate password reset
         every { mockViewModel.resetPassword(any()) } returns Unit  // Assuming resetPassword returns Unit

         // Return the mocked LiveData when resetResult is accessed in the ViewModel
         every { mockViewModel.resetResult } returns mockResetResult

         // Mock loading state (LiveData for isLoading)
         val mockIsLoading = mockk<LiveData<Boolean>>(relaxed = true)
         every { mockIsLoading.value } returns false
         every { mockViewModel.isLoading } returns mockIsLoading

         // Set the content and simulate UI interactions
         composeTestRule.setContent {
             ForgotPasswordScreen(viewModel = mockViewModel)
         }

         // Enter a valid email
         composeTestRule.onNodeWithText("Enter your email").performTextInput("test@example.com")

         // Perform reset password click
         composeTestRule.onNodeWithText("Reset password").performClick()

         // Wait for Compose to finish rendering the UI
         composeTestRule.waitForIdle()

         // Use an increased timeout and try to fetch the dialog
         val dialogExists = composeTestRule.waitUntil(10000) {
             try {
                 // Check if the dialog is rendered
                 composeTestRule.onNodeWithTag("resetPasswordDialog").assertExists()
                 true
             } catch (e: AssertionError) {
                 false
             }
         }

         // Assert that the dialog exists in the composition
         composeTestRule.onNodeWithTag("resetPasswordDialog")
             .assertExists("Dialog is not in the composition tree.")

         // Ensure the dialog is displayed
         composeTestRule.onNodeWithTag("resetPasswordDialog")
             .assertIsDisplayed()

         // Optionally, assert that the dialog title matches
         composeTestRule.onNodeWithText("Check Your Email!")
             .assertIsDisplayed()

         // Optionally, assert that the success message is displayed
         composeTestRule.onNodeWithText("We’ve sent you an email with the instructions to reset your password. Please follow the link in the email.")
             .assertIsDisplayed()

         // Verify that resetPassword was called with the correct argument (email)
         verify { mockViewModel.resetPassword("test@example.com") }
     }*/


    @Test
    fun testFailedPasswordReset() {
        // Create a mock reset result
        val mockResetResult = MutableLiveData(false)
        every { mockViewModel.resetResult } returns mockResetResult
        every { mockViewModel.isLoading } returns MutableLiveData(false)

        // Mock the resetPassword function to do nothing (side effect for test)
        every { mockViewModel.resetPassword(any()) } just Runs

        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        // Enter a valid email in the text field
        composeTestRule.onNodeWithText("Enter your email").performTextInput("test@example.com")

        // Click the reset password button
        composeTestRule.onNodeWithText("Reset password").performClick()

        // Simulate the LiveData update (failure in this case) on the main thread
        composeTestRule.runOnUiThread {
            mockResetResult.postValue(false)  // postValue updates LiveData on the main thread
        }

        // Wait for the UI to update and for the LaunchedEffect to process
        composeTestRule.waitForIdle()

        // Log the UI hierarchy to verify the dialog appears
        composeTestRule.onRoot().printToLog("UI Hierarchy After Failure")

        // Verify the failure dialog appears in the tree with the correct text
        composeTestRule.onNodeWithText("Password Reset Failed!", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Failed to send password reset email", useUnmergedTree = true).assertExists()

        // Ensure that the "OK" button exists if that's part of the dialog
        composeTestRule.onNodeWithText("OK", useUnmergedTree = true).assertExists()
    }



    @Test
    fun testLoadingIndicatorWhileResetting() {
        // Mock the loading state
        every { mockViewModel.isLoading } returns MutableLiveData(true)

        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        // Verify the CircularProgressIndicator is displayed
        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertIsDisplayed()
    }

    /* @Test
     fun testBackToLoginNavigation() {
         val navController = rememberNavController()
         composeTestRule.setContent {
             ForgotPasswordScreen(navHostController = navController, viewModel = mockViewModel)
         }

         // Perform a click on the "Back to Login" text
         composeTestRule.onNodeWithText("Back to Login").performClick()

         // Verify that the navigation happened (back stack should be popped)
         assertTrue(navController.currentBackStackEntry == null)  // Or check for login screen navigation
     }*/

   /* @Test
    fun testDialogDismissalOnOkButtonClick() {
        // Create mock objects for ViewModel and LiveData
        val mockViewModel = mockk<ForgetPasswordViewModel>(relaxed = true)
        val mockResetResult = MutableLiveData(true)
        val mockIsLoading = MutableLiveData(false)

        // Mock the ViewModel to return these LiveData objects
        every { mockViewModel.resetResult } returns mockResetResult
        every { mockViewModel.isLoading } returns mockIsLoading

        // Set the Compose content for the screen
        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        // Wait for the Compose hierarchy to settle
        composeTestRule.waitForIdle()

        // Enter valid email
        composeTestRule.onNodeWithText("Enter your email").performTextInput("test@example.com")

        // Click the reset button
        composeTestRule.onNodeWithText("Reset password").performClick()

        // Optionally, manually trigger an update of the LiveData
        mockResetResult.postValue(true)  // Ensure the LiveData triggers the dialog

        // Wait for the UI to reflect changes
        composeTestRule.waitForIdle()

        // Verify the dialog is shown
        composeTestRule.onNodeWithTag("resetPasswordDialog").assertIsDisplayed()

        // Click the "OK" button to dismiss the dialog
        composeTestRule.onNodeWithText("OK").performClick()

        // Verify that the dialog is dismissed
        composeTestRule.onNodeWithTag("resetPasswordDialog").assertDoesNotExist()
    }

*/
}
