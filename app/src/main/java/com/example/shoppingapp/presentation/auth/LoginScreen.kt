package com.example.shoppingapp.presentation.auth

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.common.CommonButton
import com.example.shoppingapp.presentation.common.CommonTextField
import com.example.shoppingapp.presentation.common.CommonDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(false)
    val loginStatus by viewModel.loginStatus.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Observe the user role from the ViewModel
    val role by viewModel.userRole.observeAsState()

    // Observe login status from ViewModel
    loginStatus?.let { result ->
        if (result.isSuccess) {
            // Fetch the role after successful login
            viewModel.fetchUserRoleFromSharedPrefs()
        } else {
            val exception = result.exceptionOrNull()
            errorMessage = exception?.message ?: context.getString(R.string.Login_failed_txt)
            showDialog = true
        }
    }

    // If role is set, navigate to the appropriate dashboard
    role?.let { validRole ->
        if (validRole.isNotEmpty()) {
            Log.e("Login Screen", "Navigating to dashboard for role: $validRole")
            navigateToDashboard(validRole, navHostController)
        }
    }

    // UI layout for login screen
    Surface(modifier = Modifier.fillMaxSize()) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login_img),
                contentDescription = context.getString(R.string.Login_des_txt),
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
                placeholder = context.getString(R.string.Email_txt),
                label = context.getString(R.string.Email_txt)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CommonTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = context.getString(R.string.Password_txt),
                label = context.getString(R.string.Password_txt),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { navHostController.navigate("forgetPass") },
                text = context.getString(R.string.Forget_pass_txt),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                CommonButton(
                    text = context.getString(R.string.Login_btn),
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showDialog) {
                CommonDialog(
                    showDialog = showDialog,
                    onDismiss = { showDialog = false },
                    title = context.getString(R.string.Error_txt),
                    message = errorMessage,
                    confirmButtonText = context.getString(R.string.Ok_txt),
                    onConfirm = { showDialog = false }
                )
            }

            Text(
                modifier = Modifier.clickable { navHostController.navigate("signUp") },
                text = context.getString(R.string.Sign_up_txt),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun navigateToDashboard(role: String, navHostController: NavHostController) {
    Log.e("Login Screen 153", "Login => $role")
    val context = LocalContext.current
    when (role) {
        context.getString(R.string.admin_txt) -> {
            Log.e("Login Screen 158", "Login => $role")
            navHostController.navigate("admin_dashboard") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
        context.getString(R.string.Customer_txt) -> {
            Log.e("Login Screen 165", "Login => $role")
            navHostController.navigate("customer_dashboard") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
        else -> {
            Log.e("Login Screen 172", "Login => $role")
            navHostController.navigate("login") {
                popUpTo("splash_screen") { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
