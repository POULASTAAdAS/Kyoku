package com.poulastaa.play.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ThreeDotIcon
import com.poulastaa.core.presentation.designsystem.ThreeLineIcon
import com.poulastaa.core.presentation.designsystem.components.AppBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {},
        navigationIcon = {
            AppBackButton {
                navigateBack()
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            ViewTopBar(
                title = "Playlist",
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            ) {

            }
        }
    }
}