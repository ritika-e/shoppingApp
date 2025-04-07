package com.example.shoppingapp.domain

import android.net.Uri
import com.example.shoppingapp.domain.model.ProductList
import com.example.shoppingapp.domain.repositories.FirebaseProductRepository
import com.example.shoppingapp.domain.usecase.admin.AddProductUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AddProductUseCaseTest {

    private lateinit var addProductUseCase: AddProductUseCase
    private lateinit var repository: FirebaseProductRepository

    @Before
    fun setUp() {
        // Mock the repository
        repository = mock()
        addProductUseCase = AddProductUseCase(repository)
    }

    @Test
    fun `test execute - success case`() = runTest {
        val product = ProductList(
            productId = 123,
            categoryId = "1",
            title = "Test Product",
            description = "Test Description",
            price = 100.0,
            picUrl = "https://someurl.com/image.jpg"
        )

        val uri = Uri.parse("https://someurl.com/image.jpg")

        // Simulate successful image upload
        whenever(repository.uploadImageToStorage(any(), any(), any(), any())).thenAnswer {
            val onComplete = it.getArgument<(String?) -> Unit>(3)
            onComplete("https://someurl.com/image.jpg")  // Mock successful image URL return
        }

        // Simulate saving product to database
        whenever(repository.saveProductDataToDatabase(any())).thenReturn(true)

        // Call the use case
        addProductUseCase.execute(product, { progress -> }, { success ->
            // Verify success callback
            assert(success)
        })

        // Verify interactions
        verify(repository).uploadImageToStorage(any(), any(), any(), any())  // Ensure image upload was triggered
        verify(repository).saveProductDataToDatabase(any())  // Ensure product save was triggered
    }


    @Test
    fun `test execute - failure case`() = runTest {
        val product = ProductList(
            productId = 123,
            categoryId = "1",
            title = "Test Product",
            description = "Test Description",
            price = 100.0,
            picUrl = "https://someurl.com/image.jpg"
        )

        val uri = Uri.parse("https://someurl.com/image.jpg")

        // Simulate failed image upload
        whenever(repository.uploadImageToStorage(any(), any(), any(), any())).thenAnswer {
            val onComplete = it.getArgument<(String?) -> Unit>(3)
            onComplete(null)  // Simulate failure to get image URL
        }

        // Call the use case
        addProductUseCase.execute(product, { progress -> }, { success ->
            // Verify failure callback
            assert(!success)
        })

        // Verify interactions
        verify(repository).uploadImageToStorage(any(), any(), any(), any())  // Ensure image upload was attempted
        verify(repository, times(0)).saveProductDataToDatabase(any())  // Ensure product was not saved
    }
}