
package com.example.myapplication.data

import com.example.myapplication.data.cities.buildFukuokaPlan
import com.example.myapplication.data.cities.buildNagoyaPlan
import com.example.myapplication.data.cities.buildOsakaPlan
import com.example.myapplication.data.cities.buildSapporoPlan
import com.example.myapplication.data.cities.buildTokyoPlan

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
