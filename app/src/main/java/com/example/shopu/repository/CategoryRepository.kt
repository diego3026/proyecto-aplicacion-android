package com.example.mercaditu.repository

import com.example.mercaditu.model.Category
import com.example.mercaditu.model.dto.CategoryDto

interface CategoryRepository {
    suspend fun createCategory(category: Category): Boolean
    suspend fun getAllCategory(): List<CategoryDto>?
    suspend fun getCategory(id: String): CategoryDto
    suspend fun deleteCategory(id: String)
    suspend fun updateCategory(
        id: String,
        name: String
    )
}