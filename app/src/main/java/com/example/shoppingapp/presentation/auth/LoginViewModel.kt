package com.example.shoppingapp.presentation.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.auth.AuthRepositoryImpl
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.domain.usecase.UserUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.Result
import java.io.IOException

/*
   ViewModel will maintain state and other validation logic.
 ViewModel handle login attempts, returning results like,
  validation errors or successful login states.

 */
open class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    //  val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()

    // LiveData to hold login status
    private val _loginStatus = MutableLiveData<Result<String>?>()
    val loginStatus: LiveData<Result<String>?> = _loginStatus

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _userRole = MutableLiveData<String?>()
    val userRole: MutableLiveData<String?> get() = _userRole

    private val _logoutStatus = MutableLiveData<String?>()
    val logoutStatus: LiveData<String?> = _logoutStatus

    // Function to handle login action
    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            _loginStatus.value = result

            if (result.isSuccess) {
                val userId = result.getOrNull() ?: return@launch
                val role = loginUseCase.invoke(userId)
                val userName = loginUseCase.getUserName(userId)
                _userRole.value = role
                Log.e("Login VM 60","userId => $userId role => $role useName => $userName")
                sharedPreferencesManager.saveUserData(userId, userName ?: "", role)
                _isLoading.value = false
            } else {
                _isLoading.value = false
            }
        }
    }

    /* fun getUserRoleFromSharedPrefs(): String? {
         return sharedPreferencesManager.getUserData().userRole
     }*/

    fun fetchUserRoleFromSharedPrefs() {
        val role = sharedPreferencesManager.getUserData().userRole
        _userRole.value = role
    }


    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loginUseCase.logout()  // Firebase sign-out
                sharedPreferencesManager.clearUserData()  // Clear SharedPreferences
                _logoutStatus.postValue("Logged out")
            } catch (e: Exception) {
                _logoutStatus.postValue("Logout failed: ${e.message}")
            }
        }

    }

}



