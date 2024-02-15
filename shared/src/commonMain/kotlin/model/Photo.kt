package model

import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val id: String,
    val description: String? = null,
    val alt_description: String? = null,
    val urls: UrlsX
)