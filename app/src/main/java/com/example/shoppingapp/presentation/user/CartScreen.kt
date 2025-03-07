package com.example.shoppingapp.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.domain.model.CartItemModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navHostController: NavHostController = rememberNavController(),
    cartViewModel: CartViewModel = koinViewModel()
) {
    val cart = cartViewModel.cart.observeAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") }
            )
        },
        content = { paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(cart.value?.items ?: emptyList()) { cartItem ->
                    CartItemView(cartItem, onRemoveClick = {
                       // cartViewModel.removeFromCart(cartItem.productId)
                    })
                }
            }
        }
    )
}

@Composable
fun CartItemView(cartItem: CartItemModel, onRemoveClick: () -> Unit) {
    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(cartItem.productName)
        Text("x${cartItem.quantity}")
        Text("$${cartItem.productPrice * cartItem.quantity}")
        IconButton(onClick = onRemoveClick) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Remove from cart")
        }
    }
}