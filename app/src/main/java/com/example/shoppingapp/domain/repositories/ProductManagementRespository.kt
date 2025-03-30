package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.ProductList

interface ProductManagementRespository {
    suspend fun getProducts(): List<ProductList>
    suspend fun updateProduct(productId: Int, product: ProductList)
    suspend fun deleteProduct(productId: Int)
    suspend fun getProductById(productId: Int): ProductList?
}