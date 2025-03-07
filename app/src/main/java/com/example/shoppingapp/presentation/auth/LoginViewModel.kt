package com.example.shoppingapp.presentation.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.auth.AuthRepositoryImpl
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.domain.usecase.UserUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import com.example.shoppingapp.utils.SharedPreferencesManager.isLoggedIn
import kotlinx.coroutines.launch
import kotlin.Result
import java.io.IOException

/*
   ViewModel will maintain state and other validation logic.
 ViewModel handle login attempts, returning results like,
  validation errors or successful login states.

 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userUseCase: UserUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager


) : ViewModel() {
    // LiveData to hold login status (loading, success, failure)
    private val _loginStatus = MutableLiveData<Result<String>>()
    val loginStatus: LiveData<Result<String>> get() = _loginStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    private val _userRole = MutableLiveData<String?>()
    val userRole: LiveData<String?> get() = _userRole

    private val _userName = MutableLiveData<String?>()
    val userName : LiveData<String?> get() = _userName

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val showDialog = MutableLiveData(false)

    // Function to handle login action
    fun login(email: String, password: String) {
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    loginUseCase.invoke(email, password)
                    getUserNameAndRole()
                    _isLoggedIn.value = true // Set to true if login is successful
                    _isLoading.value = false
                    Log.e("Login Status", _isLoggedIn.value.toString() )
                } catch (exception: Exception) {
                    _isLoggedIn.value = false
                    _error.value = exception.message ?: "Login Failed"
                    _isLoading.value = false
                }

        }
    }

    private fun getUserNameAndRole() {
        viewModelScope.launch {
            val result = userUseCase.execute()

            result.onSuccess {
                _userName.value = it.toString()
                val (name, role) = it
                // Assign to LiveData
                _userName.value = name
                _userRole.value = role

                saveUserDetailsToSharedPreferences(name, role)
                Log.e("userNAME 73", name.toString() )
                Log.e("userNAME 73", role.toString() )
               // Log.e("userNAME 73", _userName.value!!.sec().toString() )
            }.onFailure {
                _error.value = it.message ?: "An error occurred"
            }
        }
    }

    private fun saveUserDetailsToSharedPreferences(userName: String?, userRole: String?) {

       /* if (userName != null && userRole != null) {
            SharedPreferencesManager.saveUserData( userName, userRole)
        }*/
        userName?.let { name ->
            userRole?.let { role ->
                sharedPreferencesManager.saveUserData(name, role)
            }
        }

        val isLoggedIn = sharedPreferencesManager.isLoggedIn() // Check if user is logged in
        val userRole = sharedPreferencesManager.getUserRole() // Get user role
        Log.e("Login ","status"+isLoggedIn)
        Log.e("Login","userRole"+userRole)
    }

    fun getUserDetailsFromSharedPreferences(): Pair<String?, String?> {
        val userName = sharedPreferencesManager.getUserName()
        val userRole = sharedPreferencesManager.getUserRole()
        return Pair(userName, userRole)
    }





    // Function to handle logout
    fun logout() {
        viewModelScope.launch {
            SharedPreferencesManager.clearUserData()
            _isLoggedIn.value = false
        }
    }


}



