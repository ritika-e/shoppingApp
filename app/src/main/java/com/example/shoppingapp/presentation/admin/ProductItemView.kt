package com.example.shoppingapp.presentation.admin

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.ProductList

@Composable
fun ProductItemView(product: ProductList, navController: NavHostController, productViewModel: ProductViewModel) {
    val context:Context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Product Name: ${product.title}")
        Text("Price: $${product.price}")

        Button(onClick = { navController.navigate("productDetails/${product.productId}") }) {
            Text(context.getString(R.string.view_details))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                productViewModel.deleteProduct(product.productId)
                      },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(context.getString(R.string.delete_btn), color = Color.White)
        }
    }
}
