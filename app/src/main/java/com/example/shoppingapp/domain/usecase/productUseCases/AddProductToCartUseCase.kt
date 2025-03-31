package com.example.shoppingapp.domain.usecase.productUseCases

import com.example.shoppingapp.domain.model.ItemsModel
import com.example.shoppingapp.domain.repositories.CartRepository

open class AddProductToCartUseCase(private val cartRepository: CartRepository) {
  open  fun execute(product: ItemsModel) {
        cartRepository.addProduct(product)
    }
}