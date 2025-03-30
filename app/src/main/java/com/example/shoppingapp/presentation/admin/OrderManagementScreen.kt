package com.example.shoppingapp.presentation.admin

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.Order
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderManagementScreen(
    navController: NavHostController,
    adminOrderViewModel: AdminOrderViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val orderHistory by adminOrderViewModel.orderHistory.observeAsState(emptyList())
    val loading by adminOrderViewModel.isLoading.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.order_list)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.Back_txt))
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (orderHistory.isEmpty()) {
                    Text(context.getString(R.string.no_order))
                } else {
                    LazyColumn {
                        items(orderHistory) { order ->
                            AdminOrderItemView(order, adminOrderViewModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AdminOrderItemView(order: Order, adminOrderViewModel: AdminOrderViewModel) {
    val context: Context = LocalContext.current

    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    val formattedDate = try {
        val timestamp = order.orderDate.toLongOrNull()
        timestamp?.let { dateFormat.format(Date(it)) } ?: context.getString(R.string.unknown_dates)
    } catch (e: Exception) {
        context.getString(R.string.invalid_dates)
    }

    // Set the status color based on the current status
    val statusColor = when (order.status) {
        "Accept" -> Color(0xFFFF9800)  // Orange
        "Reject" -> Color(0xFFE53935)  // Red
        "Delivered" -> Color(0xFF388E3C)  // Green
        else -> Color.Gray  // Default gray for pending or unknown status
    }

    // Button text style
    val buttonTextStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)

    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
            .shadow(5.dp, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Order ID and Total Amount
        Text(text = "Order ID: ${order.orderId}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Total Amount: $${order.totalAmount}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Order Date: $formattedDate", style = MaterialTheme.typography.bodyLarge)

        // Order Status (with dynamic color)
        Text(
            text = "Current Status: ${order.status}",
            style = MaterialTheme.typography.bodyMedium.copy(color = statusColor),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Display order status buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),  // Space buttons evenly
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Accept Button
            Button(
                onClick = { adminOrderViewModel.updateOrderStatus(order.orderId, context.getString(R.string.confirm_btn)) },
                enabled = order.status == "Pending"  ,  // Active only if the status is Pending
                modifier = Modifier.weight(1f),  // Ensure buttons are evenly spaced
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))  // Orange Color
            ) {
                Text(text = context.getString(R.string.confirm_btn), style = buttonTextStyle)
            }

            // Reject Button
            Button(
                onClick = { adminOrderViewModel.updateOrderStatus(order.orderId, context.getString(R.string.cancel_btn)) },
                enabled = order.status == "Pending" ,  // Active only if the status is Pending or Confirmed
                modifier = Modifier.weight(1f),  // Ensure buttons are evenly spaced
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))  // Red Color
            ) {
                Text(text = context.getString(R.string.cancel_btn), style = buttonTextStyle)
            }

            // Delivered Button (enabled if the order is confirmed)
            Button(
                onClick = { adminOrderViewModel.updateOrderStatus(order.orderId, context.getString(R.string.completed_btn)) },
                enabled = order.status == "Accept"  ,  // Active only if the status is Confirmed
                modifier = Modifier.weight(1f),  // Increase weight to make the button wider and avoid wrapping
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))  // Green Color
            ) {
                Text(text = context.getString(R.string.completed_btn), style = buttonTextStyle)
            }
        }
    }
}
