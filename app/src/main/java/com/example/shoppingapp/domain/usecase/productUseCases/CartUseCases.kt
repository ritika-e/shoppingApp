package com.example.shoppingapp.domain.usecase.productUseCases

open class CartUseCases(
  open  val addProductToCartUseCase: AddProductToCartUseCase,
   open val getCartItemsUseCase: GetCartItemsUseCase,
    open val updateProductQuantityUseCase: UpdateProductQuantityUseCase,
    open val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    open val clearCartUseCase: ClearCartUseCase
)