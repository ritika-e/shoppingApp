package com.example.shoppingapp.presentation.auth

import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.common.CommonButton
import com.example.shoppingapp.presentation.common.CommonDialog
import com.example.shoppingapp.presentation.common.CommonTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: ForgetPasswordViewModel = koinViewModel(),


) {
    val context: Context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val resetResult = viewModel.resetResult.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.observeAsState(false)
    var dialogTitle by remember { mutableStateOf("") }


    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    // Validate the email format and check for empty email
    fun validateEmail(email: String): Boolean {
        return when {
            email.isEmpty() -> {
                emailError = context.getString(R.string.email_validation_txt)
                dialogTitle = context.getString(R.string.Error_txt)
                showDialog = true
                false
            }
            !email.matches(emailRegex) -> {
                emailError = context.getString(R.string.Invalid_email_txt)
                dialogTitle = context.getString(R.string.Error_txt)
                showDialog = true
                false
            }
            else -> {
                emailError = "" // No error
                showDialog = false
                true
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box (
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.forgot_password),
                contentDescription = context.getString(R.string.Reset_pass_txt),
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
                placeholder = context.getString(R.string.Enter_email_txt),
                label = context.getString(R.string.Enter_email_txt))
            
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = context.getString(R.string.Reset_txt),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
            CommonButton(
                    text = context.getString(R.string.Reset_pass_txt),
                    onClick = {
                        if (validateEmail(email)) {
                            viewModel.resetPassword(email)
                        }
                    },
                modifier = Modifier.fillMaxWidth()
            )}


                LaunchedEffect(resetResult.value) {
                    resetResult.value?.let { success ->
                    if (success) {
                        emailError = context.getString(R.string.Reset_msg_txt)
                        dialogTitle = context.getString(R.string.reset_dialog_title)
                        email = ""
                    } else {
                        emailError = context.getString(R.string.Reset_fail_msg_txt)
                        dialogTitle = context.getString(R.string.reset_dialog_title_fail)
                        //showDialog = false
                    }

                        showDialog = true
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.clickable {
                    navHostController.popBackStack() // Or navigate to login
                },
                text = context.getString(R.string.back_to_login_txt),
                color = MaterialTheme.colorScheme.primary
            )

        }

        if (showDialog) {
            CommonDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                title = dialogTitle,
                message = emailError,
                confirmButtonText = context.getString(R.string.Ok_txt),
                onConfirm = { showDialog = false }
            )
        }


    }
}

@Preview
@Composable
fun ForgetPassPrev(){
    ForgotPasswordScreen()
}