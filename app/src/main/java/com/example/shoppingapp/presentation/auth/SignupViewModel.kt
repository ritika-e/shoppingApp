package com.example.shoppingapp.presentation.auth


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.Result

open class SignupViewModel(private val signUpUseCase: SignUpUseCase,
                           private val sharedPreferencesManager: SharedPreferencesManager): ViewModel() {
    // State for form fields
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var role by mutableStateOf("customer") // Default to "customer"

    // Validation error messages
    var errorMessage = MutableLiveData <String?>("")
    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading


    // LiveData to track if signup is successful
    private val _isSignedUp = MutableLiveData(false)
    val isSignedUp: MutableLiveData<Boolean> get() = _isSignedUp

    // Change to Result<String>? to hold the result (success/failure)
    private val _signUpStatus = MutableLiveData<Result<String>?>()
    val signUpStatus: LiveData<Result<String>?> get() = _signUpStatus


    open fun signUp(name: String, email: String, password: String, role: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = signUpUseCase(name,email, password, role)
            _signUpStatus.value = result  // Assign the Result from use case

            if (result.isSuccess) {
                // Save user data in SharedPreferences after successful sign-up
                val userId = result.getOrNull() ?: return@launch
                sharedPreferencesManager.saveUserData(userId, name ?: "", role )
                _isSignedUp.value = true
                _isLoading.value = false
            }else{
                _isLoading.value = false
            }
        }
    }
}