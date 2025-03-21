package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.repositories.CustomerRepository

class DeleteCustomerUseCase(private val customerRepository: CustomerRepository) {

    suspend fun execute(customerId: String): Result<Boolean> {
        return customerRepository.deleteCustomer(customerId)
    }
}
