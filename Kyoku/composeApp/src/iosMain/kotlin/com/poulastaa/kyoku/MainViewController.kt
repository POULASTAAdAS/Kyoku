package com.poulastaa.kyoku

import androidx.compose.ui.window.ComposeUIViewController
import com.poulastaa.common.ui.design_system.AppTheme
import com.poulastaa.common.ui.root.RootUiState

fun MainViewController() = ComposeUIViewController { AppTheme { App(RootUiState()) } }