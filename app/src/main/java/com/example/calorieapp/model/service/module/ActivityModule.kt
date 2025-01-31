package com.example.calorieapp.model.service.module


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

//    @Provides
//    fun provideLocationService(
//        locationClient: FusedLocationProviderClient,
//        sensorManager: SensorManager,
//        storageService: StorageService,
//        accountService: AccountService,
//        lifecycle: Lifecycle,
//        @ApplicationContext context: Context
//    ): LocationService {
//        return LocationServiceImpl(
//            context = context,
//            locationClient = locationClient,
//            sensorManager = sensorManager,
//            storageService = storageService,
//            accountService = accountService,
//            lifecycle = lifecycle
//        )
//    }
}