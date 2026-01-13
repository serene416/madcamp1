package com.example.myapplication.ui.tab1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FirstTabQuestionFlow(
    vm: FirstTabViewModel = viewModel()
) {
    val state by vm.state.collectAsState()
    FirstTabScreen(state = state, onAction = vm::onAction)
}
