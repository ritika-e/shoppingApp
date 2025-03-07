package com.example.shoppingapp.presentation.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.common.CommonButton
import com.example.shoppingapp.presentation.common.CommonTextField
import com.example.shoppingapp.presentation.common.CommonDialog
import com.example.shoppingapp.utils.SharedPreferencesManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel = koinViewModel()  // Koin ViewModel injection
) {

    // observe states from viewModel
    val loginStatus by viewModel.loginStatus.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)


    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            // Navigate to the customer dashboard
            navHostController.navigate("customer_dashboard") {
                popUpTo("login") { inclusive = true } // Remove the login screen from back stack
            }
        }
    }


    // If loginStatus is not null, observe its state
   /* loginStatus?.let { result ->
        if (result.isSuccess) {
             val role = SharedPreferencesManager.getUserRole() ?: "user"
            val userName = SharedPreferencesManager.getUserName() ?: "user"
            // Navigate based on role
            if (role == "admin") {
                navHostController.navigate("admin_dashboard") {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }

            } else {
                navHostController.navigate("customer_dashboard"){
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            }
        } else {
            val exception = result.exceptionOrNull()
            errorMessage = exception?.message ?: "Login Failed"
            showDialog = true
        }
    }
*/    // Show dialog if there's an error message
   /* if (showDialog) {
        CommonDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            title = "Error",
            message = errorMessage,
            confirmButtonText = "OK",
            onConfirm = { showDialog = false }
        )
    }*/

    // UI layout for login screen
    Surface(modifier = Modifier.fillMaxSize()) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login_img),
                contentDescription = "Login an account",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "user Email",
                label = "User Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CommonTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Enter password",
                label = "Enter password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        navHostController.navigate("forgetPass")
                    },
                text = "Forget Password?",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator() // Show loading spinner
            } else {
                CommonButton(
                    text = "Login",
                    onClick = {
                        viewModel.login(email, password)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showDialog) {
                CommonDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    title = "Error",
                    message = errorMessage,
                    confirmButtonText = "OK",
                    onConfirm = { showDialog = false }
                )
            }

            Text(
                modifier = Modifier.clickable {
                    navHostController.navigate("signUp")
                },
                text = "Create an Account Sign Up",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
