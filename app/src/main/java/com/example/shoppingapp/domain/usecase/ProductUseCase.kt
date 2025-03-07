/*
package com.example.shoppingapp.domain.usecase

import com.example.shoppingapp.data.repository.ProductRepository
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import kotlin.Result

class ProductUseCase {

    // UseCase for fetching product details
    class GetProductDetailsUseCase(private val productRepository: ProductRepository.ProductRepository) {
        suspend fun execute(productId: Int): Result<ItemsModel> {
            return productRepository.getProductDetails(productId)
        }
    }

    // UseCase for fetching categories
    class GetCategoriesUseCase(private val productRepository: ProductRepository) {
        suspend fun execute(): Result<List<CategoryModel>> {
            return productRepository.getCategories()
        }
    }

    // UseCase for fetching banners
    class GetBannersUseCase(private val productRepository: ProductRepository) {
        suspend fun execute(): Result<List<SliderModel>> {
            return productRepository.getBanners()
        }
    }

    // UseCase for fetching recommended items
    class GetRecommendedItemsUseCase(private val productRepository: ProductRepository) {
        suspend fun execute(): Result<List<ItemsModel>> {
            return productRepository.getRecommendedItems()
        }
    }

}*/
