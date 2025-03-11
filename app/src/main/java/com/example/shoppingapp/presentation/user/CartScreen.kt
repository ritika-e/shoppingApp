package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shoppingapp.domain.model.CartItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = koinViewModel()
) {
    // Observe the cart items
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val orderStatus by cartViewModel.orderStatus.observeAsState("")
    Log.e("CART ITEMS","--- $cartItems")

    if (cartItems != null && cartItems!!.isNotEmpty()) {
        Log.e("Order", "Cart is NOT empty")
    } else {
        Log.e("Order", "Cart is empty")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (cartItems!!.isEmpty()) {
                    Text(
                        text = "Your cart is empty",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                } else {
                    LazyColumn {
                        items(cartItems!!) { cartItem ->
                            CartItemView(cartItem, cartViewModel)
                        }
                    }

                    // Place order button
                    Button(
                        onClick = {
                            cartViewModel.placeOrder()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Place Order")
                    }

                    // Show order status (success or failure message)
                    if (orderStatus.isNotEmpty()) {
                        Text(
                            text = orderStatus,
                            color = Color.Green,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CartItemView(
    cartItem: CartItem,
    cartViewModel: CartViewModel
) {
    // Handle the increase and decrease actions for quantity
    var quantity by remember { mutableStateOf(cartItem.quantity) }

    // Handle the image loading (make sure cartItem.imageUrl is correct)
    val imageUrl = cartItem.product.picUrl ?: "default_image_url"

    // Cart item view layout
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Product Image
        AsyncImage(
            model = imageUrl,
            contentDescription = cartItem.product.title,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Product Details
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cartItem.product.title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$${cartItem.product.price * cartItem.quantity}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Quantity Counter
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            IconButton(onClick = {
                if (quantity > 1) {
                    quantity--
                    cartViewModel.updateProductQuantity(cartItem.product.productId, quantity)
                }
            }) {
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Decrement")
            }

            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(onClick = {
                quantity++
                cartViewModel.updateProductQuantity(cartItem.product.productId, quantity)
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Increment")
            }
        }

        // Delete button
        IconButton(onClick = {
            cartViewModel.removeProductFromCart(cartItem.product.productId)
        }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Remove")
        }
    }
}
