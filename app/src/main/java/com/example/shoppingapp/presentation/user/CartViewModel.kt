package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.domain.model.CartItemModel
import com.example.shoppingapp.domain.model.CartModel
import com.example.shoppingapp.domain.model.ItemsModel

class CartViewModel():ViewModel() {

    // LiveData to observe the cart state in the UI
    private val _cart = MutableLiveData<CartModel>(CartModel())
    val cart: MutableLiveData<CartModel> = _cart

    // Add product to the cart
    fun addToCart(item: ItemsModel) {
        val currentCart = _cart.value ?: CartModel()
        val existingItem = currentCart.items.find { it.productId == item.productId }

        Log.e("Item Added","Cart Updated")
        if (existingItem != null) {
            // If the item is already in the cart
            existingItem.quantity += 1
        } else {
            // If the item is not in the cart, add a new cart item
            val cartItem = CartItemModel(
                productId = item.productId,
                productName = item.title,
                productPrice = item.price,
                quantity = 1,
                productImageUrl = item.picUrl.firstOrNull() ?: ""
            )
            currentCart.items.add(cartItem)
        }

        // Updating the cart LiveData
        _cart.value = currentCart
    }

    // Remove item from the cart based on product ID
    fun removeFromCart(item: ItemsModel) {
        val currentCart = _cart.value ?: CartModel()
        currentCart.items.removeAll { it.productId == item.productId }
        _cart.value = currentCart  // Cart update after removal
    }

    // Increment the quantity of a cart item
    fun incrementQuantity(item: ItemsModel) {
        val currentCart = _cart.value ?: CartModel()
        val cartItem = currentCart.items.find { it.productId == item.productId }
        if (cartItem != null) {
            cartItem.quantity += 1
            _cart.value = currentCart
        }
    }

    // Decrement the quantity of a cart item
    fun decrementQuantity(item: ItemsModel) {
        val currentCart = _cart.value ?: CartModel()
        val cartItem = currentCart.items.find { it.productId == item.productId }
        if (cartItem != null && cartItem.quantity > 1) {
            cartItem.quantity -= 1
            _cart.value = currentCart
        }
    }

    // Check if an item is in the cart
    fun isItemInCart(productId: Int): Boolean {
        return _cart.value?.items?.any { it.productId == productId } == true
    }

    // Clear the cart
    fun clearCart() {
        _cart.value = CartModel()
    }
}
