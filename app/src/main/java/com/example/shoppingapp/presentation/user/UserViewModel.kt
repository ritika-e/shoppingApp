package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.domain.model.User
import com.example.shoppingapp.domain.usecase.UpdateUserProfileUseCase
import com.example.shoppingapp.utils.SharedPreferencesManager
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class UserViewModel(private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _profileUpdated = MutableLiveData<Boolean>()
    val profileUpdated: LiveData<Boolean> get() = _profileUpdated

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> get() = _userProfile

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val sharedPreferencesManager: SharedPreferencesManager = getKoin().get()

    //  private val sharedPrefsHelper = SharedPrefsHelper(context)

    // Function to load the user profile from Firebase
    fun loadUserProfile() {
         val userId = sharedPreferencesManager.getUserData().userId
        if (userId != null) {
            if (userId.isNotEmpty()) {
                Log.d("UserProfile", "Loading user profile for userId: $userId")

                viewModelScope.launch {
                    // Call the UseCase to get the user profile
                    val result = updateUserProfileUseCase.getUserProfile(userId)
                    if (result.isSuccess) {
                        _userProfile.value = result.getOrNull()
                        Log.d("UserProfile", "User profile loaded successfully")
                    } else {
                        _userProfile.value = null
                        Log.d("UserProfile", "Failed to load user profile")
                    }
                }
            }
        }
    }
    // Function to update the user profile
    fun updateUserProfile(mobileNumber: String, address: String) {
        _isLoading.value = true
        val userId = sharedPreferencesManager.getUserData().userId
        if (userId != null) {
            if (userId.isNotEmpty()) {
                viewModelScope.launch {
                    val result = updateUserProfileUseCase.execute(userId, mobileNumber, address)
                    _profileUpdated.value = result.isSuccess
                    _isLoading.value = false
                }
            }
        }
    }
}
