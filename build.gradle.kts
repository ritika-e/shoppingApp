// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false

}

/*
buildscript {
    dependencies {
        // Update these versions if necessary
        classpath("com.android.tools.build:gradle:8.0.2") // or the latest AGP version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Ensure this is the latest Kotlin version compatible with your AGP version
    }
}*/
