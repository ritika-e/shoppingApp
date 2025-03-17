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

    // Handle the password reset request
    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                val result = resetPasswordUseCase.execute(email)
                _resetResult.postValue(result)
                if (!result) {
                    _error.postValue("Failed to send password reset email.")
                }
            } catch (e: Exception) {
                _error.postValue("Error occurred: ${e.message}")
            }
        }
    }
}