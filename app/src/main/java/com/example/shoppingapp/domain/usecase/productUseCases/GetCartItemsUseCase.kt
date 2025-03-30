package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.repositories.CartRepository

open class GetCartItemsUseCase(private val cartRepository: CartRepository) {
   open fun execute(): List<CartItem> {
        return cartRepository.getCartItems()
    }
}