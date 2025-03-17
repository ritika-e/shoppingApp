package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.CartRepository

class RemoveProductFromCartUseCase(private val cartRepository: CartRepository) {
    fun execute(productId: Int) {
        cartRepository.removeProductFromCart(productId)
    }
}