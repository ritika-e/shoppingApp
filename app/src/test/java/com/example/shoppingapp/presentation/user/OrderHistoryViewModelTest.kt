import android.annotation.SuppressLint
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.usecase.productUseCases.GetOrderByIdUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetOrderHistoryUseCase
import com.example.shoppingapp.presentation.user.OrderHistoryViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class OrderHistoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // Ensures LiveData works synchronously

    private lateinit var orderHistoryViewModel: OrderHistoryViewModel
    private lateinit var getOrderHistoryUseCase: GetOrderHistoryUseCase
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase

    private val testDispatcher = StandardTestDispatcher()

    @SuppressLint("CheckResult")
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Use Mockk to mock static methods
        mockkStatic(Log::class)

        getOrderHistoryUseCase = mockk()
        getOrderByIdUseCase = mockk()
        orderHistoryViewModel = OrderHistoryViewModel(getOrderHistoryUseCase, getOrderByIdUseCase)

        Dispatchers.setMain(testDispatcher) // Set test dispatcher to ensure coroutines run synchronously
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchOrders success`() = runTest {
        // Prepare test data
        val userId = "123"
        val orders = listOf(
            Order(
                orderId = "fLO4YyiKRs5urr1NKlwZ",
                cartItems = listOf(
                    CartItem(
                        product = ItemsModel(productId = 3, title = "Elegant Black Blazer", price = 38.0),
                        quantity = 1
                    )
                ),
                totalAmount = 38.0,
                status = "Pending",
                orderDate = "2025-03-27",
                userId = userId
            ),
            Order(
                orderId = "LJZeQAKqGKwMeekavtcf",
                cartItems = listOf(
                    CartItem(
                        product = ItemsModel(productId = 1, title = "Formal White Shirt", price = 20.0),
                        quantity = 1
                    )
                ),
                totalAmount = 20.0,
                status = "Accepted",
                orderDate = "2025-03-26",
                userId = userId
            )
        )

        // Mock the use case to return orders
        coEvery { getOrderHistoryUseCase.execute(userId) } returns Result.success(orders)

        // Create an observer for the LiveData
        val observer = mockk<Observer<List<Order>?>>(relaxed = true)
        orderHistoryViewModel.orderHistory.observeForever(observer)

        // Call fetchOrders
        orderHistoryViewModel.fetchOrders(userId)

        // Advance the test dispatcher to run coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Check that LiveData has been updated
        assertNotNull(orderHistoryViewModel.orderHistory.value)  // Ensure it's not null
        assertEquals(orders, orderHistoryViewModel.orderHistory.value)

        // Verify observer is notified with the expected value
        verify { observer.onChanged(orders) }

        // Ensure loading state is false after completion
        assertFalse(orderHistoryViewModel.isLoading.value == true)
    }

    @Test
    fun `test fetchOrders failure`() = runTest {
        val userId = "123"
        val errorMessage = "Failed to fetch orders"

        // Mock the use case to return a failure (Exception is thrown)
        coEvery { getOrderHistoryUseCase.execute(userId) } returns Result.failure(Exception(errorMessage))

        // Create observers for orderHistory and error LiveData
        val orderHistoryObserver = mockk<Observer<List<Order>?>>(relaxed = true)
        val errorObserver = mockk<Observer<String?>>(relaxed = true)

        // Observe the LiveData values
        orderHistoryViewModel.orderHistory.observeForever(orderHistoryObserver)
        orderHistoryViewModel.error.observeForever(errorObserver)

        // First, call fetchOrders
        orderHistoryViewModel.fetchOrders(userId)

        // Ensure loading state is true right after calling fetchOrders
        println("After calling fetchOrders: Loading = ${orderHistoryViewModel.isLoading.value}")
        assertTrue("Loading state should be true after calling fetchOrders", orderHistoryViewModel.isLoading.value == true)

        // Advance the test dispatcher to make sure the coroutines finish
        testDispatcher.scheduler.advanceUntilIdle()

        // After the operation completes, assert the loading state
        println("After fetchOrders completes: Loading = ${orderHistoryViewModel.isLoading.value}")
        assertFalse("Loading state should be false after the operation completes", orderHistoryViewModel.isLoading.value == true)

        // Verify that orderHistory was not updated (because of the failure)
        verify(exactly = 0) { orderHistoryObserver.onChanged(any()) }

        // Verify that the error LiveData was updated with the correct message
        verify { errorObserver.onChanged("Failed to fetch orders") }

        // Assert that the LiveData for orderHistory is null
        assertNull("Order history should be null after failure", orderHistoryViewModel.orderHistory.value)  // Ensure no orders are set
    }

    @Test
    fun `test fetchOrderById success`() = runTest {
        // Prepare test data
        val orderId = "1"
        val order = Order(orderId = "1", status = "Pending")

        // Mock the use case to return the order
        coEvery { getOrderByIdUseCase.execute(orderId) } returns order

        // Create observer for order LiveData
        val orderObserver = mockk<Observer<Order?>>(relaxed = true)
        orderHistoryViewModel.order.observeForever(orderObserver)

        // Create observer for loading state
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        orderHistoryViewModel.isLoading.observeForever(loadingObserver)

        // Call fetchOrderById
        orderHistoryViewModel.fetchOrderById(orderId)

        // Ensure that the dispatcher advances all coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that loading state was set to true first
        verify { loadingObserver.onChanged(true) }

        // Verify that the order LiveData has been updated with the expected value
        verify { orderObserver.onChanged(order) }

        // Verify that loading state was set to false after the fetch operation completes
        verify { loadingObserver.onChanged(false) }

        // Ensure isLoading is false after the operation
        assertFalse(orderHistoryViewModel.isLoading.value == true)
    }



    @Test
    fun `test fetchOrderById failure`() = runTest {
        val orderId = "1"
        val errorMessage = "Failed to fetch order details"

        // Mock the use case to throw an exception
        coEvery { getOrderByIdUseCase.execute(orderId) } throws Exception(errorMessage)

        // Create observer for order LiveData
        val orderObserver = mockk<Observer<Order?>>(relaxed = true)
        orderHistoryViewModel.order.observeForever(orderObserver)

        // Create observer for loading state
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        orderHistoryViewModel.isLoading.observeForever(loadingObserver)

        // Call fetchOrderById
        orderHistoryViewModel.fetchOrderById(orderId)

        // Ensure that the dispatcher advances all coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that loading state was set to true first
        verify { loadingObserver.onChanged(true) }

        // Verify that the order LiveData is updated to null (because of the failure)
        verify { orderObserver.onChanged(null) }

        // Verify that loading state was set to false after the fetch operation completes
        verify { loadingObserver.onChanged(false) }

        // Ensure isLoading is false after the operation
        assertFalse(orderHistoryViewModel.isLoading.value == true)

        // Ensure the correct error message is set
        assertEquals("Failed to load order details", orderHistoryViewModel.error.value)
    }

    @Test
    fun `test fetchOrderById invalid orderId`() = runTest {
        // Prepare test data
        val invalidOrderId = ""

        // Create observer for LiveData
        val observer = mockk<Observer<String?>>(relaxed = true)
        orderHistoryViewModel.error.observeForever(observer)

        // Call fetchOrderById with invalid ID
        orderHistoryViewModel.fetchOrderById(invalidOrderId)

        // Verify error handling
        verify { observer.onChanged("Invalid order ID") }
        assertNull(orderHistoryViewModel.order.value)
        assertFalse(orderHistoryViewModel.isLoading.value == true)
    }
}
