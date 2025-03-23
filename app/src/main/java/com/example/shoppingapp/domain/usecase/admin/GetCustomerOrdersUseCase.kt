package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.CustomerRepository

class GetCustomerOrdersUseCase (private val customerRepository: CustomerRepository){
    suspend fun execute(customerId: String): Result<List<Order>> {
        return customerRepository.getOrdersByCustomerId(customerId)
    }
}