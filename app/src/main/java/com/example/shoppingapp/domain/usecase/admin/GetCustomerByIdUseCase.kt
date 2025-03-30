package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.repositories.CustomerRepository

open class GetCustomerByIdUseCase(private val customerRepository: CustomerRepository) {

  open  suspend fun execute(customerId: String): Result<Customer> {
        val customer = customerRepository.getCustomerById(customerId)

        return if (customer != null) {
            Result.success(customer) // Return a successful result with the customer
        } else {
            Result.failure(Exception("Customer not found")) // Return a failure result if customer is null
        }
    }
}
