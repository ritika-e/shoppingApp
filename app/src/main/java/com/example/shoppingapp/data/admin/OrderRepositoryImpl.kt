package com.example.shoppingapp.data.admin

import android.util.Log
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.OrderRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl(private val firebaseFirestore: FirebaseFirestore) : OrderRepository {

    override suspend fun getAllOrders(): Result<List<Order>> {
        return try {
            val querySnapshot = firebaseFirestore.collection("orders")
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get().await()
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

    // Update order status in Firestore
    override suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Unit> {
        return try {
            val orderRef = firebaseFirestore.collection("orders").document(orderId)
            orderRef.update("status", newStatus).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("OrderRepositoryImpl", "Error updating order status: ${e.message}")
            Result.failure(e)
        }
    }
}
