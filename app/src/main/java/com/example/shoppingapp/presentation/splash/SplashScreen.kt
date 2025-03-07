package com.example.shoppingapp.presentation.splash


import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.auth.LoginViewModel
import com.example.shoppingapp.utils.SharedPreferencesManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(navHostController: NavHostController = rememberNavController(),
                 loginViewModel: LoginViewModel = koinViewModel(),
                 context:Context = LocalContext.current){


   // val userName = SharedPreferencesManager.getUserName(context)
    LaunchedEffect(Unit) {
        // Check login status and user role from Shared Preferences
        val isLoggedIn = SharedPreferencesManager.isLoggedIn() // Check if user is logged in
        val userRole = SharedPreferencesManager.getUserRole() // Get user role
        Log.e("Splash","status"+isLoggedIn)
        Log.e("Splash","userRole"+userRole)
        if (isLoggedIn) {
            // If logged in, navigate to respective dashboard based on role
            when (userRole) {
                "admin" -> {
                    navHostController.navigate("admin_dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                "customer" -> {
                    navHostController.navigate("customer_dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true

                    }
                }
                else -> {
                    // Handle case where role is undefined (e.g., redirect to login)
                    navHostController.navigate("login")
                }
            }
        } else {
            // If not logged in, navigate to the login screen
            navHostController.navigate("login") {
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }


    /*  LaunchedEffect(Unit) {
       delay(2000)     // delay for 2 seconds

          if (isLoggedIn){
              navHostController.navigate("customer_dashboard"){
                  popUpTo("splash"){inclusive = true}
              }
          }else{
              navHostController.navigate("login"){
                  popUpTo("splash"){inclusive = true}
              }
          }

      }*/

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
        ) {
        Box (contentAlignment = Alignment.Center){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp),
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPrev(){
    SplashScreen()
}


