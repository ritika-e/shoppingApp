package com.example.shoppingapp.presentation.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.domain.model.CategoryModel
import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ProductDetailsViewModel():ViewModel() {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    private val _category = MutableLiveData<List<CategoryModel>>()
    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _recommended = MutableLiveData<MutableList<ItemsModel>>()

    val banners:LiveData<List<SliderModel>> = _banner
    val categories: MutableLiveData<List<CategoryModel>> = _category
    val recommended:LiveData<MutableList<ItemsModel>> = _recommended

    private val _productDetails = MutableLiveData<ItemsModel?>()
    val productDetails: MutableLiveData<ItemsModel?> = _productDetails

    // Function to fetch product details by product ID from Firebase
    fun loadProductDetails(productId: Int) {
        val ref = firebaseDatabase.getReference("Items")
        ref.orderByChild("productId").equalTo(productId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val product = snapshot.children
                            .mapNotNull { it.getValue(ItemsModel::class.java) }
                            .firstOrNull()
                        _productDetails.value = product
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ProductDetailsViewModel", "Error loading product: ${error.message}")
                }
            })
    }

    //Product filter based on categoryID
    fun loadCategoryFiltered(categoryID: String){
        // Reference to Firebase Database
        val Ref = firebaseDatabase.getReference("Items")
        // Query to filter items based on categoryID
        val query:Query=Ref.orderByChild("categoryId").equalTo(categoryID)

        // Add listener for query results
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<ItemsModel>()
                // Loop through the children and map them to ItemsModel
                for (childSnapshot in snapshot.children){
                    val list=childSnapshot.getValue(ItemsModel::class.java)
                    if (list!=null){
                        lists.add(list)
                    }
                }
                _recommended.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductDetailsViewModel", "Error loading items: ${error.message}")
            }

        })
    }

    fun loadRecommended(){
        val Ref = firebaseDatabase.getReference("Items")
        val query:Query=Ref.orderByChild("showRecommended").equalTo(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children){
                    val list=childSnapshot.getValue(ItemsModel::class.java)
                    if (list!=null){
                        lists.add(list)
                    }
                }
                _recommended.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun loadBanners(){
        val Ref = firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list!= null){
                        lists.add(list)
                    }
                }
                _banner.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun loadCategory(){
        val Ref = firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(CategoryModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                }
                _category.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}