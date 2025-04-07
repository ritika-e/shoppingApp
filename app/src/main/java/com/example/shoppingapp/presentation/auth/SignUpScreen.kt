package com.example.shoppingapp.presentation.auth

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.platform.testTag
import com.example.shoppingapp.presentation.common.CommonDialog
import com.example.shoppingapp.utils.SharedPreferencesManager
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.Result



@Composable
fun SignUpScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: SignupViewModel = koinViewModel()) {

    val context:Context = LocalContext.current

    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
    // Observe states from viewModel
    val isSignedUp by viewModel.isSignedUp.observeAsState(initial = false)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    val signUpStatus by viewModel.signUpStatus.observeAsState()

    var dialogMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }


    // Handle sign up result
    signUpStatus?.let { result ->
        if (result.isSuccess) {
            val role = sharedPreferencesManager.getUserData().userRole
            if (role == context.getString(R.string.admin_txt)) {
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
            val exception = result.exceptionOrNull() // Gets the exception (on failure)
            dialogMessage = exception?.message ?: context.getString(R.string.Sign_up_failed_txt)
            showDialog = true
            //viewModel.showDialog.value = true
        }
    }

    LaunchedEffect(isSignedUp) {
        if (isSignedUp) {
             if (viewModel.role == context.getString(R.string.admin_txt)) {
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
                contentDescription = context.getString(R.string.create_acc_txt),
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
                placeholder = context.getString(R.string.User_name_txt),
                label = context.getString(R.string.User_name_txt)
            )
            Spacer(modifier = Modifier.height(16.dp))
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
                label = context.getString(R.string.Password_txt),
                placeholder = context.getString(R.string.Password_txt),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CommonTextField(
                value = confirmPass,
                onValueChange = { confirmPass = it },
                placeholder = context.getString(R.string.Confirm_pass_txt),
                label = context.getString(R.string.Confirm_pass_txt),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))
            // Role selection (Customer / Admin)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = context.getString(R.string.Select_role_txt))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = role == context.getString(R.string.Customer_txt),
                        onClick = { role = context.getString(R.string.Customer_txt) })
                    Text(context.getString(R.string.Customer_txt), modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1F))
                    RadioButton(selected = role == context.getString(R.string.admin_txt),
                        onClick = {  role = context.getString(R.string.admin_txt) })
                    Text(context.getString(R.string.admin_txt), modifier = Modifier
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
                  //  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .testTag("loading_indicator")
                        )
                  //  }
                } else {
                    Log.e("Sign UI 1", "Button Press Log")
                    CommonButton(
                        text = context.getString(R.string.Signup_btn),

                        onClick = {
                            if (password == confirmPass) {
                                viewModel.signUp(name, email, password, role)
                            } else {
                                dialogMessage = context.getString(R.string.password_error)
                                showDialog = true
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Log.e("Sign UI 2", "Button Press Log")
                }

            Spacer(modifier = Modifier.height(16.dp))


            // Show dialog if necessary
            if (showDialog && dialogMessage != null ) {
                CommonDialog(
                    showDialog = showDialog,
                    onDismiss = {  showDialog = false },
                    title = context.getString(R.string.Error_txt),
                    message = dialogMessage ?: "",
                    confirmButtonText = context.getString(R.string.Ok_txt),
                    onConfirm = {  showDialog = false },
                    modifier = Modifier.testTag("ErrorDialog")
                )
            }

            Text(
                modifier = Modifier.clickable {
                    navHostController.navigate("login")
                },
                text = context.getString(R.string.already_have_acc_txt),
                color = MaterialTheme.colorScheme.primary)
        }
    }

}

