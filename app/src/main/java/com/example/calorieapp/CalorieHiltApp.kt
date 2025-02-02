package com.example.calorieapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Calorie App, enabling Hilt dependency injection.
 */
@HiltAndroidApp
class CalorieHiltApp : Application()