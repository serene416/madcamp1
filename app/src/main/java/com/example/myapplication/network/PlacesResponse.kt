package com.example.myapplication.network

data class PlacesResponse(
    val results: List<PlaceResult> = emptyList(),
    val status: String? = null,
    val error_message: String? = null
)

data class PlaceResult(
    val name: String? = null,
    val rating: Double? = null,
    val user_ratings_total: Int? = null,
    val vicinity: String? = null,
    val photos: List<PlacePhoto>? = null
)

data class PlacePhoto(
    val photo_reference: String? = null,
    val width: Int? = null,
    val height: Int? = null
)