package com.example.shoppingapp.data.admin

import android.net.Uri
import android.util.Log
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.repositories.ProductManagementRespository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

open class ProductManagementRepositoryImpl(
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
): ProductManagementRespository {

    override suspend fun getProducts(): List<ProductList> {
        val productsRef = database.reference.child("Items")
        val snapshot = productsRef.get().await()
        val products = mutableListOf<ProductList>()

        snapshot.children.forEach { data ->
            val product = data.getValue(ProductList::class.java)
            product?.let { products.add(it) }
        }
        return products
    }

   override suspend fun getProductById(productId: Int): ProductList? {
       return try {
           Log.e("Use case", "GetProductByIdUseCase productId $productId")

           val productRef = database.reference.child("Items")
           val query = productRef.orderByChild("productId").equalTo(productId.toDouble())
           val snapshot = query.get().await()

           return snapshot.children.firstOrNull()?.getValue(ProductList::class.java)
       } catch (e: Exception) {
           Log.e("ProductRepository", "Error fetching product by ID: ${e.message}")
           null
       }
   }
    override suspend fun updateProduct(productId: Int, product: ProductList) {
        Log.e("Use case", "updateProduct productId $productId")

        // Query to find the product by productId
        val productRef = database.reference.child("Items")
        val query = productRef.orderByChild("productId").equalTo(productId.toDouble())

        // Fetch the snapshot of the queried data
        val snapshot = query.get().await()

        // Check if product exists
        if (!snapshot.exists()) {
            // Log the productId to verify if the product is being queried correctly
            Log.e("Use case", "Product not found in the database for productId: $productId")
            throw Exception("Product not found in database.")
        }

        // Log if the product is retrieved successfully
        Log.e("Use case", "Product found, updating product details")

        // Get the actual product data from the snapshot
        val productSnapshot = snapshot.children.firstOrNull()
        if (productSnapshot != null) {
            val productRefToUpdate = productSnapshot.ref

            // Prepare the map for updating product fields
            val updates = mutableMapOf<String, Any?>(
                "title" to product.title,
                "description" to product.description,
                "price" to product.price
            )

            // Only add picUrl if it's not null or empty
            if (!product.picUrl.isNullOrEmpty()) {
                // Step 1: Upload the image to Firebase Storage and get the download URL
                val imageRef = storage.reference.child("product_images/${productId}.jpg")
                try {
                    val uploadTask = imageRef.putFile(Uri.parse(product.picUrl)) // Upload new image
                    uploadTask.await() // Wait for the upload to complete
                    val imageDownloadUrl = imageRef.downloadUrl.await().toString() // Get the image URL

                    // Step 2: Update the product with the new image URL
                    updates["picUrl"] = imageDownloadUrl // Update the picUrl field in the product data
                } catch (e: Exception) {
                    Log.e("Firebase", "Image upload failed", e)
                    throw Exception("Image upload failed")
                }
            }

            // Step 3: Update the product fields in Firebase Realtime Database
            productRefToUpdate.updateChildren(updates).await() // Update the database record with the new details

            Log.e("Use case", "Product update successful for productId: $productId")
        } else {
            // Handle case where the product is not found in the snapshot
            throw Exception("Product not found after querying in database.")
        }
    }

    override suspend fun deleteProduct(productId: Int) {
        val productRef = database.reference.child("Items")
        val query = productRef.orderByChild("productId").equalTo(productId.toDouble())
        val snapshot = query.get().await()

        if (!snapshot.exists()) {
            throw Exception("Product not found in database.")
        }

        val productSnapshot = snapshot.children.firstOrNull()

        // If product is found, delete it from the database
        productSnapshot?.ref?.removeValue()?.await()

        // After removing the product, delete the product image from Firebase Storage
        val imageRef = storage.reference.child("product_images/$productId.jpg")
        imageRef.delete().await()

        Log.e("Use case", "Product and image deleted successfully for productId: $productId")
    }

}