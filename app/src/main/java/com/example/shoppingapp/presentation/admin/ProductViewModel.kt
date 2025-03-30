package com.example.shoppingapp.presentation.admin

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.repositories.FirebaseProductRepository
import com.example.shoppingapp.domain.usecase.admin.AddProductUseCase
import com.example.shoppingapp.domain.usecase.admin.DeleteProductUseCase
import com.example.shoppingapp.domain.usecase.admin.GetAllProductsUseCase
import com.example.shoppingapp.domain.usecase.admin.GetProductByIdUseCase
import com.example.shoppingapp.domain.usecase.admin.UpdateProductUseCase
import kotlinx.coroutines.launch

open class ProductViewModel(
    private val addProductUseCase: AddProductUseCase,
    var updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
) : ViewModel() {

    private val _imageUploadProgress = MutableLiveData<Float>()
    val imageUploadProgress: LiveData<Float> get() = _imageUploadProgress

    private val _productUploadStatus = MutableLiveData<Boolean>()
    val productUploadStatus: LiveData<Boolean> get() = _productUploadStatus

    private val _productsList = MutableLiveData<List<ProductList>>()
    val productsList: LiveData<List<ProductList>> get() = _productsList

    private val _product = MutableLiveData<ProductList?>()  // Updated to hold a single product
    val product: LiveData<ProductList?> get() = _product

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    var selectedImageUri: Uri? by mutableStateOf(null)

    open   fun addProductWithImage(
        product: ProductList,
        selectedImageUri: Uri,
        onProgress: (Float) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            addProductUseCase.execute(
                product = product.copy(picUrl = selectedImageUri.toString()),
                onProgress = onProgress,
                onComplete = onComplete
            )
        }
    }

    open  fun getProductById(productId: Int) {
        Log.e("ViewModel getProductById","getProductById productId $productId")
        viewModelScope.launch {
            _isLoading.value = true
            val result = getProductByIdUseCase.execute(productId)
            _isLoading.value = false
            _product.value = result
        }
    }

    open  fun getProducts() {
       viewModelScope.launch {
           _isLoading.value = true
           val result = getAllProductsUseCase.execute()
           _isLoading.value = false
           _productsList.value = result
       }
   }

    open   fun updateProduct(productId: Int, product: ProductList) {
        Log.d("ProductViewModel", "Updating product with id: $productId")
        viewModelScope.launch {
            _isLoading.value = true
            updateProductUseCase.execute(productId, product)
            _isLoading.value = false
        }
    }

    open fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            deleteProductUseCase.execute(productId)
        }
    }
}
