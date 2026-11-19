package com.sena.crud_dummy.data.remote.dto.product

import com.squareup.moshi.Json

data class PatchProduct(
    @param:Json(name = "category")
    val category : String,
)
