package com.example.shoppingapp.presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommonDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    message: String,
    icon: @Composable (() -> Unit)? = null, // Optional icon
    confirmButtonText: String = "OK",
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                title?.let {
                    Text(text = it, style = MaterialTheme.typography.headlineMedium)
                }
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    icon?.let {
                        Icon(Icons.Default.Warning, contentDescription = "Icon", modifier = Modifier.padding(end = 8.dp))
                    }
                    Text(text = message)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss() // Dismiss the dialog after confirming
                    }
                ) {
                    Text(text = confirmButtonText)
                }
            }
        )
    }
}