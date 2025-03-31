package com.example.shoppingapp.data.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.repositories.ProductRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProductRepositoryImpl(private val firebaseDatabase: FirebaseDatabase): ProductRepository {

    override fun getProductDetails(productId: Int): LiveData<ItemsModel?> {
        val liveData = MutableLiveData<ItemsModel?>()
        val ref = firebaseDatabase.getReference("Items")
        ref.orderByChild("productId").equalTo(productId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.children
                        .mapNotNull { it.getValue(ItemsModel::class.java) }
                        .firstOrNull()
                    liveData.value = product
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return liveData
    }

    override fun getRecommendedProducts(): LiveData<List<ItemsModel>> {
        val liveData = MutableLiveData<List<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")
        ref.orderByChild("showRecommended").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val products = snapshot.children
                        .mapNotNull { it.getValue(ItemsModel::class.java) }
                   // liveData.value = products
                    liveData.postValue(products)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        return liveData
    }

   override fun getCategories(): LiveData<List<CategoryModel>> {
        val liveData = MutableLiveData<List<CategoryModel>>()
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = snapshot.children
                    .mapNotNull { it.getValue(CategoryModel::class.java) }
                liveData.value = categories
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
        return liveData
    }

    override suspend fun getCategoryItems(categoryId: String): Result<List<ItemsModel>> {
        val reference = firebaseDatabase.getReference("Items")
        val query = reference.orderByChild("categoryId").equalTo(categoryId)

        return try {
            val itemsList = mutableListOf<ItemsModel>()

            // Use a suspendCoroutine to work with the callback-based API
            suspendCoroutine { continuation ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (childSnapshot in snapshot.children) {
                            val item = childSnapshot.getValue(ItemsModel::class.java)
                            item?.let { itemsList.add(it) }
                        }
                        continuation.resume(Result.success(itemsList))  // Returning the result
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resume(Result.failure(error.toException()))  // Handling errors
                    }
                })
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getBanners(): LiveData<List<SliderModel>> {
        val bannersLiveData  = MutableLiveData<List<SliderModel>>()
        val bannerRef  = firebaseDatabase.getReference("Banner")

        bannerRef .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerList = mutableListOf<SliderModel>()
                snapshot.children.forEach {
                    val banner = it.getValue(SliderModel::class.java)
                    if (banner != null) {
                        bannerList.add(banner)
                    }
                }
                bannersLiveData .value = bannerList
            }
            override fun onCancelled(error: DatabaseError) {
               // Log.e("ProductRepository", "Error loading banners: ${error.message}")
            }
        })
        return bannersLiveData
    }

}