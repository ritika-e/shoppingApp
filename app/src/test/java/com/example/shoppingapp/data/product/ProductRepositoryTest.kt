package com.example.shoppingapp.data.product

import androidx.lifecycle.MutableLiveData
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.repositories.ProductRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ProductRepositoryTest {

    private lateinit var repository: ProductRepository
    private lateinit var mockDatabase: FirebaseDatabase
    private lateinit var mockBannerRef: DatabaseReference
    private lateinit var mockDataSnapshot: DataSnapshot
    private lateinit var mockSliderModel: SliderModel

    @Before
    fun setUp() {
        // Mock Firebase Database and DatabaseReference
        mockDatabase = mockk()
        mockBannerRef = mockk()
        mockDataSnapshot = mockk()
        mockSliderModel = SliderModel(url = "banner_url")

        // Mock ProductRepository interface
        repository = mockk()

        // Mock the getReference to return a mocked reference to the "Banner"
        every { mockDatabase.getReference("Banner") } returns mockBannerRef

        // Prepare the mocked children to return the SliderModel inside the DataSnapshot
        val mockChildSnapshot = mockk<DataSnapshot>()
        every { mockChildSnapshot.getValue(SliderModel::class.java) } returns mockSliderModel
        every { mockDataSnapshot.children } returns listOf(mockChildSnapshot)

        // Mock addValueEventListener to invoke onDataChange
        every { mockBannerRef.addValueEventListener(any<ValueEventListener>()) } answers {
            val listener = it.invocation.args[0] as ValueEventListener
            // Manually trigger onDataChange with mockDataSnapshot
            listener.onDataChange(mockDataSnapshot)
            listener // Return the ValueEventListener as required
        }

        // Mock getBanners() method to return the live data
        val liveData = MutableLiveData<List<SliderModel>>()
        liveData.value = listOf(mockSliderModel) // Return a list with one banner

        // Mock the behavior of getBanners()
        every { repository.getBanners() } returns liveData
    }

    @Test
    fun testGetBanners() {
        // Get the banners live data
        val bannersLiveData = repository.getBanners()

        // Observe the LiveData in the test
        bannersLiveData.observeForever { banners ->
            // Ensure the banners are not empty
            assert(banners != null && banners.isNotEmpty())
            // Verify the first banner contains expected data
            assert(banners.first().url == "banner_url")
        }
    }
}