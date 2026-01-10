package com.example.myapplication

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.myapplication.data.City
import com.example.myapplication.data.DayPlan
import com.example.myapplication.data.SpotDetail
import com.example.myapplication.data.TripLength
import com.example.myapplication.data.TripPlan
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import com.example.myapplication.data.TripPlanFactory
import com.example.myapplication.network.PlacesClient
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                App()
            }
        }
    }
}

private enum class BottomTab { FIRST, SECOND, THIRD }

@Composable
private fun App() {
    var currentTab by remember { mutableStateOf(BottomTab.FIRST) }
    val imagesList = remember { mutableStateListOf<Bitmap>() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == BottomTab.FIRST,
                    onClick = { currentTab = BottomTab.FIRST },
                    icon = { Text("📄") },
                    label = { Text("추천") }
                )
                NavigationBarItem(
                    selected = currentTab == BottomTab.SECOND,
                    onClick = { currentTab = BottomTab.SECOND },
                    icon = { Text("🧭") },
                    label = { Text("경로") }
                )
                NavigationBarItem(
                    selected = currentTab == BottomTab.THIRD,
                    onClick = { currentTab = BottomTab.THIRD },
                    icon = { Text("📸") },
                    label = { Text("사진") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                BottomTab.FIRST -> FirstTabQuestionFlow()
                BottomTab.SECOND -> SecondTab()
                BottomTab.THIRD -> CameraTab()
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, fontSize = 20.sp)
    }
}

/* -------------------- 1번 탭 -------------------- */

private enum class Step { Q1, Q2, Q3, Q4, Q5, Q6, Q7, RESULT }

private enum class Region {
    TOKYO, OSAKA, FUKUOKA, SAPPORO, NAGOYA
}

