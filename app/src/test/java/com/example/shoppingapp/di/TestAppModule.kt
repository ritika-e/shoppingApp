package com.example.shoppingapp.di

import com.example.shoppingapp.domain.repositories.AuthRepository
import com.example.shoppingapp.domain.repositories.CartRepository
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.domain.usecase.admin.AddProductUseCase
import com.example.shoppingapp.domain.usecase.admin.DeleteProductUseCase
import com.example.shoppingapp.domain.usecase.admin.GetAllProductsUseCase
import com.example.shoppingapp.domain.usecase.admin.GetProductByIdUseCase
import com.example.shoppingapp.domain.usecase.admin.UpdateProductUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.AddProductToCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.CartUseCases
import com.example.shoppingapp.domain.usecase.productUseCases.ClearCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCartItemsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.PlaceOrderUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.RemoveProductFromCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.UpdateProductQuantityUseCase
import com.example.shoppingapp.presentation.admin.ProductViewModel
import com.example.shoppingapp.presentation.auth.ForgetPasswordViewModel
import com.example.shoppingapp.presentation.auth.LoginViewModel
import com.example.shoppingapp.presentation.auth.SignupViewModel
import com.example.shoppingapp.presentation.user.CartViewModel
import com.example.shoppingapp.utils.SharedPreferencesManager
import io.mockk.mockk
import org.koin.dsl.module
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

val testAppModule = module {
    single { mockk<LoginUseCase>() }  // Mock LoginUseCase
    single { mockk<SharedPreferencesManager>() }  // Mock SharedPreferencesManager
    single { LoginViewModel(get(),get()) }

    single { mockk<SignUpUseCase>() }
    single { SignupViewModel(get(),get()) }

    single { mockk<ResetPasswordUseCase>() }
    single { ForgetPasswordViewModel(get()) }

   /* single { mockk<CartUseCases>() }
    single { mockk<PlaceOrderUseCase>() }
    single { CartViewModel(get(),get()) }*/

    single { mockk<CartUseCases>() }  // Mock CartUseCases
    single { mockk<PlaceOrderUseCase>() }
    single { mockk<GetCartItemsUseCase>() }
    single { mockk<AddProductToCartUseCase>() }
    single { mockk<UpdateProductQuantityUseCase>() }
    single { mockk<RemoveProductFromCartUseCase>() }
    single { mockk<ClearCartUseCase>() }

    single { mock<SharedPreferencesManager>().apply {
        `when`(getUserData()).thenReturn(SharedPreferencesManager.UserData(
            userId = "123",
            userName = "Test User",
            userRole = "Customer"
        ))
    } }

    // Mock CartRepository if it's part of your CartUseCases
    single { mock<CartRepository>() }


   single { mockk<ProductViewModel>() }
   single{ mockk< AddProductUseCase>()}
   single{ mockk<UpdateProductUseCase>()}
   single { mockk<DeleteProductUseCase>()}
   single { mockk <GetAllProductsUseCase>()}
    single { mockk <GetProductByIdUseCase>()}
}