package com.example.calorieapp.model.service

import com.example.calorieapp.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Service interface for managing user accounts.
 */
interface AccountService {

    /**
     * The ID of the currently logged-in user.
     */
    val currentUserId: String

    /**
     * Indicates whether a user is currently authenticated.
     */
    val hasUser: Boolean

    /**
     * A flow representing the current authenticated user.
     */
    val currentUser: Flow<User>

    /**
     * Authenticates a user with email and password.
     * @param email The user's email address.
     * @param password The user's password.
     */
    suspend fun authenticate(email: String, password: String)

    /**
     * Sends a password recovery email.
     * @param email The user's email address.
     */
    suspend fun sendRecoveryEmail(email: String)

    /**
     * Signs out the current user.
     */
    suspend fun signOut()

    /**
     * Links an additional authentication method to the current user.
     * @param user The user to link.
     */
    suspend fun linkAccount(user: User)
    //suspend fun deleteAccount()

    /**
     * Signs in using Google authentication.
     * @param idToken The Google authentication token.
     */
    suspend fun signInWithGoogle(idToken: String)
}