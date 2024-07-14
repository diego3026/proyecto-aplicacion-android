package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.PurchaseStatus
import com.example.mercaditu.model.dto.DeliveryMethodDto
import com.example.mercaditu.model.dto.PurchaseStatusDto
import com.example.mercaditu.repository.PurchaseStatusRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PurchaseStatusRepositoryImpl (private val postgrest: Postgrest, private val storage: Storage) :
    PurchaseStatusRepository {
    override suspend fun createPurchaseStatus(purchaseStatus: PurchaseStatus): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val purchaseStatusDto = PurchaseStatusDto(
                    name = purchaseStatus.name,
                )
                postgrest.from("estados_de_compra").insert(purchaseStatusDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }    }

    override suspend fun getAllPurchaseStatus(): List<PurchaseStatusDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("estados_de_compra")
                .select().decodeList<PurchaseStatusDto>()
            result
        }
    }

    override suspend fun getPurchaseStatus(id: String): PurchaseStatusDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("estados_de_compra").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<PurchaseStatusDto>()
        }
    }

    override suspend fun deletePurchaseStatus(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("estados_de_compra").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updatePurchaseStatus(id: String, name: String) {
        withContext(Dispatchers.IO) {
            if (name == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (name != null) updates["name"] = name

            postgrest.from("estados_de_compra").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }
    }
}