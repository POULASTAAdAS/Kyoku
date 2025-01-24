package com.poulastaa.setup.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PigScreenTopBar(
    @StringRes title: Int,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(title),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        PigScreenTopBar(R.string.pic_genre_title)
    }
}