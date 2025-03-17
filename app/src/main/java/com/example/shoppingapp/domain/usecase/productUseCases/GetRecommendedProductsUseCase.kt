package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.repositories.ProductRepository

class GetRecommendedProductsUseCase(private val productRepository: ProductRepository) {
    fun execute() = productRepository.getRecommendedProducts()
}