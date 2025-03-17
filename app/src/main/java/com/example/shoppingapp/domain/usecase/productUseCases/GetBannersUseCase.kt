package com.example.shoppingapp.domain.usecase.productUseCases

import androidx.lifecycle.LiveData
import com.example.shoppingapp.domain.model.SliderModel
import com.example.shoppingapp.domain.repositories.ProductRepository

class GetBannersUseCase(private val productRepository: ProductRepository) {

   fun execute(): LiveData<List<SliderModel>> {
      return productRepository.getBanners() // Fetch banners from the repository
   }

}