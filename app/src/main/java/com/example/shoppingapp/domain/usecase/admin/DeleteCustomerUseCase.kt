package com.example.shoppingapp.domain.usecase.admin

import com.example.shoppingapp.domain.repositories.CustomerRepository

open class DeleteCustomerUseCase(private val customerRepository: CustomerRepository) {

   open suspend fun execute(customerId: String): Result<Boolean> {
        return customerRepository.deleteCustomer(customerId)
    }
}
