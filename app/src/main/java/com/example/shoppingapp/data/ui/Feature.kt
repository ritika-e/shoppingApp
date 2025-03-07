package com.example.shoppingapp.data.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Feature(
    val title:String,
    //@DrawableRes val iconId:Int,
    val  icon: ImageVector,
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color
)