package com.example.shoppingapp.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.auth.AuthService
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import kotlinx.coroutines.launch

open class ForgetPasswordViewModel(private val resetPasswordUseCase: ResetPasswordUseCase):ViewModel() {
    val _resetResult = MutableLiveData<Boolean>()
    val resetResult: LiveData<Boolean> get() = _resetResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Handle the password reset request
    open   fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                val result = resetPasswordUseCase.execute(email)
                Log.d("ViewModel", "Reset password called for: $email")
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