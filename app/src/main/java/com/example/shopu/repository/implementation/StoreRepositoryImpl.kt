package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.Store
import com.example.mercaditu.model.dto.ProductDto
import com.example.mercaditu.model.dto.StoreDto
import com.example.mercaditu.repository.StoreRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Time

class StoreRepositoryImpl (private val postgrest: Postgrest, private val storage: Storage) :
    StoreRepository {
    override suspend fun createStore(store: Store): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val storeDto = StoreDto(
                    nameStore = store.nameStore,
                    address = store.address,
                    endTime = store.endTime,
                    startTime = store.startTime,
                    location = store.location,
                    openNow = store.openNow,
                    coverImage = store.coverImage,
                    logo = store.logo
                )
                postgrest.from("compras").insert(storeDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getAllStore(): List<StoreDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("tiendas")
                .select().decodeList<StoreDto>()
            result
        }
    }

    override suspend fun getStore(id: String): StoreDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("tiendas").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<StoreDto>()
        }
    }

    override suspend fun deleteStore(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("tiendas").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateStore(
        id: String,
        nameStore: String,
        address: String,
        location: String,
        startTime: Time,
        endTime: Time,
        openNow: Boolean,
        coverImage: String,
        coverImageFile: ByteArray,
        logoName: String,
        logoFile: ByteArray
    ) {
        withContext(Dispatchers.IO) {
            if (nameStore == null && address == null && location == null && startTime == null &&
                endTime == null && openNow == null && coverImage == null && coverImageFile == null
                && logoName == null && logoFile == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (nameStore != null) updates["nombre"] = nameStore
            if (address != null) updates["direccion"] = address
            if (location != null) updates["ubicacion"] = location
            if (startTime != null) updates["hora_de_inicio"] = startTime
            if (startTime != null) updates["hora_de_fin"] = endTime
            if (openNow != null) updates["abierto_ahora"] = openNow

            // IdproductoImage
            if (coverImage != null && coverImageFile != null) {
                // Subir imagen si hay datos de imagen
                val imageUrl = storage.from("imagenes").upload(
                    path = "tiendas/${id}portada.png",
                    data = coverImageFile,
                    upsert = true
                )
                updates["portada"] = buildImageUrl(imageFileName = imageUrl)
            }

            if (logoName != null && logoFile != null) {
                // Subir imagen si hay datos de imagen
                val imageUrl = storage.from("imagenes").upload(
                    path = "tiendas/${id}logo.png",
                    data = logoFile,
                    upsert = true
                )
                updates["logo"] = buildImageUrl(imageFileName = imageUrl)
            }

            postgrest.from("tiendas").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }
    }
    private fun buildImageUrl(imageFileName: String) =
        "https://wcfvebhpiszdvvvjywxy.supabase.co/storage/v1/object/public/${imageFileName}".replace(" ", "%20")
}