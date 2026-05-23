package com.poulastaa.common.ui.components.top_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.poulastaa.common.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopBar(
    titleContent: @Composable () -> Unit = {},
    onBackCLick: () -> Unit,
    trailingContent: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
    ),
) {
    TopAppBar(
        title = titleContent,
        navigationIcon = {
            BackButton(onClick = onBackCLick)
        },
        actions = trailingContent,
        colors = colors,
    )
}