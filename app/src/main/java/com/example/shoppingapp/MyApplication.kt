package com.example.shoppingapp

import android.app.Application
import com.example.shoppingapp.di.appModule
import com.example.shoppingapp.utils.SharedPreferencesManager
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()


        // start koin for DI
        startKoin{
            androidContext(this@MyApplication)
            modules(appModule)
        }
        SharedPreferencesManager(applicationContext)
      //  SharedPreferencesManager.init(this)
    }
}