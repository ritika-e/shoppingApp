package com.example.shoppingapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * A helper function to get the value from LiveData synchronously for testing purposes.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    timeout: Long = 2_000, // Default timeout 2 seconds
    afterObserve: (() -> Unit)? = null
): T {
    var data: T? = null
    val observer = Observer<T> { value ->
        data = value
    }

    // Start observing LiveData
    observeForever(observer)

    // Wait until LiveData emits a value or timeout occurs
    try {
        afterObserve?.invoke()
        runBlocking {
            withContext(Dispatchers.Main) {
                // Suspend until data is received or timeout occurs
                var startTime = System.currentTimeMillis()
                while (data == null && System.currentTimeMillis() - startTime < timeout) {
                    delay(50)
                }
            }
        }
    } finally {
        removeObserver(observer)
    }

    return data ?: throw IllegalStateException("LiveData value was not set")
}
