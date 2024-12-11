package com.example.calorieapp.model.service

/*
* An interface for managing data relate to
* user's height, weight and calories, health metrics ...
*
* getTask to get the task that the user has picked the date to do/ to finish
*/
interface StorageService {

    suspend fun getHeight(height: Double): Double?
    suspend fun saveHeight(height: Double)
    suspend fun getWeight(weight: Double): Double?
    suspend fun saveWeight(weight: Double)
    suspend fun getCalories(calories: Double): Double?
    suspend fun saveCalories(calories: Double)

    suspend fun getTask(task: String): String?
}