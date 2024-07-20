package com.example.shopu.data.network

import com.example.shopu.ui.register.model.UserRegister
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserService @Inject constructor(private val firebase: FirebaseClient) {

    companion object {
        const val USER_COLLECTION = "users"
    }

    suspend fun createUserTable(userRegister: UserRegister) = runCatching {

        val user = hashMapOf(
            "email" to userRegister.email,
            "fullname" to userRegister.name,
        )

        firebase.db
            .collection(USER_COLLECTION)
            .add(user).await()

    }.isSuccess
}