package com.example.myapplication.data

enum class City(val displayName: String) {
    TOKYO("도쿄"),
    OSAKA("오사카"),
    FUKUOKA("후쿠오카"),
    SAPPORO("삿포로"),
    NAGOYA("나고야")
}

enum class TripLength { D3_4, D4_5, D5_6 }

data class SpotDetail(
    val name: String,
    val description: String,
    val imageResId: Int? = null // drawable 연결 전이면 null 가능
)

data class DayPlan(
    val day: Int,
    val spots: List<SpotDetail>
)

data class TripPlan(
    val city: City,
    val length: TripLength,
    val days: List<DayPlan>
)