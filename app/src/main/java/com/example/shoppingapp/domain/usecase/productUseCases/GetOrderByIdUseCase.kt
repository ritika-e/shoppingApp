package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.OrderHistoryRepository

class GetOrderByIdUseCase(private val orderHistoryRepository: OrderHistoryRepository) {

    suspend fun execute(orderId: String?): Order? {
        return orderHistoryRepository.getOrderById(orderId)
    }
}