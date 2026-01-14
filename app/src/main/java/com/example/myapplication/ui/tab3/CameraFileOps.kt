package com.example.myapplication.ui.tab3

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

const val FILE_PROVIDER_AUTHORITY = "com.example.myapplication.fileprovider"

/** 폴더 목록 */
fun loadFolders(context: Context): List<File> =
    context.filesDir.listFiles()
        ?.filter { it.isDirectory }
        ?.sortedBy { it.name }
        ?: emptyList()

/** 폴더 안 사진 목록 */
fun loadPhotos(folder: File): List<Uri> =
    folder.listFiles()
        ?.filter { it.isFile && it.name != "cover.jpg" }
        ?.sortedByDescending { it.lastModified() }
        ?.map { Uri.fromFile(it) }
        ?: emptyList()

/** ⭐ 폴더 썸네일 가져오기 */
fun getFolderThumbnail(folder: File): Uri? {
    val cover = File(folder, "cover.jpg")
    return if (cover.exists()) {
        Uri.fromFile(cover)
    } else {
        folder.listFiles()
            ?.firstOrNull { it.isFile && it.name != "cover.jpg" }
            ?.let { Uri.fromFile(it) }
    }
}

/** 현재 썸네일인지 확인 */
fun isThumbnail(folder: File, uri: Uri): Boolean {
    val cover = File(folder, "cover.jpg")
    return cover.exists() && cover.absolutePath == uri.path
}

/** 폴더 생성 */
fun createFolder(context: Context, name: String): File? {
    val trimmed = name.trim()
    if (trimmed.isBlank()) return null
    val folder = File(context.filesDir, trimmed)
    if (!folder.exists()) folder.mkdirs()
    return folder
}

/** 폴더 삭제 */
fun deleteFolder(folder: File): Boolean = folder.deleteRecursively()

/** 사진 삭제 */
fun deletePhoto(uri: Uri): Boolean =
    uri.path?.let { File(it).delete() } ?: false

/** 카메라 저장용 Uri */
fun createPhotoUri(context: Context, currentFolder: File?): Uri {
    val dir = currentFolder ?: context.filesDir
    val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
}
