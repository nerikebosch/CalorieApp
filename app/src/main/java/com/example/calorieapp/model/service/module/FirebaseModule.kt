package com.example.calorieapp.model.service.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dagger Hilt module for providing Firebase-related services.
 * This module ensures that Firebase services such as authentication
 * and Firestore database are available for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Provides an instance of Firebase Authentication.
     *
     * @return FirebaseAuth instance for handling user authentication.
     */
    @Provides fun auth(): FirebaseAuth = Firebase.auth


    /**
     * Provides an instance of Firebase Firestore.
     *
     * @return FirebaseFirestore instance for handling cloud database operations.
     */
    @Provides fun firestore(): FirebaseFirestore = Firebase.firestore
}
