package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.Order

interface OrderRepository {
    suspend fun getAllOrders(): Result<List<Order>>
    suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Unit>
}