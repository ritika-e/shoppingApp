package com.example.shoppingapp.domain.usecase.productUseCases

class CartUseCases(
    val addProductToCartUseCase: AddProductToCartUseCase,
    val getCartItemsUseCase: GetCartItemsUseCase,
    val updateProductQuantityUseCase: UpdateProductQuantityUseCase,
    val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    val clearCartUseCase: ClearCartUseCase
)