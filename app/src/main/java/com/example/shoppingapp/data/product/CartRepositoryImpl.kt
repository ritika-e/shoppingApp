package com.example.shoppingapp.data.product

import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.repositories.CartDataSourceRepository
import com.example.shoppingapp.domain.repositories.CartRepository

class CartRepositoryImpl(private val cartDataSourceRepository: CartDataSourceRepository) : CartRepository {
    override fun addProduct(product: ItemsModel) {
        cartDataSourceRepository.addProduct(product)
    }

    override fun getCartItems(): List<CartItem> = cartDataSourceRepository.getCartItems()

    override fun updateProductQuantity(productId: Int, quantity: Int) {
        cartDataSourceRepository.updateProductQuantity(productId, quantity)
    }

    override fun removeProductFromCart(productId: Int) {
        cartDataSourceRepository.removeProduct(productId)
    }
}
