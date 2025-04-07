plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services) // firebase plugin
    id ("kotlin-kapt") // Add this line for kapt
}

android {
    namespace = "com.example.shoppingapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shoppingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
       // kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.firebase.storage.ktx)
    implementation (libs.androidx.constraintlayout.compose)
    implementation (libs.accompanist.pager)

    // Coil
    implementation (libs.coil.compose)
    implementation(libs.firebase.storage)
    // implementation(libs.androidx.constraintlayout)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    // Firebase App Check for Realtime Database
   // implementation ("com.google.firebase:firebase-appcheck-playintegrity:17.0.0")
    // Firebase App Check for Firebase Storage (if you're using Firebase Storage)
  //  implementation ("com.google.firebase:firebase-appcheck-safetynet:17.0.0")

    // Room Database
   // implementation ("androidx.room:room-runtime:2.5.0")
    // Room Database dependencies
    // implementation ("androidx.room:room-ktx:2.5.0")
   // kapt ("androidx.room:room-compiler:2.5.0")



    //Navigation
    implementation(libs.androidx.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    // LiveData
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.androidx.runtime.livedata)
    // Koin: dependency injection
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
    implementation(libs.koin.androidx.compose)

    // Testing dependencies for Unit Tests and UI Tests
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit.v113)
    androidTestImplementation (libs.androidx.espresso.core.v340)
    testImplementation ("org.mockito:mockito-core:3.12.4")
    testImplementation (libs.mockk)
    androidTestImplementation (libs.mockk.android)  // For Android tests
    testImplementation ("org.mockito:mockito-inline:3.11.2")


    androidTestImplementation (libs.mockito.android)
    testImplementation (libs.mockito.kotlin)  // Use the latest version

    testImplementation (libs.kotlinx.coroutines.test) // For coroutines testing
    testImplementation (libs.androidx.core.testing)  // For LiveData testing
    testImplementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")


    testImplementation (libs.kotlin.test) // For assertion functions like assertFailsWith
    testImplementation (libs.junit.jupiter.api)

    implementation (libs.glide)  // Or the latest version
//    kapt 'com.github.bumptech.glide:compiler:4.15.1'

    // Robolectric for unit tests
    testImplementation (libs.robolectric)

// Compose test rules
    androidTestImplementation (libs.androidx.ui.test)
    androidTestImplementation (libs.ui.test.junit4)


}