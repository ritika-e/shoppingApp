package com.example.shoppingapp.data.product

import android.util.Log
import com.example.shoppingapp.domain.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl(private val firebaseFirestore: FirebaseFirestore) {

    // Method to save the orders to Firestore
    suspend fun saveOrder(order: Order): Result<String> {
        return try {

            Log.d("OrderRepositoryImpl", "Saving Order: $order")

            // Add the order to Firestore (under "orders" collection)
            val documentReference = firebaseFirestore.collection("orders")
                .add(order)
                .await()
            Log.d("OrderRepositoryImpl", "Order saved with ID: ${documentReference.id}")

            Result.success(documentReference.id) // Return the order ID on success
        } catch (exception: Exception) {
            Log.e("OrderRepositoryImpl", "Error saving order: ${exception.message}")
            Result.failure(exception)
        }
    }
}
