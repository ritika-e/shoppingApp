package com.example.shoppingapp.presentation.admin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.ProductList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavHostController, productViewModel: ProductViewModel = koinViewModel()) {
    val products by productViewModel.productsList.observeAsState(emptyList())
    val isLoading by productViewModel.isLoading.observeAsState(false)

    val context:Context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        productViewModel.getProducts()  // Re-fetch products every time screen is loaded
    }
    LaunchedEffect(key1 = products) {
        if (products.isEmpty() && !isLoading) {
            productViewModel.getProducts() // Fetch products again if the list is empty or no products found
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.product_list)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = context.getString(R.string.Back_txt))
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(products) { product ->
                            ProductItemView(product, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProductItemView(product: ProductList, navController: NavHostController) {
    val context:Context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(context.getString(R.string.product_id)+": ${product.productId}",
                style = MaterialTheme.typography.labelLarge)
            Text(context.getString(R.string.product_name_txt)+": ${product.title}",
                style = MaterialTheme.typography.labelLarge)
            Text(context.getString(R.string.product_price_txt)+" $${product.price}",
                style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { navController.navigate("productScreen/${product.productId}") }) {
                Text(context.getString(R.string.view_details))
            }
        }
    }
}
