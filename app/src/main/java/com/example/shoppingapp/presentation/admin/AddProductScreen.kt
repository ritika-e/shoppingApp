package com.example.shoppingapp.presentation.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.model.categories
import org.koin.androidx.compose.koinViewModel
 import com.example.shoppingapp.presentation.admin.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen( navController: NavHostController,
    viewModel: ProductViewModel = koinViewModel()) {
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }

    // State for category dropdown
    var selectedCategoryId  by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // Button to open image picker
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    // Handling when the user selects an image
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.add_product_txt)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack,
                            contentDescription = context.getString(R.string.Back_txt))
                    }
                }
            )
        }
    ) { paddingValues ->
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        // Product Category Dropdown

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedCategory ?: context.getString(R.string.select_category_txt),
                onValueChange = {},
                label = { Text(context.getString(R.string.product_category_txt)) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category.name) },
                        onClick = {
                            selectedCategory = category.name  // Store the category name
                            selectedCategoryId = category.id  // Store the category ID
                            expanded = false  // Close the dropdown after selection
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Name
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text(context.getString(R.string.product_name_txt)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product Description
        TextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text(context.getString(R.string.product_desc_txt)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product Price
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text(context.getString(R.string.product_price_txt)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Image Picker
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(context.getString(R.string.product_image_txt))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // If an image is selected, show a preview
        if (selectedImageUri != null) {
            Image(painter = rememberImagePainter(selectedImageUri),
                contentDescription = context.getString(R.string.product_image_txt),
                modifier = Modifier.size(100.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Button
        Button(
            onClick = {
                if (productName.isNotEmpty() && productDescription.isNotEmpty() &&
                    productPrice.isNotEmpty() && selectedImageUri != null) {
                    isUploading = true
                    val product = ProductList(
                        productId = (0..Int.MAX_VALUE).random(),
                        categoryId = selectedCategoryId!!,
                        title = productName,
                        description = productDescription,
                        price = productPrice.toDouble(),
                        picUrl = selectedImageUri.toString() // Store the URI as a string temporarily
                    )

                    // Call the ViewModel to upload the product and image
                    viewModel.addProductWithImage(product, selectedImageUri!!, onProgress = {
                        uploadProgress = it
                    }, onComplete = { success ->
                        isUploading = false
                        if (success) {
                            Toast.makeText(context, context.getString(R.string.product_succ_txt),
                                Toast.LENGTH_SHORT).show()
                            // Reset the fields to their initial state
                            productName = ""
                            productDescription = ""
                            productPrice = ""
                            selectedImageUri = null
                            selectedCategory = null
                            selectedCategoryId = null
                        } else {
                            Toast.makeText(context,context.getString(R.string.product_fail_txt),
                                Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, context.getString(R.string.product_error_txt),
                        Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isUploading
        ) {
            Text(if (isUploading) "Uploading... $uploadProgress%" else "Add Product")
           /* val uploadProgressText = if (isUploading) {
                stringResource(R.string.upload_txt, uploadProgress)
            } else {
                stringResource(R.string.add_product_txt)
            }

            Text(text = uploadProgressText)*/
        }
    }
}
    }