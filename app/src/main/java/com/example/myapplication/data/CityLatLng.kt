package com.example.myapplication.data

data class LatLng(val lat: Double, val lng: Double)

fun City.toLatLng(): LatLng = when (this) {
    City.TOKYO -> LatLng(35.681236, 139.767125)   // 도쿄역
    City.OSAKA -> LatLng(34.702485, 135.495951)   // 오사카역
    City.FUKUOKA -> LatLng(33.590355, 130.401716) // 하카타역
    City.SAPPORO -> LatLng(43.062096, 141.354376) // 삿포로역
    City.NAGOYA -> LatLng(35.170915, 136.881537)  // 나고야역
}
