package com.example.myapplication.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun nearbyRestaurants(
        @Query("location") location: String, // "lat,lng"
        @Query("radius") radius: Int = 1200,
        @Query("type") type: String = "restaurant",
        @Query("key") key: String
    ): PlacesResponse
}
