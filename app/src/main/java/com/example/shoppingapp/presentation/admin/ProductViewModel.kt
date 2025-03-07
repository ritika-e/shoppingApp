package com.example.shoppingapp.presentation.admin

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

class ProductViewModel() : ViewModel() {

  /*  private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _productsWithImages = MutableLiveData<List<ProductWithImage>>()
    val productsWithImages: LiveData<List<ProductWithImage>> get() = _productsWithImages

    fun loadProducts() {
        viewModelScope.launch {
            val result = productUseCase.getAllProducts()
            _products.postValue(result)
        }
    }

    fun addProduct(product: Product, productImage: ProductImage) {
        viewModelScope.launch {
            val result = productUseCase.addProduct(product, productImage)
            if (result.isSuccess) {
                loadProducts()  // Refresh the list after adding
            }else {
                // Handle failure
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val products = productUseCase.getProductsWithImages()
                _productsWithImages.value = products
                Log.d("ProductViewModel", "Fetched products with images: ${products.size}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching products: ${e.message}")
                // Handle errors, like showing a message to the user
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            val result = productUseCase.deleteProduct(productId)
            if (result.isSuccess) {
                loadProducts()  // Refresh the list after deleting
            }
        }
    }*/
}
