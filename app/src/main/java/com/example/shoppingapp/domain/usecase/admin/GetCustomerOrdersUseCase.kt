package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.CustomerRepository

open class GetCustomerOrdersUseCase (private val customerRepository: CustomerRepository){
  open  suspend fun execute(customerId: String): Result<List<Order>> {
        return customerRepository.getOrdersByCustomerId(customerId)
    }
}