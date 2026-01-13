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

    val showCreateFolderDialog: Boolean = false,
    val newFolderName: String = "",

    val showDeleteFolderDialog: Boolean = false,
    val folderToDelete: File? = null
)

class CameraViewModel(app: Application) : AndroidViewModel(app) {
    private val context = app.applicationContext

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    /** 앱 시작/탭 진입 시 폴더 목록 로드 */
    fun loadInitial() {
        _uiState.update { it.copy(folders = loadFolders(context)) }
    }

    /** 폴더 클릭 → 폴더 열고 사진 로드 */
    fun openFolder(folder: File) {
        _uiState.update {
            it.copy(
                currentFolder = folder,
                photoUris = loadPhotos(folder),
                selectedUri = null
            )
        }
    }

    /** 뒤로가기(폴더 목록으로) */
    fun backToFolderList() {
        _uiState.update {
            it.copy(
                currentFolder = null,
                photoUris = emptyList(),
                selectedUri = null
            )
        }
    }

    /** 사진 선택/해제 */
    fun selectPhoto(uri: Uri?) {
        _uiState.update { it.copy(selectedUri = uri) }
    }

    /** 촬영 성공 시 사진 추가 */
    fun addCapturedPhoto(uri: Uri) {
        _uiState.update { it.copy(photoUris = it.photoUris + uri) }
    }

    /** 폴더 생성 다이얼로그 on/off */
    fun showCreateDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateFolderDialog = show) }
    }

    /** 새 폴더명 입력 */
    fun setNewFolderName(name: String) {
        _uiState.update { it.copy(newFolderName = name) }
    }

    /** 폴더 생성 확정 */
    fun confirmCreateFolder() {
        val name = uiState.value.newFolderName
        val folder = createFolder(context, name)

        _uiState.update { cur ->
            if (folder == null) {
                cur.copy(newFolderName = "", showCreateFolderDialog = false)
            } else {
                cur.copy(
                    folders = (cur.folders + folder).distinctBy { it.absolutePath }.sortedBy { it.name },
                    newFolderName = "",
                    showCreateFolderDialog = false
                )
            }
        }
    }

    /** 폴더 삭제 다이얼로그 띄우기(롱클릭) */
    fun askDeleteFolder(folder: File) {
        _uiState.update {
            it.copy(
                folderToDelete = folder,
                showDeleteFolderDialog = true
            )
        }
    }

    /** 폴더 삭제 다이얼로그 취소 */
    fun cancelDeleteFolder() {
        _uiState.update { it.copy(folderToDelete = null, showDeleteFolderDialog = false) }
    }

    /** 폴더 삭제 확정 */
    fun confirmDeleteFolder() {
        val target = uiState.value.folderToDelete ?: return
        deleteFolder(target)

        _uiState.update { cur ->
            cur.copy(
                folders = cur.folders.filterNot { it.absolutePath == target.absolutePath },
                folderToDelete = null,
                showDeleteFolderDialog = false,
                currentFolder = null,
                photoUris = emptyList(),
                selectedUri = null
            )
        }
    }

    /** 선택된 사진 삭제 */
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
}
