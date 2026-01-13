package com.example.myapplication.ui.tab3

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// authority는 네 프로젝트에 맞게 유지
const val FILE_PROVIDER_AUTHORITY = "com.example.myapplication.fileprovider"

/** 앱 내부 저장소(filesDir)에서 폴더 목록 로드 */
fun loadFolders(context: Context): List<File> =
    context.filesDir.listFiles()
        ?.filter { it.isDirectory }
        ?.sortedBy { it.name } // 보기 좋게 정렬(선택)
        ?: emptyList()

/** 폴더 안 사진 Uri 목록 로드 */
fun loadPhotos(folder: File): List<Uri> =
    folder.listFiles()
        ?.filter { it.isFile }
        ?.sortedByDescending { it.lastModified() } // 최신순(선택)
        ?.map { Uri.fromFile(it) }
        ?: emptyList()

/** 폴더 생성 */
fun createFolder(context: Context, name: String): File? {
    val trimmed = name.trim()
    if (trimmed.isBlank()) return null
    val folder = File(context.filesDir, trimmed)
    if (!folder.exists()) folder.mkdirs()
    return folder
}

/** 폴더 삭제(내부 사진 포함) */
fun deleteFolder(folder: File): Boolean = folder.deleteRecursively()

/** 사진 삭제 */
fun deletePhoto(uri: Uri): Boolean {
    val path = uri.path ?: return false
    return File(path).delete()
}

/** 카메라 저장용 Uri 생성 */
fun createPhotoUri(
    context: Context,
    currentFolder: File?
): Uri {
    val dir = currentFolder ?: context.filesDir
    val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        FILE_PROVIDER_AUTHORITY,
        file
    )
}
