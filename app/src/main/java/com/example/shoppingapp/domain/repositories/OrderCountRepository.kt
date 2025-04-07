package com.example.shoppingapp.domain.repositories

interface OrderCountRepository {
    suspend fun getTotalPendingOrders(): Int
    suspend fun getTotalOrders():Int
    suspend fun getTotalDeliverdOrders():Int
    suspend fun getTotalAcceptedOrders():Int
    suspend fun getTotalRejectedOrders():Int

}