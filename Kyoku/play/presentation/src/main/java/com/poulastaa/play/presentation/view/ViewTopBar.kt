package com.poulastaa.play.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.components.AppBackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTopBar(
    isEditable: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onEditClick: () -> Unit,
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
        scrollBehavior = scrollBehavior,
        actions = {
            if (isEditable) AppBackButton(
                icon = Icons.Rounded.Edit,
                onClick = onEditClick
            )
        }
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
                isEditable = true,
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                onEditClick = {}
            ) {

            }
        }
    }
}