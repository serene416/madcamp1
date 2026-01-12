package com.example.myapplication.ui.tab2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.PlacesRepository
import com.example.myapplication.network.PlaceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Tab2UiState(
    val loading: Boolean = false,
    val items: List<PlaceResult> = emptyList(),
    val error: String? = null
)
// 스팟 하나에 들어갈 맛집 하나를 받아 관리하는 뷰모델
class Tab2ViewModel(
    private val repo: PlacesRepository = PlacesRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(Tab2UiState())
    val state: StateFlow<Tab2UiState> = _state

    fun load(lat: Double, lng: Double) {
        viewModelScope.launch {
            _state.value = Tab2UiState(loading = true)

            try {
                val one = repo.nearbyRestaurant(lat, lng) // PlaceResult?
                _state.value = Tab2UiState(items = listOfNotNull(one))
            } catch (e: Exception) {
                _state.value = Tab2UiState(error = e.message ?: "Unknown error")
            }
        }
    }
}

