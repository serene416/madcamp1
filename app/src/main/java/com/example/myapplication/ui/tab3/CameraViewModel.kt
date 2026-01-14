package com.example.myapplication.ui.tab3

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.File

data class CameraUiState(
    val folders: List<File> = emptyList(),
    val currentFolder: File? = null,
    val photoUris: List<Uri> = emptyList(),
    val selectedUri: Uri? = null,

    // 폴더 생성
    val showCreateFolderDialog: Boolean = false,
    val newFolderName: String = "",

    // 폴더 삭제
    val showDeleteFolderDialog: Boolean = false,
    val folderToDelete: File? = null,

    // ⭐ 썸네일 설정
    val showThumbnailDialog: Boolean = false,
    val thumbnailCandidate: Uri? = null
)

class CameraViewModel(app: Application) : AndroidViewModel(app) {

    private val context = app.applicationContext

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    /** 초기 로드 */
    fun loadInitial() {
        _uiState.update { it.copy(folders = loadFolders(context)) }
    }

    /** 폴더 열기 */
    fun openFolder(folder: File) {
        _uiState.update {
            it.copy(
                currentFolder = folder,
                photoUris = loadPhotos(folder),
                selectedUri = null
            )
        }
    }

    /** 폴더 목록으로 */
    fun backToFolderList() {
        _uiState.update {
            it.copy(
                currentFolder = null,
                photoUris = emptyList(),
                selectedUri = null
            )
        }
    }

    /** 사진 선택 */
    fun selectPhoto(uri: Uri?) {
        _uiState.update { it.copy(selectedUri = uri) }
    }

    /** 사진 촬영 후 순서대로 추가 */
    fun addCapturedPhoto(uri: Uri) {
        _uiState.update {
            it.copy(photoUris = listOf(uri) + it.photoUris)
        }
    }


    // ================= 폴더 생성 =================

    fun showCreateDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateFolderDialog = show) }
    }

    fun setNewFolderName(name: String) {
        _uiState.update { it.copy(newFolderName = name) }
    }

    fun confirmCreateFolder() {
        val name = uiState.value.newFolderName
        val folder = createFolder(context, name)

        _uiState.update { cur ->
            if (folder == null) {
                cur.copy(
                    newFolderName = "",
                    showCreateFolderDialog = false
                )
            } else {
                cur.copy(
                    folders = (cur.folders + folder)
                        .distinctBy { it.absolutePath }
                        .sortedBy { it.name },
                    newFolderName = "",
                    showCreateFolderDialog = false
                )
            }
        }
    }

    // ================= 폴더 삭제 =================

    fun askDeleteFolder(folder: File) {
        _uiState.update {
            it.copy(
                folderToDelete = folder,
                showDeleteFolderDialog = true
            )
        }
    }

    fun cancelDeleteFolder() {
        _uiState.update {
            it.copy(
                folderToDelete = null,
                showDeleteFolderDialog = false
            )
        }
    }

    fun confirmDeleteFolder() {
        val target = uiState.value.folderToDelete ?: return
        deleteFolder(target)

        _uiState.update { cur ->
            cur.copy(
                folders = cur.folders.filterNot {
                    it.absolutePath == target.absolutePath
                },
                folderToDelete = null,
                showDeleteFolderDialog = false,
                currentFolder = null,
                photoUris = emptyList(),
                selectedUri = null
            )
        }
    }

    // ================= 사진 삭제 =================

    fun deleteSelectedPhoto() {
        val uri = uiState.value.selectedUri ?: return
        val ok = deletePhoto(uri)

        _uiState.update { cur ->
            if (ok) {
                cur.copy(
                    photoUris = cur.photoUris.filterNot { it == uri },
                    selectedUri = null
                )
            } else {
                cur.copy(selectedUri = null)
            }
        }
    }

    // ================= ⭐ 썸네일 설정 =================

    fun askSetThumbnail(uri: Uri) {
        _uiState.update {
            it.copy(
                showThumbnailDialog = true,
                thumbnailCandidate = uri
            )
        }
    }

    fun cancelSetThumbnail() {
        _uiState.update {
            it.copy(
                showThumbnailDialog = false,
                thumbnailCandidate = null
            )
        }
    }

    fun confirmSetThumbnail() {
        val folder = uiState.value.currentFolder ?: return
        val uri = uiState.value.thumbnailCandidate ?: return

        val src = File(uri.path ?: return)
        val cover = File(folder, "cover.jpg")
        src.copyTo(cover, overwrite = true)

        _uiState.update {
            it.copy(
                folders = loadFolders(context),
                showThumbnailDialog = false,
                thumbnailCandidate = null
            )
        }
    }
}
