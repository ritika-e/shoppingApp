package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.repositories.ProductRepository

class GetCategoryItemUseCase(private val productRepository: ProductRepository){
    suspend fun execute(categoryId: String): Result<List<ItemsModel>> {
        return productRepository.getCategoryItems(categoryId)
    }
}