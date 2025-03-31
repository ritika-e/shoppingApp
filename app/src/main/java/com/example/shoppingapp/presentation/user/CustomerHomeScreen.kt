package com.example.shoppingapp.presentation.user

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shoppingapp.R
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.presentation.auth.LoginViewModel
import com.example.shoppingapp.presentation.common.LogoutDialog
import com.example.shoppingapp.utils.SharedPreferencesManager
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun CustomerDashboardScreen(
    navHostController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel = koinViewModel(),
    productDetailsViewModel: ProductDetailsViewModel = koinViewModel()
) {
    val context:Context = LocalContext.current
    val categories = remember { mutableStateListOf<CategoryModel>() }
    val recommended = remember { mutableStateListOf<ItemsModel>() }
    var showCategoryLoading by remember { mutableStateOf(true) }
    var showRecommendedLoading by remember { mutableStateOf(true) }

    val banners by productDetailsViewModel.banners.observeAsState(emptyList()) // Observe banners LiveData

    var showBannerLoading by remember { mutableStateOf(true) }
    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
    val userName = sharedPreferencesManager.getUserData().userName
    val logoutStatus = viewModel.logoutStatus.observeAsState().value

    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(logoutStatus) {
        if (logoutStatus == context.getString(R.string.Logged_out_txt)) {
            Toast.makeText(navHostController.context,context.getString(R.string.Log_out_msg_txt),
                Toast.LENGTH_SHORT).show()
            navHostController.navigate("login") {
                popUpTo("customer_dashboard") { inclusive = true }
            }
        } else if (logoutStatus != null) {
            Toast.makeText(navHostController.context, logoutStatus, Toast.LENGTH_SHORT).show()
        }
    }

    Log.d("CustomerDashboardScreen", "Observed banners: $banners")

    // Banner
    LaunchedEffect(Unit) {
        productDetailsViewModel.loadBanners() // Load banners when the screen is launched
    }
    // When banners are fetched, stop loading
    if (banners.isNotEmpty()) {
        showBannerLoading = false
    }


    // Category
    LaunchedEffect(Unit) {
        productDetailsViewModel.loadCategories()
        productDetailsViewModel.categories.observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }
    }

    // Recommended
    LaunchedEffect(Unit) {
        productDetailsViewModel.loadRecommended()
        productDetailsViewModel.recommended.observeForever {
            recommended.clear()
            recommended.addAll(it)
            showRecommendedLoading=false
        }
    }


    ConstraintLayout(
        modifier =  Modifier.background(Color.White)) {
         val (scrollList, bottomMenu) = createRefs()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(scrollList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = context.getString(R.string.Welcome_txt), color = Color.Black)

                        Text(
                            text = "$userName!", color = Color.Black,
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(imageVector = Icons.Sharp.ExitToApp,
                        contentDescription = context.getString(R.string.Profile_txt),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                showDialog = true

                            }
                    )
                }
            }
            //Banners
            item {
                if (showBannerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Log.d("CustomerDashboardScreen", "Displaying banners: $banners")
                    Banners(banners)
                }
            }
            item {
                SectionTitle(context.getString(R.string.Categories_txt))
            }
            item {
                if (showCategoryLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    CategoryList(navHostController= navHostController,categories)
                }
            }
            item {
                SectionTitle(context.getString(R.string.Recommendation_txt))
            }
            item {
                if (showRecommendedLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    ListItems(items = recommended, navHostController = navHostController)
                }

                // Update showRecommendedLoading based on data availability
                LaunchedEffect(recommended) {
                    showRecommendedLoading = recommended.isEmpty()
                }
            }


            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        BottomMenu(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(bottomMenu) {
                bottom.linkTo(parent.bottom)
            },
            onItemClick = { route ->
                navHostController.navigate(route)
            }
        )
    }

    LogoutDialog(
        showDialog = showDialog,
        onConfirm = {
            viewModel.logout()  // Handle the logout logic

        },
        onDismiss = {
            showDialog = false // Close the dialog when 'No' is clicked
        }
    )
}

