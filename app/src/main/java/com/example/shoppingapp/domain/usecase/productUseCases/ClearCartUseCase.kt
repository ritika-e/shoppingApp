package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.CartRepository

open class ClearCartUseCase(private val cartRepository: CartRepository) {
  open  fun execute() {
        cartRepository.clearCart()
    }
}
