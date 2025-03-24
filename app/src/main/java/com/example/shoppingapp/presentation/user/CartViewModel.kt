package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.usecase.productUseCases.CartUseCases
import com.example.shoppingapp.domain.usecase.productUseCases.PlaceOrderUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class CartViewModel(private val cartUseCases: CartUseCases,
                    private val placeOrderUseCase: PlaceOrderUseCase
        ) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>?>()
    val cartItems: MutableLiveData<List<CartItem>?> get() = _cartItems

    private val _orderStatus = MutableLiveData<String>()
    val orderStatus: LiveData<String> get() = _orderStatus

    init {
         loadCartItems()
    }

    private fun loadCartItems() {
        _cartItems.value = cartUseCases.getCartItemsUseCase.execute() ?: emptyList()
    }

  fun addProductToCart(product: ItemsModel) {
      val existingCartItems = _cartItems.value?.toMutableList() ?: mutableListOf()
      val existingCartItem = existingCartItems.find { it.product.productId == product.productId }

      if (existingCartItem != null) {
          // If the product is already in the cart, increase the quantity
          existingCartItem.quantity++
      } else {
          // Otherwise, add a new CartItem
          existingCartItems.add(CartItem(product, 1))
      }

      _cartItems.value = existingCartItems
      // Persist updated cart (save to a database)
      cartUseCases.addProductToCartUseCase.execute(product)
  }

    fun updateProductQuantity(productId: Int, newQuantity: Int) {
        val updatedCartItems = _cartItems.value?.map {
            if (it.product.productId == productId) {
                it.copy(quantity = newQuantity)
            } else {
                it
            }
        }
        _cartItems.value = updatedCartItems
        // Update the cart in the repository database
        cartUseCases.updateProductQuantityUseCase.execute(productId, newQuantity)

    }

    fun removeProductFromCart(productId: Int) {
        val updatedCartItems = _cartItems.value?.filter { it.product.productId != productId }
        _cartItems.value = updatedCartItems
        cartUseCases.removeProductFromCartUseCase.execute(productId)
    }

    // Place order function to call the use case
    fun placeOrder() {
        val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
        val cartItemsList = _cartItems.value ?: emptyList()

        Log.d("CartViewModel", "Cart Items: $cartItemsList")

        val totalAmount = cartItemsList.sumOf { it.product.price * it.quantity }
        Log.d("CartViewModel", "Total Amount: $totalAmount")

        val updatedCartItems = cartItemsList.map {
            it.copy(productTotal = it.product.price * it.quantity) // Update each CartItem with its productTotal
        }

        val userId = sharedPreferencesManager.getUserData().userId

        Log.d("CartViewModel", "User ID: $userId")

        if (cartItemsList.isEmpty()) {
            _orderStatus.value = "Your cart is empty!"
            Log.e("CartViewModel", "User ID is null or empty")
            return
        }

        // Call the use case to place the order
        viewModelScope.launch {
            val result = placeOrderUseCase.execute(updatedCartItems, totalAmount, userId!!)
            if (result.isSuccess) {
                _orderStatus.value = "Order placed successfully! Order ID: ${result.getOrNull()}"
            } else {
                _orderStatus.value = "Failed to place order: ${result.exceptionOrNull()?.message}"
                Log.e("CartViewModel", "Failed to place order: ${result.exceptionOrNull()?.message}")
            }
        }
    }

    fun clearCart() {
        cartUseCases.clearCartUseCase.execute() // Calls the repository to clear the cart
    }
}