package com.example.calorieapp.model.service.module

import android.content.Context
import com.example.calorieapp.CalorieApp
import com.example.calorieapp.CalorieHiltApp
import com.example.calorieapp.screens.recipe.RecipeRepository
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
}