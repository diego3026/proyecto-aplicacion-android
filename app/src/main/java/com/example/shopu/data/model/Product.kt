package com.example.shopu.data.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val discount: Int?,
    val idTienda: String,
    val idCategory: String,
    val image: String
)