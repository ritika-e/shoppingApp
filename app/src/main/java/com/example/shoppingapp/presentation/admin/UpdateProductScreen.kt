package com.example.shoppingapp.presentation.admin

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.ProductList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductScreen(
    navController: NavHostController,
    productId: Int,
    productViewModel: ProductViewModel = koinViewModel()
) {

    LaunchedEffect(productId) {
        Log.e("UPDATE PRODUCT SCREEN"," productId $productId")
        productViewModel.getProductById(productId)
    }
    // Observe the product details from the ViewModel
    val product by productViewModel.product.observeAsState(null)
    val isLoading by productViewModel.isLoading.observeAsState(false)
    val context = LocalContext.current

    if (product == null) {
        Text(context.getString(R.string.product_not_found), style = MaterialTheme.typography.headlineSmall)
        return
    }

    // Pre-fill the fields with product data
    var title by remember { mutableStateOf(product?.title ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var price by remember { mutableStateOf(product?.price?.toString() ?: "") }

    // Handle image picking from gallery
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.update_product_btn)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.Back_txt)
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                // Show existing image if available
                product?.picUrl?.let {
                    Image(painter = rememberImagePainter(it),
                        contentDescription = context.getString(R.string.product_image),
                        modifier = Modifier.size(100.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input fields for product update
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(context.getString(R.string.product_title)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(context.getString(R.string.product_desc_txt)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(context.getString(R.string.product_price_txt)) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Image upload button
                Button(onClick = { launcher.launch("image/*") }) {
                    Text(context.getString(R.string.choose_image_txt))
                }

                // Show selected image as a preview
                selectedImageUri.value?.let {
                    Image(painter = rememberImagePainter(it),
                        contentDescription = context.getString(R.string.selected_image_txt),
                        modifier = Modifier.size(100.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Update Product button
                Button(
                    onClick = {
                        // Create the updated product object
                        val updatedProduct = ProductList(
                            productId = productId,
                            title = title,
                            description = description,
                            price = price.toDouble(),
                            picUrl = selectedImageUri.value?.toString() // Upload the selected image URL
                        )

                        // Call the ViewModel to update the product
                        productViewModel.updateProduct(productId, updatedProduct)
                       // navController.navigateUp()
                        navController.navigate("productManagement") {
                            popUpTo("updateProductScreen") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(context.getString(R.string.update_product_btn))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show loading indicator while updating
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}
