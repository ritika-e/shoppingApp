package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.repositories.CartRepository

class GetCartItemsUseCase(private val cartRepository: CartRepository) {
    fun execute(): List<CartItem> {
        return cartRepository.getCartItems()
    }
}