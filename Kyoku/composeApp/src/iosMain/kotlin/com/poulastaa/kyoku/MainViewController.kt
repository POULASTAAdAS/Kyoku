package com.poulastaa.kyoku

import androidx.compose.ui.window.ComposeUIViewController
import com.poulastaa.common.ui.AppTheme

fun MainViewController() = ComposeUIViewController { AppTheme { App() } }