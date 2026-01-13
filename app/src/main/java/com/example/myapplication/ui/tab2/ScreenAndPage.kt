package com.example.myapplication.ui.tab2

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.data.City
import com.example.myapplication.data.DayPlan
import com.example.myapplication.data.TripLength
import com.example.myapplication.data.TripPlan
import com.example.myapplication.network.placePhotoUrl
import com.example.myapplication.ui.tab2.MapScreen
import com.example.myapplication.ui.tab2.SpotRestaurantViewModel

@Composable
fun SelectionScreen(
    modifier: Modifier = Modifier,
    selectedCity: City?,
    selectedLength: TripLength?,
    onSelectCity: (City) -> Unit,
    onSelectLength: (TripLength) -> Unit,
    onGoNext: () -> Unit,
    onGoRestaurants: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("기간 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = selectedLength == TripLength.D3_4, onClick = { onSelectLength(TripLength.D3_4) }, label = { Text("3~4일") })
            FilterChip(selected = selectedLength == TripLength.D4_5, onClick = { onSelectLength(TripLength.D4_5) }, label = { Text("4~5일") })
            FilterChip(selected = selectedLength == TripLength.D5_6, onClick = { onSelectLength(TripLength.D5_6) }, label = { Text("5~6일") })
        }

        Spacer(Modifier.height(8.dp))
        Text("도시 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            City.values().forEach { city ->
                ElevatedButton(
                    onClick = { onSelectCity(city) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedCity == city) "✓ ${city.name}" else city.name)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onGoNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = selectedLength != null && selectedCity != null
        ) { Text("루트 보기", fontSize = 16.sp) }
    }
}

@Composable
fun DayPagerScreen(plan: TripPlan, onBack: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { plan.days.size })
    var showMap by remember { mutableStateOf(false) }

    if (showMap) {
        MapScreen(plan = plan, onBack = { showMap = false })
        return
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("뒤로")
            }
            Text("${plan.city.name} - 일정", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { showMap = true }) {
                Icon(Icons.Filled.Map, contentDescription = "지도 보기")
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val dayPlan = plan.days[page]
            DayDetailPage(dayPlan = dayPlan)
        }
    }
}

@Composable
fun DayDetailPage(dayPlan: DayPlan) {
    val vm: SpotRestaurantViewModel = viewModel()
    val state by vm.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Day ${dayPlan.day}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        items(dayPlan.spots) { spot ->
            val spotKey = "${dayPlan.day}-${spot.name}" // 스팟별 유니크 키

            // ✅ 스팟 카드가 화면에 올라오면 1회 호출(캐싱으로 중복 방지)
            LaunchedEffect(spotKey) {
                vm.loadForSpot(spotKey, spot)
            }

            Card(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(spot.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                    if (spot.imageResId != null) {
                        Image(
                            painter = painterResource(id = spot.imageResId),
                            contentDescription = spot.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    } else {
                        Text("사진 준비 중", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Text(spot.description, fontSize = 14.sp)

                    HorizontalDivider()
                    Text("주변 맛집 1곳", fontWeight = FontWeight.Bold)

                    when {
                        state.loading.contains(spotKey) -> {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp))
                        }

                        state.error[spotKey] != null -> {
                            Text("불러오기 실패: ${state.error[spotKey]}")
                        }

                        else -> {
                            val r = state.data[spotKey]
                            if (r == null) Text("근처 맛집 결과가 없습니다.")
                            else OneRestaurantCard(r)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OneRestaurantCard(r: com.example.myapplication.network.PlaceResult) {
    val context = LocalContext.current
    val photoRef = r.photos?.firstOrNull()?.photo_reference

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = r.place_id != null) {
                r.place_id?.let { placeId ->
                    val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(r.name)}&query_place_id=$placeId")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps")
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback if Google Maps is not installed
                        val webIntent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(webIntent)
                    }
                }
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!photoRef.isNullOrBlank()) {
            AsyncImage(
                model = placePhotoUrl(photoRef),
                contentDescription = r.name,
                modifier = Modifier
                    .size(76.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFECEFF1)),
                contentAlignment = Alignment.Center
            ) {
                Text("No\nImage", textAlign = TextAlign.Center, fontSize = 12.sp)
            }
        }

        Column(Modifier.weight(1f)) {
            Text(r.name ?: "(no name)", fontWeight = FontWeight.SemiBold)
            Text(
                "평점: ${r.rating ?: "-"} · 리뷰: ${r.user_ratings_total ?: 0}",
                fontSize = 12.sp
            )
            if (!r.vicinity.isNullOrBlank()) {
                Text(r.vicinity, fontSize = 12.sp)
            }
        }
    }
}