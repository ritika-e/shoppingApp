package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.CartRepository

open class RemoveProductFromCartUseCase(private val cartRepository: CartRepository) {
  open  fun execute(productId: Int) {
        cartRepository.removeProductFromCart(productId)
    }
}