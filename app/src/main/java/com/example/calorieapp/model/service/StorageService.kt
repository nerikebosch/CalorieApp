package com.example.calorieapp.model.service

import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserActivity
import com.example.calorieapp.model.UserData
import kotlinx.coroutines.flow.Flow
import com.example.calorieapp.model.UserProducts

/*
* An interface for managing data relate to
* user's height, weight and calories, health metrics ...
*
* getTask to get the task that the user has picked the date to do/ to finish
*/
interface StorageService {

    val userData: Flow<List<UserData>>
    val userProducts: Flow<List<UserProducts>>
    val userActivity: Flow<List<UserActivity>>

    suspend fun getUserDataByDate(date: String): UserData?
    suspend fun updateUserData(userData: UserData)
    suspend fun saveUserData(userData: UserData): String

    suspend fun getUserProduct(userProductId: String): UserProducts?
    suspend fun saveUserProduct(userProduct: UserProducts): String
    suspend fun updateUserProduct(userProduct: UserProducts)
    suspend fun getUserProductByDate(date: String): UserProducts?
    suspend fun deleteUserProduct(userProductId: String)

    suspend fun getUserActivity(userActivityId: String): UserActivity?
    suspend fun getUserActivityByDate(date: String): UserActivity?
    suspend fun updateUserActivity(userActivity: UserActivity)
    suspend fun saveUserActivity(userActivity: UserActivity): String
}