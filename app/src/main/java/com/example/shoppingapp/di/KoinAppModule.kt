package com.example.shoppingapp.di


import com.example.shoppingapp.data.auth.AuthService
import com.example.shoppingapp.data.auth.AuthRepositoryImpl
import com.example.shoppingapp.data.user.UserRepository
import com.example.shoppingapp.domain.repositories.AuthRepository
import com.example.shoppingapp.domain.usecase.LoginUseCase
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import com.example.shoppingapp.domain.usecase.SignUpUseCase
import com.example.shoppingapp.domain.usecase.UserUseCase
import com.example.shoppingapp.presentation.admin.ProductViewModel
import com.example.shoppingapp.presentation.auth.LoginViewModel
import com.example.shoppingapp.presentation.auth.ForgetPasswordViewModel
import com.example.shoppingapp.presentation.auth.SignupViewModel
import com.example.shoppingapp.presentation.splash.SplashViewModel
import com.example.shoppingapp.presentation.user.CartViewModel
import com.example.shoppingapp.presentation.user.ProductDetailsViewModel
import com.example.shoppingapp.utils.SharedPreferencesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.get
import org.koin.dsl.module

val appModule = module{

    // Firebase Auth instance
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Room
   // single { Room.databaseBuilder(get(), AppDatabase::class.java, "product_db").build() }
  //  single { get<AppDatabase>().productImageDao() }

    // Services
    single { AuthService() }

    // SharedPref
    single { SharedPreferencesManager }

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(),get())  }
    single { UserRepository(get()) }
  //  single { ProductRepository(get()) }



    // UseCases
    single { SignUpUseCase(get()) }
    single { LoginUseCase(get()) }
    single { ResetPasswordUseCase(get()) }
    single { UserUseCase(get(),get()) }
   // single { ProductUseCase( get(),get()) }

    // viewModel
    viewModel { SplashViewModel() }
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel { ProductViewModel() }
    viewModel { ProductDetailsViewModel() }
    viewModel { CartViewModel() }

}
