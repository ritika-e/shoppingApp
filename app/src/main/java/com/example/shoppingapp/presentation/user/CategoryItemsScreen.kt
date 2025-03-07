package com.example.shoppingapp.presentation.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryItemsScreen(navHostController: NavHostController = rememberNavController(),
                        viewModel: ProductDetailsViewModel = koinViewModel(),
                        categoryID:String,
                        title:String,
                        onBackClick:()->Unit
) {

    val items by viewModel.recommended.observeAsState(initial = emptyList())
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(categoryID) {
        viewModel.loadCategoryFiltered(categoryID)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout( modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)) {
            val(backBtn,cartTxt) = createRefs()

            Text(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(cartTxt) { centerTo(parent) },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                text = title
                )

            Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onBackClick()
                    }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )

    }
        if (isLoading){
            Box (modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }  else{
            ListItemsFullSize(items = items,navHostController=navHostController)
        }
    }

        LaunchedEffect(items) {
            isLoading=items.isEmpty()
        }

}

