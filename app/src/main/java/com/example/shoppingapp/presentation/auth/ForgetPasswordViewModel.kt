package com.example.shoppingapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.auth.AuthService
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private val resetPasswordUseCase: ResetPasswordUseCase):ViewModel() {

    private val _resetResult = MutableLiveData<Boolean?>()
    val resetResult: LiveData<Boolean?> get() = _resetResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Handle the password reset request
    fun resetPassword(email: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = resetPasswordUseCase.execute(email)
                _resetResult.postValue(result)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.postValue("Error occurred: ${e.message}")
                _isLoading.value = false
               // _resetResult.postValue(false) // Indicate failure
            }
        }
    }
    /*fun resetPassword(email: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = resetPasswordUseCase.execute(email)
                _resetResult.postValue(result)
                if (!result) {
                    _error.postValue("Failed to send password reset email.")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred: ${e.message}")
                _isLoading.value = false
            }
        }
    }*/
}