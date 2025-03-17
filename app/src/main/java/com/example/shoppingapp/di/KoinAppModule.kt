package com.example.shoppingapp.di


import com.example.shoppingapp.data.auth.AuthService
import com.example.shoppingapp.data.auth.AuthRepositoryImpl
import com.example.shoppingapp.data.product.CartRepositoryImpl
import com.example.shoppingapp.data.product.InMemoryCartDataSourceRepository
import com.example.shoppingapp.domain.repositories.ProductRepository
import com.example.shoppingapp.data.product.ProductRepositoryImpl
import com.example.shoppingapp.data.user.UserRepository
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.example.shoppingapp.domain.repositories.CartDataSourceRepository
import com.example.shoppingapp.domain.repositories.CartRepository
import com.example.shoppingapp.data.product.OrderRepositoryImpl
import com.example.shoppingapp.domain.usecase.GetCurrentUserIdUseCase
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.domain.usecase.UserUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.AddProductToCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.CartUseCases
import com.example.shoppingapp.domain.usecase.productUseCases.GetBannersUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCartItemsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoriesUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetCategoryItemUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetProductDetailsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.GetRecommendedProductsUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.PlaceOrderUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.RemoveProductFromCartUseCase
import com.example.shoppingapp.domain.usecase.productUseCases.UpdateProductQuantityUseCase
import com.example.shoppingapp.presentation.admin.ProductViewModel
import com.example.shoppingapp.presentation.auth.LoginViewModel
import com.example.shoppingapp.presentation.auth.ForgetPasswordViewModel
import com.example.shoppingapp.presentation.auth.SignupViewModel
import com.example.shoppingapp.presentation.splash.SplashViewModel
import com.example.shoppingapp.presentation.user.CartViewModel
import com.example.shoppingapp.presentation.user.ProductDetailsViewModel
import com.example.shoppingapp.utils.SharedPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{

    // Firebase Auth instance
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseDatabase.getInstance() }

    // Room
   // single { Room.databaseBuilder(get(), AppDatabase::class.java, "product_db").build() }
  //  single { get<AppDatabase>().productImageDao() }

    // Services
    single { AuthService() }

    // SharedPref
    single { SharedPreferencesManager (androidApplication())}

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(),get())  }
  //  single { UserRepository(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CartDataSourceRepository> { InMemoryCartDataSourceRepository() }
    single<CartRepository>{CartRepositoryImpl(get())}
    single { OrderRepositoryImpl(get()) }



    // UseCases
    single { SignUpUseCase(get()) }
    single { LoginUseCase(get()) }
    single { ResetPasswordUseCase(get()) }
    single { UserUseCase(get()) }
    single { GetProductDetailsUseCase(get()) }
    single { GetRecommendedProductsUseCase(get()) }
    single { GetCategoriesUseCase(get()) }
    single { GetBannersUseCase(get()) }
    single { AddProductToCartUseCase(get()) }
    single { CartUseCases(get(),get(),get(),get()) }
    single{GetBannersUseCase(get())}
    single { GetCartItemsUseCase(get()) }
    single { GetCategoriesUseCase(get()) }
    single { GetProductDetailsUseCase(get()) }
    single { GetRecommendedProductsUseCase(get()) }
    single { RemoveProductFromCartUseCase(get()) }
    single { UpdateProductQuantityUseCase(get()) }
    single { PlaceOrderUseCase(get()) }
    single { GetCurrentUserIdUseCase(get()) }
    single { GetCategoryItemUseCase(get()) }

    // viewModel
    viewModel { SplashViewModel() }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel { ProductViewModel() }
    viewModel { ProductDetailsViewModel(get(), get(), get(),get() ,get()) }
    viewModel { CartViewModel(get(),get()) }
}
