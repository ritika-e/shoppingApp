package com.example.shoppingapp.presentation.admin

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.usecase.admin.FetchOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.UpdateOrderStatusUseCase
import kotlinx.coroutines.launch

class AdminOrderViewModel(
    private val fetchOrdersUseCase: FetchOrdersUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    private val _orderHistory = MutableLiveData<List<Order>>(emptyList())
    val orderHistory: MutableLiveData<List<Order>> get() = _orderHistory

    private val _isLoading = MutableLiveData(false)
    val isLoading: MutableLiveData<Boolean> get() = _isLoading

    init {
        fetchOrders()
    }

    // Fetch all orders
    private fun fetchOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = fetchOrdersUseCase.execute()
            if (result.isSuccess) {
                _orderHistory.value = result.getOrNull() ?: emptyList()
            } else {
                Log.e("AdminOrderViewModel", "Error fetching orders: ${result.exceptionOrNull()}")
            }
            _isLoading.value = false
        }
    }

    // Update order status (Pending -> Confirm, Cancel, Completed)
    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            val result = updateOrderStatusUseCase.execute(orderId, newStatus)
            if (result.isSuccess) {
                Log.d("AdminOrderViewModel", "Order status updated to $newStatus for Order ID: $orderId")
                fetchOrders() // Fetch updated orders after status change
            } else {
                Log.e("AdminOrderViewModel", "Error updating order status: ${result.exceptionOrNull()}")
            }
        }
    }
}
