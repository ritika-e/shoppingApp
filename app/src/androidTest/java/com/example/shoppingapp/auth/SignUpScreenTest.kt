package com.example.shoppingapp.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.presentation.auth.LoginScreen
import com.example.shoppingapp.presentation.auth.SignUpScreen
import com.example.shoppingapp.presentation.auth.SignupViewModel
import com.example.shoppingapp.utils.SharedPreferencesManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import net.bytebuddy.matcher.ElementMatchers.any
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Create a mock viewModel and NavController
    private lateinit var mockViewModel: SignupViewModel
    private lateinit var mockNavController: NavHostController
    private lateinit var mockSignUpUseCase: SignUpUseCase

    private lateinit var mockSharedPreferencesManager: SharedPreferencesManager

    @Before
    fun setup() {
        mockSignUpUseCase = mockk()
        mockSharedPreferencesManager = mockk()

        mockViewModel = mockk(relaxed = true)

        val isLoadingLiveData = MutableLiveData<Boolean>()
        every { mockViewModel.isLoading } returns isLoadingLiveData

        // Mock the signUpUseCase to return a successful result
        mockViewModel = spyk(SignupViewModel(mockSignUpUseCase, mockSharedPreferencesManager))

        // Setup LiveData to return a successful sign-up result
        coEvery  { mockSignUpUseCase.invoke(any(), any(), any(), any()) } returns Result.success("userId")

        // Mock the saveUserData method to avoid MockK exception
        every { mockSharedPreferencesManager.saveUserData(any(), any(), any()) } returns Unit

        // Mock LiveData states
        every { mockViewModel.signUpStatus } returns MutableLiveData(Result.success("userId"))
        every { mockViewModel.isLoading } returns MutableLiveData(false)
        every { mockViewModel.isSignedUp } returns MutableLiveData(true)

        mockNavController = mockk(relaxed = true)

        // Launch the SignUpScreen with the mock viewModel
        composeTestRule.setContent {
            SignUpScreen(navHostController = mockk(relaxed = true), viewModel = mockViewModel)
        }
        isLoadingLiveData.postValue(true)
    }

    //PASSED
    @Test
    fun testUIComponentsVisible() {

        composeTestRule.waitForIdle()
        composeTestRule.onRoot().printToLog("UI Tree")

        // Verify that the text fields and buttons are visible
        composeTestRule.onNodeWithText("User Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("User Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create Account").assertIsDisplayed()
    }

    //PASSED
    @Test
    fun testSignUpButtonClick() = runTest {
        // Simulate clicking the "Sign Up" button
        composeTestRule.onNodeWithText("Create Account").performClick()

        // Verify that the signUp function of viewModel is called
        verify { mockViewModel.signUp(any(), any(), any(), any()) }

        // Ensure saveUserData was called with the correct arguments
        verify { mockSharedPreferencesManager.saveUserData("userId", any(), "customer") }

    }

    @Test
    fun testSignUpButtonClicked_SuccessfulSignUp() {
        // Setup mock NavHostController and ViewModel for testing
        val mockNavController: NavHostController = mockk(relaxed = true)
        val mockViewModel: SignupViewModel = mockk(relaxed = true)

        // Mock LiveData for isSignedUp to trigger navigation after sign-up
        val isSignedUpLiveData = MutableLiveData(false)
        every { mockViewModel.isSignedUp } returns isSignedUpLiveData

        // Mock the behavior of the ViewModel's signUp method
        every { mockViewModel.signUp(any(), any(), any(), any()) } answers {
            // Simulate a successful sign-up action
            isSignedUpLiveData.postValue(true)
        }

        // Now simulate the logic without using `setContent`
        // Trigger the sign-up action by manually invoking the signUp function
        mockViewModel.signUp("user", "user@example.com", "password123", "customer")

        // Wait for the UI to become idle
        composeTestRule.waitForIdle()

        // Verify that the ViewModel's signUp function is called
        verify { mockViewModel.signUp(any(), any(), any(), any()) }

        // Verify that navigation happened after sign-up
        val login: NavDirections = mockk()  // This should be the NavDirections you expect to navigate to
        verify { mockNavController.navigate(eq(login)) }
        Log.d("Test", "Verification passed for navigation")

    }

//PASSED
    @Test
    fun testPasswordMismatch_ShowsErrorDialog() {
        // Setup the composable with mismatched password

        // Enter mismatched passwords
        composeTestRule.onNodeWithText("Enter password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password321")

        // Perform click on the "Sign Up" button
        composeTestRule.onNodeWithText("Create Account").performClick()

        composeTestRule.waitForIdle()

        // Verify the error dialog appears with the correct message
        composeTestRule.onNodeWithText("Passwords does not match")
            .assertIsDisplayed()
            .assertTextContains("Passwords does not match")    }





}