package com.example.mercaditu.repository

import com.example.mercaditu.model.DeliveryMethod
import com.example.mercaditu.model.dto.DeliveryMethodDto

interface DeliveryMethodRepository {
    suspend fun createDeliveryMethod(deliveryMethod: DeliveryMethod): Boolean
    suspend fun getAllDeliveryMethod(): List<DeliveryMethodDto>?
    suspend fun getDeliveryMethod(id: String): DeliveryMethodDto
    suspend fun deleteDeliveryMethod(id: String)
    suspend fun updateDeliveryMethod(
        id: String,
        name: String,
        cost: Double
    )
}