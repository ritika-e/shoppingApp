package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.CartRepository

open class UpdateProductQuantityUseCase(private val cartRepository: CartRepository) {
  open  fun execute(productId: Int, quantity: Int) {
        cartRepository.updateProductQuantity(productId, quantity)
    }
}