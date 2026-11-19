package com.sena.crud_dummy.data.mapper

import com.sena.crud_dummy.data.remote.dto.product.ProductX
import com.sena.crud_dummy.domain.model.ProductModel
import java.util.Locale

private val categorySpanishMap = mapOf(
    "beauty" to "Belleza",
    "fragrances" to "Fragancias",
    "furniture" to "Muebles",
    "groceries" to "Comestibles",
    "home-decoration" to "Decoración de Hogar",
    "kitchen-accessories" to "Accesorios de Cocina",
    "laptops" to "Portátiles",
    "mens-shirts" to "Camisas de Hombre",
    "mens-shoes" to "Zapatos de Hombre",
    "mens-watches" to "Relojes de Hombre",
    "mobile-accessories" to "Accesorios de Móvil",
    "motorcycle" to "Motocicletas",
    "skin-care" to "Cuidado de la Piel",
    "smartphones" to "Teléfonos Inteligentes",
    "sports-accessories" to "Deportes",
    "sunglasses" to "Gafas de Sol",
    "tops" to "Ropa",
    "vehicle" to "Vehículos",
    "womens-bags" to "Bolsos de Mujer",
    "womens-dresses" to "Vestidos de Mujer",
    "womens-jewellery" to "Joyería de Mujer",
    "womens-shoes" to "Zapatos de Mujer",
    "womens-watches" to "Relojes de Mujer"
)

private val titleTranslations = mapOf(
    "Essence Mascara Lash Princess" to "Máscara de Pestañas Essence Lash Princess",
    "Eyeshadow Palette with Mirror" to "Paleta de Sombras con Espejo",
    "Powder Canister" to "Polvo Compacto Facial",
    "Red Lipstick" to "Lápiz Labial Rojo Intense",
    "Red Nail Polish" to "Esmalte de Uñas Rojo",
    "Calvin Klein CK One" to "Perfume Calvin Klein CK One",
    "Chanel Coco Mademoiselle" to "Perfume Chanel Coco Mademoiselle",
    "Dior J'adore" to "Perfume Dior J'adore",
    "Dolce Shine Eau de Parfum" to "Perfume Dolce Shine Eau de Parfum",
    "Gucci Bloom Eau de Parfum" to "Perfume Gucci Bloom Eau de Parfum",
    "Annibale Colombo Bed" to "Cama de Lujo Annibale Colombo",
    "Annibale Colombo Sofa" to "Sofá Elegante Annibale Colombo",
    "Bedside Table Nightstand" to "Mesa de Noche para Dormitorio",
    "Knoll Saarinen Executive Chair" to "Silla Ejecutiva Knoll Saarinen",
    "Wooden Bathroom Sink Cabinet" to "Mueble de Baño de Madera"
)

fun ProductX.toDomain(): ProductModel {
    val originalTitle = title.orEmpty().trim()
    val translatedTitle = titleTranslations[originalTitle] ?: translateTitleWords(originalTitle)

    val rawCategory = category.orEmpty().trim().lowercase(Locale.ROOT)
    val translatedCategory = categorySpanishMap[rawCategory]
        ?: rawCategory.replace("-", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }

    val translatedDescription = translateDescriptionText(description.orEmpty())

    return ProductModel(
        id = id ?: 0,
        title = translatedTitle,
        description = translatedDescription,
        category = translatedCategory.ifBlank { "General" },
        price = price ?: 0.0,
        imageUrl = thumbnail?.ifBlank { images?.firstOrNull().orEmpty() }.orEmpty()
    )
}

private fun translateTitleWords(title: String): String {
    if (title.isBlank()) return "Producto"

    return title
        .replace("Mascara", "Máscara de Pestañas", ignoreCase = true)
        .replace("Eyeshadow Palette", "Paleta de Sombras", ignoreCase = true)
        .replace("Powder Canister", "Polvo Compacto", ignoreCase = true)
        .replace("Lipstick", "Lápiz Labial", ignoreCase = true)
        .replace("Nail Polish", "Esmalte de Uñas", ignoreCase = true)
        .replace("Eau de Parfum", "Perfume", ignoreCase = true)
        .replace("Bedside Table", "Mesa de Noche", ignoreCase = true)
        .replace("Nightstand", "Mesa de Noche", ignoreCase = true)
        .replace("Bed", "Cama", ignoreCase = true)
        .replace("Sofa", "Sofá", ignoreCase = true)
        .replace("Executive Chair", "Silla Ejecutiva", ignoreCase = true)
        .replace("Chair", "Silla", ignoreCase = true)
        .replace("Table", "Mesa", ignoreCase = true)
        .replace("Sink Cabinet", "Mueble de Lavabo", ignoreCase = true)
        .replace("Cabinet", "Mueble", ignoreCase = true)
}

private fun translateDescriptionText(desc: String): String {
    if (desc.isBlank()) return "Sin descripción disponible."

    return desc
        .replace("The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects.", "La máscara de pestañas Essence Lash Princess es muy popular por sus efectos de volumen y alargamiento.", ignoreCase = true)
        .replace("The Eyeshadow Palette with Mirror offers a versatile range of shades for creating various eye looks.", "La paleta de sombras con espejo ofrece una gama versátil de tonos para crear diversos estilos de maquillaje.", ignoreCase = true)
        .replace("The Powder Canister is a finely milled setting powder designed to set makeup and control shine.", "El polvo compacto está diseñado para fijar el maquillaje y controlar el brillo facial.", ignoreCase = true)
        .replace("The Red Lipstick is a classic and bold lipstick that adds a pop of color to your lips.", "El lápiz labial rojo es un clásico intenso que aporta un toque vibrante a tus labios.", ignoreCase = true)
        .replace("The Red Nail Polish offers a rich and vibrant red color for a polished and stylish nail look.", "El esmalte de uñas rojo ofrece un color vibrante para un acabado elegante.", ignoreCase = true)
        .replace("is a popular fragrance", "es una fragancia popular", ignoreCase = true)
        .replace("is a classic fragrance", "es una fragancia clásica", ignoreCase = true)
        .replace("known for its", "conocida por su", ignoreCase = true)
        .replace("notes of", "notas de", ignoreCase = true)
        .replace("crafted with", "elaborado con", ignoreCase = true)
        .replace("high-quality", "alta calidad", ignoreCase = true)
        .replace("for everyday use", "para uso diario", ignoreCase = true)
}
