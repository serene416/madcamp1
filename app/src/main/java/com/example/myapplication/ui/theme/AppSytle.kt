package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppStyle {
    object Colors {
        val primary = Color(0xFFF5C1CA)
        val primarySoft = Color(0xFFFBE9EA)
        val surfaceSoft = Color(0xFFF7F7F7)
        val borderSoft = Color(0xFFE6E6E6)
        val placeholder = Color(0xFFECEFF1)
        val bottomNav = Color(0xFFF7D3DA)
        val disabledPrimary = Color(0xFFF0DDE0)
        val disabledText = Color(0xFF777777)
        val cardBackground = Color.White
    }

    object Dimens {
        val screenPadding = 24.dp
        val sectionGap = 12.dp
        val itemGap = 8.dp
        val chipHeight = 40.dp
        val ctaHeight = 52.dp
        val cityButtonHeight = 46.dp
        val radiusCard = 18.dp
        val radiusPill = 999.dp
        val imageRadius = 14.dp
        val bottomNavRadius = 24.dp
    }
}
