package com.example.shoppingapp.presentation.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.shoppingapp.domain.usecase.admin.GetTotalAcceptedOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.GetTotalDeliverdOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.GetTotalOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.GetTotalPendingOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.GetTotalRejectedOrdersUseCase


class AdminDashboardViewModel(
    private val getTotalPendingOrdersUseCase: GetTotalPendingOrdersUseCase,
   private val getTotalAcceptedOrdersUseCase: GetTotalAcceptedOrdersUseCase,
    private val getTotalDeliverdOrdersUseCase: GetTotalDeliverdOrdersUseCase,
    private val getTotalOrdersUseCase: GetTotalOrdersUseCase,
    private val getTotalRejectedOrdersUseCase: GetTotalRejectedOrdersUseCase
) : ViewModel() {


    val totalOrders = liveData {
            val result = getTotalOrdersUseCase.execute()
            Log.d("AdminDashboardViewModel", "Total Orders: $result")  // Log the result
            emit(result)  // Emit the result to be observed by the UI
    }

    // Total Pending Orders
    val totalPendingOrders = liveData {
        val result = getTotalPendingOrdersUseCase.execute()  // Get from UseCase
        Log.d("AdminDashboardViewModel", "Total Pending Orders fetched from use case: $result")  // Log the result
        emit(result)  // Emit to UI
    }

    // Total Delivered Orders
   val totalDeliverdOrders = liveData {
        try {
            val result = getTotalDeliverdOrdersUseCase.execute()
            Log.d("AdminDashboardViewModel", "Total Delivered Orders: $result")  // Log the result
            emit(result)  // Emit the result to be observed by the UI
        } catch (e: Exception) {
            Log.e("AdminDashboardViewModel", "Error fetching total delivered orders: ${e.message}")
            emit(0)  // Default to 0 in case of error
        }
    }

    // Total Accepted Orders
    val totalAcceptedOrders = liveData {
        try {
            val result = getTotalAcceptedOrdersUseCase.execute()
            Log.d("AdminDashboardViewModel", "Total Accepted Orders: $result")  // Log the result
            emit(result)  // Emit the result to be observed by the UI
        } catch (e: Exception) {
            Log.e("AdminDashboardViewModel", "Error fetching total accepted orders: ${e.message}")
            emit(0)  // Default to 0 in case of error
        }
    }

    // Total Rejected Orders
    val totalRejectedOrders = liveData {
        try {
            val result = getTotalRejectedOrdersUseCase.execute()
            Log.d("AdminDashboardViewModel", "Total Rejected Orders: $result")  // Log the result
            emit(result)  // Emit the result to be observed by the UI
        } catch (e: Exception) {
            Log.e("AdminDashboardViewModel", "Error fetching total rejected orders: ${e.message}")
            emit(0)  // Default to 0 in case of error
        }
    }
}