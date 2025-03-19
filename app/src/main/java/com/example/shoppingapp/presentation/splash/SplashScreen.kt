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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.utils.SharedPreferencesManager
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun SplashScreen(navHostController: NavHostController = rememberNavController(),
                 context:Context = LocalContext.current){


     LaunchedEffect(Unit) {
        // Check login status and user role from Shared Preferences

        val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
        val role = sharedPreferencesManager.getUserData().userRole

      //  if (isLoggedIn) {
            // If logged in, navigate to respective dashboard based on role
            when (role) {
                "admin" -> {
                    Log.e("Splash Screen ","admin => $role")
                    navHostController.navigate("admin_dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                "customer" -> {
                    Log.e("Splash Screen ","customer => $role")
                    navHostController.navigate("customer_dashboard") {
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                else -> {
                    Log.e("Splash Screen ","else => $role")
                    // Handle case where role is undefined (e.g., redirect to login)
                    navHostController.navigate("login"){
                        popUpTo("splash_screen") { inclusive = true }
                        launchSingleTop = true
                    }
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
  //  SplashScreen()
}


