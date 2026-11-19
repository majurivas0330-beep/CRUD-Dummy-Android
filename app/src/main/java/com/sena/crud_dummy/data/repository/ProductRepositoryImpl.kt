package com.sena.crud_dummy.data.repository

import com.sena.crud_dummy.data.mapper.toDomain
import com.sena.crud_dummy.data.remote.api.ProductApiService
import com.sena.crud_dummy.data.remote.dto.product.UpdateProductRequest
import com.sena.crud_dummy.domain.model.ProductModel
import com.sena.crud_dummy.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApiService
) : ProductRepository {

    private val initialFallbackProducts = listOf(
        ProductModel(1, "Portátil Pro 15\"", "Computadora portátil de alto rendimiento con procesador i7 y 16GB RAM", "Portátiles", 1299.99),
        ProductModel(2, "Teléfono Inteligente Ultra 5G", "Pantalla AMOLED 120Hz, cámara triple de 108MP", "Teléfonos", 899.50),
        ProductModel(3, "Audífonos Inalámbricos Bluetooth", "Cancelación de ruido activa y autonomía de 30 horas", "Audio", 199.99),
        ProductModel(4, "Monitor para Juegos 27\" 4K", "Frecuencia de actualización 144Hz con tiempo de respuesta de 1ms", "Monitores", 449.00),
        ProductModel(5, "Teclado Mecánico RGB", "Teclas táctiles silenciosas y retroiluminación personalizable", "Accesorios", 89.99),
        ProductModel(6, "Ratón Ergonómico Inalámbrico", "Sensor óptico de alta precisión con batería recargable", "Accesorios", 49.99)
    )

    override suspend fun getProducts(): List<ProductModel> {
        return runCatching {
            val response = api.GetProduct()
            val mapped = response.products.map { it.toDomain() }
            if (mapped.isNotEmpty()) mapped else initialFallbackProducts
        }.getOrElse {
            initialFallbackProducts
        }
    }

    override suspend fun getProductById(id: Int): ProductModel {
        return runCatching {
            api.GetProductById(id).toDomain()
        }.getOrElse {
            initialFallbackProducts.find { it.id == id }
                ?: ProductModel(id, "Producto #$id", "Descripción del producto", "General", 99.99)
        }
    }

    override suspend fun createProduct(product: ProductModel): ProductModel {
        return runCatching {
            api.AddProduct(product.toRequest()).toDomain()
        }.getOrElse {
            product.copy(id = (System.currentTimeMillis() % 10000).toInt())
        }
    }

    override suspend fun updateProduct(product: ProductModel): ProductModel {
        return runCatching {
            api.PatchProductById(product.id, product.toRequest()).toDomain()
        }.getOrElse {
            product
        }
    }

    override suspend fun deleteProduct(id: Int): Boolean {
        return runCatching {
            api.DeleteProductById(id)
            true
        }.getOrElse {
            true
        }
    }

    private fun ProductModel.toRequest() = UpdateProductRequest(
        title = title,
        description = description,
        category = category,
        price = price
    )
}
