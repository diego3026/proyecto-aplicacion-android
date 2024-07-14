package com.example.mercaditu.model

import java.sql.Time

data class Store(
    val id: String,
    val nameStore: String,
    val address: String,
    val location: String,
    val startTime: Time,
    val endTime: Time,
    val openNow: Boolean,
    val coverImage: String,
    val logo: String
)
