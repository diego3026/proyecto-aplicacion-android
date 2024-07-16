package com.example.shopu.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtraDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String,

    @SerialName("cost")
    val cost: Double,

    @SerialName("id_product")
    val idProduct: String
)
