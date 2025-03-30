package com.example.shoppingapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoppingapp.presentation.admin.AdminDashboardScreen
import com.example.shoppingapp.presentation.admin.CustomerDetailsScreen
import com.example.shoppingapp.presentation.admin.CustomerListScreen
import com.example.shoppingapp.presentation.admin.GreetingSection
import com.example.shoppingapp.presentation.admin.OrderManagementScreen
import com.example.shoppingapp.presentation.admin.ProductManagementScreen
import com.example.shoppingapp.presentation.admin.ProductViewModel
import com.example.shoppingapp.presentation.auth.LoginScreen
import com.example.shoppingapp.presentation.auth.SignUpScreen
import com.example.shoppingapp.presentation.auth.ForgotPasswordScreen
import com.example.shoppingapp.ui.screen.AddProductScreen
import com.example.shoppingapp.presentation.splash.SplashScreen
import com.example.shoppingapp.presentation.user.CartScreen
import com.example.shoppingapp.presentation.user.CategoryItemsScreen
import com.example.shoppingapp.presentation.user.CustomerDashboardScreen
import com.example.shoppingapp.presentation.user.OrderDetailScreen
import com.example.shoppingapp.presentation.user.OrderHistoryScreen
import com.example.shoppingapp.presentation.user.ProductDetailsScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "splash") {
            composable("splash") {
                SplashScreen(navController)

            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("customer_dashboard") {
                    /*backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName")*/
                CustomerDashboardScreen(
                    navController)
            }
            composable("product_details/{productId}/{productName}",
                arguments = listOf(
                    navArgument("productId") { type = NavType.IntType },
                    navArgument("productName") { type = NavType.StringType }
                )){
                    backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                val productName = backStackEntry.arguments?.getString("productName") ?: ""
                ProductDetailsScreen(navController,
                    productId = productId,
                    //onBackClick = { navController.navigateUp() },
                    )
            }
            composable(
                "category_items/{categoryID}/{title}",
                arguments = listOf(navArgument("categoryID") { type = NavType.StringType },
                    navArgument("title"){type = NavType.StringType}
                )
            ) { backStackEntry ->
                val categoryID = backStackEntry.arguments?.getString("categoryID") ?: ""
                val title = backStackEntry.arguments?.getString("title") ?: ""
                CategoryItemsScreen(
                    navController,
                    categoryID = categoryID,
                    title = "Items for $title",
                    onBackClick = { navController.navigateUp() },
                )
            }
            composable("cart") {
                CartScreen(navController)
            }
            composable("order_history") {
                OrderHistoryScreen(navController)
            }
            composable("orderDetail/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")
                OrderDetailScreen(navController = navController, orderId = orderId)
            }

            composable("admin_dashboard") {
                AdminDashboardScreen(navController)
            }
            composable("greeting_section") {
                GreetingSection(navHostController = navController)
            }
            composable("signUp") {
                SignUpScreen(navController)
            }
            composable("forgetPass") {
                ForgotPasswordScreen(navController)
            }
            composable("orderManagement"){
                OrderManagementScreen(navController)
            }
            composable("addProducts"){
                AddProductScreen()
            }
            composable("productManagement"){
                ProductManagementScreen(navController)
            }
            composable("customer_list"){
                CustomerListScreen(navController)
            }
            composable("customerDetails/{userId}"){ backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                if (userId != null) {
                    CustomerDetailsScreen(navController,customerId = userId)
                }
            }
        }
    }
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Update the ViewModel with the selected image URI
                viewModel.selectedImageUri = uri
            }
        }
    }
}