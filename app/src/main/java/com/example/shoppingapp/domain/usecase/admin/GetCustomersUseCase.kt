package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.repositories.CustomerRepository

open class GetCustomersUseCase(private val customerRepository: CustomerRepository) {

  open  suspend fun execute(): Result<List<Customer>> {
        return customerRepository.getAllCustomers()
    }
}
