package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.Order

interface OrderHistoryRepository {
    suspend fun getOrdersByUserId(userId: String): Result<List<Order>>
    suspend fun getOrderById(orderId: String?): Order?
}