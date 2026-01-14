package com.example.myapplication.ui.tab3

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.io.File
import com.example.myapplication.ui.theme.AppStyle
import androidx.compose.ui.graphics.Color

@Composable
fun CameraScreen(vm: CameraViewModel) {

    val context = LocalContext.current
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) { vm.loadInitial() }

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

        // ================== MAIN CONTENT ==================
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
                        Box {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .combinedClickable(
                                        onClick = { vm.selectPhoto(uri) },
                                        onLongClick = { vm.askSetThumbnail(uri) }
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            if (
                                state.currentFolder != null &&
                                isThumbnail(state.currentFolder!!, uri)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(6.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "대표",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        // ================== FAB ==================
        if (state.currentFolder != null) {
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


            // ================== DIALOGS ==================
            state.selectedUri?.let {
                PhotoPreviewDialog(
                    uri = it,
                    onDismiss = { vm.selectPhoto(null) },
                    onDelete = { vm.deleteSelectedPhoto() }
                )
            }

            if (state.showCreateFolderDialog) {
                CreateFolderDialog(
                    folderName = state.newFolderName,
                    onChange = vm::setNewFolderName,
                    onDismiss = { vm.showCreateDialog(false) },
                    onConfirm = { vm.confirmCreateFolder() }
                )
            }

            if (state.showDeleteFolderDialog && state.folderToDelete != null) {
                DeleteFolderDialog(
                    folderName = state.folderToDelete!!.name,
                    onDismiss = { vm.cancelDeleteFolder() },
                    onConfirm = { vm.confirmDeleteFolder() }
                )
            }

            if (state.showThumbnailDialog && state.thumbnailCandidate != null) {
                AlertDialog(
                    onDismissRequest = { vm.cancelSetThumbnail() },
                    title = { Text("대표 사진 설정") },
                    text = { Text("이 사진을 폴더 대표 이미지로 설정할까요?") },
                    confirmButton = {
                        TextButton(onClick = { vm.confirmSetThumbnail() }) {
                            Text("설정")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { vm.cancelSetThumbnail() }) {
                            Text("취소")
                        }
                    }
                )
            }
        }
    }
}

