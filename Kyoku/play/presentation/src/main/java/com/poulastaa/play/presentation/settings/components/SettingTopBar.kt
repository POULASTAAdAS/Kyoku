package com.poulastaa.play.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(start = MaterialTheme.dimens.small2),
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() },
            ) {
                Icon(
                    imageVector = ArrowBackIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.dimens.small1),
                    tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            SettingTopBar(
                title = "Setting",
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            ) {

            }
        }
    }
}