package com.example.mercaditu.repository.implementation

import com.example.mercaditu.model.Product
import com.example.mercaditu.model.dto.ProductDto
import com.example.mercaditu.repository.ProductRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl (private val postgrest: Postgrest, private val storage: Storage) : ProductRepository {
    override suspend fun createProduct(product: Product): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val productDto = ProductDto(
                    name = product.name,
                    price = product.price,
                    description = product.description,
                    discount = product.discount,
                    image = product.image,
                    idCategory = product.idCategory,
                    idTienda = product.idTienda,
                )
                postgrest.from("productos").insert(productDto)
                true
            }
            true
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getProducts(): List<ProductDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("productos")
                .select().decodeList<ProductDto>()
            result
        }
    }

    override suspend fun getProductsByDiscount(): List<ProductDto>? {
        return withContext(Dispatchers.IO) {
            val result = postgrest.from("productos")
                .select {
                    filter {
                        gt("discount", 0)
                    }
                }.decodeList<ProductDto>()
            result
        }
    }

    override suspend fun getProduct(id: String): ProductDto {
        return withContext(Dispatchers.IO) {
            postgrest.from("productos").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<ProductDto>()
        }
    }

    override suspend fun deleteProduct(id: String) {
        return withContext(Dispatchers.IO) {
            postgrest.from("products").delete {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    override suspend fun updateProduct(
        id: String,
        name: String,
        price: Double,
        imageName: String,
        imageFile: ByteArray,
        discount: Int,
        description: String,
        idCategory: String
    ) {
        withContext(Dispatchers.IO) {
            if (name == null && price == null && imageName == null && imageFile == null &&
                discount == null && description == null && idCategory == null) {
                return@withContext  // No hay cambios para actualizar
            }

            val updates = mutableMapOf<String, Any?>()

            if (name != null) updates["nombre"] = name
            if (price != null) updates["precio"] = price
            if (discount != null) updates["descuento"] = discount
            if (description != null) updates["descripcion"] = description

            if (idCategory != null) {
                updates["idCategoria"] = idCategory
            }

            // IdproductoImage
            if (imageFile != null && imageName != null) {
                // Subir imagen si hay datos de imagen
                val imageUrl = storage.from("imagenes").upload(
                    path = "productos/${id}productoImage.png",
                    data = imageFile,
                    upsert = true
                )
                updates["imagen"] = buildImageUrl(imageFileName = imageUrl)
            }

            // Realizar la actualización PATCH solo con los campos que se han cambiado
            postgrest.from("productos").update(updates) {
                filter {
                    eq("id", id)
                }
            }
        }
    }

    // Because I named the bucket as "Product Image" so when it turns to an url, it is "%20"
    // For better approach, you should create your bucket name without space symbol
    private fun buildImageUrl(imageFileName: String) =
        "https://wcfvebhpiszdvvvjywxy.supabase.co/storage/v1/object/public/${imageFileName}".replace(" ", "%20")
}
