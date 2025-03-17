package com.example.shoppingapp.domain.repositories

import androidx.lifecycle.LiveData
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel

interface ProductRepository {
    fun getProductDetails(productId: Int): LiveData<ItemsModel?>
    fun getRecommendedProducts(): LiveData<List<ItemsModel>>
    suspend fun getCategoryItems(categoryId: String): Result<List<ItemsModel>>
    fun getCategories(): LiveData<List<CategoryModel>>
    fun getBanners(): LiveData<List<SliderModel>>
}