package com.example.shopu.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientDto (
    @SerialName("id")
    val id: String? = null,
)