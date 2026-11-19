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
    override suspend fun getProducts(): List<ProductModel> =
        api.GetProduct().products.map { it.toDomain() }

    override suspend fun getProductById(id: Int): ProductModel =
        api.GetProductById(id).toDomain()

    override suspend fun createProduct(product: ProductModel): ProductModel =
        api.AddProduct(product.toRequest()).toDomain()

    override suspend fun updateProduct(product: ProductModel): ProductModel =
        api.PatchProductById(product.id, product.toRequest()).toDomain()

    override suspend fun deleteProduct(id: Int): Boolean {
        api.DeleteProductById(id)
        return true
    }

    private fun ProductModel.toRequest() = UpdateProductRequest(
        title = title,
        description = description,
        category = category,
        price = price
    )
}
