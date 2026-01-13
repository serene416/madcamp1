package com.example.myapplication.ui.tab3

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@Composable
fun PhotoPreviewDialog(
    uri: Uri,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
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
                    onClick = onDelete,
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

@Composable
fun CreateFolderDialog(
    folderName: String,
    onChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 폴더") },
        text = {
            OutlinedTextField(
                value = folderName,
                onValueChange = onChange,
                placeholder = { Text("예: 도쿄 1일차") }
            )
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text("생성") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("취소") } }
    )
}

@Composable
fun DeleteFolderDialog(
    folderName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("폴더 삭제") },
        text = { Text("정말 \"$folderName\" 폴더를 삭제할까요?\n사진도 함께 삭제됩니다.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("삭제", color = Color(0xFFF44336))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("취소") } }
    )
}
