package com.example.shoppingapp.domain.model

data class ItemsModel(
    var title:String="",
    var description:String="",
    //var picUrl:ArrayList<String> = ArrayList(),
    val picUrl: String? = "",
    var model:ArrayList<String> = ArrayList(),
    var price:Double= 0.0,
    var rating:Double=0.0,
    var numberInCart:Int=0,
    val productId:Int=0,
    var showRecomended:Boolean= false,
    var categoryId:String=""
)
