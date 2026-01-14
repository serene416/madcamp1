@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.ui.tab2

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// -------------------- 스타일 토큰(여기만 바꾸면 전체 톤이 바뀜) --------------------
private val PinkPrimary = Color(0xFFF5C1CA)      // 버튼/선택 포인트(기존 유지)
private val PinkSelectedChip = Color(0xFFFBE9EA) // 기간칩 선택 배경(기존 유지)
private val SurfaceSoft = Color(0xFFF7F7F7)      // 비선택 배경
private val BorderSoft = Color(0xFFE6E6E6)
private val PlaceholderBg = Color(0xFFECEFF1)

private val ScreenPadding = 24.dp
private val SectionGap = 12.dp
private val ItemGap = 8.dp

private val ChipHeight = 40.dp
private val CtaHeight = 52.dp
private val CityButtonHeight = 46.dp

private val RadiusCard = 18.dp
private val RadiusPill = 999.dp
private val ImageRadius = 14.dp

// -------------------- 1) SelectionScreen (스타일링만 변경) --------------------
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
    val chipItems = listOf(
        TripLength.D3_4 to "3~4일",
        TripLength.D4_5 to "4~5일",
        TripLength.D5_6 to "5~6일",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(SectionGap)
    ) {
        SectionTitle("기간 선택")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ItemGap)
        ) {
            chipItems.forEach { (length, label) ->
                LengthChip(
                    modifier = Modifier.weight(1f),
                    text = label,
                    selected = selectedLength == length,
                    onClick = { onSelectLength(length) }
                )
            }
        }

        Spacer(Modifier.height(4.dp))
        SectionTitle("도시 선택")

        // 체크표시 제거 + 선택 시 배경색 채움(핑크) + 부드러운 색 전환
        Column(verticalArrangement = Arrangement.spacedBy(ItemGap)) {
            City.values().forEach { city ->
                val isSelected = selectedCity == city
                SelectPillButton(
                    text = city.name,
                    selected = isSelected,
                    onClick = { onSelectCity(city) }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // CTA: 기존 동작 유지(enabled 조건 동일), 스타일만 정리
        Button(
            onClick = onGoNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(CtaHeight),
            enabled = selectedLength != null && selectedCity != null,
            shape = RoundedCornerShape(RadiusCard),
            colors = ButtonDefaults.buttonColors(
                containerColor = PinkPrimary,
                contentColor = Color.Black,
                disabledContainerColor = Color(0xFFF0DDE0),
                disabledContentColor = Color(0xFF777777)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 6.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text("루트 보기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// -------------------- 2) DayPagerScreen (스타일링만 변경) --------------------
@Composable
fun DayPagerScreen(plan: TripPlan, onBack: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { plan.days.size })
    var showMap by remember { mutableStateOf(false) }

    if (showMap) {
        MapScreen(plan = plan, onBack = { showMap = false }) // 기능 동일
        return
    }

    Column(Modifier.fillMaxSize()) {
        TopBar(
            title = "${plan.city.name} - 일정",
            onLeft = onBack,
            onRight = { showMap = true }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val dayPlan = plan.days[page]
            DayDetailPage(dayPlan = dayPlan)
        }
    }
}

// -------------------- 3) DayDetailPage (카드/여백/텍스트/로딩 스타일링) --------------------
@Composable
fun DayDetailPage(dayPlan: DayPlan) {
    val vm: SpotRestaurantViewModel = viewModel()
    val state by vm.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(SectionGap)
    ) {
        item {
            Text(
                "Day ${dayPlan.day}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(dayPlan.spots) { spot ->
            val spotKey = "${dayPlan.day}-${spot.name}"

            LaunchedEffect(spotKey) {
                vm.loadForSpot(spotKey, spot) // 기능 동일
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(RadiusCard),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSoft),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(spot.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

                    if (spot.imageResId != null) {
                        Image(
                            painter = painterResource(id = spot.imageResId),
                            contentDescription = spot.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .clip(RoundedCornerShape(ImageRadius))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .clip(RoundedCornerShape(ImageRadius))
                                .background(PlaceholderBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("사진 준비 중", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Text(spot.description, fontSize = 14.sp, lineHeight = 19.sp)

                    HorizontalDivider(color = BorderSoft)

                    Text("주변 맛집 1곳", fontWeight = FontWeight.Bold)

                    when {
                        state.loading.contains(spotKey) -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(10.dp))
                                Text("불러오는 중…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        state.error[spotKey] != null -> {
                            Text(
                                "불러오기 실패: ${state.error[spotKey]}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {
                            val r = state.data[spotKey]
                            if (r == null) {
                                Text("근처 맛집 결과가 없습니다.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            } else {
                                OneRestaurantCard(r)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------- 4) OneRestaurantCard (이미지/행/클릭 스타일링) --------------------
@Composable
private fun OneRestaurantCard(r: com.example.myapplication.network.PlaceResult) {
    val context = LocalContext.current
    val photoRef = r.photos?.firstOrNull()?.photo_reference

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSoft),
        border = BorderStroke(1.dp, BorderSoft)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = r.place_id != null) {
                    r.place_id?.let { placeId ->
                        val uri = Uri.parse(
                            "https://www.google.com/maps/search/?api=1&query=${Uri.encode(r.name)}&query_place_id=$placeId"
                        )
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                        }
                    }
                }
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!photoRef.isNullOrBlank()) {
                AsyncImage(
                    model = placePhotoUrl(photoRef),
                    contentDescription = r.name,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(ImageRadius)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(ImageRadius))
                        .background(PlaceholderBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No\nImage", textAlign = TextAlign.Center, fontSize = 12.sp)
                }
            }

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(r.name ?: "(no name)", fontWeight = FontWeight.SemiBold)
                Text(
                    "평점: ${r.rating ?: "-"} · 리뷰: ${r.user_ratings_total ?: 0}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!r.vicinity.isNullOrBlank()) {
                    Text(r.vicinity, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // 미세한 힌트 아이콘(선택 사항) — 기능 영향 없음
            Icon(
                imageVector = Icons.Filled.Map,
                contentDescription = "지도",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------- 공통 재사용 UI(스타일링 전용) --------------------
@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun TopBar(
    title: String,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onLeft,
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
        ) { Text("뒤로") }

        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        IconButton(onClick = onRight) {
            Icon(Icons.Filled.Map, contentDescription = "지도 보기")
        }
    }
}

@Composable
private fun LengthChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = if (selected) PinkSelectedChip else SurfaceSoft,
        animationSpec = tween(durationMillis = 160),
        label = "chipBg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) PinkPrimary else BorderSoft,
        animationSpec = tween(durationMillis = 160),
        label = "chipBorder"
    )

    FilterChip(
        modifier = modifier.height(ChipHeight),
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(RadiusCard),
        border = BorderStroke(1.dp, border),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = bg,
            containerColor = bg,
            selectedLabelColor = Color.Black,
            labelColor = Color.Black
        ),
        label = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text, fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

@Composable
private fun SelectPillButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg by animateColorAsState(
        targetValue = if (selected) PinkPrimary else SurfaceSoft,
        animationSpec = tween(durationMillis = 160),
        label = "cityBg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) PinkPrimary else BorderSoft,
        animationSpec = tween(durationMillis = 160),
        label = "cityBorder"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(CityButtonHeight),
        shape = RoundedCornerShape(RadiusPill),
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, border),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (selected) 2.dp else 0.dp,
            pressedElevation = 6.dp
        )
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
