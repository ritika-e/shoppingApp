package com.example.shoppingapp.user

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.repositories.ProductRepository
import com.example.shoppingapp.domain.usecase.productUseCases.GetBannersUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoriesUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoryItemUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetProductDetailsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetRecommendedProductsUseCase
import com.example.shoppingapp.presentation.auth.LoginScreen
import com.example.shoppingapp.presentation.user.CategoryItem
import com.example.shoppingapp.presentation.user.CategoryList
import com.example.shoppingapp.presentation.user.CustomerDashboardScreen
import com.example.shoppingapp.presentation.user.ProductDetailsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CustomerDashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var mockGetBannersUseCase: GetBannersUseCase
    private lateinit var mockGetCategoriesUseCase: GetCategoriesUseCase
    private lateinit var mockGetCategoryItemUseCase: GetCategoryItemUseCase
    private lateinit var mockGetRecommendedProductsUseCase: GetRecommendedProductsUseCase
    private lateinit var  mockGetProductDetailsUseCase: GetProductDetailsUseCase

    @Before
    fun setUp() {
        // Initialize mocks using mockk()
        mockGetBannersUseCase = mockk()
        mockGetCategoriesUseCase = mockk()
        mockGetCategoryItemUseCase = mockk()
        mockGetRecommendedProductsUseCase = mockk()
        mockGetProductDetailsUseCase = mockk()

        // Create a mock ViewModel instance
        viewModel = mockk()

        // Mock LiveData for categories
        val mockCategories = listOf(CategoryModel(id = 1, title = "Category 1", picUrl = "url1"))
        val liveDataCategories = MutableLiveData<List<CategoryModel>>()
        liveDataCategories.postValue(mockCategories)

      //  liveDataCategories.value = mockCategories

        // Mock the getCategories method to return liveDataCategories
        every { viewModel.categories } returns liveDataCategories
    }

    @Test
    fun testWelcomeTextDisplaysCorrectly() {
        composeTestRule.setContent {
            CustomerDashboardScreen(navHostController = rememberNavController())
        }

        // Wait for the UI to finish loading
     //   composeTestRule.onNodeWithContentDescription("Loading categories").assertDoesNotExist()
        composeTestRule.onNodeWithTag("WelcomeText").assertIsDisplayed()

    }

    @Test
    fun testLoadingIndicatorDisplays() {
        composeTestRule.setContent {
            CustomerDashboardScreen(navHostController = rememberNavController())
        }

        // Wait for the UI to settle and recompose
        composeTestRule.waitForIdle()

        // Verify the loading indicator for banners is shown initially
        composeTestRule.onNodeWithTag("Loading banners").assertIsDisplayed()

        //   verify other indicators for categories and recommended items
        composeTestRule.onNodeWithTag("Loading categories").assertIsDisplayed()
    }

    /*@Test
    fun testCategoryItemNavigation() {
        // Create a TestNavHostController for navigation
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        // Initialize mock categories data
        val category = CategoryModel(id = 1, title = "Category 1", picUrl = "url1")
        val categories = listOf(category)// This ensures the LiveData is not null

        // Initialize the LiveData with the mock categories
        val liveDataCategories = MutableLiveData<List<CategoryModel>>()

        //   postValue to safely update the LiveData (important for background thread safety)
        liveDataCategories.postValue(categories)

        // Mock the ViewModel and properly mock the LiveData
        val viewModel = mock(ProductDetailsViewModel::class.java)
        `when`(viewModel.categories).thenReturn(liveDataCategories)  // This line correctly mocks the `categories` LiveData

        // Set the content for the test
        composeTestRule.setContent {
            CustomerDashboardScreen(
                navHostController = navController,
                productDetailsViewModel = viewModel
            )
        }

        // Wait for the UI to be idle
        composeTestRule.waitForIdle()

        // Get the test tag for the first category item (assuming Category 1)
        val testTag = "CategoryItem_${category.title.replace(" ", "_")}"

        // Ensure that the category item is available
        composeTestRule.onNodeWithTag(testTag).assertExists()

        // Scroll the category item into view
        composeTestRule.onNodeWithTag(testTag).performScrollTo()

        // Assert that the category item is displayed
        composeTestRule.onNodeWithTag(testTag).assertIsDisplayed()

        // Interact with the category item node (simulate click)
        composeTestRule.onNodeWithTag(testTag).performClick()

        // Wait a bit to ensure navigation happens
        composeTestRule.mainClock.advanceTimeBy(100)

        // Get the current route from the navHostController
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        // Verify that the current route matches the expected route
        val expectedRoute = "category_items/1/Category_1"
        assert(currentRoute?.contains(expectedRoute) == true) {
            "Expected route to be '$expectedRoute' but was $currentRoute"
        }
    }
*/

    @Test
    fun testLogoutDialogShowsUp() {
        // Set the composable content
        composeTestRule.setContent {
            CustomerDashboardScreen(navHostController = rememberNavController())
        }

        // Wait for the UI to finish rendering
        composeTestRule.waitForIdle()

        // Click on the logout button (Exit icon)
        composeTestRule.onNodeWithTag("LogoutIcon")  // Ensure this triggers the dialog
            .performClick()

        // Wait for the dialog to appear after the button click
        composeTestRule.waitForIdle()

        // Verify if the logout dialog is displayed using the text from the string resource
        composeTestRule.onNodeWithText("Are you sure you want to logout?")  // Match the exact text from R.string.logout_txt
            .assertExists()  // Ensure that the node exists

        // Check if the "Yes" button (confirm) is displayed by its tag
        composeTestRule.onNodeWithTag("LogoutConfirmButton")
            .assertIsDisplayed()

        // Check if the "No" button (dismiss) is displayed by its tag
        composeTestRule.onNodeWithTag("LogoutDismissButton")
            .assertIsDisplayed()
    }


    @Test
    fun testLogoutFunctionality() {
        composeTestRule.setContent {
           // CustomerDashboardScreen(navHostController = rememberNavController())
            val navController = rememberNavController()
            // Set up the NavHost here
            NavHost(navController = navController, startDestination = "customer_dashboard") {
                composable("customer_dashboard") {
                    CustomerDashboardScreen(navHostController = navController)
                }
                composable("login") {
                    // Replace with actual login screen composable for testing
                    LoginScreen()
                }
            }
        }


        // Trigger the dialog to show
        composeTestRule.onNodeWithTag("LogoutIcon")  // Make sure this triggers the dialog
            .performClick()

        // Ensure the dialog is displayed
        composeTestRule.onNodeWithTag("LogoutConfirmButton")  // Check the 'Yes' button by its tag
            .assertIsDisplayed()

        // Click the "Yes" button to confirm logout
        composeTestRule.onNodeWithTag("LogoutConfirmButton")
            .performClick()

          composeTestRule.onNodeWithText("Login")
            .assertIsDisplayed()
    }

    @Test
    fun testCategoryItemSelectedHighlight() {
        // Prepare category data
        val categories = mutableStateListOf(CategoryModel("Category Name", 1))

        // Set content for the test
        composeTestRule.setContent {
            CategoryList(categories = categories)
        }

        // Wait for the UI to finish rendering and categories to be loaded
        composeTestRule.waitForIdle()

        val categoryName = "Category Name"
        val testTag = "CategoryItem_${categoryName.replace(" ", "_")}"

        // Ensure the category item exists in the UI
        composeTestRule.onNodeWithTag(testTag).assertExists("Category item with tag $testTag does not exist.")

        // Assert that the content description contains the category name, even if it includes selection status
        composeTestRule.onNodeWithTag(testTag)
            .assertContentDescriptionContains(categoryName)
    }

}
