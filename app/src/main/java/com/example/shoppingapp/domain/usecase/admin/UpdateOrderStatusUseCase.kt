package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.repositories.OrderRepository

open class UpdateOrderStatusUseCase(private val orderRepository: OrderRepository) {
    open  suspend fun execute(orderId: String, newStatus: String): Result<Unit> {
        return orderRepository.updateOrderStatus(orderId, newStatus)
    }
}
