package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.Category
import com.example.mercaditu.model.dto.CategoryDto
import com.example.mercaditu.repository.CategoryRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepositoryImpl(private val postgrest: Postgrest, private val storage: Storage) :
    CategoryRepository {

    override suspend fun createCategory(category: Category): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val categoryDto = CategoryDto(
                    name = category.name
                )
                postgrest.from("products").insert(categoryDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }    }

    override suspend fun getAllCategory(): List<CategoryDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("categorias")
                .select().decodeList<CategoryDto>()
            result
        }
    }

    override suspend fun getCategory(id: String): CategoryDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("categorias").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<CategoryDto>()
        }    }

    override suspend fun deleteCategory(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("categorias").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateCategory(id: String, name: String) {
        withContext(Dispatchers.IO) {
            if (name == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (name != null) updates["name"] = name

            postgrest.from("categorias").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }    }

}