@Composable
fun CategoryList(
    navHostController: NavHostController = rememberNavController(),
    categories: SnapshotStateList<CategoryModel>

){
    var selectedIndex by remember { mutableStateOf(-1) }

    LazyRow(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp,end = 16.dp, top = 8.dp)
    ) {
            items(categories.size){
                index ->
                CategoryItem(item = categories[index],
                    isSelected = selectedIndex == index,
                    onItemClick = {
                        selectedIndex=index
                       navHostController.navigate(
                         "category_items/${categories[index].id}/${categories[index].title}")
                    })
            }
    }
}

@Composable
fun CategoryItem(item:CategoryModel,isSelected:Boolean,onItemClick:()->Unit){
    Row (modifier = Modifier
        .clickable(onClick = onItemClick)
        .background(
            color = if (isSelected) colorResource(id = R.color.purple_500) else Color.Transparent,
            shape = RoundedCornerShape(8.dp)
        ),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(model = (item.picUrl), contentDescription = item.title,
            modifier = Modifier
                .size(45.dp)
                .background(
                    color = if (isSelected) Color.Transparent else Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                ),
                contentScale = ContentScale.Inside,
            colorFilter = if (isSelected){
                ColorFilter.tint(Color.White)
            }else{
                ColorFilter.tint(Color.Black)
            })
        if (isSelected){
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp,)
            )
        }
    }
}

@Composable
fun Banners(banners:List<SliderModel>){
    AutoSlidingCarousel(banners=banners)
}

@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    banners: List<SliderModel>,
    pagerState: PagerState = rememberPagerState(pageCount = {banners.size}),
){
    val context:Context = LocalContext.current
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    Column(modifier = modifier.fillMaxSize()) {

        HorizontalPager(state = pagerState) {
                page ->

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banners[page].url)
                    .build(),
                contentDescription = context.getString(R.string.Banner_txt),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    .height(150.dp))
        }
        DotIndicator(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            totalDots = banners.size,
            selectedIndex = if (isDragged) pagerState.currentPage else pagerState.currentPage,
            dotSize = 8.dp
        )

    }
    // Auto sliding effect
    /*LaunchedEffect(pagerState.currentPage) {
        if (banners.isNotEmpty()) {
            kotlinx.coroutines.delay(3000)  // Delay in milliseconds before switching pages
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % banners.size)
        }
    }*/
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDots:Int,
    selectedIndex:Int,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unSelectedColor: Color = Color.Gray,
    dotSize:Dp
){
    LazyRow(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
    ) {
        items(totalDots){
                index ->
            IndicatorDot(
                color = if (index==selectedIndex)selectedColor else unSelectedColor,
                size = dotSize
            )
            if (index!=totalDots-1){
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(modifier: Modifier = Modifier,
                 size: Dp,
                 color: Color){
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    ) {
    }
}
@Composable
fun SectionTitle(title:String){
    Row (modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = title, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
       // Text(text = actionText, color = MaterialTheme.colorScheme.primary)

    }
}

@Composable
fun BottomMenu(modifier: Modifier,onItemClick: (String) -> Unit){
    val context:Context = LocalContext.current
    Row (modifier = modifier
        .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        BottomMenuItem(icon = Icons.Default.Home,
            text = context.getString(R.string.Home_txt), onItemClick = { onItemClick("customer_dashboard") })
        BottomMenuItem(icon = Icons.Default.ShoppingCart,
            text = context.getString(R.string.Cart_txt), onItemClick = { onItemClick("cart") })
        BottomMenuItem(icon = Icons.Default.List,
            text = context.getString(R.string.Orders_txt), onItemClick = { onItemClick("order_history") })
        BottomMenuItem(icon = Icons.Default.Phone,
            text = context.getString(R.string.support_txt), onItemClick = { onItemClick("supportScreen") })
    }
}

@Composable
fun BottomMenuItem(icon: ImageVector, text:String, onItemClick:(String)->Unit){
    Column(modifier = Modifier
        .height(60.dp)
        .clickable { onItemClick.invoke(text) }
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = text, tint = Color.White)
        Text(text = text, color = Color.White, fontSize = 10.sp)
    }
}