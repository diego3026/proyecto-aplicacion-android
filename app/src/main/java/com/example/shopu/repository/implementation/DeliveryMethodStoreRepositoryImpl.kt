package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.DeliveryMethodStore
import com.example.mercaditu.model.dto.DeliveryMethodStoreDto
import com.example.mercaditu.repository.DeliveryMethodStoreRepository
import io.github.jan.supabase.gotrue.SessionSource
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeliveryMethodStoreRepositoryImpl (private val postgrest: Postgrest, private val storage: SessionSource.Storage) :
    DeliveryMethodStoreRepository {
    override suspend fun createDeliveryMethodStore(deliveryMethodStore: DeliveryMethodStore): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val deliveryMethodStoreDto = DeliveryMethodStoreDto(
                    idDeliveryMethod = deliveryMethodStore.idDeliveryMethod,
                    idStore = deliveryMethodStore.idStore,
                )
                postgrest.from("metodos_de_entrega_por_tienda").insert(deliveryMethodStoreDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getAllDeliveryMethodStore(): List<DeliveryMethodStoreDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("metodos_de_entrega_por_tienda")
                .select().decodeList<DeliveryMethodStoreDto>()
            result
        }
    }

    override suspend fun getDeliveryMethodStore(id: String): DeliveryMethodStoreDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("metodos_de_entrega_por_tienda").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<DeliveryMethodStoreDto>()
        }
    }

    override suspend fun deleteDeliveryMethodStore(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("metodos_de_entrega_por_tienda").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateDeliveryMethodStore(
        id: String,
        idStore: String,
        idDeliveryMethod: String
    ) {
        withContext(Dispatchers.IO) {
            if (idStore == null && idDeliveryMethod == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (idStore != null) updates["name"] = idStore
            if (idDeliveryMethod != null) updates["price"] = idDeliveryMethod

            postgrest.from("metodos_de_entrega_por_tienda").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }
    }
}