package com.example.shoppingapp.auth

import androidx.compose.material3.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.example.shoppingapp.presentation.auth.LoginScreen
import com.example.shoppingapp.presentation.auth.LoginViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.slot
import org.junit.runner.RunWith
import org.mockito.Mockito.mock


@MockK(relaxed = true)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    private var mockViewModel: LoginViewModel = mockk()
    private var navController: NavHostController = mockk()  // Mock the NavHostController
    private lateinit var loginUseCase: LoginUseCase


    @Before
    fun setup() {

        MockKAnnotations.init(this, relaxUnitFun = true)

        // Initialize your mock ViewModel
        mockViewModel = mockk()

        // Initialize your NavController (could be a mock or a real one if needed)
        navController = mockk(relaxed = true)

        loginUseCase = mockk()

        // Mock the 'isLoading' LiveData
        val isLoadingLiveData = MutableLiveData<Boolean>()
        every { mockViewModel.isLoading } returns isLoadingLiveData

        // You can mock other LiveData as needed, for example:
        val loginStatusLiveData = MutableLiveData<Result<String>>()
        every { mockViewModel.loginStatus } returns loginStatusLiveData

        val userRoleLiveData = MutableLiveData<String?>()
        every { mockViewModel.userRole } returns userRoleLiveData

        isLoadingLiveData.postValue(true)

    }

//PASSED
    @Test
    fun testLoginButtonClick_withValidCredentials_triggersLogin() = runTest {

        coEvery { mockViewModel.login(any(), any()) } just Runs

        coEvery { mockViewModel.isLoading } returns MutableLiveData(false)

        // Mocking the login function in ViewModel
        coEvery { mockViewModel.login("validUser@example.com", "password123") } returns Unit

        // Mocking the loginUseCase
        coEvery { loginUseCase.invoke("validUser@example.com", "password123") } returns Result.success("userId")

        // Setting the content to be tested
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel)
        }

        // Wait for the UI to be idle before interacting with it
        composeTestRule.waitForIdle()

        // Ensure the login button is present and visible
        composeTestRule.onNodeWithTag("LoginButton").assertExists("LoginButton should exist on the screen")

        // Type valid email and password into the input fields
        composeTestRule.onNodeWithTag("Email").performTextInput("validUser@example.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("password123")

        // Click the Login button
        composeTestRule.onNodeWithTag("LoginButton").performClick()

        // Verify that the login function in the ViewModel was called
        coVerify { mockViewModel.login("validUser@example.com", "password123") }

        // Optionally, verify the loading indicator appears
       // composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

//PASSED
    @Test
    fun testLoginButtonClick_withInvalidCredentials_displaysError() = runTest {
        // Mock the login use case to return failure
        coEvery { loginUseCase.invoke("invalidUser@example.com", "wrongpassword") } returns Result.failure(Exception("Invalid credentials"))

        // Mock the ViewModel's login function to just run without doing anything
        coEvery { mockViewModel.login(any(), any()) } just Runs

        // Create a MutableLiveData for loginStatus to simulate failure
        val loginStatusLiveData = MutableLiveData<Result<String>>()
        coEvery { mockViewModel.loginStatus } returns loginStatusLiveData

        // Mock the LiveData for isLoading to be false (to ensure button is visible)
        coEvery { mockViewModel.isLoading } returns MutableLiveData(false)

        // Set the content of the screen
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel)
        }

        // Wait for UI updates after setting the content
        composeTestRule.waitForIdle()

        // Type invalid email and password
        composeTestRule.onNodeWithTag("Email").performTextInput("invalidUser@example.com")
        composeTestRule.onNodeWithTag("Password").performTextInput("wrongpassword")

        // Wait for UI updates after typing
        composeTestRule.waitForIdle()

        // Click the login button to trigger login failure
        composeTestRule.onNodeWithTag("LoginButton").performClick()

        // Simulate login failure by updating loginStatus LiveData
        loginStatusLiveData.postValue(Result.failure(Exception("Invalid credentials")))

        // Wait for UI updates after loginStatus update
        composeTestRule.waitForIdle()

        // Assert that the error dialog is displayed
        composeTestRule.onNodeWithTag("ErrorDialog").assertIsDisplayed()

        // Check the content of the error message
        composeTestRule.onNodeWithText("Invalid credentials").assertExists()
    }

    //PASSED
    @Test
    fun testLoadingIndicatorVisibility() {
        // Test loading state
//        viewModel._isLoading.value = true
        mockViewModel._isLoading.postValue(true)
        composeTestRule.setContent {
            LoginScreen(viewModel = mockViewModel)
        }

        // Check that the loading indicator is displayed when isLoading is true
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()

        // Set isLoading to false and check if the button becomes visible
//        viewModel._isLoading.value = false
        mockViewModel._isLoading.postValue(false)
        composeTestRule.onNodeWithTag("LoginButton").assertIsDisplayed()
    }

    //PASSED
    @Test
    fun testSignUpNavigation() {
        // Mock the navController
        val navController = mockk<NavHostController>(relaxed = true)

        // Set up the composable in the test
        composeTestRule.setContent {
            LoginScreen(navHostController = navController, viewModel = mockViewModel)
        }
        composeTestRule.waitForIdle()

        // Click on "Sign Up"
        //  composeTestRule.onNodeWithText("Sign Up", ignoreCase = true).performClick()
        composeTestRule.onNodeWithContentDescription("Sign Up button").performClick()

        // Verify that the navigation to "signUp" happens
        verify { navController.navigate("signUp") }
    }
}
