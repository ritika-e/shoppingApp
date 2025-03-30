package com.example.shoppingapp.presentation.admin

import android.os.Looper
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.usecase.admin.FetchOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.UpdateOrderStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE) // Disable AndroidManifest to use Robolectric in unit test
@ExperimentalCoroutinesApi
class AdminOrderViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var adminOrderViewModel: AdminOrderViewModel

    // Mocks for the use cases
    private val fetchOrdersUseCase: FetchOrdersUseCase = mock()
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase = mock()

    @Before
    fun setup() {
        // Set the main dispatcher to the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel
        adminOrderViewModel = AdminOrderViewModel(fetchOrdersUseCase, updateOrderStatusUseCase)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the tests
        Dispatchers.resetMain()
    }

    // Test: Fetch Orders - Success
    @Test
    fun testFetchOrders_Success() = runTest {
        // Prepare mock data
        val order1 = Order(orderId = "1", status = "Pending", totalAmount = 100.0)
        val order2 = Order(orderId = "2", status = "Completed", totalAmount = 200.0)
        val ordersList = listOf(order1, order2)

        // Mock FetchOrdersUseCase to return success
        whenever(fetchOrdersUseCase.execute()).thenReturn(Result.success(ordersList))

        // Observer for LiveData
        val ordersObserver = mock<Observer<List<Order>>>()
        adminOrderViewModel.orderHistory.observeForever(ordersObserver)

        // Fetch orders
        adminOrderViewModel.fetchOrders()

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the LiveData was updated with orders
        verify(ordersObserver).onChanged(ordersList)

        // Assert that orderHistory contains the expected values
        assertEquals(ordersList, adminOrderViewModel.orderHistory.value)
    }

    // Test: Fetch Orders - Failure
    @Test
    fun testFetchOrders_Failure() = runTest {
        // Mock FetchOrdersUseCase to return failure
        whenever(fetchOrdersUseCase.execute()).thenReturn(Result.failure(Exception("Failed to fetch orders")))

        // Observer for LiveData
        val ordersObserver = mock<Observer<List<Order>>>()
        adminOrderViewModel.orderHistory.observeForever(ordersObserver)

        // Fetch orders
        adminOrderViewModel.fetchOrders()

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the LiveData was updated with an empty list (or error state)
        verify(ordersObserver).onChanged(emptyList())

        // Assert that orderHistory is empty
        adminOrderViewModel.orderHistory.value?.let { assertTrue(it.isEmpty()) }
    }


  /*  @Test
    fun testUpdateOrderStatus_Success() = runTest {
        // Prepare mock data
        val order1 = Order(orderId = "1", status = "Pending", totalAmount = 100.0)

        // Mock UpdateOrderStatusUseCase to return success
        whenever(updateOrderStatusUseCase.execute(order1.orderId, "Completed"))
            .thenReturn(Result.success(Unit))

        // Mock fetchOrdersUseCase to return a specific result after successful update
        val updatedOrder = order1.copy(status = "Completed")
        whenever(fetchOrdersUseCase.execute()).thenReturn(Result.success(listOf(updatedOrder)))

        // Observer for LiveData
        val ordersObserver = mock<Observer<List<Order>?>>()
        adminOrderViewModel.orderHistory.observeForever(ordersObserver)

        // Call the updateOrderStatus method
        adminOrderViewModel.updateOrderStatus(order1.orderId, "Completed")

        // Advance coroutines to ensure the task is completed
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that fetchOrdersUseCase.execute() was called only once
        verify(fetchOrdersUseCase, Mockito.times(1)).execute()

        // Ensure no further interactions with fetchOrdersUseCase
        verifyNoMoreInteractions(fetchOrdersUseCase)

        // Retrieve the value from LiveData and verify that the observer was triggered
        val orderHistoryValue = adminOrderViewModel.orderHistory.value
        assertNotNull(orderHistoryValue)
        assertEquals(listOf(updatedOrder), orderHistoryValue)

        // Verify that the observer was triggered with the correct list
        verify(ordersObserver).onChanged(orderHistoryValue)
    }

*/    // Test: Update Order Status - Failure
    @Test
    fun testUpdateOrderStatus_Failure() = runTest {
        // Prepare mock data
        val order1 = Order(orderId = "1", status = "Pending", totalAmount = 100.0)

        // Mock UpdateOrderStatusUseCase to return failure
        whenever(updateOrderStatusUseCase.execute(order1.orderId, "Completed"))
            .thenReturn(Result.failure(Exception("Failed to update order status")))

        // Mock fetchOrdersUseCase to return a specific result
        whenever(fetchOrdersUseCase.execute()).thenReturn(Result.success(emptyList()))

        // Call the updateOrderStatus method (this will fail)
        adminOrderViewModel.updateOrderStatus(order1.orderId, "Completed")

        // Advance coroutines to ensure the task is completed
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that fetchOrdersUseCase.execute() was called even if update failed
        verify(fetchOrdersUseCase, Mockito.times(1)).execute()
    }


    // Test: Check if Loading Indicator is toggled correctly
   /* @Test
    fun testLoadingIndicator() = runTest {
        // Prepare mock data
        val order1 = Order(orderId = "1", status = "Pending", totalAmount = 100.0)
        val ordersList = listOf(order1)

        // Mock FetchOrdersUseCase to return success
        whenever(fetchOrdersUseCase.execute()).thenReturn(Result.success(ordersList))

        // Observer for loading state
        val loadingObserver = mock<Observer<Boolean>>()
        adminOrderViewModel.isLoading.observeForever(loadingObserver)

        // Fetch orders (start the loading)
        adminOrderViewModel.fetchOrders()

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify loading state: it should be true while fetching and false after it's done
        verify(loadingObserver, Mockito.times(1)).onChanged(true)  // Loading should be true while fetching
        verify(loadingObserver, Mockito.times(1)).onChanged(false) // Loading should be false after fetch is completed

        // Ensure no additional loading state changes
        verifyNoMoreInteractions(loadingObserver)
    }
*/
}
