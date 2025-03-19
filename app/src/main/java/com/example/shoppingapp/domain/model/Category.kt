package com.example.shoppingapp.domain.model

data class Category(val id: String, val name: String)

val categories = listOf(
    Category("0", "Adidas"),
    Category("1", "Nike"),
    Category("2", "Puma"),
    Category("3", "Zara"),
    Category("4", "Gucci"),
    Category("5", "Prada")
)