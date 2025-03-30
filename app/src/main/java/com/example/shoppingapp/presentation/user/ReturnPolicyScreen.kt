package com.example.shoppingapp.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.R

@Composable
fun ReturnPolicyScreen(navController: NavHostController) {
    val context = LocalContext.current

    // Example policy text
    val returnPolicyText = context.getString(R.string.return_policy_text)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Title for the Return Policy Screen
            Text(
                text = context.getString(R.string.return_policyTxt),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Display policy details
            Text(
                text = returnPolicyText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Spacer
            Spacer(modifier = Modifier.weight(1f))

            // Button to close or navigate back
            Button(
                onClick = {
                    // Pop back to the previous screen (Support Screen)
                    navController.navigateUp()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = context.getString(R.string.Back_txt))
            }
        }
    }
}
