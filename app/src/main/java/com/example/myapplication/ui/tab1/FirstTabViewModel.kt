package com.example.myapplication.ui.tab1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FirstTabViewModel : ViewModel() {

    private val scores: MutableMap<Region, Int> = mutableMapOf(
        Region.TOKYO to 0,
        Region.OSAKA to 0,
        Region.FUKUOKA to 0,
        Region.SAPPORO to 0,
        Region.NAGOYA to 0
    )

    private val _state = MutableStateFlow(FirstTabUiState())
    val state: StateFlow<FirstTabUiState> = _state

    init {
        // 첫 화면 세팅
        setQuestionStep(0)
    }

    fun onAction(action: FirstTabAction) {
        when (action) {
            is FirstTabAction.SelectChoice -> handleSelect(action.choiceId)
            FirstTabAction.Restart -> restart()
        }
    }

    private fun handleSelect(choiceId: String) {
        val s = _state.value
        if (s.isResult) return

        val q = s.question ?: return
        val choice = q.choices.firstOrNull { it.id == choiceId } ?: return

        // 점수 가산
        choice.addRegions.forEach { r ->
            scores[r] = (scores[r] ?: 0) + 1
        }

        val nextIndex = s.stepIndex + 1
        if (nextIndex >= 7) {
            // Q7 다음은 RESULT(stepIndex=7)
            setResultStep()
        } else {
            setQuestionStep(nextIndex)
        }
    }

    private fun setQuestionStep(stepIndex: Int) {
        val question = firstTabQuestions.getOrNull(stepIndex) // 0..6
        _state.update {
            it.copy(
                stepIndex = stepIndex,
                totalSteps = 8,
                question = question,
                isResult = false,
                resultText = "",
                selectedTopRegions = emptyList()
            )
        }
    }

    private fun setResultStep() {
        val top2 = scores.entries
            .sortedWith(
                compareByDescending<Map.Entry<Region, Int>> { it.value }
                    .thenBy { firstTabTiePriority.indexOf(it.key) }
            )
            .take(2)
            .map { it.key }

        val resultText = top2.joinToString(" / ") { regionKoreanName(it) }

        _state.update {
            it.copy(
                stepIndex = 7,
                totalSteps = 8,
                question = null,
                isResult = true,
                resultText = resultText,
                selectedTopRegions = top2
            )
        }
    }

    private fun restart() {
        scores.keys.forEach { scores[it] = 0 }
        setQuestionStep(0)
    }
}
