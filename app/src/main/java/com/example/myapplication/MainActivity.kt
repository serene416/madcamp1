package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ui.bottomui.BottomNavBarOverlay
import com.example.myapplication.ui.bottomui.BottomTab
import com.example.myapplication.ui.bottomui.TabContent
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.maps.MapsInitializer
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LEGACY, null)
        }

        setContent {
            MyApplicationTheme {
                App()
            }
        }
    }
}

@Composable
private fun App() {
    var currentTab by rememberSaveable { mutableStateOf(BottomTab.FIRST) }

    Scaffold(
        containerColor = Color.Transparent, // 1. Scaffold 배경을 투명하게 만듭니다.
        bottomBar = {
            BottomNavBarOverlay(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { innerPadding ->
        // 2. 콘텐츠 영역에만 배경색을 지정하고 Scaffold가 계산한 패딩을 적용합니다.
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            TabContent(currentTab)
        }
    }
}
