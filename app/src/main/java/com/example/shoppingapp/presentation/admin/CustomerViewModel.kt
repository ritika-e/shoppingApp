package com.example.shoppingapp.presentation.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.CustomerRepository
import com.example.shoppingapp.domain.usecase.admin.DeleteCustomerUseCase
import com.example.shoppingapp.domain.usecase.admin.GetCustomerByIdUseCase
import com.example.shoppingapp.domain.usecase.admin.GetCustomerOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.GetCustomersUseCase
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val getCustomersUseCase: GetCustomersUseCase,
    private val getCustomerByIdUseCase: GetCustomerByIdUseCase,
    private val deleteCustomerUseCase: DeleteCustomerUseCase
) : ViewModel() {

    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> get() = _customer

    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getCustomers() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = getCustomersUseCase.execute()
            if (result.isSuccess) {
                _customers.value = result.getOrNull() ?: emptyList() // Update the list of customers
            }else {
                _customers.value = emptyList() // If no customers found, set an empty list
            }
            _isLoading.value = false
        }
    }
    // Get customer by ID
    fun getCustomerById(customerId: String) {
        Log.e("customerId in VM : "," $customerId")
        _isLoading.value = true
        viewModelScope.launch {
            val result = getCustomerByIdUseCase.execute(customerId)
            if (result.isSuccess) {
                _customer.value = result.getOrNull() // Setting customer data
            } else {
                // Handle failure or null customer case
                _customer.value = null
            }
            _isLoading.value = false
        }
    }

    // Get orders by customer ID
    /*fun getOrdersByCustomer(customerId: String) {
        Log.e("customerId in VM getORders : "," $customerId")

        _isLoading.value = true
       Log.d("ViewModel", "Loading started")
        viewModelScope.launch {
            val result = getCustomerOrdersUseCase.execute(customerId)
            if (result.isSuccess) {
                _orders.value = result.getOrNull() ?: emptyList()
            }
            _isLoading.value = false
            Log.d("ViewModel", "Loading finished")
        }
    }*/

    fun getOrdersByCustomer(customerId: String) {
        viewModelScope.launch {
            try {
                val result = getCustomerOrdersUseCase.execute(customerId)
                result.onSuccess {
                    _orders.value = it
                }.onFailure {
                    _orders.value = emptyList()  // Set empty list on failure
                }
            } catch (e: Exception) {
                _orders.value = emptyList()  // Set empty list on exception
            }
        }
    }


    // Delete customer by ID
   /* fun deleteCustomer(customerId: String) {
        viewModelScope.launch {
            val result = deleteCustomerUseCase.execute(customerId)
            if (result.isSuccess) {
                // Refresh the customer list after deleting
                getCustomersUseCase.execute()
            }
        }
    }*/
}
