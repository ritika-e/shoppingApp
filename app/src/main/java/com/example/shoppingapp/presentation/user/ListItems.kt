package com.example.shoppingapp.presentation.user

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.ItemsModel

@Composable
fun ListItems (
    items:List<ItemsModel>,navHostController: NavHostController = rememberNavController()
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(500.dp)
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.size){ row ->
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                RecommendedItem(items,row, navHostController = navHostController)
            }
        }

    }
}

@Composable
fun ListItemsFullSize (items:List<ItemsModel>,
                       navHostController: NavHostController = rememberNavController()
){
    Log.d("ListItemsFullSize","ListItemsFullSize Executed")
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Log.d("ListItemsFullSize","ListItemsFullSize ${items.size}")
        items(items.size){ row ->
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                RecommendedItem(items,row,navHostController=navHostController)
            }
        }

    }
}

@Composable
fun RecommendedItem(items: List<ItemsModel>, pos:Int,
                    navHostController: NavHostController = rememberNavController()
){
    val context:Context = LocalContext.current
    Column(modifier = Modifier
        .padding(8.dp)
        .height(225.dp)
    ) {
        AsyncImage(model = items[pos].picUrl, contentDescription =items[pos].title,
            modifier = Modifier
                .width(175.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                .height(175.dp)
                .padding(8.dp)
                .clickable {
                    navHostController.navigate("product_details/${items[pos].productId}" +
                            "/${items[pos].title}")
                   /* navHostController.navigate("product_detail/${items[pos].categoryId}" +
                            "/${items[pos].title}/${items[pos].description}/${items[pos].price}" +
                            "/${items[pos].picUrl}/${items[pos].rating}")*/
                },
            contentScale = ContentScale.Inside
        )
        Text(text = items[pos].title,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp))
        Row (
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Row {
                Icon(
                    imageVector = Icons.Filled.Star, // Predefined icon
                    contentDescription = context.getString(R.string.Rating_txt),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = items[pos].rating.toString(),
                    color = Color.Black, fontSize = 15.sp)

                 }

            Text(text = "$${items[pos].price}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)

        }
    }
}

