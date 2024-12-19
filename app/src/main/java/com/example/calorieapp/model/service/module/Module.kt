package com.example.calorieapp.model.service.module

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.calorieapp.R
import com.example.calorieapp.auth.GoogleSignInManager
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.impl.AccountServiceImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Binds
    @Provides
    fun provideGoogleSignInManager(
        @ApplicationContext context: Context,
        accountService: AccountService,
        credentialManager: CredentialManager,
        webClientId: String
    ): GoogleSignInManager {
        return GoogleSignInManager(context, accountService, credentialManager, webClientId)
    }

    @Binds
    @Provides
    fun provideAccountService(auth: FirebaseAuth): AccountService =
        AccountServiceImpl(auth)

    @Binds
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Binds
    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManager.create(context)

    @Binds
    @Provides
    fun provideWebClientId(@ApplicationContext context: Context): String {
        // Retrieve web_client_id from the strings.xml resource
        return context.getString(R.string.web_client_id)
    }
}