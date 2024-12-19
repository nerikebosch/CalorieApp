// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.53.1" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false
    // Add the dependency for the Crashlytics Gradle plugin
    //id("org.jetbrains.kotlin.kapt") version "2.1.0"
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

//    dependencies {
//        classpath("com.google.dagger:hilt-android-gradle-plugin:2.53.1")
//    }
}