@Composable
fun FirstTabQuestionFlow() {
    var step by remember { mutableStateOf(Step.Q1) }

    val scores = remember {
        mutableStateMapOf(
            Region.TOKYO to 0,
            Region.OSAKA to 0,
            Region.FUKUOKA to 0,
            Region.SAPPORO to 0,
            Region.NAGOYA to 0
        )
    }

    val tiePriority = listOf(
        Region.TOKYO,
        Region.OSAKA,
        Region.FUKUOKA,
        Region.SAPPORO,
        Region.NAGOYA
    )

    // ✅ 진행률 계산 (총 8단계)
    val progress = when (step) {
        Step.Q1 -> 1f / 8f
        Step.Q2 -> 2f / 8f
        Step.Q3 -> 3f / 8f
        Step.Q4 -> 4f / 8f
        Step.Q5 -> 5f / 8f
        Step.Q6 -> 6f / 8f
        Step.Q7 -> 7f / 8f
        Step.RESULT -> 1f
    }

    fun addScore(vararg regions: Region) {
        regions.forEach {
            scores[it] = scores.getValue(it) + 1
        }
    }

    val resultText = remember(step) {
        if (step == Step.RESULT) {
            scores.entries
                .sortedWith(
                    compareByDescending<Map.Entry<Region, Int>> { it.value }
                        .thenBy { tiePriority.indexOf(it.key) }
                )
                .take(2)
                .map { it.key }
                .joinToString(" / ") {
                    when (it) {
                        Region.TOKYO -> "도쿄"
                        Region.OSAKA -> "오사카"
                        Region.FUKUOKA -> "후쿠오카"
                        Region.SAPPORO -> "삿포로"
                        Region.NAGOYA -> "나고야"
                    }
                }
        } else ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // ✅ 제목 + 점만 게이지: 상단 고정 (bottom 줄임)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 28.dp), // 👈 bottom 줄임
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "일본 여행지 추천",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🎨 점만 게이지 (8개)
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(8) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp) // 👈 조금 크게
                            .clip(CircleShape)
                            .background(
                                color = if (index < (progress * 8).toInt()) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color(0xFFB0BEC5)
                                },
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        // ✅ 카드들: 나머지 공간 중앙 배치
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (step) {
                Step.Q1 -> QuestionCard("도시가 좋아? 자연이 좋아?") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("🏙️ 도시") {
                            addScore(Region.TOKYO, Region.OSAKA)
                            step = Step.Q2
                        }
                        ChoiceCard("🌿 자연") {
                            addScore(Region.FUKUOKA, Region.SAPPORO, Region.NAGOYA)
                            step = Step.Q2
                        }
                    }
                }

                Step.Q2 -> QuestionCard("온천 여행 좋아해?♨️") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("♨️ 좋아") {
                            addScore(Region.FUKUOKA)
                            step = Step.Q3
                        }
                        ChoiceCard("❌ 싫어") {
                            step = Step.Q3
                        }
                    }
                }

                Step.Q3 -> QuestionCard("눈 좋아해?❄️") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("❄️ 좋아") {
                            addScore(Region.SAPPORO)
                            step = Step.Q4
                        }
                        ChoiceCard("❌ 싫어") {
                            step = Step.Q4
                        }
                    }
                }

                Step.Q4 -> QuestionCard("하루 종일 쇼핑하는 거 좋아해?🛍️") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("🛍️ 좋아") {
                            addScore(Region.OSAKA)
                            step = Step.Q5
                        }
                        ChoiceCard("❌ 싫어") {
                            step = Step.Q5
                        }
                    }
                }

                Step.Q5 -> QuestionCard("절과 사찰의 차분한 분위기 좋아해?") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("🙏 좋아") {
                            addScore(Region.NAGOYA)
                            step = Step.Q6
                        }
                        ChoiceCard("❌ 싫어") {
                            step = Step.Q6
                        }
                    }
                }

                Step.Q6 -> QuestionCard("럭셔리한 여행 좋아해?✨") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("✨ 좋아") {
                            addScore(Region.TOKYO)
                            step = Step.Q7
                        }
                        ChoiceCard("❌ 싫어") {
                            step = Step.Q7
                        }
                    }
                }

                Step.Q7 -> QuestionCard("사람들이 많이 가는 여행지가 좋아?") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChoiceCard("👍 좋아") { step = Step.RESULT }
                        ChoiceCard("🤔 상관없어") { step = Step.RESULT }
                    }
                }

                Step.RESULT -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        ResultCard(result = resultText)

                        Button(
                            onClick = {
                                scores.keys.forEach { scores[it] = 0 }
                                step = Step.Q1
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("처음으로")
                        }
                    }
                }
            }
        }
    }
}

/* -------------------- UI 컴포넌트 -------------------- */

@Composable
fun QuestionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
fun ChoiceCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ResultCard(result: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4FF)),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("✈️", fontSize = 36.sp)
            Spacer(Modifier.height(12.dp))
            Text("추천 여행지", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(result, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }
    }
}


// 2번 탭, 도쿄부터


@Composable
fun SecondTab() {
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var selectedLength by remember { mutableStateOf<TripLength?>(null) }
    var tripPlan by remember { mutableStateOf<TripPlan?>(null) }

    // ✅ 스낵바 상태
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ✅ 화면에 SnackbarHost를 붙여줘야 실제로 뜹니다
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->

        if (tripPlan == null) {
            SelectionScreen(
                modifier = Modifier.padding(padding),
                selectedCity = selectedCity,
                selectedLength = selectedLength,
                onSelectCity = { selectedCity = it },
                onSelectLength = { selectedLength = it },
                onGoNext = {
                    // ✅ 1) 도시 미선택이면 막기
                    if (selectedCity == null) {
                        scope.launch { snackbarHostState.showSnackbar("도시를 골라주세요") }
                        return@SelectionScreen
                    }
                    // ✅ 2) 기간 미선택이면 막기
                    if (selectedLength == null) {
                        scope.launch { snackbarHostState.showSnackbar("기간을 골라주세요") }
                        return@SelectionScreen
                    }

                    // ✅ 둘 다 선택된 경우에만 다음으로
                    val city = selectedCity!!
                    val length = selectedLength!!

                    tripPlan = TripPlanFactory.create(city, length)
                }
            )
        } else {
            DayPagerScreen(
                plan = tripPlan!!,
                onBack = { tripPlan = null }
            )
        }
    }
}

