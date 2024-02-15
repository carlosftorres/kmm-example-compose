package model

import kotlinx.serialization.Serializable

@Serializable
data class PhotosResponse(
    val results: List<Photo>,
    val total: Int,
    val total_pages: Int
)