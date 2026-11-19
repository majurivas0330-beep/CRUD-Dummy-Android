package com.sena.crud_dummy.data.mapper

import com.sena.crud_dummy.data.remote.dto.product.ProductX
import com.sena.crud_dummy.domain.model.ProductModel

fun ProductX.toDomain(): ProductModel = ProductModel(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    imageUrl = thumbnail.ifBlank { images.firstOrNull().orEmpty()}
)