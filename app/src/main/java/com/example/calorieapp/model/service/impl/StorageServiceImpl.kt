package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserData
import com.example.calorieapp.model.UserProducts
import com.example.calorieapp.model.service.AccountService
import com.example.calorieapp.model.service.StorageService
import com.example.calorieapp.model.service.trace
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService,
) : StorageService {


    @OptIn(ExperimentalCoroutinesApi::class)
    override val userData: Flow<List<UserData>>
        get() = auth.currentUser.flatMapLatest { user ->
            currentCollection(user.id, USER_DATA_COLLECTION).snapshots().map { snapshot ->
                snapshot.toObjects<UserData>() // Use KTX toObjects()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userProducts: Flow<List<UserProducts>>
        get() = auth.currentUser.flatMapLatest { user ->
            currentCollection(user.id, USER_PRODUCT_COLLECTION).snapshots().map { snapshot ->
                snapshot.toObjects<UserProducts>() // Use KTX toObjects()
            }
        }

    override suspend fun updateUser(user: User) {
        TODO("Not yet implemented")
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

    override suspend fun getUserProduct(userProductId: String): UserProducts? =
        currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION).document(userProductId).get().await().toObject()


    override suspend fun saveUserProduct(userProduct: UserProducts): String =
        trace(SAVE_USER_PRODUCT) {
            currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION).add(userProduct).await().id
        }

    override suspend fun getUserProductByDate(date: Long): UserProducts? =
        currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION)
            .whereEqualTo("date", date)
            .get()
            .await()
            .toObjects<UserProducts>()
            .firstOrNull()

    private fun currentCollection(uid: String, data: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(data)


    companion object {
        private const val USER_COLLECTION = "users"
        private const val USER_DATA_COLLECTION = "userData"
        private const val USER_PRODUCT_COLLECTION = "userProducts"
        private const val SAVE_USER_PRODUCT = "saveUserProducts"

    }

}