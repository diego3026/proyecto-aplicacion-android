package com.example.mercaditu.model.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class StoreDto(
    @SerialName("id")
    val id: String? = null,

    @SerialName("nameStore")
    val nameStore: String,

    @SerialName("adress")
    val address: String,

    @SerialName("location")
    val location: String,

    @SerialName("start_time")
    @Contextual
    val startTime: Date,

    @SerialName("end_time")
    @Contextual
    val endTime: Date,

    @SerialName("open_now")
    val openNow: Boolean,

    @SerialName("cover_image")
    val coverImage: String,

    @SerialName("logo")
    val logo: String
)
