package com.example.shoppingapp.presentation.admin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.usecase.admin.DeleteCustomerUseCase
import com.example.shoppingapp.domain.usecase.admin.GetCustomerByIdUseCase
import com.example.shoppingapp.domain.usecase.admin.GetCustomerOrdersUseCase
import com.example.shoppingapp.domain.usecase.admin.GetCustomersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.timeout
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CustomerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var customerViewModel: CustomerViewModel

    private val getCustomersUseCase: GetCustomersUseCase = mock()
    private val getCustomerByIdUseCase: GetCustomerByIdUseCase = mock()
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase = mock()
    private val deleteCustomerUseCase: DeleteCustomerUseCase = mock()

    private val testDispatcher = UnconfinedTestDispatcher() // Use TestCoroutineDispatcher instead

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        customerViewModel = CustomerViewModel(
            getCustomerOrdersUseCase,
            getCustomersUseCase,
            getCustomerByIdUseCase,
            deleteCustomerUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
       // testDispatcher.cleanupTestCoroutines() // Ensure the test dispatcher is cleaned up
    }

    @Test
    fun testGetCustomersSuccess() = runTest {
        val mockCustomers = listOf(Customer("1", "John Doe"), Customer("2", "Jane Smith"))
        whenever(getCustomersUseCase.execute()).thenReturn(Result.success(mockCustomers))

        // Observer to observe LiveData
        val observer = mock<Observer<List<Customer>>>()
        customerViewModel.customers.observeForever(observer)

        // Call the function
        customerViewModel.getCustomers()

        // Verify that the observer was notified with the mock data
        verify(observer).onChanged(mockCustomers)
        assertEquals(mockCustomers, customerViewModel.customers.value)

        // Clean up the observer
        customerViewModel.customers.removeObserver(observer)
    }

    @Test
    fun testGetCustomersFailure() = runTest {
        whenever(getCustomersUseCase.execute()).thenReturn(Result.failure(Exception("Error fetching customers")))

        val observer = mock<Observer<List<Customer>>>()
        customerViewModel.customers.observeForever(observer)

        customerViewModel.getCustomers()

        verify(observer).onChanged(emptyList()) // Empty list when failure

        // Clean up the observer
        customerViewModel.customers.removeObserver(observer)
    }

    @Test
    fun testGetCustomerByIdSuccess() = runTest {
        val customerId = "1"
        val mockCustomer = Customer(customerId, "John Doe")
        whenever(getCustomerByIdUseCase.execute(customerId)).thenReturn(Result.success(mockCustomer))

        val observer = mock<Observer<Customer?>>()
        customerViewModel.customer.observeForever(observer)

        customerViewModel.getCustomerById(customerId)

        verify(observer).onChanged(mockCustomer)

        // Clean up the observer
        customerViewModel.customer.removeObserver(observer)
    }

    @Test
    fun testGetCustomerByIdFailure() = runTest {
        val customerId = "1"
        whenever(getCustomerByIdUseCase.execute(customerId)).thenReturn(Result.failure(Exception("Customer not found")))

        val observer = mock<Observer<Customer?>>()
        customerViewModel.customer.observeForever(observer)

        customerViewModel.getCustomerById(customerId)

        verify(observer).onChanged(null) // Customer not found

        // Clean up the observer
        customerViewModel.customer.removeObserver(observer)
    }

    @Test
    fun testGetOrdersByCustomerSuccess() = runTest {
        val customerId = "1"
        val mockOrders = listOf(
            Order(
                orderId = "1",
                cartItems = listOf(
                    CartItem(
                        product = ItemsModel(
                            categoryId = "cat1",
                            title = "Product 1",
                            description = "Description of product 1",
                            price = 25.0,
                            productId = 101,
                            numberInCart = 2
                        ),
                        quantity = 2,
                        productTotal = 25.0 * 2 // product price * quantity
                    )
                ),
                totalAmount = 50.0, // totalAmount of the order
                productTotal = 50.0, // productTotal of the order
                status = "Pending",
                orderDate = "2025-03-30",
                userId = customerId // assuming customerId is defined earlier in the test
            ),
            Order(
                orderId = "2",
                cartItems = listOf(
                    CartItem(
                        product = ItemsModel(
                            categoryId = "cat2",
                            title = "Product 2",
                            description = "Description of product 2",
                            price = 38.5,
                            productId = 102,
                            numberInCart = 1
                        ),
                        quantity = 1,
                        productTotal = 38.5 * 1
                    )
                ),
                totalAmount = 38.5,
                productTotal = 38.5,
                status = "Completed",
                orderDate = "2025-03-29",
                userId = customerId
            )
        )
        whenever(getCustomerOrdersUseCase.execute(customerId)).thenReturn(Result.success(mockOrders))

        val observer = mock<Observer<List<Order>>>()
        customerViewModel.orders.observeForever(observer)

        customerViewModel.getOrdersByCustomer(customerId)

        verify(observer).onChanged(mockOrders)

        // Clean up the observer
        customerViewModel.orders.removeObserver(observer)
    }

    @Test
    fun testGetOrdersByCustomerFailure() = runTest {
        val customerId = "1"
        whenever(getCustomerOrdersUseCase.execute(customerId)).thenReturn(Result.failure(Exception("Orders not found")))

        val observer = mock<Observer<List<Order>>>()
        customerViewModel.orders.observeForever(observer)

        customerViewModel.getOrdersByCustomer(customerId)

        advanceUntilIdle() // This will ensure that all coroutines are finished running
      //  verify(observer).onChanged(emptyList()) // Empty list on failure
        delay(50)
        verify(observer, timeout(1000)).onChanged(emptyList()) // Timeout after 1 second

        // Clean up the observer
        customerViewModel.orders.removeObserver(observer)
    }


}
