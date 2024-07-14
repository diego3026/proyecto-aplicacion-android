package com.example.mercaditu.repository

import com.example.mercaditu.model.Buys
import com.example.mercaditu.model.dto.BuysDto
import com.example.mercaditu.model.dto.ProductDto

interface BuysRepository {
    suspend fun createBuys(buys: Buys): Boolean
    suspend fun getAllBuys(): List<BuysDto>?
    suspend fun getBuys(id: String): BuysDto
    suspend fun deleteBuys(id: String)
    suspend fun updateBuys(
        id: String,
        rating: Int,
        comment: String,
        favorite: Boolean,
        idPurchaseStatus: String
    )
}