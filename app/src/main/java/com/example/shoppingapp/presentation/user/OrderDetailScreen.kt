package com.example.shoppingapp.presentation.user

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import com.example.shoppingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    navController: NavHostController,
    orderId: String?,
    orderHistoryViewModel: OrderHistoryViewModel = koinViewModel()
) {
    val context:Context = LocalContext.current
    val order by orderHistoryViewModel.order.observeAsState(null)
    val isLoading by orderHistoryViewModel.isLoading.observeAsState(false)
    val error by orderHistoryViewModel.error.observeAsState(null)

    LaunchedEffect(orderId) {
        orderHistoryViewModel.fetchOrderById(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.order_details)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.Back_txt))
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Text(text = error ?: context.getString(R.string.unknown_error),
                        color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                } else if (order != null) {
                    OrderDetailView(order = order!!)
                } else {
                    Text(text = context.getString(R.string.no_order),
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                }
            }
        }
    )
}

@Composable
fun OrderDetailView(order: Order) {
    val context:Context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = context.getString(R.string.order_id, order.orderId)
            , style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = context.getString(R.string.status, order.status)
            , style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = context.getString(R.string.total_amt, order.totalAmount),
            style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Order Date: ${order.orderDate}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = context.getString(R.string.items), style = MaterialTheme.typography.bodyMedium)

        order.cartItems.forEach { cartItem ->
            Text(text = " - ${cartItem.product.title} x ${cartItem.quantity}",
                style = MaterialTheme.typography.bodyMedium)
            Text(text = "   Total: $${cartItem.productTotal}",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}
