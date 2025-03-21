package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.utils.SharedPreferencesManager
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.getKoin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavHostController,
    orderHistoryViewModel: OrderHistoryViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val orderHistory by orderHistoryViewModel.orderHistory.observeAsState(emptyList())
    val loading by orderHistoryViewModel.isLoading.observeAsState(false)

    // Get user ID (assuming the user is logged in)
    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
    val userId = sharedPreferencesManager.getUserData().userId

    Log.e("OrderHistoryScreen", "User ID: $userId") // Log user ID

    // Fetch orders if user ID exists
    if (userId != null) {
        LaunchedEffect(userId) {
            orderHistoryViewModel.fetchOrders(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (orderHistory.isEmpty()) {
                    Text(
                        "No orders found.",
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
                    )
                } else {
                    LazyColumn {
                        items(orderHistory) { order ->
                            OrderItemView(navController,order)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderItemView(navHostController: NavHostController,
    order: Order) {
    // Convert timestamp to a human-readable date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val formattedDate = try {
        val timestamp = order.orderDate.toLongOrNull()
        timestamp?.let { dateFormat.format(Date(it)) } ?: "Unknown Date"
    } catch (e: Exception) {
        "Invalid Date"
    }

    // Highlight order status (Pending, Completed, etc.)
    val statusColor = when (order.status.lowercase()) {
        "pending" -> Color.Blue
        "completed" -> Color.Green
        "cancelled" -> Color.Red
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Order ID: ${order.orderId}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total Amount: $${order.totalAmount}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Status: ${order.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Optional: Add a small icon indicating order status
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Order Status",
                    tint = statusColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Order Date: $formattedDate",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Button to navigate to Order Details (optional)
            Button(
                onClick = { navHostController.navigate("orderDetail/${order.orderId}") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = "View Details", color = Color.White)
            }
        }
    }
}
