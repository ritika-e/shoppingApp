package com.example.shoppingapp.data.admin

import android.util.Log
import com.example.shoppingapp.domain.repositories.OrderCountRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderCountRepositoryImpl:OrderCountRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getTotalPendingOrders(): Int {
        return try {
            val ordersSnapshot = firestore.collection("orders")
                .whereEqualTo("status", "Pending")
                .get()
                .await()
            val orderCount = ordersSnapshot.size()  // Number of documents matching the query

            Log.d("TOTAL Pending","$orderCount")
            return orderCount
        } catch (e: Exception) {
            return  0  // Return 0 if there is an error
        }
    }

    override suspend fun getTotalOrders(): Int {
        return try {
            val ordersSnapshot = firestore.collection("orders")
                .get()
                .await()
            val orderCount = ordersSnapshot.size()  // Number of documents matching the query

            Log.d("TOTAL ","$orderCount")
            return orderCount
        } catch (e: Exception) {
          return  0  // Return 0 if there is an error
        }
    }

    override suspend fun getTotalDeliverdOrders(): Int {
        return try {
            val ordersSnapshot = firestore.collection("orders")
                .whereEqualTo("status", "Delivered")
                .get()
                .await()
            val orderCount = ordersSnapshot.size()  // Number of documents matching the query

            Log.d("TOTAL Delivered","$orderCount")
            return orderCount
        } catch (e: Exception) {
           return 0  // Return 0 if there is an error
        }
    }

    override suspend fun getTotalAcceptedOrders(): Int {
        return try {
            val ordersSnapshot = firestore.collection("orders")
                .whereEqualTo("status", "Accept")
                .get()
                .await()
            val orderCount = ordersSnapshot.size()  // Number of documents matching the query

            Log.d("TOTAL Accepted","$orderCount")
            return orderCount
        } catch (e: Exception) {
           return 0  // Return 0 if there is an error
        }
    }

    override suspend fun getTotalRejectedOrders(): Int {
        return try {
            val ordersSnapshot = firestore.collection("orders")
                .whereEqualTo("status", "Reject")
                .get()
                .await()
            val orderCount = ordersSnapshot.size()  // Number of documents matching the query

            Log.d("TOTAL Rejected","$orderCount")
            return orderCount
        } catch (e: Exception) {
           return 0  // Return 0 if there is an error
        }
    }
}