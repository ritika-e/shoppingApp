package com.example.shoppingapp.data.product

import android.util.Log
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.OrderHistoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class OrderHistoryImpl(private val firebaseFirestore: FirebaseFirestore):OrderHistoryRepository {

    override suspend fun getOrdersByUserId(userId: String): Result<List<Order>> {
        return try {
            // No limit on the query
            val querySnapshot = firebaseFirestore.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .await()

            val orders = querySnapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Order::class.java)
                } catch (e: Exception) {
                    Log.e("OrderRepository", "Error deserializing order: ${e.message}")
                    null
                }
            }

            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderById(orderId: String?): Order? {
        return try {
            if (orderId.isNullOrBlank()) return null

            // Fetching order by orderId
            val docSnapshot = firebaseFirestore.collection("orders")
                .document(orderId)
                .get()
                .await()

            // Returning the Order object if the document exists
            docSnapshot.toObject(Order::class.java)
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching order by ID: ${e.message}")
            null
        }
    }


}
