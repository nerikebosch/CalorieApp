package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.StorageService

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {

    override suspend fun getHeight(height: Double): Double? {
        TODO("Not yet implemented")
    }

    override suspend fun saveHeight(height: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getWeight(weight: Double): Double? {
        TODO("Not yet implemented")
    }

    override suspend fun saveWeight(weight: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getCalories(calories: Double): Double? {
        TODO("Not yet implemented")
    }

    override suspend fun saveCalories(calories: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(task: String): String? {
        TODO("Not yet implemented")
    }


}