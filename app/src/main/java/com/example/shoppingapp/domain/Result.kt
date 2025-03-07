package com.example.shoppingapp.domain

sealed class Result {
    data class Success(val data: Any?) : Result()
    data class Failure(val exception: Throwable) : Result()
}
