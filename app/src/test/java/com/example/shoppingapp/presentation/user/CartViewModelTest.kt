package com.example.shoppingapp.presentation.user


import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.model.CartItem
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.usecase.productUseCases.AddProductToCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.CartUseCases
import com.example.shoppingapp.domain.usecase.productUseCases.ClearCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCartItemsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.PlaceOrderUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.RemoveProductFromCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.UpdateProductQuantityUseCase
import com.example.shoppingapp.presentation.user.CartViewModel
import com.example.shoppingapp.utils.SharedPreferencesManager
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config
import kotlin.test.Test


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CartViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var cartViewModel: CartViewModel

    // Mocks for dependencies
    private val cartUseCases: CartUseCases = mock()
    private val placeOrderUseCase: PlaceOrderUseCase = mock()

    private val getCartItemsUseCase: GetCartItemsUseCase = mock()
    private val addProductToCartUseCase: AddProductToCartUseCase = mock()
    private val updateProductQuantityUseCase: UpdateProductQuantityUseCase = mock()
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase = mock()
    private val clearCartUseCase: ClearCartUseCase = mock()
    private val sharedPreferencesManager: SharedPreferencesManager = mock()

    @Before
    fun setup() {
        // Start Koin in the test environment
        startKoin {
            modules(testModule()) // Provide your test module here
        }

        // Set the main dispatcher to the test dispatcher before running tests
        Dispatchers.setMain(testDispatcher)

        // Mock CartUseCases to return mock use case instances
        Mockito.`when`(cartUseCases.getCartItemsUseCase).thenReturn(getCartItemsUseCase)
        Mockito.`when`(cartUseCases.addProductToCartUseCase).thenReturn(addProductToCartUseCase)
        Mockito.`when`(cartUseCases.updateProductQuantityUseCase)
            .thenReturn(updateProductQuantityUseCase)
        Mockito.`when`(cartUseCases.removeProductFromCartUseCase)
            .thenReturn(removeProductFromCartUseCase)
        Mockito.`when`(cartUseCases.clearCartUseCase).thenReturn(clearCartUseCase)

        // Mock GetCartItemsUseCase
        Mockito.`when`(getCartItemsUseCase.execute()).thenReturn(emptyList())

        // Initialize ViewModel with properly mocked CartUseCases
        cartViewModel = CartViewModel(cartUseCases, placeOrderUseCase)
    }

    @After
    fun tearDown() {
        // Stop Koin after the tests are finished
        stopKoin()

        // Reset the main dispatcher after the tests
        Dispatchers.resetMain()
    }

    // Mock a Koin module for testing
    private fun testModule() = module {
        // You can mock Koin dependencies here that are needed for the ViewModel
        single { cartUseCases }
        single { placeOrderUseCase }
        single { getCartItemsUseCase }
        single { addProductToCartUseCase }
        single { updateProductQuantityUseCase }
        single { removeProductFromCartUseCase }
        single { clearCartUseCase }

        single { mock<SharedPreferencesManager>().apply {
            // Mock the return value of getUserData to avoid NullPointerException
            `when`(getUserData()).thenReturn(SharedPreferencesManager.UserData(
                userId = "123", // Return a mock user ID
                userName = "Test User", // Mock user name
                userRole = "Customer" // Mock user role
            ))
        } }
    }

    @Test
    fun testAddProductToCart() = runTest {
        // Mock product and cart items
        val product = ItemsModel(
            categoryId = "1",
            title = "Product 1",
            description = "10.0",
            picUrl = "",  // Set picUrl to empty string or your desired value
            model = emptyList(),  // Empty list for model
            price = 0.0,
            rating = 0.0,
            numberInCart = 0,
            productId = 1,
            showRecomended = false
        )

        // Observe LiveData changes
        val cartItemsObserver = mock<Observer<List<CartItem>?>>()
        cartViewModel.cartItems.observeForever(cartItemsObserver)

        // Trigger add product to cart
        cartViewModel.addProductToCart(product)

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the product is added to the cart
        val expectedCartItems = listOf(CartItem(product = product, quantity = 1, productTotal = 0.0))
        verify(cartItemsObserver).onChanged(expectedCartItems)

        // Verify that the product was added to the cart in the repository
        verify(addProductToCartUseCase).execute(product)
    }

    @Test
    fun testClearCart() = runTest {
        // Mock product and cart items
        val product1 = ItemsModel(
            categoryId = "1",
            title = "Product 1",
            description = "10.0",
            picUrl = "",
            model = emptyList(),
            price = 10.0,
            rating = 0.0,
            numberInCart = 1,
            productId = 1,
            showRecomended = false
        )

        val product2 = ItemsModel(
            categoryId = "2",
            title = "Product 2",
            description = "20.0",
            picUrl = "",
            model = emptyList(),
            price = 20.0,
            rating = 0.0,
            numberInCart = 2,
            productId = 2,
            showRecomended = false
        )

        val initialCartItems = listOf(
            CartItem(product = product1, quantity = 1, productTotal = 10.0),
            CartItem(product = product2, quantity = 2, productTotal = 40.0)
        )

        // Mock getCartItemsUseCase to return initial cart items
        Mockito.`when`(getCartItemsUseCase.execute()).thenReturn(initialCartItems)

        // Observe LiveData changes
        val cartItemsObserver = mock<Observer<List<CartItem>?>>()
        cartViewModel.cartItems.observeForever(cartItemsObserver)

        // Trigger clear cart
        cartViewModel.clearCart()

        // Advance coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the cart is now empty
        val expectedCartItems = emptyList<CartItem>()
        verify(cartItemsObserver).onChanged(expectedCartItems)

        // Verify that the clearCartUseCase was called
        verify(clearCartUseCase).execute()
    }

    @Test
    fun testUpdateProductQuantity() = runTest {
        // Setup product and initial cart state
        val product = ItemsModel(productId = 1, title = "Product 1", price = 10.0)
        val initialCartItems = listOf(CartItem(product = product, quantity = 1, productTotal = 10.0))

        // Mock the initial cart items to return when queried
        Mockito.`when`(getCartItemsUseCase.execute()).thenReturn(initialCartItems)

        // Observer for the cart items LiveData
        val updatedCartItemsObserver = mock<Observer<List<CartItem>?>>()
        cartViewModel.cartItems.observeForever(updatedCartItemsObserver)

        // Collect LiveData emissions
        val actualCartItems = mutableListOf<List<CartItem>?>()
        cartViewModel.cartItems.observeForever { items -> actualCartItems.add(items) }

        // Simulate adding a product to the cart
        cartViewModel.addProductToCart(product)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the initial state after product is added
        assertTrue(actualCartItems.contains(listOf(CartItem(product = product, quantity = 1, productTotal = 10.0))))

        // Now update the product quantity
        cartViewModel.updateProductQuantity(productId = product.productId, newQuantity = 3)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the productTotal was updated correctly
        val expectedUpdatedCartItems = listOf(CartItem(product = product, quantity = 3, productTotal = 30.0))

        val latestCartItems = actualCartItems.lastOrNull()

        assertNotNull(latestCartItems)
        assertTrue(latestCartItems == expectedUpdatedCartItems)
    }

    @Test
    fun testPlaceOrderWhenCartIsEmpty() = runTest {
        // Mock empty cart
        Mockito.`when`(getCartItemsUseCase.execute()).thenReturn(emptyList())

        val orderStatusObserver = mock<Observer<String>>()
        cartViewModel.orderStatus.observeForever(orderStatusObserver)

        cartViewModel.placeOrder()

        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the cart is empty
        verify(orderStatusObserver).onChanged("Your cart is empty!")
    }

    @Test
    fun testRemoveProductFromCart() = runTest {
        // Setup product and initial cart state
        val product = ItemsModel(productId = 1, title = "Product 1", price = 10.0)
        val cartItem = CartItem(product = product, quantity = 1)

        // Mock current cart items
        val currentCartItems = listOf(cartItem)

        // Mock the repository to return the current cart items
        Mockito.`when`(getCartItemsUseCase.execute()).thenReturn(currentCartItems)

        // Create a mock observer to observe the cart items LiveData
        val cartItemsObserver = mock<Observer<List<CartItem>?>>()

        // Observe the cartItems LiveData
        cartViewModel.cartItems.observeForever(cartItemsObserver)

        // Trigger the remove product action
        cartViewModel.removeProductFromCart(productId = 1)

        // Ensure the test dispatcher completes all coroutine work
        testDispatcher.scheduler.advanceUntilIdle()

        // Allow time for LiveData updates to complete
        delay(100)  // Wait for any side-effects

        // Verify the observer was called exactly once with the updated cart (empty cart)
        val expectedCartItems = emptyList<CartItem>()
        verify(cartItemsObserver, times(1)).onChanged(expectedCartItems)

        // Verify that the removeProductFromCartUseCase was executed only once with the correct productId
        verify(removeProductFromCartUseCase, times(1)).execute(productId = 1)

        // Clean up observer to avoid memory leaks or unexpected test results
        cartViewModel.cartItems.removeObserver(cartItemsObserver)
    }

}
