package com.example.myapplication.ui.tab2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RestaurantListScreen(
    title: String,
    lat: Double,
    lng: Double,
    onBack: () -> Unit,
    vm: Tab2ViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(lat, lng) {
        vm.load(lat, lng)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = onBack) { Text("뒤로") }
        }

        Spacer(Modifier.height(12.dp))

        when {
            state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            state.error != null -> {
                Text("에러: ${state.error}")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { vm.load(lat, lng) }) { Text("다시 시도") }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.items) { p ->
                        Card {
                            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                Text(p.name ?: "(no name)", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text("평점: ${p.rating ?: "-"} · 리뷰: ${p.user_ratings_total ?: 0}")
                                Spacer(Modifier.height(4.dp))
                                Text(p.vicinity ?: "")
                            }
                        }
                    }
                }
            }
        }
    }
}
