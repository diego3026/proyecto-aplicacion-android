package com.example.mercaditu.repository

import com.example.mercaditu.model.Extra
import com.example.mercaditu.model.dto.ExtraDto

interface ExtraRepository {
    suspend fun createExtra(extra: Extra): Boolean
    suspend fun getAllExtra(): List<ExtraDto>?
    suspend fun getExtra(id: String): ExtraDto
    suspend fun deleteExtra(id: String)
    suspend fun updateExtra(
        id: String,
        name: String,
        cost: Double,
        idProduct: String
    )
}