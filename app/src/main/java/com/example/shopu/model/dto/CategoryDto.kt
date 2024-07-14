package com.example.mercaditu.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String
)
