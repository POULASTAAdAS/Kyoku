package com.poulastaa.add.presentation.playlist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistPageUiItem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AddSongToPlaylistStaticDataTopBar(
    staticData: List<AddSongToPlaylistPageUiItem>,
    currentPage: Int,
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(staticData[currentPage].type.value),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.animateContentSize(tween(400))
            )
        },
        navigationIcon = {
            IconButton(
                onClick = navigateBack,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = CloseIcon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(.7f)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
}