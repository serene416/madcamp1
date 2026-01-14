// FirstTabScreen.kt
package com.example.myapplication.ui.tab1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

@Composable
fun FirstTabScreen(
    state: FirstTabUiState,
    onAction: (FirstTabAction) -> Unit
) {
    // Q1~Q7에 대응하는 고양이 이미지 (stepIndex 0~6)
    val catByQuestion = listOf(
        R.drawable.cat_city,      // Q1 도시
        R.drawable.cat_onsen,     // Q2 온천
        R.drawable.cat_snow,      // Q3 눈
        R.drawable.cat_shopping,  // Q4 쇼핑
        R.drawable.cat_temple,    // Q5 사찰
        R.drawable.cat_luxury,    // Q6 럭셔리
        R.drawable.cat_style      // Q7 멋쟁이
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(20.dp))
            ProgressDots(stepIndex = state.stepIndex, totalSteps = state.totalSteps)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (!state.isResult) {
                val q = state.question ?: return@Box
                val catResId = catByQuestion.getOrElse(state.stepIndex) { catByQuestion.last() }

                QuestionCard(
                    title = q.text,
                    catResId = catResId
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        q.choices.forEach { c ->
                            ChoiceCard(text = c.label) {
                                onAction(FirstTabAction.SelectChoice(c.id))
                            }
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    ResultCard(result = state.resultText)
                    Button(
                        onClick = { onAction(FirstTabAction.Restart) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("처음으로")
                    }
                }
            }
        }
    }
}
