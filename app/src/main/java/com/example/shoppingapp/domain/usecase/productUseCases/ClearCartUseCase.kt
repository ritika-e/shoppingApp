package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.CartRepository

class ClearCartUseCase(private val cartRepository: CartRepository) {
    fun execute() {
        cartRepository.clearCart()
    }
}
