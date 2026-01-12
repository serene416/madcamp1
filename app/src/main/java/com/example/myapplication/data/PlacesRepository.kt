package com.example.myapplication.data

import com.example.myapplication.network.PlaceResult
import com.example.myapplication.network.PlacesClient
import com.example.myapplication.BuildConfig

class PlacesRepository {

    /**
     * 주변 맛집 1개만 가져옵니다(스팟 1개당 맛집 1개 정책).
     * - Google Places Nearby Search 결과에서 첫 번째(또는 평점순 정렬 후 첫 번째)만 사용.
     *
     * 이걸 tab2viewmodel에서 호출함.
     **/
    suspend fun nearbyRestaurant(lat: Double, lng: Double): PlaceResult? {
        val location = "$lat,$lng"

        val res = PlacesClient.api.nearbyRestaurants(
            location = location,
            key = BuildConfig.PLACES_API_KEY
        )

        if (res.status != "OK") {
            // ZERO_RESULTS도 여기로 올 수 있음
            return null
        }

        // “스팟당 1개” 정책: 그냥 첫 번째 사용
        return res.results.firstOrNull()
    }
}
