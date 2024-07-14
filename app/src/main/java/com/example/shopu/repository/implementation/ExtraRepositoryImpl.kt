package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.Extra
import com.example.mercaditu.model.dto.ExtraDto
import com.example.mercaditu.repository.ExtraRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExtraRepositoryImpl (private val postgrest: Postgrest, private val storage: Storage) :
    ExtraRepository {
    override suspend fun createExtra(extra: Extra): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val extraDto = ExtraDto(
                    name = extra.name,
                    cost = extra.cost,
                    idProduct = extra.idProduct
                )
                postgrest.from("extras").insert(extraDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getAllExtra(): List<ExtraDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("extras")
                .select().decodeList<ExtraDto>()
            result
        }
    }

    override suspend fun getExtra(id: String): ExtraDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("extras").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<ExtraDto>()
        }
    }

    override suspend fun deleteExtra(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("extras").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateExtra(id: String, name: String, cost: Double, idProduct: String) {
        withContext(Dispatchers.IO) {
            if (name == null && cost == null && idProduct == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (name != null) updates["name"] = name
            if (cost != null) updates["price"] = cost
            if (idProduct != null) updates["idProduct"] = idProduct

            postgrest.from("extras").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }    }
}