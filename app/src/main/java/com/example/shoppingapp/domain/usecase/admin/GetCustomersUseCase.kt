package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.repositories.CustomerRepository

class GetCustomersUseCase(private val customerRepository: CustomerRepository) {

    suspend fun execute(): Result<List<Customer>> {
        return customerRepository.getAllCustomers()
    }
}
