package com.example.shoppingapp.presentation.auth

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.common.CommonButton
import com.example.shoppingapp.presentation.common.CommonTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: ForgetPasswordViewModel = koinViewModel()

) {

    var email by remember { mutableStateOf("") }

    // Observe reset password state
    //val resetPasswordState by viewModel.resetPasswordState.observeAsState(ForgetPasswordViewModel.ResetPasswordState.Loading)

    var isNavigated by remember { mutableStateOf(false) }
    val resetPasswordState by viewModel.resetPasswordState.observeAsState()

    when (val state = resetPasswordState) {
        is ForgetPasswordViewModel.ResetPasswordState.Loading -> {
            CircularProgressIndicator() // Show loading spinner
        }
        is ForgetPasswordViewModel.ResetPasswordState.Success -> {
            if (!isNavigated) {
                // Only navigate and show toast once
                isNavigated = true
                Toast.makeText(navHostController.context, state.message, Toast.LENGTH_LONG).show()

                // Navigate to the login screen
                navHostController.navigate("login") {
                    popUpTo("forgetPass") { inclusive = true }
                }
            }
        }
        is ForgetPasswordViewModel.ResetPasswordState.Error -> {
            Toast.makeText(navHostController.context, state.message, Toast.LENGTH_LONG).show()
        }

        else -> {}
    }



    Surface(modifier = Modifier.fillMaxSize()) {
        Box (
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.forgot_password),
                contentDescription = "Reset password",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp))
        }
        Column (modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CommonTextField(
                value = email,
                onValueChange = {email = it},
                placeholder = "Enter your email",
                label = "Email")
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "We will send you a message to set or reset your new password.",
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))

            CommonButton(
                    text = "Reset Password",
                    onClick = {
                        viewModel.sendPasswordResetEmail(email)
                    },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.clickable {
                    navHostController.popBackStack() // Or navigate to login
                },
                text = "Back to Login",
                color = MaterialTheme.colorScheme.primary
            )

        }
        
    }
}

@Preview
@Composable
fun ForgetPassPrev(){
    ForgotPasswordScreen()
}