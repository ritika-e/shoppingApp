package com.example.shoppingapp.presentation.user

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.MoreVert
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.ItemsModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navHostController: NavHostController = rememberNavController(),
    productId: Int,
    viewModel: ProductDetailsViewModel = koinViewModel(),
    cartViewModel: CartViewModel = koinViewModel()

) {

    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())

    // Observe the product details LiveData
     viewModel.loadProductDetails(productId)
    val productDetails by viewModel.productDetails.observeAsState(null)

    // To keep track of cart state
     var isInCart by remember { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(0) }


    // Show loading state while productDetails is null
    if (productDetails == null) {
        // Loading or empty state UI
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        /*val product = productDetails!!

        // Check if the product is already in the cart
        var isInCart = cartViewModel.isItemInCart(product.productId)
        val cartItem = cartViewModel.cart.value?.items?.find { it.productId == product.productId }

*/
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Product Details")
                    },
                    navigationIcon = {
                        IconButton(onClick = { navHostController.navigateUp() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Product Image
                    AsyncImage(
                        model = productDetails!!.picUrl.firstOrNull(),
                        contentDescription = productDetails!!.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Name
                    Text(
                        text = productDetails!!.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )

                    // Product Description
                    Text(
                        text = productDetails!!.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rating and Price Row
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Rating
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Product Rating",
                                tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = productDetails!!.rating.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        // Price
                        Text(
                            text = "$${productDetails!!.price}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            /*val product = ItemsModel(productId = 1, title = "Sample Product", price = 100.0)
                            cartViewModel.addProductToCart(product)*/
                            val product = ItemsModel(
                                productId = productDetails!!.productId,
                                title = productDetails!!.title,
                                price = productDetails!!.price
                            )
                            // Add the product to the cart
                            cartViewModel.addProductToCart(product)
                            // Navigate to the Cart screen
                            navHostController.navigate("cart")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("Add to Cart")
                    }

                   /* LazyColumn {
                        items(cartItems) { cartItem ->
                            CartItemView(cartItem, cartViewModel)
                        }
                    }*/

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        )
    }

}
   /* Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Product Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(productImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Product Name
        Text(
            text = productName,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Product Description
        Text(
            text = productDescription,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Product Rating (Stars)
        Row(verticalAlignment = Alignment.CenterVertically) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= productRating.toInt()) Icons.Default.Star else Icons.Default.Star,
                    contentDescription = "Rating Star",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$productRating",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Product Price
        Text(
            text = "$${"%.2f".format(productPrice)}",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add to Cart Button
        Button(
            onClick = { *//* Handle add to cart *//* },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Add to Cart",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductDetailScreenPreview() {
    ProductDetailsScreen(
        productName = "Awesome Product",
        productDescription = "This is a very cool product with amazing features.",
        productPrice = 99.99F,
        productRating = 4.5f,
        productImageUrl = "https://via.placeholder.com/300"
    )*/

/*
@Composable
fun ProductDetailsScreen(navHostController: NavHostController = rememberNavController(),
                         viewModel: ProductDetailsViewModel = koinViewModel(),
                         onBackClick:()->Unit,
                         onAddToCartClick:() -> Unit,
                         onCartClick:()->Unit
) {
   */
/* var selectedImageUrl by remember {
        mutableStateOf(item.picUrl.first())
    }*//*


    var selectedModelIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
            ConstraintLayout (
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ){
                val (back,fav) = createRefs()
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onBackClick()
                        }
                        .constrainAs(back) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
                Icon(imageVector = Icons.Sharp.Favorite, contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onBackClick()
                        }
                        .constrainAs(fav) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                )
            }
    }
}*/
