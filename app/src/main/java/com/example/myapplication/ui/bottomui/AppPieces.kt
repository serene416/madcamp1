package com.example.myapplication.ui.bottomui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.tab1.FirstTabQuestionFlow
import com.example.myapplication.ui.tab2.SecondTab
import com.example.myapplication.ui.tab3.CameraTab

enum class BottomTab { FIRST, SECOND, THIRD }

@Composable
fun TabContent(currentTab: BottomTab) {
    when (currentTab) {
        BottomTab.FIRST -> FirstTabQuestionFlow()
        BottomTab.SECOND -> SecondTab()
        BottomTab.THIRD -> CameraTab()
    }
}

@Composable
fun BottomNavBarOverlay(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 24.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier.clip(RoundedCornerShape(24.dp))
        ) {
            NavigationBarItem(
                selected = currentTab == BottomTab.FIRST,
                onClick = { onTabSelected(BottomTab.FIRST) },
                icon = { Icon(Icons.Filled.Recommend, contentDescription = "추천") },
                label = { Text("추천") }
            )
            NavigationBarItem(
                selected = currentTab == BottomTab.SECOND,
                onClick = { onTabSelected(BottomTab.SECOND) },
                icon = { Icon(Icons.Filled.Map, contentDescription = "경로") },
                label = { Text("경로") }
            )
            NavigationBarItem(
                selected = currentTab == BottomTab.THIRD,
                onClick = { onTabSelected(BottomTab.THIRD) },
                icon = { Icon(Icons.Filled.PhotoCamera, contentDescription = "사진") },
                label = { Text("사진") }
            )
        }
    }
}
