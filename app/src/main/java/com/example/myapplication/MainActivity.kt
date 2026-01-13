package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.data.City
import com.example.myapplication.data.DayPlan
import com.example.myapplication.data.TripLength
import com.example.myapplication.data.TripPlan
import com.example.myapplication.data.TripPlanFactory
import com.example.myapplication.data.toLatLng
import com.example.myapplication.network.placePhotoUrl
import com.example.myapplication.ui.tab2.MapScreen
import com.example.myapplication.ui.tab2.RestaurantListScreen
import com.example.myapplication.ui.tab2.SpotRestaurantViewModel
import com.example.myapplication.network.placePhotoUrl
import com.example.myapplication.ui.tab2.SpotRestaurantUiState
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.maps.MapsInitializer
import kotlinx.coroutines.launch
import java.io.File
import com.example.myapplication.ui.bottomui.BottomNavBarOverlay
import com.example.myapplication.ui.bottomui.BottomTab
import com.example.myapplication.ui.bottomui.TabContent
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.myapplication.ui.tab2.DayPagerScreen
import com.example.myapplication.ui.tab2.SelectionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LEGACY, null)
        }

        setContent {
            MyApplicationTheme {
                App()
            }
        }
    }
}

@Composable
private fun App() {
    var currentTab by rememberSaveable { mutableStateOf(com.example.myapplication.ui.bottomui.BottomTab.FIRST) }

    Scaffold(
        containerColor = Color.Transparent, // 1. Scaffold 배경을 투명하게 만듭니다.
        bottomBar = {
            BottomNavBarOverlay(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { innerPadding ->
        // 2. 콘텐츠 영역에만 배경색을 지정하고 Scaffold가 계산한 패딩을 적용합니다.
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            TabContent(currentTab)
        }
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


// ----------------2번 탭---------------


@Composable
fun SecondTab() {
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var selectedLength by remember { mutableStateOf<TripLength?>(null) }
    var tripPlan by remember { mutableStateOf<TripPlan?>(null) }
    var showRestaurants by remember { mutableStateOf(false) }

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
            DayPagerScreen(
                plan = tripPlan!!,
                onBack = { tripPlan = null }
            )
        }
    }
}


//---------------3탭 --------------------
@Composable
fun CameraTab() {
    val context = LocalContext.current

    // 📁 폴더 목록
    val folders = remember { mutableStateListOf<File>() }
    var currentFolder by remember { mutableStateOf<File?>(null) }

    // 🖼️ 사진 목록
    val photoUris = remember { mutableStateListOf<Uri>() }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    // 📸 카메라
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // ➕ 폴더 생성
    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var newFolderName by remember { mutableStateOf("") }

    // 🗑️ 폴더 삭제
    var folderToDelete by remember { mutableStateOf<File?>(null) }
    var showDeleteFolderDialog by remember { mutableStateOf(false) }

    /** 앱 시작 시 폴더 로드 */
    LaunchedEffect(Unit) {
        folders.clear()
        context.filesDir.listFiles()
            ?.filter { it.isDirectory }
            ?.forEach { folders.add(it) }
    }

    /** 사진 URI 생성 */
    fun createPhotoUri(): Uri {
        val dir = currentFolder ?: context.filesDir
        val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "com.example.myapplication.fileprovider",
            file
        )
    }

    /** 카메라 런처 */
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempPhotoUri?.let { photoUris.add(it) }
            }
        }

    /** 권한 요청 */
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
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
            Text(
                text = currentFolder?.name ?: "여행 사진 기록",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (currentFolder == null) {

                Button(
                    onClick = { showCreateFolderDialog = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("폴더 만들기")
                }

                Spacer(Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(folders) { folder ->
                        FolderGridItem(
                            folder = folder,
                            onClick = {
                                currentFolder = folder
                                photoUris.clear()
                                folder.listFiles()?.forEach {
                                    photoUris.add(Uri.fromFile(it))
                                }
                            },
                            onLongClick = {
                                folderToDelete = folder
                                showDeleteFolderDialog = true
                            }
                        )
                    }
                }

            } else {

                OutlinedButton(onClick = {
                    currentFolder = null
                    photoUris.clear()
                }) {
                    Icon(Icons.Default.ArrowBack, null)
                    Spacer(Modifier.width(8.dp))
                    Text("폴더 목록")
                }

                Spacer(Modifier.height(12.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photoUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { selectedUri = uri },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        /** 📸 FAB */
        if (currentFolder != null) {
            FloatingActionButton(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.CameraAlt, null)
            }
        }

        /** 🔍 사진 크게 보기 + 삭제 */
        selectedUri?.let { uri ->
            Dialog(onDismissRequest = { selectedUri = null }) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.75f),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                File(uri.path!!).delete()
                                photoUris.remove(uri)
                                selectedUri = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Delete, null)
                            Spacer(Modifier.width(8.dp))
                            Text("삭제")
                        }
                    }
                }
            }
        }

        /** ➕ 폴더 생성 다이얼로그 */
        if (showCreateFolderDialog) {
            AlertDialog(
                onDismissRequest = { showCreateFolderDialog = false },
                title = { Text("새 폴더") },
                text = {
                    OutlinedTextField(
                        value = newFolderName,
                        onValueChange = { newFolderName = it },
                        placeholder = { Text("예: 도쿄 1일차") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newFolderName.isNotBlank()) {
                            val folder = File(context.filesDir, newFolderName)
                            folder.mkdirs()
                            folders.add(folder)
                        }
                        newFolderName = ""
                        showCreateFolderDialog = false
                    }) { Text("생성") }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateFolderDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }

        /** 🗑️ 폴더 삭제 다이얼로그 */
        if (showDeleteFolderDialog && folderToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteFolderDialog = false },
                title = { Text("폴더 삭제") },
                text = {
                    Text("정말 \"${folderToDelete!!.name}\" 폴더를 삭제할까요?\n사진도 함께 삭제됩니다.")
                },
                confirmButton = {
                    TextButton(onClick = {
                        folderToDelete!!.deleteRecursively()
                        folders.remove(folderToDelete)
                        folderToDelete = null
                        showDeleteFolderDialog = false
                    }) {
                        Text("삭제", color = Color(0xFFF44336))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        folderToDelete = null
                        showDeleteFolderDialog = false
                    }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}



// 📸 폴더 썸네일 (첫 번째 사진)
fun folderThumbnail(folder: File): Uri? {
    return folder.listFiles()
        ?.firstOrNull { it.isFile }
        ?.let { Uri.fromFile(it) }
}

@Composable
fun FolderGridItem(
    folder: File,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val thumbnail = folder.listFiles()?.firstOrNull()?.let { Uri.fromFile(it) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            Box(Modifier.weight(1f)) {
                if (thumbnail != null) {
                    AsyncImage(
                        model = thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xFFECEFF1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Image, null)
                    }
                }
            }
            Text(
                folder.name,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


