package com.example.shoppingapp.presentation.admin

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductManagementScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: ProductViewModel = koinViewModel()
) {
  //  val productsWithImages by viewModel.productsWithImages.observeAsState(emptyList())

   /* if (productsWithImages.isEmpty()) {
        Text(text = "No products available.")
    }
*/
   /* LaunchedEffect(true) {
        viewModel.fetchProducts()
    }*/

   /* LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(productsWithImages) { item ->
            ProductItem(productWithImage = item)
        }
    }*/
}

/*@Composable
fun ProductItem(productWithImage: ProductWithImage) {
    val product = productWithImage.product
    val image = productWithImage.productImage

    Log.d("ProductItem", "Image Path: ${image?.imagePath}")

    // Display the product with image (if exists)
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
        Text(text = "Price: ${product.price}", style = MaterialTheme.typography.bodySmall)

        image?.let {
            Log.e("ImagePath", "Image Path: ${it.imagePath}")
            GlideImage(imageUri = Uri.parse(it.imagePath)) // Pass URI to GlideImage
        } ?: run {
            Text(text = "No image available")
        }
    }
}*/

@Composable
fun GlideImage(imageUri: Uri?, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    if (imageUri != null) {
        Log.d("GlideImage", "Image URI: $imageUri")
        AndroidView(
            modifier = modifier,
            factory = { context ->
                ImageView(context).apply {
                    // Handle loading image from content:// or file:// URI
                    Glide.with(context)
                        .load(imageUri)  // Glide can handle both content:// and file:// URIs
                        .apply(RequestOptions().centerCrop())
                        .into(this)  // Load the image into the ImageView
                }
            }
        )
    } else {
        Text(text = "No image available")
    }
}

@Preview
@Composable
fun ProductManagementScreenPreview() {
    ProductManagementScreen()
}
