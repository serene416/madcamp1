// ui/tab2/SecondTab.kt
package com.example.myapplication.ui.tab2

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import com.example.myapplication.data.City
import com.example.myapplication.data.TripLength
import com.example.myapplication.data.TripPlan
import com.example.myapplication.data.TripPlanFactory
import com.example.myapplication.data.toLatLng

@Composable
fun SecondTab() {
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var selectedLength by remember { mutableStateOf<TripLength?>(null) }
    var tripPlan by remember { mutableStateOf<TripPlan?>(null) }
    var showRestaurants by remember { mutableStateOf(false) }

    // ✅ DayPagerScreen 진입 시 스낵바 1회 띄우기용 플래그
    var showSwipeHintOnce by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (showRestaurants) {
            val city = selectedCity ?: return@Scaffold
            val ll = city.toLatLng()

            RestaurantListScreen(
                title = "${city.name} 주변 맛집",
                lat = ll.lat,
                lng = ll.lng,
                onBack = { showRestaurants = false }
            )
            return@Scaffold
        }

        if (tripPlan == null) {
            SelectionScreen(
                modifier = Modifier.padding(padding),
                selectedCity = selectedCity,
                selectedLength = selectedLength,
                onSelectCity = { selectedCity = it },
                onSelectLength = { selectedLength = it },
                onGoNext = {
                    if (selectedCity == null) {
                        scope.launch { snackbarHostState.showSnackbar("도시를 골라주세요") }
                        return@SelectionScreen
                    }
                    if (selectedLength == null) {
                        scope.launch { snackbarHostState.showSnackbar("기간을 골라주세요") }
                        return@SelectionScreen
                    }

                    val city = selectedCity!!
                    val length = selectedLength!!

                    // ✅ 다음 화면으로 넘어가면서 스낵바 1회 띄우도록 예약
                    showSwipeHintOnce = true

                    // ✅ 바로 DayPagerScreen으로 전환
                    tripPlan = TripPlanFactory.create(city, length)
                },
                onGoRestaurants = {
                    if (selectedCity == null) {
                        scope.launch { snackbarHostState.showSnackbar("도시를 먼저 골라주세요") }
                        return@SelectionScreen
                    }
                    showRestaurants = true
                }
            )
        } else {
            // ✅ DayPagerScreen "진입 직후" 1회만 스낵바 표시
            LaunchedEffect(showSwipeHintOnce) {
                if (showSwipeHintOnce) {
                    snackbarHostState.showSnackbar("옆으로 스와이프해서 일정을 살펴보세요")
                    showSwipeHintOnce = false
                }
            }

            DayPagerScreen(
                plan = tripPlan!!,
                onBack = {
                    tripPlan = null
                    // (선택) 뒤로 갈 때 플래그도 초기화하고 싶으면 아래 유지
                    // showSwipeHintOnce = false
                }
            )
        }
    }
}
