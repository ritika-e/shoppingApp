package com.example.shoppingapp.presentation.admin


import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.ExitToApp
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.data.ui.BottomMenuContent
import com.example.shoppingapp.data.ui.Feature
import com.example.shoppingapp.presentation.auth.LoginViewModel
import com.example.shoppingapp.ui.theme.Beige1
import com.example.shoppingapp.ui.theme.Beige2
import com.example.shoppingapp.ui.theme.Beige3
import com.example.shoppingapp.ui.theme.BlueViolet1
import com.example.shoppingapp.ui.theme.BlueViolet2
import com.example.shoppingapp.ui.theme.BlueViolet3
import com.example.shoppingapp.ui.theme.LightGreen1
import com.example.shoppingapp.ui.theme.LightGreen2
import com.example.shoppingapp.ui.theme.LightGreen3
import com.example.shoppingapp.ui.theme.OrangeYellow1
import com.example.shoppingapp.ui.theme.OrangeYellow2
import com.example.shoppingapp.ui.theme.OrangeYellow3
import com.example.shoppingapp.utils.SharedPreferencesManager
import com.example.shoppingapp.utils.standardQuadFromTo
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun AdminDashboardScreen(navHostController: NavHostController = rememberNavController()) {


    /*Text(text = "Welcome to the Admin Dashboard!")

    Button(onClick = {
        // Log out the user
        viewModel.logoutUser()

        // Navigate back to the login screen
        navHostController.navigate("login")
    }) {
        Text(text = "Logout")
    }

    val userName by viewModel.userName.observeAsState("")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome, $userName!") // Display the user's name
    }*/
    
    Box(modifier = Modifier
        .fillMaxSize()){
        Column {
            GreetingSection()
            ChipSection(chips = listOf("Total Order","New Orders","Pending Orders"))
            FeaturesSection(features = listOf(
                Feature(
                    title = "Customer Management",
                    icon = Icons.Filled.AccountCircle,
                    MaterialTheme.colorScheme.BlueViolet1,
                    MaterialTheme.colorScheme.BlueViolet2,
                    MaterialTheme.colorScheme.BlueViolet3
                ),Feature(
                    title = "Add Products",
                    icon = Icons.Filled.AddCircle,
                    MaterialTheme.colorScheme.LightGreen1,
                    MaterialTheme.colorScheme.LightGreen2,
                    MaterialTheme.colorScheme.LightGreen3
                ),Feature(
                    title = "Order Management",
                    icon = Icons.Filled.ShoppingCart,
                    MaterialTheme.colorScheme.OrangeYellow1,
                    MaterialTheme.colorScheme.OrangeYellow2,
                    MaterialTheme.colorScheme.OrangeYellow3
                ),Feature(
                    title = "Product Management",
                    icon = Icons.Filled.Home,
                    MaterialTheme.colorScheme.Beige1,
                    MaterialTheme.colorScheme.Beige2,
                    MaterialTheme.colorScheme.Beige3
                )
            ),
                navHostController = navHostController
                )
        }
            BottomMenu(items = listOf(
                BottomMenuContent("Home",Icons.Default.Home),
                BottomMenuContent("settings",Icons.Default.Settings),
                BottomMenuContent("Profile",Icons.Default.AccountCircle),
                BottomMenuContent("Help",Icons.Default.Info),
             //   BottomMenuContent("Chat",R.drawable.ic_launcher_foreground),

            ), modifier = Modifier.align(Alignment.BottomCenter))
    }
 }

@Composable
fun BottomMenu(
    items: List<BottomMenuContent>,
    modifier: Modifier = Modifier,
    activeHighlightColor : Color = Color.Blue,
    activeTextColor: Color = Color.White,
    inactiveTextColor :Color = Color.Gray,
    initialSelectedItemIndex: Int = 0
){
        var selectedItemIndex by remember {
            mutableStateOf(initialSelectedItemIndex)
        }
    Row (
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(15.dp)
    ){
        items.forEachIndexed{ index, item ->
            BottomMenuItem(item = item,
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                activeTextColor = activeTextColor,
                inactiveTextColor = inactiveTextColor
            ) {
                selectedItemIndex = index
            }
        }
    }
}

@Composable
fun BottomMenuItem(
    item: BottomMenuContent,
    isSelected:Boolean = false,
    activeHighlightColor : Color = Color.Blue,
    activeTextColor: Color = Color.White,
    inactiveTextColor :Color = Color.Cyan,
    onItemClick:() -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable {
            onItemClick()
        }
    ) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) activeHighlightColor else Color.Transparent)
                    .padding(10.dp)
            ){
                Icon(imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (isSelected) activeTextColor else inactiveTextColor,
                    modifier = Modifier.size(20.dp)
                    )
            }
        Text(text = item.title,
            color = if (isSelected) activeTextColor else inactiveTextColor)
    }
}

