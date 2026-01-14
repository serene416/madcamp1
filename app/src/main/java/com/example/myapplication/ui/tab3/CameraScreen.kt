package com.example.myapplication.ui.tab3
///ui수정
import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import java.io.File
import com.example.myapplication.ui.theme.AppStyle

@Composable
fun CameraScreen(
    vm: CameraViewModel
) {
    val context = LocalContext.current
    val state by vm.uiState.collectAsState()

    // 최초 폴더 로드
    LaunchedEffect(Unit) { vm.loadInitial() }

    // 카메라/권한 런처는 UI에 남겨도 됨(시스템 연동)
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) tempPhotoUri?.let(vm::addCapturedPhoto)
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                val uri = createPhotoUri(context, state.currentFolder)
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
                text = state.currentFolder?.name ?: "여행 사진 기록",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (state.currentFolder == null) {

                Button(
                    onClick = { vm.showCreateDialog(true) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppStyle.Colors.primary,
                        contentColor = Color.Black
                    ),

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
                    items(state.folders) { folder ->
                        FolderGridItem(
                            folder = folder,
                            onClick = { vm.openFolder(folder) },
                            onLongClick = { vm.askDeleteFolder(folder) }
                        )
                    }
                }

            } else {

                OutlinedButton(onClick = { vm.backToFolderList() }) {
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
                    items(state.photoUris) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { vm.selectPhoto(uri) },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // FAB
            FloatingActionButton(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = AppStyle.Colors.primary,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.CameraAlt, null)
            }


        // 사진 확대 + 삭제
        state.selectedUri?.let { uri ->
            PhotoPreviewDialog(
                uri = uri,
                onDismiss = { vm.selectPhoto(null) },
                onDelete = { vm.deleteSelectedPhoto() }
            )
        }

        // 폴더 생성 다이얼로그
        if (state.showCreateFolderDialog) {
            CreateFolderDialog(
                folderName = state.newFolderName,
                onChange = vm::setNewFolderName,
                onDismiss = { vm.showCreateDialog(false) },
                onConfirm = { vm.confirmCreateFolder() }
            )
        }

        // 폴더 삭제 다이얼로그
        if (state.showDeleteFolderDialog && state.folderToDelete != null) {
            DeleteFolderDialog(
                folderName = state.folderToDelete!!.name,
                onDismiss = { vm.cancelDeleteFolder() },
                onConfirm = { vm.confirmDeleteFolder() }
            )
        }
    }
}


