package com.example.shoppingapp.domain.model

/* for item in cart */
data class CartItemModel(
    val productId: Int,
    val productName: String,
    val productPrice: Double,
    var quantity: Int,
    val productImageUrl: String
)

