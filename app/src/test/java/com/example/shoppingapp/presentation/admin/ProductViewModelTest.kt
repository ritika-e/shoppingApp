package com.example.shoppingapp.presentation.admin

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shoppingapp.data.admin.ProductManagementRepositoryImpl
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.repositories.ProductManagementRespository
import com.example.shoppingapp.domain.usecase.admin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.timeout
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()  // Ensures LiveData works on the main thread

    private lateinit var viewModel: ProductViewModel
    private val addProductUseCase: AddProductUseCase = mock()
    private val updateProductUseCase: UpdateProductUseCase = mock()
    private val deleteProductUseCase: DeleteProductUseCase = mock()
    private val getAllProductsUseCase: GetAllProductsUseCase = mock()
    private val getProductByIdUseCase: GetProductByIdUseCase = mock()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val observer: Observer<Boolean> = mock()

    @Before
    fun setUp() {
       Dispatchers.setMain(testDispatcher)


        // Initialize ViewModel with mocked UseCases
        viewModel = ProductViewModel(
            addProductUseCase,
            updateProductUseCase,
            deleteProductUseCase,
            getAllProductsUseCase,
            getProductByIdUseCase
        )

        viewModel.isLoading.observeForever(observer)
    }

    @After
    fun tearDown() {
         Dispatchers.resetMain()
    }

    @Test
    fun `getProducts() should fetch products and update productsList LiveData`() = runTest {
        // Given
        val mockProductList = listOf(ProductList(1, "Product 1", "Description", 8.0, true, "1", "https://image.url"))

        // Mock the ProductRepository and GetAllProductsUseCase
        val mockProductRepository: ProductManagementRespository = mock()
        val mockGetAllProductsUseCase = GetAllProductsUseCase(mockProductRepository)

        // Define behavior of mock repository
        whenever(mockProductRepository.getProducts()).thenReturn(mockProductList)

        // Create the ViewModel with the mocked use cases
        viewModel = ProductViewModel(
            addProductUseCase,
            updateProductUseCase,
            deleteProductUseCase,
            mockGetAllProductsUseCase,
            getProductByIdUseCase
        )

        // Observe LiveData
        val observer = mock<Observer<List<ProductList>>>()
        viewModel.productsList.observeForever(observer)

        // When
        viewModel.getProducts()

        // Then
        verify(observer).onChanged(mockProductList)  // Check if the LiveData updates
        assert(viewModel.productsList.value == mockProductList)  // Assert if the LiveData holds the correct value
    }

    @Test
    fun `getProductById() should fetch the product by ID and update product LiveData`() = runTest {
        // Given
        val mockProduct = ProductList(1, "Product 1", "Description",
            8.0, true, "1", "https://image.url")

        // Mock the ProductManagementRespository and GetProductByIdUseCase
        val mockProductRepository: ProductManagementRespository = mock()
        val mockGetProductByIdUseCase = GetProductByIdUseCase(mockProductRepository)

        // Define the behavior of the mock repository to return the mock product
        `when`(mockProductRepository.getProductById(1)).thenReturn(mockProduct)

        // Initialize ViewModel with the mocked GetProductByIdUseCase
        viewModel = ProductViewModel(
            addProductUseCase,
            updateProductUseCase,
            deleteProductUseCase,
            getAllProductsUseCase,
            mockGetProductByIdUseCase // Pass the mocked use case here
        )

        // Observe LiveData
        val observer = mock<Observer<ProductList?>>()
        viewModel.product.observeForever(observer)

        // When
        viewModel.getProductById(1)

        // Then
        verify(observer).onChanged(mockProduct)  // Check if LiveData updates with the mock product
        assert(viewModel.product.value == mockProduct)  // Assert that the LiveData contains the correct product
    }

    @Test
    fun `addProductWithImage() should call addProductUseCase and update productUploadStatus`() = runTest {
        // Given
        val mockProduct = ProductList(1, "Product 1", "Description",
            8.0, true, "1", "https://image.url")
        val mockUri = Uri.parse("https://image.url")

        // Use mockUri for the product's picUrl
        val onProgress: (Float) -> Unit = { progress -> }
        val onComplete: (Boolean) -> Unit = { success -> }

        // When
        viewModel.addProductWithImage(mockProduct, mockUri, onProgress, onComplete)

        // Then
        verify(addProductUseCase).execute(
            eq(mockProduct.copy(picUrl = mockUri.toString())),  // Match product with updated picUrl
            any(),  // Match the onProgress function
            any()   // Match the onComplete function
        )
    }

    @Test
    fun `updateProduct() should call updateProductUseCase with correct parameters`() = runTest {
        // Given
        val mockProduct = ProductList(1, "Product 1", "Description",
            8.0, true, "1", "https://image.url")

        // Mock the ProductManagementRepositoryImpl
        val mockProductRepository: ProductManagementRespository = mock()  // Mock repository

        // Create the UseCase and inject the mock repository
        val updateProductUseCase = UpdateProductUseCase(mockProductRepository)

        // Mock the behavior of the repository method
        `when`(mockProductRepository.updateProduct(any(), any())).thenReturn(Unit)

        // Initialize the ViewModel with the mocked UseCase
        viewModel = ProductViewModel(
            addProductUseCase,
            updateProductUseCase,  // Pass the mocked updateProductUseCase
            deleteProductUseCase,
            getAllProductsUseCase,
            getProductByIdUseCase
        )

        // When
        viewModel.updateProduct(1, mockProduct)

        // Then
        // Verify that the updateProduct method on the mock repository was called with correct parameters
        verify(mockProductRepository).updateProduct(1, mockProduct)
    }

    @Test
    fun `deleteProduct() should call deleteProductUseCase with correct productId`() = runTest {
        // Given: productId to delete
        val productId = 1

        // Create a mock repository
        val mockProductRepository: ProductManagementRespository = mock()

        // Now mock the deleteProductUseCase with the mock repository
        val mockDeleteProductUseCase = DeleteProductUseCase(mockProductRepository)

        // Mock the repository's deleteProduct method to do nothing (adjust as needed)
        whenever(mockProductRepository.deleteProduct(eq(productId))).thenReturn(Unit)

        // Create the ViewModel with the mocked use case
        viewModel = ProductViewModel(
            addProductUseCase,
            updateProductUseCase,
            mockDeleteProductUseCase, // Pass the mocked deleteProductUseCase
            getAllProductsUseCase,
            getProductByIdUseCase
        )

        // When: Calling deleteProduct method
        viewModel.deleteProduct(productId)

        // Then: Verify that deleteProductUseCase.execute() was called with the correct productId
        verify(mockProductRepository).deleteProduct(eq(productId))  // Verifying the method is called correctly
    }


   /* @Test
    fun `getProducts() should set isLoading to true and false`() = runTest {
        // Arrange: mock the repository behavior
        val mockProduct = ProductList(1, "Product 1", "Description", 8.0, true, "1", "https://image.url")

        // Mock the ProductManagementRepositoryImpl
        val mockProductRepository: ProductManagementRespository = mock()

        // Mock the behavior of the repository method
        `when`(mockProductRepository.getProducts()).thenReturn(listOf(mockProduct))

        // Initialize the ViewModel with the mocked UseCase
        viewModel = ProductViewModel(
            addProductUseCase,
            updateProductUseCase,  // Pass the mocked updateProductUseCase
            deleteProductUseCase,
            getAllProductsUseCase,
            getProductByIdUseCase
        )

        // Mock the observer
        val observer: Observer<Boolean> = mock()
        viewModel.isLoading.observeForever(observer)

        // Act: Trigger the getProducts method
        viewModel.getProducts()

        // Assert: Verify isLoading is true
        verify(observer, timeout(1000).times(1)).onChanged(true)

        // Simulate a delay or completion of the background task
        delay(500)  // Adjust the delay to simulate asynchronous work

        // Assert: Verify isLoading is false after the task completion
        verify(observer, timeout(1000).times(1)).onChanged(false)

        // Cleanup: Remove the observer
        viewModel.isLoading.removeObserver(observer)
    }
*/
}
