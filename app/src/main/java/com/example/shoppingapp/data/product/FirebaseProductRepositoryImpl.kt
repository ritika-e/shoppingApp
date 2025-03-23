package com.example.shoppingapp.data.product

import android.net.Uri
import android.util.Log
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.repositories.FirebaseProductRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FirebaseProductRepositoryImpl : FirebaseProductRepository {

    //private val storageReference = FirebaseStorage.getInstance().reference
    //private val databaseReference = FirebaseDatabase.getInstance().reference.child("products")

    // Function to upload image to Firebase Storage
    override suspend fun uploadImageToStorage(uri: Uri, productId: Int, onProgress: (Float) -> Unit, onComplete: (String?) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "${UUID.randomUUID()}.jpg"  // Generate a random file name for the image
        val productImageRef = storageRef.child("product_images/$fileName") // Path to store the image in Firebase Storage

        // Upload the image to Firebase Storage
        val uploadTask = productImageRef.putFile(uri)

        // Track the progress of the upload
        uploadTask.addOnProgressListener { taskSnapshot ->
            // Calculate upload progress percentage and call the onProgress callback
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
            onProgress(progress)
        }

        // Handle the upload completion
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // If the upload is successful, get the download URL of the uploaded image
                productImageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Image URL successfully retrieved
                    val imageUrl = downloadUri.toString()
                    onComplete(imageUrl)  // Return the download URL via onComplete callback
                }.addOnFailureListener { exception ->
                    // Handle failure to get download URL
                    Log.e("Upload Image", "Failed to get download URL", exception)
                    onComplete(null)
                }
            } else {
                // Handle upload failure
                Log.e("Upload Image", "Image upload failed", task.exception)
                onComplete(null)
            }
        }
    }

    // Function to add a product to Firebase Realtime Database
    override suspend fun addProduct(product: ProductList): Boolean {
        try {
            val database = FirebaseDatabase.getInstance().getReference("Items")
            val productId = product.productId.toString()
            database.child(productId).setValue(product).addOnSuccessListener {
                Log.d("Add Product", "Product added successfully!")
            }.addOnFailureListener {
                Log.e("Add Product", "Failed to add product", it)
            }

            return true
        } catch (e: Exception) {
            Log.e("Add Product", "Error: ${e.message}")
            return false
        }
    }



    override suspend fun saveProductDataToDatabase(product: ProductList): Boolean {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Items")
        val productId = product.productId.toString()

        return try {
            // Use setValue inside a coroutine
            val task = databaseRef.child(productId).setValue(product).await()
            true // If successful
        } catch (e: Exception) {
            Log.e("Save Product", "Failed to save product", e)
            false // If error occurs
        }
    }


    /* fun saveProductDataToDatabase(productId: String, imageUrl: String,productData: ProductList) {
         val databaseRef = FirebaseDatabase.getInstance().reference
         val productRef = databaseRef.child("products").child(productId) // Reference to the "products" node

         // Create a product object with the data
         val productData = mapOf(
             "productId" to productId,
             "name" to productData.title, // Product name
             "price" to productData.price, // Product price
             "description" to productData.description,
             "imageUrl" to imageUrl // Add the image URL here
         )

         // Save the product data in the Firebase Realtime Database
         productRef.setValue(productData).addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 Log.d("Firebase", "Product data saved successfully")
             } else {
                 Log.e("Firebase", "Failed to save product data", task.exception)
             }
         }
     }
 */
    /*  override suspend fun addProduct(product: ProductList, onComplete: (Boolean) -> Unit) {
          try {
              databaseReference.child(product.productId).setValue(product).await()
              onComplete(true)
          } catch (e: Exception) {
              onComplete(false)
          }
      }*/

   /* override suspend fun updateProduct(product: ProductList) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(productId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProducts(): List<ProductList> {
        TODO("Not yet implemented")
    }*/
}