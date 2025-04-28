package com.poulastaa.core.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBasicTopBar(
    @StringRes titleId: Int? = null,
    title: String? = null,
    navigationIcon: ImageVector = CloseIcon,
    actions: @Composable RowScope.() -> Unit = {},
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titleId?.let { stringResource(it) } ?: title ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = actions
    )
}