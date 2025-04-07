package com.example.shoppingapp.domain.usecase.admin

import android.util.Log
import com.example.shoppingapp.domain.repositories.OrderCountRepository

class GetTotalPendingOrdersUseCase(private val orderCountRepository: OrderCountRepository) {
    suspend fun execute(): Int {
        val result = orderCountRepository.getTotalPendingOrders()
        Log.d("GetTotalPendingOrdersUseCase", "Returned from repository: $result")  // Log the result here
        return result
    }
}


class GetTotalOrdersUseCase(private val orderCountRepository: OrderCountRepository) {
    suspend fun execute(): Int {
        val totalOrders = orderCountRepository.getTotalOrders()  // Get from repo
        Log.d("GetTotalOrdersUseCase", "Returned Total Orders from Repository: $totalOrders")  // Log the result
        return totalOrders
    }
}

class GetTotalDeliverdOrdersUseCase(private val orderCountRepository: OrderCountRepository) {
    suspend fun execute(): Int {
        return orderCountRepository.getTotalDeliverdOrders()
    }
}

class GetTotalAcceptedOrdersUseCase(private val orderCountRepository: OrderCountRepository) {
    suspend fun execute(): Int {
        return orderCountRepository.getTotalAcceptedOrders()
    }
}

class GetTotalRejectedOrdersUseCase(private val orderCountRepository: OrderCountRepository) {
    suspend fun execute(): Int {
        return orderCountRepository.getTotalRejectedOrders()
    }
}