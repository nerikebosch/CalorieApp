package com.example.calorieapp.model.service.module

import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.ConfigurationService
import com.example.calorieapp.model.service.LocationService
import com.example.calorieapp.model.service.LogService
import com.example.calorieapp.model.service.MapService
import com.example.calorieapp.model.service.NutritionService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.impl.AccountServiceImpl
import com.example.calorieapp.model.service.impl.ConfigurationServiceImpl
import com.example.calorieapp.model.service.impl.LocationServiceImpl
import com.example.calorieapp.model.service.impl.LogServiceImpl
import com.example.calorieapp.model.service.impl.MapServiceImpl
import com.example.calorieapp.model.service.impl.NutritionServiceImpl
import com.example.calorieapp.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


/**
 * A Dagger module that provides bindings for various services used in the application.
 * This module is installed in the [SingletonComponent] to ensure that the services
 * are provided as singletons throughout the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    /**
     * Binds the [AccountServiceImpl] implementation to the [AccountService] interface.
     *
     * @param impl The implementation of [AccountService].
     * @return An instance of [AccountService].
     */
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    /**
     * Binds the [LogServiceImpl] implementation to the [LogService] interface.
     *
     * @param impl The implementation of [LogService].
     * @return An instance of [LogService].
     */
    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

    /**
     * Binds the [StorageServiceImpl] implementation to the [StorageService] interface.
     *
     * @param impl The implementation of [StorageService].
     * @return An instance of [StorageService].
     */
    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    /**
     * Binds the [ConfigurationServiceImpl] implementation to the [ConfigurationService] interface.
     *
     * @param impl The implementation of [ConfigurationService].
     * @return An instance of [ConfigurationService].
     */
    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService

    /**
     * Binds the [NutritionServiceImpl] implementation to the [NutritionService] interface.
     *
     * @param impl The implementation of [NutritionService].
     * @return An instance of [NutritionService].
     */
    @Binds
    abstract fun provideNutritionService(impl: NutritionServiceImpl): NutritionService

    /**
     * Binds the [LocationServiceImpl] implementation to the [LocationService] interface.
     *
     * @param impl The implementation of [LocationService].
     * @return An instance of [LocationService].
     */
    @Binds
    abstract fun provideLocationService(impl: LocationServiceImpl): LocationService

    /**
     * Binds the [MapServiceImpl] implementation to the [MapService] interface.
     *
     * @param impl The implementation of [MapService].
     * @return An instance of [MapService].
     */
    @Binds
    abstract fun provideMapService(impl: MapServiceImpl): MapService

}
