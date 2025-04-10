package com.poulastaa.add.presentation.playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun AddSongToPlaylistLoadingContent(
    values: PaddingValues,
    content: @Composable() () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
            )
            .fillMaxSize()
            .padding(values)
            .padding(MaterialTheme.dimens.medium1)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f)
                .verticalScroll(rememberScrollState()),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            repeat(10) {
                content()
                Spacer(Modifier.height(MaterialTheme.dimens.small1))
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoadingNavigationBall(MaterialTheme.colorScheme.background)
            Spacer(Modifier.width(MaterialTheme.dimens.small2))

            repeat(3) {
                LoadingNavigationBall()
                Spacer(Modifier.width(MaterialTheme.dimens.small2))
            }
        }
    }
}