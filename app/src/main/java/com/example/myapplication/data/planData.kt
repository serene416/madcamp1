package com.example.myapplication.data

enum class City { TOKYO, OSAKA, FUKUOKA, SAPPORO, NAGOYA }
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