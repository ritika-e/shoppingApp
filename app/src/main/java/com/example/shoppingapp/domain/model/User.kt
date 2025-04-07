package com.example.shoppingapp.domain.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "", // "user" or "admin"
    val mobileNumber: String = "",
    val address: String = ""
)