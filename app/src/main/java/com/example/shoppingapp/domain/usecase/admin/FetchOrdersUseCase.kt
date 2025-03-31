package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.OrderRepository

open class FetchOrdersUseCase(private val orderRepository: OrderRepository) {
    open suspend fun execute(): Result<List<Order>> {
        return orderRepository.getAllOrders()
    }
}
