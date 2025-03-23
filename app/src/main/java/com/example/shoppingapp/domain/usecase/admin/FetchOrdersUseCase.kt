package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.OrderRepository

class FetchOrdersUseCase(private val orderRepository: OrderRepository) {
    suspend fun execute(): Result<List<Order>> {
        return orderRepository.getAllOrders()
    }
}
