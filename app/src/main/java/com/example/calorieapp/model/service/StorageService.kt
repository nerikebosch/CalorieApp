package com.example.calorieapp.model.service

import com.example.calorieapp.model.User
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

    suspend fun updateUser(user: User)
    suspend fun save(userData: UserData)
    suspend fun getHeight(height: Double): Double?
    suspend fun saveHeight(height: Double)
    suspend fun getWeight(weight: Double): Double?
    suspend fun saveWeight(weight: Double)
    suspend fun getCalories(calories: Double): Double?
    suspend fun saveCalories(calories: Double)
    suspend fun getTask(task: String): String?


    suspend fun getUserProduct(userProductId: String): UserProducts?
    suspend fun saveUserProduct(userProduct: UserProducts): String

    suspend fun getUserProductByDate(date: Long): UserProducts?
}