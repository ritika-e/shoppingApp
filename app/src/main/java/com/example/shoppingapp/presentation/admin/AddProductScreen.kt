package com.example.shoppingapp.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
 import com.example.shoppingapp.presentation.admin.ProductViewModel

@Composable
fun AddProductScreen(
    navHostController: NavHostController = rememberNavController(),
    productViewModel: ProductViewModel = koinViewModel()
) {
    // States for product input fields
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }
    var productImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Pick Image from Gallery
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        productImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(text = "Add Product", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(20.dp))

        // Product Name Input Field
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Product Description Input Field
        OutlinedTextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Product Price Input Field
        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Product Price") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Product Category Input Field
        OutlinedTextField(
            value = productCategory,
            onValueChange = { productCategory = it },
            label = { Text("Product Category") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Button to pick image
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Pick Image")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Add Product Button
        Button(
            onClick = {
                if (productName.isNotEmpty() && productDescription.isNotEmpty() && productPrice.isNotEmpty() && productCategory.isNotEmpty() && productImageUri != null) {
                    isLoading = true
                    // Prepare the product data
                   /* val product = Product(
                        id = System.currentTimeMillis().toString(), // Unique product ID
                        name = productName,
                        description = productDescription,
                        price = productPrice.toDouble(),
                        category = productCategory
                    )*/
                   /* val productImage = ProductImage(
                        productId = product.id,
                        imagePath = productImageUri.toString()
                    )*/

                    // Call the ViewModel to add the product and image
                 //   productViewModel.addProduct(product, productImage)

                    // Handle the result after the operation completes
                    isLoading = false
                    Toast.makeText(context, "Product Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = "Add Product")
            }
        }
    }
}
