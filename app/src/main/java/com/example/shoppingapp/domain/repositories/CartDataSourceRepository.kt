package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.ItemsModel

interface CartDataSourceRepository {
    fun addProduct(product: ItemsModel)
    fun getCartItems(): List<CartItem>
    fun updateProductQuantity(productId: Int, quantity: Int)
    fun removeProduct(productId: Int)
}