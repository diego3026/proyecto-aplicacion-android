package com.example.mercaditu.repository

import com.example.mercaditu.model.Store
import com.example.mercaditu.model.dto.StoreDto
import java.sql.Time

interface StoreRepository {
    suspend fun createStore(store: Store): Boolean
    suspend fun getAllStore(): List<StoreDto>?
    suspend fun getStore(id: String): StoreDto
    suspend fun deleteStore(id: String)
    suspend fun updateStore(
        id: String,
        nameStore: String,
        address: String,
        location: String,
        startTime: Time,
        endTime: Time,
        openNow: Boolean,
        coverImage: String,
        coverImageFile: ByteArray,
        logoName: String,
        logoFile: ByteArray,
    )
}