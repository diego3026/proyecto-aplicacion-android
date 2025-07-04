package com.example.shopu.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseStatusDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String
)
