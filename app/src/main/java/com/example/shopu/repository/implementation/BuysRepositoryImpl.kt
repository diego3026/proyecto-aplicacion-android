package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.Buys
import com.example.mercaditu.model.dto.BuysDto
import com.example.mercaditu.repository.BuysRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BuysRepositoryImpl (private val postgrest: Postgrest, private val storage: Storage) :
    BuysRepository {

    override suspend fun createBuys(buys: Buys): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val buysDto = BuysDto(
                    rating = buys.rating,
                    comment = buys.comment,
                    favorite = buys.favorite,
                    idPurchaseStatus = buys.idPurchaseStatus
                )
                postgrest.from("compras").insert(buysDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getAllBuys(): List<BuysDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("compras")
                .select().decodeList<BuysDto>()
            result
        }
    }

    override suspend fun getBuys(id: String): BuysDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("compras").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<BuysDto>()
        }
    }

    override suspend fun deleteBuys(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("compras").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateBuys(
        id: String,
        rating: Int,
        comment: String,
        favorite: Boolean,
        idPurchaseStatus: String
    ) {
        withContext(Dispatchers.IO) {
            if (rating == null && comment == null && favorite == null && idPurchaseStatus == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (rating != null) updates["name"] = rating
            if (comment != null) updates["price"] = comment
            if (favorite != null) updates["discount"] = favorite
            if (idPurchaseStatus != null) updates["description"] = idPurchaseStatus

            // Realizar la actualización PATCH solo con los campos que se han cambiado
            postgrest.from("compras").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }
    }
}