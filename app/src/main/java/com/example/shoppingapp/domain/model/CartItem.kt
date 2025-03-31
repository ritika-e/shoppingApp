package com.example.shoppingapp.domain.model

data class CartItem(
    val product: ItemsModel = ItemsModel(),
    var quantity: Int = 0,
    var productTotal: Double = product.price * quantity)
