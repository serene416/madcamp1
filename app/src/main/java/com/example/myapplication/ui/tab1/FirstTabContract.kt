package com.example.myapplication.ui.tab1

enum class Region { TOKYO, OSAKA, FUKUOKA, SAPPORO, NAGOYA }

data class Choice(
    val id: String,
    val label: String,
    val addRegions: List<Region> = emptyList()
)

data class Question(
    val id: String,
    val text: String,
    val choices: List<Choice>
)

data class FirstTabUiState(
    val title: String = "일본 여행지 추천",
    val stepIndex: Int = 0,          // 0..7 (Q1~Q7 + RESULT)
    val totalSteps: Int = 8,
    val question: Question? = null,  // RESULT면 null
    val isResult: Boolean = false,
    val resultText: String = "",
    val selectedTopRegions: List<Region> = emptyList()
)

sealed interface FirstTabAction {
    data class SelectChoice(val choiceId: String) : FirstTabAction
    data object Restart : FirstTabAction
}
