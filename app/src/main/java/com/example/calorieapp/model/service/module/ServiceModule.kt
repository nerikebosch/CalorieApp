package com.example.calorieapp.model.service.module

import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.ConfigurationService
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.NutritionService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.impl.AccountServiceImpl
import com.example.calorieapp.model.service.impl.ConfigurationServiceImpl
import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.model.service.impl.LogServiceImpl
import com.example.calorieapp.model.service.impl.NutritionServiceImpl
import com.example.calorieapp.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService

    @Binds
    abstract fun provideNutritionService(impl: NutritionServiceImpl): NutritionService

    @Binds
    abstract fun provideLocationService(impl: LocationServiceImpl): LocationService

}
