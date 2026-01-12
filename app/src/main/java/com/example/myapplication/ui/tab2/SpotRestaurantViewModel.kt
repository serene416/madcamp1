package com.example.myapplication.ui.tab2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.PlacesRepository
import com.example.myapplication.data.SpotDetail
import com.example.myapplication.network.PlaceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
//각각의 스팟에 대해 개별적으로 맛집 비동기 로딩. 관리
data class SpotRestaurantUiState(
    val loading: Set<String> = emptySet(),
    val data: Map<String, PlaceResult?> = emptyMap(),
    val error: Map<String, String?> = emptyMap()
)

class SpotRestaurantViewModel(
    private val repo: PlacesRepository = PlacesRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(SpotRestaurantUiState())
    val state: StateFlow<SpotRestaurantUiState> = _state.asStateFlow()

    /**
     * spotKey 기준으로:
     * - 이미 로딩 중이면 return
     * - 이미 data에 있으면(성공/실패 포함 캐시) return
     */
    // 특정 스팟에 대한 맛집 정보 로드 요청 처리 함수
    fun loadForSpot(spotKey: String, spot: SpotDetail) {
        val cur = _state.value
        if (cur.loading.contains(spotKey) || cur.data.containsKey(spotKey)) return
        //중복 요청 방지(한 스팟당 api 요청 한번만 허용)
        viewModelScope.launch {
            _state.value = cur.copy(loading = cur.loading + spotKey)
            //로딩 상태를 추가하여 ui에서 이걸 보고 spot단위로 로딩창 띄움
            runCatching {
                repo.nearbyRestaurant(spot.lat, spot.lng) // ✅ 단건(실제 api호출)
            }.onSuccess { one: PlaceResult? ->
                val now = _state.value
                _state.value = now.copy(
                    loading = now.loading - spotKey,
                    data = now.data + (spotKey to one),
                    error = now.error - spotKey
                )//성공시
            }.onFailure { e ->
                val now = _state.value
                _state.value = now.copy(
                    loading = now.loading - spotKey,
                    error = now.error + (spotKey to (e.message ?: "Unknown error"))
                )//실패시
            }
        }
    }
}
