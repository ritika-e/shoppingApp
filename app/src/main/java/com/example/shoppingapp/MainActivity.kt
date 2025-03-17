package com.example.shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoppingapp.presentation.admin.AdminDashboardScreen
import com.example.shoppingapp.presentation.admin.CustomerManagementScreen
import com.example.shoppingapp.presentation.admin.OrderManagementScreen
import com.example.shoppingapp.presentation.admin.ProductManagementScreen
import com.example.shoppingapp.presentation.auth.LoginScreen
import com.example.shoppingapp.presentation.auth.SignUpScreen
import com.example.shoppingapp.presentation.auth.ForgotPasswordScreen
import com.example.shoppingapp.ui.screen.AddProductScreen
import com.example.shoppingapp.presentation.splash.SplashScreen
import com.example.shoppingapp.presentation.user.CartScreen
import com.example.shoppingapp.presentation.user.CategoryItemsScreen
import com.example.shoppingapp.presentation.user.CustomerDashboardScreen
import com.example.shoppingapp.presentation.user.ProductDetailsScreen

class MainActivity : ComponentActivity() {

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
         /*   composable("product_details/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                ProductDetailsScreen(navController, productId)
            }*/
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
           /* composable("order_summary") {
                OrderSummaryScreen("Order successfully placed!")
            }
*/
            composable("admin_dashboard") {
                AdminDashboardScreen(navController)
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
                AddProductScreen(navController)
            }
            composable("productManagement"){
                ProductManagementScreen(navController)
            }
            composable("customerManagement"){
                CustomerManagementScreen(navController)
            }

        }
    }

}
