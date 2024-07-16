package com.example.shopu.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryMethodStoreDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("id_store")
    val idStore: String,

    @SerialName("id_delivery_method")
    val idDeliveryMethod: String
)
