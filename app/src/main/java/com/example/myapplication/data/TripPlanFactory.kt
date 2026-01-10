
package com.example.myapplication.data

object TripPlanFactory {
    fun create(city: City, length: TripLength): TripPlan {
        return when (city) {
            City.TOKYO -> buildTokyoPlan(length)
            City.OSAKA -> buildOsakaPlan(length)
            City.FUKUOKA -> buildFukuokaPlan(length)
            City.SAPPORO -> buildSapporoPlan(length)
            City.NAGOYA -> buildNagoyaPlan(length)
        }
    }
}
