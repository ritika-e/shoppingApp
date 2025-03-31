package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.usecase.productUseCases.GetOrderHistoryUseCase
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.shoppingapp.domain.usecase.productUseCases.GetOrderByIdUseCase


open class OrderHistoryViewModel(private val getOrderHistoryUseCase: GetOrderHistoryUseCase,
                                 private val getOrderByIdUseCase: GetOrderByIdUseCase) : ViewModel() {

    private val _orderHistory = MutableLiveData<List<Order>?>()
    val orderHistory: MutableLiveData<List<Order>?> get() = _orderHistory

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _order = MutableLiveData<Order?>(null)
    val order: LiveData<Order?> = _order

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    open  fun fetchOrders(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = getOrderHistoryUseCase.execute(userId)
                /*result.onSuccess { orders ->
                    Log.d("OrderViewModel", "Fetched ${orders.size} orders.")
                    _orderHistory.value = orders
                    Log.d("OrderViewModel", "LiveData Updated with orders: $orders")
                }
                result.onFailure {
                    // Handle error
                    Log.e("Error", "Error fetching orders: ${it.message}")
                }*/
                if (result.isSuccess) {
                    _orderHistory.value = result.getOrNull()
                } else {
                    // Log.e("ViewModel", "Failed to fetch orders: ${result.exceptionOrNull()}")
                    _error.value = "Failed to fetch orders"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchOrderById(orderId: String?) {
        if (orderId.isNullOrBlank()) {
            _error.value = "Invalid order ID"
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val result = getOrderByIdUseCase.execute(orderId)
                _order.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load order details"
            } finally {
                _isLoading.value = false
            }
        }
    }

}
