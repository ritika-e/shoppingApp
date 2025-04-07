package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.usecase.productUseCases.GetBannersUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoriesUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoryItemUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetProductDetailsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetRecommendedProductsUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


open class ProductDetailsViewModel(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getRecommendedProductsUseCase: GetRecommendedProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryItemUseCase: GetCategoryItemUseCase,
    val getBannersUseCase: GetBannersUseCase
) : ViewModel() {

    private val _category = MutableLiveData<List<CategoryModel>>()
    open val categories: LiveData<List<CategoryModel>> = _category

    val _banner = MutableLiveData<List<SliderModel>>(emptyList()) // Initialize with an empty list
    val banners: LiveData<List<SliderModel>> = _banner

    private val _recommended = MutableLiveData<List<ItemsModel>>()
    val recommended: LiveData<List<ItemsModel>> = _recommended

    private val _productDetails = MutableLiveData<ItemsModel?>()
    val productDetails: LiveData<ItemsModel?> = _productDetails

    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String>  = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun loadProductDetails(productId: Int) {
        getProductDetailsUseCase.execute(productId).observeForever { product ->
            //_productDetails.value = product
            if (product == null) {
                _error.value = "Product not found"
            } else {
                _productDetails.value = product
            }
        }
    }

    fun loadRecommended() {
        getRecommendedProductsUseCase.execute().observeForever { products ->
            _recommended.value = products
        }
    }

    fun loadCategories() {
        getCategoriesUseCase.execute().observeForever { categories ->
            // _category.value = categories
            if (categories.isNullOrEmpty()) {
                _error.value = "Failed to load categories"
            } else {
                _category.value = categories
            }
        }
    }

    /* fun loadBanners() {
         getBannersUseCase.execute().observeForever { bannersList ->
             Log.d("ProductDetailsViewModel", "Received banners from UseCase: $bannersList")
             _banner.value = bannersList // Update ViewModel state with the fetched banners
         }
     }*/

    fun loadBanners() {
        getBannersUseCase.execute().observeForever { bannersList ->
            if (bannersList.isNullOrEmpty()) {
                _error.value = "Failed to load banners"
            } else {
                _banner.value = bannersList // Update ViewModel state with the fetched banners
            }
        }
    }


    fun loadCategoryItem(categoryId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getCategoryItemUseCase.execute(categoryId)

            _isLoading.value = false

            result.onSuccess {
                _recommended.value = it
            }

            result.onFailure { exception ->
                _error.value = "Error loading items: ${exception.localizedMessage}"
            }
        }
    }
}