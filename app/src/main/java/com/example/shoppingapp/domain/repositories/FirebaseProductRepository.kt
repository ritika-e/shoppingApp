package com.example.shoppingapp.domain.repositories

import android.net.Uri
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.ProductList

interface FirebaseProductRepository {

   suspend fun addProduct(product: ProductList): Boolean
   suspend fun saveProductDataToDatabase(product: ProductList): Boolean
   suspend fun uploadImageToStorage(uri: Uri, productId: Int, onProgress: (Float) -> Unit,
                                    onComplete: (String?) -> Unit)
}