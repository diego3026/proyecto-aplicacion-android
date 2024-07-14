package com.example.mercaditu.repository

import com.example.mercaditu.model.Product
import com.example.mercaditu.model.dto.ProductDto

interface ProductRepository {
    suspend fun createProduct(product: Product): Boolean
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProductsByDiscount(): List<ProductDto>?
    suspend fun getProduct(id: String): ProductDto
    suspend fun deleteProduct(id: String)
    suspend fun updateProduct(
        id: String,
        name: String,
        price: Double,
        imageName: String,
        imageFile: ByteArray,
        discount: Int,
        description: String,
        idCategory: String
    )
}
