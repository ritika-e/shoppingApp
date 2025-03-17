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
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()
    // LiveData to hold login status (loading, success, failure)
    private val _loginStatus = MutableLiveData<Result<String>?>()
    val loginStatus: LiveData<Result<String>?> = _loginStatus

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

    private val _logoutStatus = MutableLiveData<String?>()
    val logoutStatus: LiveData<String?> = _logoutStatus

    val user = liveData(Dispatchers.IO) {
        val currentUser = loginUseCase.getCurrentUser()
        emit(currentUser)
    }

    // Function to handle login action

    fun login(email:String, password:String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            _loginStatus.value = result  // Assign the Result from use case
            if (result.isSuccess) {
                val userId = result.getOrNull() ?: return@launch // If failed, exit the coroutine early
                val role = loginUseCase.invoke(userId)
                val useName = loginUseCase.getUserName(userId)
                Log.e("USER DATA"," $userId $role $useName")
                sharedPreferencesManager.saveUserData(userId, useName ?: "", role )
                _isLoggedIn.value = true
                _isLoading.value = false
            }else{
                val exception = result.exceptionOrNull()
                Log.e("LoginError", "Login failed: ${exception?.message}")
                _isLoading.value = false
            }

        }
    }

   /*
   WORKING FINE
   fun login(email: String, password: String) = viewModelScope.launch (Dispatchers.IO) {
       val user = try {
           loginUseCase.login(email, password)
       } catch (e: Exception) {
           null
       }

       if (user != null) {
         //  val role = getUserRoleFromFirestore(user.uid)
           val role = loginUseCase.invoke(user.uid)
           // Save user data in SharedPreferences on successful login
           sharedPreferencesManager.saveUserData(user.uid, user.displayName ?: "", role )
           _loginStatus.postValue("Login successful")
       } else {
           _loginStatus.postValue("Login failed")
       }
   }*/

    private fun getUserNameAndRole() {
        viewModelScope.launch {
            val result = userUseCase.execute()

            result.onSuccess {
                _userName.value = it.toString()
                val (name, role) = it
                // Assign to LiveData
                _userName.value = name
                _userRole.value = role

             //   saveUserDetailsToSharedPreferences(name, role)
                Log.e("userNAME 73", name.toString() )
                Log.e("userNAME 73", role.toString() )
               // Log.e("userNAME 73", _userName.value!!.sec().toString() )
            }.onFailure {
                _error.value = it.message ?: "An error occurred"
            }
        }
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



