package com.example.shoppingapp.domain.model

/* List of cart item */
data class CartModel(
val items: MutableList<CartItemModel> = mutableListOf()
)

