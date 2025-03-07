package com.example.shoppingapp.presentation.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.auth.AuthService
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private val resetPasswordUseCase: ResetPasswordUseCase):ViewModel() {

     // LiveData for tracking the password reset state
     var resetPasswordState = MutableLiveData<ResetPasswordState>()

    private var isSuccessHandled = false
    fun sendPasswordResetEmail(email: String) {
        if (email.isBlank()) {
            resetPasswordState.postValue(ResetPasswordState.Error("Email cannot be empty"))
            return
        }
        resetPasswordState.postValue(ResetPasswordState.Loading)
        viewModelScope.launch {
           // var isSuccessHandled = false
            try {
                 resetPasswordUseCase.execute(email)
                if (!isSuccessHandled){
                    resetPasswordState.postValue(ResetPasswordState.Success("Password reset email sent successfully"))
                    isSuccessHandled = true
                }
                //resetPasswordState.postValue(ResetPasswordState.Success("Password reset email sent successfully"))
             } catch (e: Exception) {

                resetPasswordState.postValue(ResetPasswordState.Error("Error: ${e.message}"))


            }
        }
    }


    sealed class ResetPasswordState {
        object Loading : ResetPasswordState()
        data class Success(val message: String) : ResetPasswordState()
        data class Error(val message: String) : ResetPasswordState()
    }
}