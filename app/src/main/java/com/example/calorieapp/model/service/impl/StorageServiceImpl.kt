package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.User
import com.example.calorieapp.model.UserActivity
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService,
) : StorageService {


//    @OptIn(ExperimentalCoroutinesApi::class)
//    override val userData: Flow<List<UserData>>
//        get() = auth.currentUser.flatMapLatest { user ->
//            currentCollection(user.id, USER_DATA_COLLECTION).snapshots().map { snapshot ->
//                snapshot.toObjects<UserData>() // Use KTX toObjects()
//            }
//        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userProducts: Flow<List<UserProducts>>
        get() = auth.currentUser.flatMapLatest { user ->
            if (user.id.isBlank()) {
                flowOf(emptyList())
            } else {
                currentCollection(user.id, USER_PRODUCT_COLLECTION).snapshots().map { snapshot ->
                    snapshot.toObjects<UserProducts>() // Use KTX toObjects()
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userActivity: Flow<List<UserActivity>>
        get() = auth.currentUser.flatMapLatest { user ->
            if (user.id.isBlank()) {
                flowOf(emptyList())
            } else {
                currentCollection(user.id, USER_ACTIVITY_COLLECTION).snapshots().map { snapshot ->
                    snapshot.toObjects<UserActivity>() // Use KTX toObjects()
                }
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

    override suspend fun getUserProduct(userProductId: String): UserProducts? =
        currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION).document(userProductId).get().await().toObject()


    override suspend fun saveUserProduct(userProduct: UserProducts): String =
        trace(SAVE_USER_PRODUCT) {
            println("Debug: Saving new userProduct: $userProduct")
            currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION).add(userProduct).await().id
        }

    override suspend fun updateUserProduct(userProduct: UserProducts): Unit =
        trace(UPDATE_USER_PRODUCT) {
            println("Debug: Updating for userProduct: $userProduct")
            currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION).document(userProduct.id).set(userProduct).await()
        }

    override suspend fun getUserProductByDate(date: String): UserProducts? =
        currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION)
            .whereEqualTo("date", date)
            .get()
            .await()
            .toObjects<UserProducts>()
            .firstOrNull()

    override suspend fun deleteUserProduct(userProductId: String) {
        currentCollection(auth.currentUserId, USER_PRODUCT_COLLECTION).document(userProductId).delete().await()
    }

    override suspend fun getUserActivity(userActivityId: String): UserActivity? =
        currentCollection(auth.currentUserId, USER_ACTIVITY_COLLECTION).document(userActivityId).get().await().toObject()

    override suspend fun getUserActivityByDate(date: String): UserActivity? =
        currentCollection(auth.currentUserId, USER_ACTIVITY_COLLECTION)
            .whereEqualTo("date", date)
            .get()
            .await()
            .toObjects<UserActivity>()
            .firstOrNull()

    override suspend fun updateUserActivity(userActivity: UserActivity): Unit =
        trace(UPDATE_USER_ACTIVITY) {
            println("Debug: Updating for userActivity: $userActivity")
            currentCollection(auth.currentUserId, USER_ACTIVITY_COLLECTION).document(userActivity.id).set(userActivity).await()
        }


    override suspend fun  saveUserActivity(userActivity: UserActivity): String =
        trace(SAVE_USER_ACTIVITY) {
            println("Debug: Saving new userActivity: $userActivity")
            currentCollection(auth.currentUserId, USER_ACTIVITY_COLLECTION).add(userActivity).await().id
        }


    private fun currentCollection(uid: String, data: String): CollectionReference =
        firestore.collection(USER_COLLECTION).document(uid).collection(data)


    companion object {
        private const val USER_COLLECTION = "users"
        private const val USER_DATA_COLLECTION = "userData"
        private const val USER_PRODUCT_COLLECTION = "userProducts"
        private const val SAVE_USER_PRODUCT = "saveUserProducts"
        private const val UPDATE_USER_PRODUCT = "updateUserProducts"

        private const val USER_ACTIVITY_COLLECTION = "userActivity"
        private const val SAVE_USER_ACTIVITY = "saveUserActivity"
        private const val UPDATE_USER_ACTIVITY = "updateUserActivity"

    }

}