package com.example.mercaditu.repository

import com.example.mercaditu.model.DeliveryMethodStore
import com.example.mercaditu.model.dto.DeliveryMethodStoreDto

interface DeliveryMethodStoreRepository {
    suspend fun createDeliveryMethodStore(deliveryMethodStore: DeliveryMethodStore): Boolean
    suspend fun getAllDeliveryMethodStore(): List<DeliveryMethodStoreDto>?
    suspend fun getDeliveryMethodStore(id: String): DeliveryMethodStoreDto
    suspend fun deleteDeliveryMethodStore(id: String)
    suspend fun updateDeliveryMethodStore(
        id: String,
        idStore: String,
        idDeliveryMethod: String
    )
}