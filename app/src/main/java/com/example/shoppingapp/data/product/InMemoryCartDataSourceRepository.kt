package com.example.shoppingapp.data.product

import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.repositories.CartDataSourceRepository

class InMemoryCartDataSourceRepository: CartDataSourceRepository {
    private val cartItems = mutableListOf<CartItem>()

    override fun addProduct(product: ItemsModel) {
        val existingItem = cartItems.find { it.product.productId == product.productId }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    override fun getCartItems(): List<CartItem> = cartItems

    override fun updateProductQuantity(productId: Int, quantity: Int) {
        //cartItems.find { it.product.productId == productId }?.quantity = quantity
        val cartItem = cartItems.find { it.product.productId == productId }
        cartItem?.let {
            it.quantity = quantity
            it.productTotal = it.product.price * quantity // Recalculate productTotal
        }
    }

    override fun removeProduct(productId: Int) {
        cartItems.removeAll { it.product.productId == productId }
    }

    override fun clearCart() {
        cartItems.clear() // Clears all items in the cart
    }
}
