package com.example.calorieapp.model.service.module

import android.content.Context
import android.hardware.SensorManager
import com.example.calorieapp.screens.recipe.RecipeRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Dagger Hilt module that provides application-wide dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the application context.
     *
     * @param context The application context injected by Hilt.
     * @return The application context.
     */
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    /**
     * Provides an instance of [RecipeRepository].
     *
     * @param context The application context.
     * @return A new instance of [RecipeRepository].
     */
    @Provides
    fun provideRecipeRepository(@ApplicationContext context: Context): RecipeRepository {
        return RecipeRepository(context)
    }

    /**
     * Provides a singleton instance of [FusedLocationProviderClient].
     *
     * @param context The application context.
     * @return A [FusedLocationProviderClient] instance for location services.
     */
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Provides an instance of [SensorManager].
     *
     * @param context The application context.
     * @return A [SensorManager] instance for accessing device sensors.
     */
    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
}