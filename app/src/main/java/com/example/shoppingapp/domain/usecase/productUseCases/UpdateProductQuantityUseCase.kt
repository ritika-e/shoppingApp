package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.CartRepository

class UpdateProductQuantityUseCase(private val cartRepository: CartRepository) {
    fun execute(productId: Int, quantity: Int) {
        cartRepository.updateProductQuantity(productId, quantity)
    }
}