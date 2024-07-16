package com.example.shopu.data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String,

    @SerialName("price")
    val price: Double,

    @SerialName("description")
    val description: String,

    @SerialName("image")
    val image: String?,

    @SerialName("discount")
    val discount: Int?,

    @SerialName("id_tienda")
    val idTienda: String,

    @SerialName("id_categoria")
    val idCategory: String,
)
