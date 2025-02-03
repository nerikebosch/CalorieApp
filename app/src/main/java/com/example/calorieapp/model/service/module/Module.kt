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

/**
 * Dagger Hilt module for providing authentication-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    /**
     * Provides an instance of [GoogleSignInManager] for handling Google authentication.
     *
     * @param context The application context.
     * @param accountService The account service responsible for user authentication.
     * @param credentialManager The CredentialManager used for credential handling.
     * @param webClientId The web client ID required for Google Sign-In.
     * @return An instance of [GoogleSignInManager].
     */
    @Provides
    fun provideGoogleSignInManager(
        @ApplicationContext context: Context,
        accountService: AccountService,
        credentialManager: CredentialManager,
        webClientId: String
    ): GoogleSignInManager {
        return GoogleSignInManager(context, accountService, credentialManager, webClientId)
    }


    /**
     * Provides an instance of [CredentialManager] for managing user credentials.
     *
     * @param context The application context.
     * @return An instance of [CredentialManager].
     */
    @Provides
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManager.create(context)


    /**
     * Retrieves the web client ID from the app's resources.
     *
     * @param context The application context.
     * @return The web client ID as a [String].
     */
    @Provides
    fun provideWebClientId(@ApplicationContext context: Context): String {
        // Retrieve web_client_id from the strings.xml resource
        return context.getString(R.string.web_client_id)
    }
}