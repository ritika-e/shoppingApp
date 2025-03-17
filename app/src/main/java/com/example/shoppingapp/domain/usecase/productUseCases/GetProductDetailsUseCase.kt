package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.ProductRepository

class GetProductDetailsUseCase(private val productRepository: ProductRepository) {
    fun execute(productId: Int) = productRepository.getProductDetails(productId)
}