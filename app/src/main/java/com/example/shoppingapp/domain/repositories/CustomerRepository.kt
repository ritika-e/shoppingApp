package com.example.shoppingapp.domain.repositories

import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.model.Order

interface CustomerRepository {
    suspend fun getAllCustomers(): Result<List<Customer>>
    suspend fun getCustomerById(customerId: String): Customer?
    suspend fun deleteCustomer(customerId: String): Result<Boolean>
    suspend fun getOrdersByCustomerId(customerId: String): Result<List<Order>>
}