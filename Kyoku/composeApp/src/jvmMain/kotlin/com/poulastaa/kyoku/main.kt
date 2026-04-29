package com.poulastaa.kyoku

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.poulastaa.common.ui.AppTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kyoku",
    ) {
        AppTheme {
            App()
        }
    }
}