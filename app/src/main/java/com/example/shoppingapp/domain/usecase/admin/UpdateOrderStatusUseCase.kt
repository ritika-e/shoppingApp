package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.repositories.OrderRepository

class UpdateOrderStatusUseCase(private val orderRepository: OrderRepository) {
    suspend fun execute(orderId: String, newStatus: String): Result<Unit> {
        return orderRepository.updateOrderStatus(orderId, newStatus)
    }
}
