// FirstTabComponents.kt
package com.example.myapplication.ui.tab1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.myapplication.ui.theme.AppStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun ProgressDots(stepIndex: Int, totalSteps: Int) {
    val filled = (stepIndex + 1).coerceAtMost(totalSteps)
    Row(
        modifier = Modifier.fillMaxWidth(0.85f),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (index < filled) AppStyle.Colors.primary else AppStyle.Colors.borderSoft
                    )
            )
        }
    }
}

@Composable
fun QuestionCard(
    title: String,
    catResId: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppStyle.Colors.cardBackground),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(AppStyle.Dimens.radiusCard)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(id = catResId),
                contentDescription = "question cat",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )

            Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
fun ChoiceCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = AppStyle.Colors.surfaceSoft),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(AppStyle.Dimens.radiusCard)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ResultCard(result: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        colors = CardDefaults.cardColors(containerColor = AppStyle.Colors.primarySoft),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(AppStyle.Dimens.radiusCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("✈️", fontSize = 36.sp)
            Spacer(Modifier.height(12.dp))
            Text("추천 여행지", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(result, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }
    }
}
