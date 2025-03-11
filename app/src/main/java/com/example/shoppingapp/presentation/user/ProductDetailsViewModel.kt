package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.usecase.productUseCases.GetBannersUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoriesUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetProductDetailsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetRecommendedProductsUseCase


class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getRecommendedProductsUseCase: GetRecommendedProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getBannersUseCase: GetBannersUseCase
) : ViewModel() {

    private val _category = MutableLiveData<List<CategoryModel>>()
    val categories: LiveData<List<CategoryModel>> = _category

   private val _banner = MutableLiveData<List<SliderModel>>(emptyList()) // Initialize with an empty list
    val banners: LiveData<List<SliderModel>> = _banner

   // val banners: State<List<SliderModel>> = _banners

    private val _recommended = MutableLiveData<List<ItemsModel>>()
    val recommended: LiveData<List<ItemsModel>> = _recommended

    private val _productDetails = MutableLiveData<ItemsModel?>()
    val productDetails: LiveData<ItemsModel?> = _productDetails

    fun loadProductDetails(productId: Int) {
        getProductDetailsUseCase.execute(productId).observeForever { product ->
            _productDetails.value = product
        }
    }

    fun loadRecommended() {
        getRecommendedProductsUseCase.execute().observeForever { products ->
            _recommended.value = products
        }
    }

    fun loadCategories() {
        getCategoriesUseCase.execute().observeForever { categories ->
            _category.value = categories
        }
    }

    fun loadBanners() {
        getBannersUseCase.execute().observeForever { bannersList ->
            Log.d("ProductDetailsViewModel", "Received banners from UseCase: $bannersList")
            _banner.value = bannersList // Update ViewModel state with the fetched banners
        }
    }

}
