package com.poulastaa.kyoku

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.presentation.ui.KyokuThem
import com.poulastaa.kyoku.navigation.RootNavigation

@Composable
fun RootUi(viewmodel: RootViewModel) {
    val mode by viewmodel.themeManager.isModeDark.collectAsStateWithLifecycle()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    val themColor = viewmodel.themeManager.themColor

    KyokuThem(mode, themColor) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            state.screen?.let { screen ->
                RootNavigation(
                    nav = rememberNavController(),
                    screens = screen
                )
            }
        }
    }
}