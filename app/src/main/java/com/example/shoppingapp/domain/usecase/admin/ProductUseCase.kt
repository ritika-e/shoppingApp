package com.example.shoppingapp.domain.usecase.admin

import android.net.Uri
import android.util.Log
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.repositories.FirebaseProductRepository
import com.example.shoppingapp.domain.repositories.ProductManagementRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductUseCase {
}

open class AddProductUseCase(private val productRepository: FirebaseProductRepository) {

    open suspend fun execute(
        product: ProductList,
        onProgress: (Float) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        try {
            // Checking if the product has a valid picUri (String)
            product.picUrl?.let { uriString ->
                val uri = Uri.parse(uriString)  // Convert the string to a Uri

                // Upload the image to Firebase Storage and get the download URL
                productRepository.uploadImageToStorage(uri, product.productId, onProgress) { imageUrl ->
                    if (imageUrl != null) {

                        //  Update the product with the image URL
                        val updatedProduct = product.copy(picUrl = imageUrl)

                        //  Save the product data (including image URL) to the database
                        GlobalScope.launch(Dispatchers.IO) { // Launch coroutine in background thread
                            val isProductSaved = saveProduct(updatedProduct)
                            withContext(Dispatchers.Main) {
                                onComplete(isProductSaved) // Notify completion on main thread
                            }
                        }
                    } else {
                        onComplete(false)  // Notify failure if image URL is null
                    }
                }
            } ?: run {
                // If no picUri is provided, notify failure
                onComplete(false)
            }
        } catch (e: Exception) {
            // Handle any exceptions during the process
            Log.e("AddProductUseCase", "Error adding product", e)
            onComplete(false)
        }
    }
    open suspend fun saveProduct(updatedProduct: ProductList): Boolean {
        return withContext(Dispatchers.IO) {
            productRepository.saveProductDataToDatabase(updatedProduct)
        }
    }
}

open class UpdateProductUseCase(private val productRepository: ProductManagementRespository) {
        suspend fun execute(productId: Int, product: ProductList) {
            productRepository.updateProduct(productId, product)
        }
    }

open class DeleteProductUseCase(private val productRepository: ProductManagementRespository) {
        suspend fun execute(productId: Int) {
            productRepository.deleteProduct(productId)
        }
    }

open class GetAllProductsUseCase(private val productRepository: ProductManagementRespository) {
        suspend fun execute(): List<ProductList> {
            return productRepository.getProducts()
        }
    }

open class GetProductByIdUseCase(private val productRepository: ProductManagementRespository) {
    suspend fun execute(productId: Int): ProductList? {
        Log.e("Use case","GetProductByIdUseCase productId $productId")
        return productRepository.getProductById(productId)
    }
}
