package com.example.shoppingapp.presentation.common

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.shoppingapp.R

@Composable
fun LogoutDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val context: Context = LocalContext.current
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text(text = context.getString(R.string.logout_txt)) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()  // Handle logout confirmation
                },
                    modifier = Modifier.testTag("LogoutConfirmButton")  // Add testTag
                ) {
                    Text(context.getString(R.string.confirm_txt))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss,
                    modifier = Modifier.testTag("LogoutDismissButton")  // Add testTag
                 ) {
                    Text(context.getString(R.string.cancel_txt))
                }
            }
        )
    }
}
