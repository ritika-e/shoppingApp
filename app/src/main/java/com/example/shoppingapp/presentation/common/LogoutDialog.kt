package com.example.shoppingapp.presentation.common

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
                }) {
                    Text(context.getString(R.string.confirm_btn))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(context.getString(R.string.cancel_btn))
                }
            }
        )
    }
}
