package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.DeliveryMethod
import com.example.mercaditu.model.dto.DeliveryMethodDto
import com.example.mercaditu.repository.DeliveryMethodRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeliveryMethodRepositoryImpl (private val postgrest: Postgrest, private val storage: Storage) :
    DeliveryMethodRepository {
    override suspend fun createDeliveryMethod(deliveryMethod: DeliveryMethod): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val deliveryMethodDto = DeliveryMethodDto(
                    name = deliveryMethod.name,
                    cost = deliveryMethod.cost
                )
                postgrest.from("compras").insert(deliveryMethodDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getAllDeliveryMethod(): List<DeliveryMethodDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("metodos_de_entrega")
                .select().decodeList<DeliveryMethodDto>()
            result
        }
    }

    override suspend fun getDeliveryMethod(id: String): DeliveryMethodDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("metodos_de_entrega").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<DeliveryMethodDto>()
        }
    }

    override suspend fun deleteDeliveryMethod(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("metodos_de_entrega").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateDeliveryMethod(id: String, name: String, cost: Double) {
        withContext(Dispatchers.IO) {
            if (name == null && cost == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (name != null) updates["name"] = name
            if (cost != null) updates["price"] = cost

            postgrest.from("metodos_de_entrega").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }
    }
}