@Composable  // 기간,장소 선택
fun SelectionScreen(
    modifier: Modifier = Modifier,
    selectedCity: City?,
    selectedLength: TripLength?,
    onSelectCity: (City) -> Unit,
    onSelectLength: (TripLength) -> Unit,
    onGoNext: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("기간 선택", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = selectedLength == TripLength.D3_4,
                onClick = { onSelectLength(TripLength.D3_4) },
                label = { Text("3~4일") }
            )
            FilterChip(
                selected = selectedLength == TripLength.D4_5,
                onClick = { onSelectLength(TripLength.D4_5) },
                label = { Text("4~5일") }
            )
            FilterChip(
                selected = selectedLength == TripLength.D5_6,
                onClick = { onSelectLength(TripLength.D5_6) },
                label = { Text("5~6일") }
            )
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

        // ✅ 버튼 1: 루트 보기
        Button(
            onClick = onGoNext,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedLength != null && selectedCity != null
        ) {
            Text("루트 보기")
        }

        // ✅ 버튼 2: 맛집 API 테스트 (루트 보기 버튼 밖에 따로!)
        Button(
            onClick = {
                scope.launch {
                    try {
                        val lat = 43.0621
                        val lng = 141.3544
                        val location = "$lat,$lng"

                        val key = "AIzaSyBOGwtZU5hBF00qhcqXcPh_YYcQNZATUJg"

                        val res = PlacesClient.api.nearbyRestaurants(
                            location = location,
                            key = key
                        )

                        println("status=${res.status}, error=${res.error_message}, count=${res.results.size}")
                        res.results.take(5).forEach { p ->
                            println("식당: ${p.name} / 평점: ${p.rating} / 리뷰수: ${p.user_ratings_total}")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        println("에러: ${e.message}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("맛집 API 테스트")
        }
    }
}

@Composable // 하루치 페이지 하나
fun DayPagerScreen(plan: TripPlan, onBack: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { plan.days.size })

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${plan.city.name} - 일정", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            OutlinedButton(onClick = onBack) { Text("뒤로") }
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

@Composable // 각 루트에서 관광지 페이지 하나
fun DayDetailPage(dayPlan: DayPlan) {
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
                }
            }
        }
    }
}

//----------------------3번탭----------------
//-----------------------------------------
@Composable
fun CameraTab() {
    val context = LocalContext.current
    val photoUris = remember { mutableStateListOf<Uri>() }

    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    // 앱 시작 시 기존 사진 로드
    LaunchedEffect(Unit) {
        context.filesDir.listFiles()
            ?.filter { it.name.startsWith("photo_") }
            ?.forEach { file ->
                photoUris.add(Uri.fromFile(file))
            }
    }

    fun createPhotoUri(): Uri {
        val file = File(
            context.filesDir,
            "photo_${System.currentTimeMillis()}.jpg"
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                tempPhotoUri?.let { uri ->
                    photoUris.add(uri)
                }
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                val uri = createPhotoUri()
                tempPhotoUri = uri
                cameraLauncher.launch(uri)
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("여행 사진 기록", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(photoUris.chunked(2)) { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(160.dp)
                                    .clickable { selectedUri = uri }
                            )
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("📸 사진 찍기", fontSize = 18.sp)
        }
    }

    selectedUri?.let { uri ->
        Dialog(onDismissRequest = { selectedUri = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.75f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            File(uri.path!!).delete()
                            photoUris.remove(uri)
                            selectedUri = null
                        }
                    ) {
                        Text("삭제")
                    }
                }
            }
        }
    }
}
