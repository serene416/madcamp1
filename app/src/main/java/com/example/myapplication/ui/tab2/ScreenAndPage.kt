package com.example.myapplication.ui.tab2

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import com.example.myapplication.ui.theme.AppStyle
import androidx.compose.foundation.BorderStroke
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
            .padding(AppStyle.Dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppStyle.Dimens.sectionGap)
    ) {
        SectionTitle("기간 선택")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppStyle.Dimens.itemGap)
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

        Column(verticalArrangement = Arrangement.spacedBy(AppStyle.Dimens.itemGap)) {
            City.values().forEach { city ->
                SelectPillButton(
                    text = city.name,
                    selected = selectedCity == city,
                    onClick = { onSelectCity(city) }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onGoNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppStyle.Dimens.ctaHeight),
            enabled = selectedLength != null && selectedCity != null,
            shape = RoundedCornerShape(AppStyle.Dimens.radiusCard),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppStyle.Colors.primary,
                contentColor = Color.Black,
                disabledContainerColor = AppStyle.Colors.disabledPrimary,
                disabledContentColor = AppStyle.Colors.disabledText
            )
        ) {
            Text("루트 보기", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
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

@Composable
fun DayDetailPage(dayPlan: DayPlan) {
    val vm: SpotRestaurantViewModel = viewModel()
    val state by vm.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppStyle.Dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppStyle.Dimens.sectionGap)
    ) {
        item {
            Text("Day ${dayPlan.day}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        items(dayPlan.spots) { spot ->
            val spotKey = "${dayPlan.day}-${spot.name}"

            LaunchedEffect(spotKey) {
                vm.loadForSpot(spotKey, spot)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppStyle.Dimens.radiusCard),
                colors = CardDefaults.cardColors(containerColor = AppStyle.Colors.cardBackground),
                border = BorderStroke(1.dp, AppStyle.Colors.borderSoft)
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
                                .clip(RoundedCornerShape(AppStyle.Dimens.imageRadius))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .clip(RoundedCornerShape(AppStyle.Dimens.imageRadius))
                                .background(AppStyle.Colors.placeholder),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("사진 준비 중")
                        }
                    }

                    Text(spot.description, fontSize = 14.sp)

                    HorizontalDivider(color = AppStyle.Colors.borderSoft)

                    Text("주변 맛집 1곳", fontWeight = FontWeight.Bold)

                    when {
                        state.loading.contains(spotKey) -> {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        }

                        state.error[spotKey] != null -> {
                            Text("불러오기 실패: ${state.error[spotKey]}", color = MaterialTheme.colorScheme.error)
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppStyle.Colors.surfaceSoft),
        border = BorderStroke(1.dp, AppStyle.Colors.borderSoft)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = r.place_id != null) {
                    r.place_id?.let { placeId ->
                        val uri = Uri.parse(
                            "https://www.google.com/maps/search/?api=1&query=${Uri.encode(r.name)}&query_place_id=$placeId"
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
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
                        .clip(RoundedCornerShape(AppStyle.Dimens.imageRadius)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(AppStyle.Dimens.imageRadius))
                        .background(AppStyle.Colors.placeholder),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No\nImage", textAlign = TextAlign.Center, fontSize = 12.sp)
                }
            }

            Column(Modifier.weight(1f)) {
                Text(r.name ?: "(no name)", fontWeight = FontWeight.SemiBold)
                Text("평점: ${r.rating ?: "-"} · 리뷰: ${r.user_ratings_total ?: 0}", fontSize = 12.sp)
                if (!r.vicinity.isNullOrBlank()) {
                    Text(r.vicinity, fontSize = 12.sp)
                }
            }
        }
    }
}

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
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // 여기서 TopBar 높이 확정
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onLeft,
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Black),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        ) { Text("뒤로") }

        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        TextButton(
            onClick = onRight,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🗺️", fontSize = 25.sp, lineHeight = 28.sp)
                Text("지도 보기", fontSize = 12.sp, lineHeight = 14.sp, maxLines = 1)
            }
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
        targetValue = if (selected) AppStyle.Colors.primarySoft else AppStyle.Colors.surfaceSoft,
        animationSpec = tween(160),
        label = "chipBg"
    )

    FilterChip(
        modifier = modifier.height(AppStyle.Dimens.chipHeight),
        selected = selected,
        onClick = onClick,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = bg,
            selectedContainerColor = bg
        ),
        label = {
            Text(text, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    )
}

@Composable
private fun SelectPillButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,

) {
    val bg by animateColorAsState(
        targetValue = if (selected) AppStyle.Colors.primarySoft else AppStyle.Colors.surfaceSoft,
        animationSpec = tween(160),
        label = "pillBg"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(AppStyle.Dimens.cityButtonHeight),
        shape = RoundedCornerShape(AppStyle.Dimens.radiusPill),
        colors = ButtonDefaults.buttonColors(containerColor = bg),
        elevation = null
    ) {
        Text(text,color = Color.Black)
    }
}
