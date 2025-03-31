package com.example.shoppingapp.presentation.user

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.ui.theme.LightGreen1
import com.example.shoppingapp.ui.theme.LightGreen2
import com.example.shoppingapp.ui.theme.LightGreen3
import com.example.shoppingapp.ui.theme.OrangeYellow2
import com.example.shoppingapp.ui.theme.OrangeYellow3
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    cartViewModel: CartViewModel = koinViewModel()
) {
    val context: Context = LocalContext.current
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val orderStatus by cartViewModel.orderStatus.observeAsState("")
    var showOrderDialog by remember { mutableStateOf(false) }
    val isLoading = cartViewModel.isLoading.observeAsState(false)

    var showDialog by remember { mutableStateOf(false) }
    var showInvoice by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.Cart_txt)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.Back_txt))
                    }
                }
            )
        },
        content = { paddingValues ->
            // Use LazyColumn for the entire content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Show empty cart message if no items in the cart
                item {
                    if (cartItems?.isEmpty() == true) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ){
                        Text(
                            text = context.getString(R.string.cart_empty_txt),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )}
                    } else {
                        // Display the cart items
                        cartItems?.forEach { cartItem ->
                            CartItemView(cartItem, cartViewModel)
                        }
                    }
                }

                // Show invoice button
                if (cartItems?.isNotEmpty() == true) {
                    item {
                        Button(
                            onClick = {
                                showInvoice = !showInvoice // Toggle invoice visibility
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(context.getString(R.string.Show_invoice_btn))
                        }
                    }
                }
                // Ensure LazyColumn is properly constrained in height
                if (showInvoice && cartItems?.isNotEmpty() == true) {
                    item {
                        // Make sure the invoice is properly constrained with height or other limits
                        Box(modifier = Modifier.heightIn(min = 200.dp, max = 600.dp).fillMaxWidth()) {
                            cartItems?.let { InvoiceView(cartItems = it) }
                        }
                    }
                }

                // Place order button
                if (cartItems?.isNotEmpty() == true) {
                    item {
                        Button(
                            onClick = {
                                showDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.LightGreen3
                            )
                        ) {
                            if (isLoading.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(text = context.getString(R.string.Place_order_btn))
                            }
                        }
                    }
                }
                // Show order status (success or failure message)
                item {
                    if (orderStatus.isNotEmpty()) {
                        showOrderDialog = true
                    }
                }

                // Continue Shopping Button
                if (cartItems?.isNotEmpty() == true) {
                    item {
                    Button(
                        onClick = {
                            navController.navigate("customer_dashboard") {
                                popUpTo("cart") { inclusive = true }
                                launchSingleTop = true }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.OrangeYellow2
                        )
                    ) {
                        Text(context.getString(R.string.shop_more_btn))
                    }
                }
                }
            }
        }
    )

    // Order confirmation dialog
    if (showOrderDialog) {
        AlertDialog(
            onDismissRequest = { /* Handle dismiss */ },
            title = { Text(context.getString(R.string.order_status)) },
            text = { Text(orderStatus) },
            confirmButton = {
                Button(
                    onClick = {
                        showOrderDialog = false
                        cartViewModel.clearCart()
                        // Navigate to the dashboard screen after confirmation
                        navController.navigate("customer_dashboard") {
                            popUpTo("cart") { inclusive = true }
                            launchSingleTop = true }
                    }
                ) {
                    Text(context.getString(R.string.Ok_txt))
                }
            }
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = context.getString(R.string.order_txt)) },
            text = { Text(text = context.getString(R.string.order_msg)) },
            confirmButton = {
                TextButton(onClick = {
                    // Place the order
                    cartViewModel.placeOrder()
                    showDialog = false
                }) {
                    Text(context.getString(R.string.confirm_order_btn))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(context.getString(R.string.add_more_btn))
                }
            }
        )
    }
}

@Composable
fun CartItemView(
    cartItem: CartItem,
    cartViewModel: CartViewModel
) {
    // Handle the increase and decrease actions for quantity
    var quantity by remember { mutableStateOf(cartItem.quantity) }

    // Handle the image loading
    val imageUrl = cartItem.product.picUrl ?: R.drawable.default_image
    Log.e("Cart Screen ", "Image url => $imageUrl")
    val context:Context = LocalContext.current

    // Cart item view layout
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp)),
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
                Image(
                    painter = painterResource(id = R.drawable.minus_icon),
                    contentDescription = context.getString(R.string.Decrement_btn),
                    modifier = Modifier.size(24.dp) // Adjust size if needed
                )
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
                Image(
                    painter = painterResource(id = R.drawable.add_circle),
                    contentDescription = context.getString(R.string.Increment_btn),
                    modifier = Modifier.size(24.dp) // Adjust size if needed
                )
            }
        }

        // Delete button
        IconButton(onClick = {
            cartViewModel.removeProductFromCart(cartItem.product.productId)
        }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription =
            context.getString(R.string.Remove_btn))
        }
    }
}
@Composable
fun InvoiceView(cartItems: List<CartItem>) {
    var context:Context = LocalContext.current
    // Calculate total amount and product totals
   // val totalAmount = cartItems.sumOf { it.productTotal }
    val totalAmount by rememberUpdatedState(
        cartItems.sumOf { it.product.price * it.quantity }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = context.getString(R.string.invoice),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(cartItems) { cartItem ->
                InvoiceItemView(cartItem)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total Amount
        Text(
            text = "Total Amount: $$totalAmount",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun InvoiceItemView(cartItem: CartItem) {
    val itemTotal = cartItem.product.price * cartItem.quantity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Product Name and Quantity
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = cartItem.product.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = "Quantity: ${cartItem.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Price per unit and Total price (price * quantity)
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$${cartItem.product.price}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "$${itemTotal} ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}