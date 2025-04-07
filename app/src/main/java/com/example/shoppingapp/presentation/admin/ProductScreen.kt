package com.example.shoppingapp.presentation.admin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.shoppingapp.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    navController: NavHostController,
    productId: Int,
    productViewModel: ProductViewModel = koinViewModel()
) {
    val context = LocalContext.current

    // Observe the product data
    val product by productViewModel.product.observeAsState(null)
    val isLoading by productViewModel.isLoading.observeAsState(false)

    // Fetch the product data when the screen is displayed
    LaunchedEffect(productId) {
        Log.e("ProductScreen", "Fetching product data for productId $productId")
        productViewModel.getProductById(productId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (product == null) {
        // Show a loading or "Not Found" message if the product is not found
        Text(context.getString(R.string.product_not_found), style = MaterialTheme.typography.headlineSmall)
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.Product_details_txt)) },
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
                // Product Image
                product!!.picUrl?.let {
                  //  Image(painter = rememberImagePainter(it), contentDescription = null)
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Set a height limit
                            .clip(RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                }

                // Product Details
                Text(context.getString(R.string.product_name_txt)+": ${product!!.title}",
                    style = MaterialTheme.typography.headlineSmall)
                Text(context.getString(R.string.product_price_txt)+": ${product!!.price}",
                    style = MaterialTheme.typography.labelLarge)
                Text(context.getString(R.string.product_desc_txt)+": ${product!!.description}",
                    style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                // Row to place buttons horizontally or you can use Column for vertical stacking
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Update Button
                    Button(
                        onClick = {
                            // Handle the update action (you can navigate to update screen or show a dialog)
                            navController.navigate("updateProduct/${product!!.productId}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(context.getString(R.string.update_product_btn))
                    }

                    // Delete Button
                    Button(
                        onClick = {
                            // Handle delete action
                            productViewModel.deleteProduct(product!!.productId)  // Assuming deleteProduct accepts productId as String
                            navController.navigateUp()  // Navigate back after deletion
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(context.getString(R.string.delete_btn), color = Color.White)
                    }
                }
            }
        }
    )
}
