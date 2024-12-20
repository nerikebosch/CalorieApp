package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.UserData
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.StorageService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val data: Flow<List<UserData>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                currentCollection(user.id).snapshots().map { snapshot -> snapshot.toObjects() }
            }

    override suspend fun save(userData: UserData) {
        TODO("Not yet implemented")
    }

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

    private fun currentCollection(uid: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(USER_DATA_COLLECTION)

    companion object {
        private const val USER_COLLECTION = "users"
        private const val USER_DATA_COLLECTION = "userData"

    }

}