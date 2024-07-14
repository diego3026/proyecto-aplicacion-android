package com.example.mercaditu.repository

import com.example.mercaditu.model.PurchaseStatus
import com.example.mercaditu.model.dto.PurchaseStatusDto

interface PurchaseStatusRepository {
    suspend fun createPurchaseStatus(purchaseStatus: PurchaseStatus): Boolean
    suspend fun getAllPurchaseStatus(): List<PurchaseStatusDto>?
    suspend fun getPurchaseStatus(id: String): PurchaseStatusDto
    suspend fun deletePurchaseStatus(id: String)
    suspend fun updatePurchaseStatus(
        id: String,
        name: String
    )
}