@Composable
fun GreetingSection(
    viewModel: LoginViewModel = koinViewModel(),
    navHostController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
){
    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
    val userName = sharedPreferencesManager.getUserData().userName
    val logoutStatus = viewModel.logoutStatus.observeAsState().value
    LaunchedEffect(logoutStatus) {
        if (logoutStatus == "Logged out") {
            Toast.makeText(navHostController.context, "Logged out successfully", Toast.LENGTH_SHORT).show()
            navHostController.navigate("login") {
                popUpTo("admin_dashboard") { inclusive = true }
            }
        } else if (logoutStatus != null) {
            Toast.makeText(navHostController.context, logoutStatus, Toast.LENGTH_SHORT).show()
        }
    }
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ){
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Welcome Back, $userName",
                style = MaterialTheme.typography.headlineMedium)
            Text(text = "We wish you have a good day!",
                style = MaterialTheme.typography.bodyLarge)
        }
        Icon(imageVector = Icons.Sharp.ExitToApp, contentDescription = "Profile",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    viewModel.logout()
                    /*navHostController.navigate("login") {
                        popUpTo("admin_dashboard") { inclusive = true }
                    }*/
                }
        )
    }
}

@Preview
@Composable
fun DashboardScreenPrev(){
    AdminDashboardScreen()
}

@Composable
fun ChipSection(
    chips: List<String>
){
    var selectedChipIndex by remember {
        mutableStateOf(0)
    }
    LazyRow {
        items (chips.size){
            Box(
                // contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                    .clickable {
                        selectedChipIndex = it
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedChipIndex == it) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.secondary
                    )
                    .padding(15.dp)
            ){
                Text(text = chips[it], color = Color.White)
            }
        }
    }
}

@Composable
fun FeaturesSection(features: List<Feature>,navHostController: NavHostController){
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Features",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(15.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 7.5.dp, end = 7.5.dp, bottom = 100.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(features.size){
                FeatureItem(feature = features[it],
                    navHostController = navHostController)
                }
        }
    }

}

@Composable
fun FeatureItem(
    feature: Feature,
    navHostController: NavHostController
){
    BoxWithConstraints(
        modifier = Modifier
            .padding(7.5.dp,)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(feature.darkColor)
    ) {
        val width = constraints.maxWidth
        val height =  constraints.maxHeight

        //Medium Color
        val mediumColoredPoint1 = Offset(0f,height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f,height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f,height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f,height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f,-height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x,mediumColoredPoint1.y)
             standardQuadFromTo(mediumColoredPoint1,mediumColoredPoint2)
             standardQuadFromTo(mediumColoredPoint2,mediumColoredPoint3)
             standardQuadFromTo(mediumColoredPoint3,mediumColoredPoint4)
             standardQuadFromTo(mediumColoredPoint4,mediumColoredPoint5)
             lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
             lineTo(-100f, height.toFloat() + 100f)
            close()
         }

        //Light Colored
        val lightPoint1 = Offset(0f,height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f,height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f,height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f,height.toFloat())
        val lightPoint5 = Offset(width * 1.4f,-height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x,lightPoint1.y)
            standardQuadFromTo(lightPoint1,lightPoint2)
            standardQuadFromTo(lightPoint2,lightPoint3)
            standardQuadFromTo(lightPoint3,lightPoint4)
            standardQuadFromTo(lightPoint4,lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(modifier = Modifier
            .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = feature.mediumColor
            )
            drawPath(
                path = lightColoredPath,
                color = feature.lightColor
            )
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
        ){
            Text(text = feature.title,
                style = MaterialTheme.typography.headlineSmall,
                lineHeight = 26.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.TopStart))
        }
        Icon(imageVector = feature.icon,
            contentDescription = feature.title,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 10.dp))

        Text(text = "Start",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable {
                    when (feature.title) {
                        "Customer Management" -> navHostController.navigate("customerManagement")
                        "Add Products" -> navHostController.navigate("addProducts")
                        "Order Management" -> navHostController.navigate("orderManagement")
                        "Product Management" -> navHostController.navigate("productManagement")
                    }
                }
                .padding(end = 10.dp, bottom = 10.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 6.dp, horizontal = 15.dp)
        )
    }

}
