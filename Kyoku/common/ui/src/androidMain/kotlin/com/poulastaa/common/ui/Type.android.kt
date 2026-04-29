package com.poulastaa.common.ui

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun calculateWindowSizeClass(): WindowSizeClass =
    androidx.compose.material3.windowsizeclass.calculateWindowSizeClass(LocalContext.current as Activity)
