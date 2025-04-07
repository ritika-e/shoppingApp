package com.example.shoppingapp.presentation.user

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.presentation.common.CommonTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navHostController: NavHostController = rememberNavController(),
                      viewModel: UserViewModel = koinViewModel()) {
    // State for editable fields
    var mobileNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

     val userProfile by viewModel.userProfile.observeAsState()
    val profileUpdated by viewModel.profileUpdated.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    var context: Context = LocalContext.current

     LaunchedEffect(true) {
        viewModel.loadUserProfile()
    }

    // Check if the user profile is loaded
    userProfile?.let { user ->
        // Initialize fields with current user profile data
        if (mobileNumber.isEmpty() && address.isEmpty()) {
            mobileNumber = user.mobileNumber
            address = user.address
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        TopAppBar(
            title = { Text(text = context.getString(R.string.update_Profile)) },
            navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack,
                        contentDescription = context.getString(R.string.Back_txt))
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Display user name, role, and email fields at the top
        userProfile?.let { user ->
            Text(context.getString(R.string.User_name_txt) + " : ${user.name}",
                style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text( context.getString(R.string.Email_txt)+ " : ${user.email}",
                style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text(context.getString(R.string.role) + " : ${user.role}",
                style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Editable Mobile Number field
        TextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text(context.getString(R.string.mobile_number)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Editable Address field
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(context.getString(R.string.address)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Update Profile button
        if(isLoading){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }else {
            Button(
                onClick = { viewModel.updateUserProfile(mobileNumber, address) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(context.getString(R.string.update_Profile))
            }
        }

        // Display success or failure message
        if (profileUpdated != null) {
            if (profileUpdated == true) {
                Text(context.getString(R.string.profile_update_successfully),
                    color = MaterialTheme.colorScheme.primary)
            } else {
                Text(context.getString(R.string.profile_update_failed),
                    color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}