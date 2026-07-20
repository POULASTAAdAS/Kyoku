package com.poulastaa.kyoku

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.common.ui.design_system.AppTheme
import com.poulastaa.common.ui.root.RootViewmodel
import com.poulastaa.kyoku.di.initKoin
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.koin.compose.viewmodel.koinViewModel
import java.awt.Taskbar
import java.awt.Toolkit
import javax.imageio.ImageIO

fun main() {
    initKoin()

    val iconBytes = checkNotNull(ClassLoader.getSystemResourceAsStream("app-icon.png"))
        .use { it.readAllBytes() }

    if (Taskbar.isTaskbarSupported()) {
        val taskbar = Taskbar.getTaskbar()

        if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            taskbar.iconImage = ImageIO.read(iconBytes.inputStream())
        }
    }

    val screenSize = Toolkit.getDefaultToolkit().screenSize

    application {
        val appIcon = remember { BitmapPainter(iconBytes.decodeToImageBitmap()) }
        val windowState = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            width = (screenSize.width * 0.5f).dp,
            height = (screenSize.height * 0.6f).dp,
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "Kyoku",
            state = windowState,
            icon = appIcon,
        ) {
            DisposableEffect(window) {
                if (System.getProperty("os.name").startsWith("Mac")) {
                    window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
                    window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
                    window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
                }

                onDispose {}
            }

            AppTheme {
                val viewmodel = koinViewModel<RootViewmodel>()
                val rootState by viewmodel.uiState.collectAsStateWithLifecycle()
                RootNavigation(state = rootState)
            }
        }
    }
}
