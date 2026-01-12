package com.example.myapplication.network

import com.example.myapplication.BuildConfig
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun placePhotoUrl(photoRef: String, maxWidth: Int = 800): String {
    val ref = URLEncoder.encode(photoRef, StandardCharsets.UTF_8.toString())
    val key = URLEncoder.encode(BuildConfig.PLACES_API_KEY, StandardCharsets.UTF_8.toString())
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=$maxWidth&photoreference=$ref&key=$key"
}
