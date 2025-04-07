package com.example.shoppingapp.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.usecase.productUseCases.GetBannersUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoriesUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoryItemUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetProductDetailsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetRecommendedProductsUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductDetailsViewModelTest {

    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var getProductDetailsUseCase: GetProductDetailsUseCase
    private lateinit var getRecommendedProductsUseCase: GetRecommendedProductsUseCase
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var getCategoryItemUseCase: GetCategoryItemUseCase
    private lateinit var getBannersUseCase: GetBannersUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getProductDetailsUseCase = mockk()
        getRecommendedProductsUseCase = mockk()
        getCategoriesUseCase = mockk()
        getCategoryItemUseCase = mockk()
        getBannersUseCase = mockk()

        productDetailsViewModel = ProductDetailsViewModel(
            getProductDetailsUseCase,
            getRecommendedProductsUseCase,
            getCategoriesUseCase,
            getCategoryItemUseCase,
            getBannersUseCase
        )
    }

    @Test
    fun `test loadProductDetails success`() = runTest {
        val productId = 1
        val product = ItemsModel(productId = productId, title = "Product 1") // Mock product details

        // Mock the use case to return product details
        coEvery { getProductDetailsUseCase.execute(productId) } returns MutableLiveData(product)

        // Observe LiveData
        val observer = mockk<Observer<ItemsModel?>>(relaxed = true)
        productDetailsViewModel.productDetails.observeForever(observer)

        // Call the method
        productDetailsViewModel.loadProductDetails(productId)

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the product details are set
        verify { observer.onChanged(product) }
        assertEquals(product, productDetailsViewModel.productDetails.value)
    }

    @Test
    fun `test loadProductDetails failure`() = runTest {
        val productId = 1
        val errorMessage = "Product not found"

        // Mock the use case to return null
        val liveDataMock: LiveData<ItemsModel?> = MutableLiveData(null)
        coEvery { getProductDetailsUseCase.execute(productId) } returns liveDataMock

        // Observe LiveData
        val observer = mockk<Observer<ItemsModel?>>(relaxed = true)
        val errorObserver = mockk<Observer<String>>(relaxed = true)
        productDetailsViewModel.productDetails.observeForever(observer)
        productDetailsViewModel.error.observeForever(errorObserver)

        // Call the method
        productDetailsViewModel.loadProductDetails(productId)

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that the error message is set
        verify { errorObserver.onChanged("Product not found") }

        // Verify that productDetails is null
        assertNull(productDetailsViewModel.productDetails.value)
    }

    @Test
    fun `test loadRecommended success`() = runTest {
        val recommendedProducts = listOf(
            ItemsModel(productId = 1, title = "Product 1"),
            ItemsModel(productId = 2, title = "Product 2")
        )

        // Mock the use case to return recommended products
        coEvery { getRecommendedProductsUseCase.execute() } returns MutableLiveData(recommendedProducts)

        // Observe LiveData
        val observer = mockk<Observer<List<ItemsModel>>>(relaxed = true)
        productDetailsViewModel.recommended.observeForever(observer)

        // Call the method
        productDetailsViewModel.loadRecommended()

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify recommended products are set
        verify { observer.onChanged(recommendedProducts) }
        assertEquals(recommendedProducts, productDetailsViewModel.recommended.value)
    }

    @Test
    fun `test loadCategories success`() = runTest {
        val categoriesList = listOf(
            CategoryModel(id = 1, title = "Category 1"),
            CategoryModel(id = 2, title = "Category 2")
        )

        // Mock the use case to return categories
        coEvery { getCategoriesUseCase.execute() } returns MutableLiveData(categoriesList)

        // Observe LiveData
        val observer = mockk<Observer<List<CategoryModel>>>(relaxed = true)
        productDetailsViewModel.categories.observeForever(observer)

        // Call the method
        productDetailsViewModel.loadCategories()

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify categories are set
        verify { observer.onChanged(categoriesList) }
        assertEquals(categoriesList, productDetailsViewModel.categories.value)
    }

    @Test
    fun `test loadCategories failure`() = runTest {
        val errorMessage = "Failed to load categories"

        // Step 1: Mock the use case to return an empty list wrapped in LiveData
        val emptyCategoriesLiveData = MutableLiveData<List<CategoryModel>>()
        emptyCategoriesLiveData.value = null

        // Mock the use case to return the empty LiveData
        coEvery { getCategoriesUseCase.execute() } returns emptyCategoriesLiveData

        // Step 2: Prepare the observers for LiveData
        val errorObserver = mockk<Observer<String>>(relaxed = true)
        productDetailsViewModel.error.observeForever(errorObserver)

        //  val categoryObserver = mockk<Observer<List<CategoryModel>>>(relaxed = true)
        //  productDetailsViewModel.categories.observeForever(categoryObserver)

        // Step 3: Call the loadCategories method
        productDetailsViewModel.loadCategories()

        // Step 4: Post the empty list to simulate failure condition
        emptyCategoriesLiveData.postValue(emptyList()) // post empty list to LiveData

        // Step 5: Manually trigger Robolectric’s Looper to process all tasks
        //   ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Step 6: Verify the expected behavior: error message and empty categories
        verify { errorObserver.onChanged("Failed to load categories") }
        //   verify { categoryObserver.onChanged(emptyList()) }

        // Step 7: Additional assertion to confirm the categories are empty
        //  assertTrue(productDetailsViewModel.categories.value?.isEmpty() == true, "Categories should be empty")
    }

    @Test
    fun `test loadBanners success`() = runTest {
        val bannersList = listOf(
            SliderModel( url = "url1"),
            SliderModel(url = "url2")
        )

        // Mock the use case to return banners
        coEvery { getBannersUseCase.execute() } returns MutableLiveData(bannersList)

        // Observe LiveData
        val observer = mockk<Observer<List<SliderModel>>>(relaxed = true)
        productDetailsViewModel.banners.observeForever(observer)

        // Call the method
        productDetailsViewModel.loadBanners()

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify banners are set
        verify { observer.onChanged(bannersList) }
        assertEquals(bannersList, productDetailsViewModel.banners.value)
    }

    @Test
    fun `test loadBanners failure`() = runTest {
        val errorMessage = "Failed to load banners"

        // Simulate failure scenario by creating a LiveData that will return null or empty list
        val failedLiveData = MutableLiveData<List<SliderModel>>()
        failedLiveData.value = null // Simulating the case where banners are not fetched (e.g., error or empty response)

        // Mock the getBanners() to return the failedLiveData
        coEvery { getBannersUseCase.execute() } returns failedLiveData

        // Observe LiveData
        val errorObserver = mockk<Observer<String>>(relaxed = true)
        productDetailsViewModel.error.observeForever(errorObserver)

        // Call the method
        productDetailsViewModel.loadBanners()

        // Advance the test dispatcher to ensure coroutines are executed
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify the error message is set
        verify { errorObserver.onChanged("Failed to load banners") }
    }


    @Test
    fun `test loadCategoryItem success`() = runTest {
        val categoryId = "1"
        val items = listOf(
            ItemsModel(productId = 1, title = "Item 1"),
            ItemsModel(productId = 2, title = "Item 2")
        )

        // Mock the use case to return items
        coEvery { getCategoryItemUseCase.execute(categoryId) } returns  Result.success(items)

        // Observe LiveData
        val observer = mockk<Observer<List<ItemsModel>>>(relaxed = true)
        productDetailsViewModel.recommended.observeForever(observer)

        // Call the method
        productDetailsViewModel.loadCategoryItem(categoryId)

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify recommended items are set
        verify { observer.onChanged(items) }
        assertEquals(items, productDetailsViewModel.recommended.value)
    }

    @Test
    fun `test loadCategoryItem failure`() = runTest {
        val categoryId = "1"
        val errorMessage = "Error loading category items"

        // Mock the use case to return a failure result
        coEvery { getCategoryItemUseCase.execute(categoryId) } returns Result.failure(Exception(errorMessage))

        // Observe LiveData
        val errorObserver = mockk<Observer<String>>(relaxed = true)
        productDetailsViewModel.error.observeForever(errorObserver)

        // Call the method
        productDetailsViewModel.loadCategoryItem(categoryId)

        // Advance the test dispatcher
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify error message is set
        verify { errorObserver.onChanged("Error loading items: $errorMessage") }
    }

}
