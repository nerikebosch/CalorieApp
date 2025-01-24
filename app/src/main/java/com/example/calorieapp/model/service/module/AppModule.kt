package com.example.calorieapp.model.service.module

import android.content.Context
import android.hardware.SensorManager
import com.example.calorieapp.CalorieApp
import com.example.calorieapp.CalorieHiltApp
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.screens.recipe.RecipeRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides application context for dependency injection
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    // Provides RecipeRepository instance for dependency injection
    @Provides
    fun provideRecipeRepository(@ApplicationContext context: Context): RecipeRepository {
        return RecipeRepository(context)
    }

    @Provides
    fun provideLocationService(
        @ApplicationContext context: Context,
        locationClient: FusedLocationProviderClient,
        sensorManager: SensorManager,
        storageService: StorageService
    ): LocationService {
        return LocationServiceImpl(context, locationClient, sensorManager, storageService)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
}