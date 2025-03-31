package com.example.shoppingapp.domain.model

data class Customer(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val order: List<Order> = emptyList() //   customer's order history
)
