package com.example.calorieapp.model.service.module

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.calorieapp.R
import com.example.calorieapp.auth.GoogleSignInManager
import com.example.calorieapp.model.service.AccountService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideGoogleSignInManager(
        @ApplicationContext context: Context,
        accountService: AccountService,
        credentialManager: CredentialManager,
        webClientId: String
    ): GoogleSignInManager {
        return GoogleSignInManager(context, accountService, credentialManager, webClientId)
    }


    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManager.create(context)

    @Provides
    fun provideWebClientId(@ApplicationContext context: Context): String {
        // Retrieve web_client_id from the strings.xml resource
        return context.getString(R.string.web_client_id)
    }
}