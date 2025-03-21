package com.example.shoppingapp.domain.usecase.productUseCases

import android.util.Log
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.data.product.OrderRepositoryImpl

class PlaceOrderUseCase(private val orderRepositoryImpl: OrderRepositoryImpl) {

    // Function to place the order
    suspend fun execute(cartItems: List<CartItem>, totalAmount: Double, userId: String): Result<String> {

        Log.d("PlaceOrderUseCase", "Cart Items: $cartItems")
        Log.d("PlaceOrderUseCase", "Total Amount: $totalAmount")
        Log.d("PlaceOrderUseCase", "User ID: $userId")

        val totalAmountCalculated  = cartItems.sumOf { it.product.price * it.quantity }
        Log.d("PlaceOrderUseCase", "Product Total: $totalAmountCalculated ")

        val order = Order(
           // orderId = (0..Int.MAX_VALUE).random().toString(),
            cartItems = cartItems,
            totalAmount = totalAmountCalculated,
            productTotal = totalAmountCalculated ,
            orderDate = System.currentTimeMillis().toString(),
            userId = userId
        )
        return orderRepositoryImpl.saveOrder(order)
    }
}