package com.example.shoppingapp.domain.model

import android.net.Uri

data class ProductList(
    val productId: Int,
    val title: String,
    val description: String = "",
    val price: Double,
    val showRecommended:Boolean = true,
    var categoryId:String="",
    val picUrl: String? = null  // This will store the URL of the uploaded image.
)