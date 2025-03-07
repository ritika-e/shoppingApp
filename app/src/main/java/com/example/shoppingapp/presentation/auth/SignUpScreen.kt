package com.example.shoppingapp.presentation.auth

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.common.CommonButton
import com.example.shoppingapp.presentation.common.CommonTextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.example.shoppingapp.presentation.common.CommonDialog
import org.koin.androidx.compose.koinViewModel
import kotlin.Result



@Composable
fun SignUpScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: SignupViewModel = koinViewModel(),
    context: Context = LocalContext.current) {

    // Observe states from viewModel
    val isSignedUp by viewModel.isSignedUp.observeAsState(initial = false)
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    val signUpStatus by viewModel.signUpStatus.observeAsState()

    var dialogMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // Handle sign up result
    signUpStatus?.let { result ->
        if (result.isSuccess) {
            val name = result.getOrNull() // Gets the user ID (on success)
            dialogMessage = "Signed up successfully, User Name: $name"
            showDialog = true
        } else {
            val exception = result.exceptionOrNull() // Gets the exception (on failure)
            dialogMessage = exception?.message ?: "Unknown error"
            showDialog = true
            //viewModel.showDialog.value = true
        }
    }

    LaunchedEffect(isSignedUp) {
        if (isSignedUp) {
             if (viewModel.role == "admin") {
                 navHostController.navigate("admin_dashboard") {
                    popUpTo("signUp") { inclusive = true }
                }
            } else {
                 navHostController.navigate("customer_dashboard") {
                    popUpTo("signUp") { inclusive = true }
                }
            }
        }
    }



    Surface(modifier = Modifier.fillMaxSize()
    ) {
        var name by remember { mutableStateOf(viewModel.name) }
        var email by remember { mutableStateOf(viewModel.email) }
        var password by remember { mutableStateOf(viewModel.password) }
        var confirmPass by remember { mutableStateOf(viewModel.confirmPassword) }
        var role by remember { mutableStateOf(viewModel.role) }

        Box (
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.create_an_account),
                contentDescription = "Create an account",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp))
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CommonTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "User Name",
                label = "User Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CommonTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "User Email",
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CommonTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CommonTextField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
                placeholder = "Confirm Password",
                label = "Confirm Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))
            // Role selection (Customer / Admin)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Select Role")

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = role == "customer",
                        onClick = { role = "customer" })
                    Text("Customer", modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1F))
                    RadioButton(selected = role == "admin",
                        onClick = {  role = "admin" })
                    Text("Admin", modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1F))
                }

            }

            // Show error message if any
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            if (isLoading) {
                CircularProgressIndicator() // Show loading spinner
            } else {
            CommonButton(
                text = "Create Account",

                onClick = {
                    if (password == confirmPass) {
                    viewModel.signUp(name,email,password,role)
                }else{
                        dialogMessage = "Passwords does not match"
                        showDialog = true
                }

                },
                modifier = Modifier.fillMaxWidth()
            )
            }
            Spacer(modifier = Modifier.height(16.dp))


            // Show dialog if necessary
            if (showDialog && dialogMessage != null ) {
                CommonDialog(
                    showDialog = showDialog,
                    onDismiss = {  showDialog = false },
                    title = "Error",
                    message = dialogMessage ?: "",
                    confirmButtonText = "OK",
                    onConfirm = {  showDialog = false }
                )
            }

            Text(
                modifier = Modifier.clickable {
                    navHostController.navigate("login")
                },
                text = "I already have an Account Login",
                color = MaterialTheme.colorScheme.primary)
        }
    }

}

