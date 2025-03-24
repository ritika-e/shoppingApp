package com.example.shoppingapp.presentation.splash

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
    val role = sharedPreferencesManager.getUserData().userRole

    // Check if the user is logged in
    val isLoggedIn = sharedPreferencesManager.isLoggedIn()

    // Launch a delay to simulate splash screen time
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds delay for the splash screen

        // Navigate based on login status
        if (isLoggedIn) {
            when (role){
                "admin" -> {
                    Log.e("Splash Screen ","admin => $role")
                    navController.navigate("admin_dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                "customer" -> {
                    Log.e("Splash Screen ","customer => $role")
                    navController.navigate("customer_dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }

    // UI for splash screen (App logo, name, etc.)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
