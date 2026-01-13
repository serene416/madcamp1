package com.example.myapplication.ui.tab2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.DayPlan
import com.example.myapplication.data.SpotDetail
import com.example.myapplication.data.TripPlan
import com.example.myapplication.data.toLatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * 여행 계획에 포함된 모든 관광지를 지도에 표시하는 화면
 * @param plan 전체 여행 계획
 * @param onBack 뒤로가기 버튼 클릭 시 호출될 함수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(plan: TripPlan, onBack: () -> Unit) {
    // Day별로 마커 색상을 다르게 하기 위한 HUE(색상) 값 리스트
    val dayHues = listOf(
        0f,    // 빨강
        30f,   // 주황
        60f,   // 노랑
        120f,  // 초록
        180f,  // 청록
        210f,  // 하늘
        240f,  // 파랑
        270f,  // 보라
        300f,  // 자홍
        330f   // 장미
    )

    // 카메라 초기 위치를 도시의 중심으로 설정
    val cityCoords = plan.city.toLatLng()
    val cityLatLng = LatLng(cityCoords.lat, cityCoords.lng)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityLatLng, 11f)
    }

    // [수정] 선택된 스팟 정보를 DayPlan과 SpotDetail 쌍으로 저장
    var selectedInfo by remember { mutableStateOf<Pair<DayPlan, SpotDetail>?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${plan.city.name} 전체 경로") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                plan.days.forEachIndexed { dayIndex, dayPlan ->
                    val markerHue = dayHues[dayIndex % dayHues.size]

                    dayPlan.spots.forEach { spot ->
                        val spotLatLng = LatLng(spot.lat, spot.lng)

                        Marker(
                            state = MarkerState(position = spotLatLng),
                            title = spot.name, // 기본 정보창 제목
                            onClick = {
                                // [수정] 클릭 시 Day 정보와 스팟 정보를 함께 저장
                                selectedInfo = dayPlan to spot
                                true
                            },
                            icon = BitmapDescriptorFactory.defaultMarker(markerHue)
                        )
                    }
                }
            }

            // 하단 정보 카드
            selectedInfo?.let { (dayPlan, spot) ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // [수정] Day 정보와 스팟 이름을 함께 표시
                        Text(text = "Day ${dayPlan.day}: ${spot.name}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = spot.description)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = { selectedInfo = null }) {
                                Text("닫기")
                            }
                        }
                    }
                }
            }
        }
    }
}
