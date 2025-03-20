package com.example.shoppingapp.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.Order
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavHostController,
    orderId: String?,
    orderHistoryViewModel: OrderHistoryViewModel = koinViewModel()
) {
    val order by orderHistoryViewModel.order.observeAsState(null)
    val isLoading by orderHistoryViewModel.isLoading.observeAsState(false)
    val error by orderHistoryViewModel.error.observeAsState(null)

    LaunchedEffect(orderId) {
        orderHistoryViewModel.fetchOrderById(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (error != null) {
                    Text(text = error ?: "Unknown error", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                } else if (order != null) {
                    OrderDetailView(order = order!!)
                } else {
                    Text(text = "No order found.", modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                }
            }
        }
    )
}

@Composable
fun OrderDetailView(order: Order) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Order ID: ${order.orderId}", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Status: ${order.status}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Total Amount: $${order.totalAmount}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Order Date: ${order.orderDate}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Items:", style = MaterialTheme.typography.bodyMedium)

        order.cartItems.forEach { cartItem ->
            Text(text = " - ${cartItem.product.title} x ${cartItem.quantity}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "   Total: $${cartItem.productTotal}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
