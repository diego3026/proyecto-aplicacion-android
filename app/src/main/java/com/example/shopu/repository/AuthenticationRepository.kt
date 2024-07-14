package com.example.mercaditu.repository

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String, name: String): Boolean
    suspend fun signInWithGoogle(): Boolean
    suspend fun logOut(): Boolean
    suspend fun resetPassword(email: String): Boolean
}