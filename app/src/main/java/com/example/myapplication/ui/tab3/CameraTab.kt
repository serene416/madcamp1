package com.example.myapplication.ui.tab3

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CameraTab(
    vm: CameraViewModel = viewModel()
) {
    CameraScreen(vm = vm)
}
