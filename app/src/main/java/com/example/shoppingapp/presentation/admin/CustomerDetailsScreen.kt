package com.example.shoppingapp.presentation.admin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreen(
    navHostController: NavHostController,
    customerId: String,
    customerViewModel: CustomerViewModel = koinViewModel()
) {
    val context = LocalContext.current
    // Observe customer data and orders from ViewModel
    val customer by customerViewModel.customer.observeAsState(null)
    val orders by customerViewModel.orders.observeAsState(emptyList())

    // Log to check the flow
    LaunchedEffect(customerId) {
        Log.d("CustomerDetailsScreen", "Fetching customer and orders for $customerId")
        customerViewModel.getCustomerById(customerId)
        customerViewModel.getOrdersByCustomer(customerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.customer_details)) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.Back_txt))
                    }
                }
            )
        },
        content = { paddingValues ->
            // Column for the content
            Column(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ) {
                // Show loading indicator while customer details are being fetched
                if (customer == null) {
                    // Show circular progress indicator while fetching
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    // Customer info section
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Profile image placeholder
                            /*Image(
                                painter = painterResource(id = R.drawable.ic_user_placeholder),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            )*/

                            Spacer(modifier = Modifier.height(16.dp))

                            // Display Customer Name
                            Text(
                                text = customer?.name ?: "N/A",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Display Customer Email
                            Text(
                                text = customer?.email ?: "N/A",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    // Show order history
                    if (orders.isNotEmpty()) {
                        LazyColumn {
                            items(orders) { order ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Order ID: ${order.orderId}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Total: $${order.totalAmount}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            text = "Order Status: $${order.status}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = context.getString(R.string.customer_no_order),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    )
}
