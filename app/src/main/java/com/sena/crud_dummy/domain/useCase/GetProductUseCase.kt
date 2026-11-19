package com.sena.crud_dummy.domain.useCase

import com.sena.crud_dummy.domain.model.ProductModel
import com.sena.crud_dummy.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<ProductModel> = repository.getProducts()
    suspend fun byId(id: Int): ProductModel = repository.getProductById(id)
}
