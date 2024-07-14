package com.example.mercaditu.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuysDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("rating")
    val rating: Int,

    @SerialName("comment")
    val comment: String,

    @SerialName("favorite")
    val favorite: Boolean,

    @SerialName("id_purchase_status")
    val idPurchaseStatus: String
)
