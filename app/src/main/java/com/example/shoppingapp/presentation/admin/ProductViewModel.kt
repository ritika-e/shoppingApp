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
import com.example.shoppingapp.domain.usecase.admin.UpdateProductUseCase
import kotlinx.coroutines.launch

class ProductViewModel(
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val fbProductRepository: FirebaseProductRepository,
) : ViewModel() {

    private val _imageUploadProgress = MutableLiveData<Float>()
    val imageUploadProgress: LiveData<Float> get() = _imageUploadProgress

    private val _productUploadStatus = MutableLiveData<Boolean>()
    val productUploadStatus: LiveData<Boolean> get() = _productUploadStatus

    private val _products = MutableLiveData<List<ProductList>>()
    val products: LiveData<List<ProductList>> get() = _products

    var selectedImageUri: Uri? by mutableStateOf(null)

    fun addProductWithImage(
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
}
