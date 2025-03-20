package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.data.product.OrderHistoryImpl
import com.example.shoppingapp.data.product.OrderRepositoryImpl
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.OrderHistoryRepository

class GetOrderHistoryUseCase(private val orderHistoryRepository: OrderHistoryRepository) {

    suspend fun execute(userId: String): Result<List<Order>> {
        // Encapsulate logic for fetching orders
        return orderHistoryRepository.getOrdersByUserId(userId)
    }

}
