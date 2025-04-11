package com.poulastaa.add.presentation.playlist.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiAction
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun AddSongToPlaylistCommonContent(
    isExpanded: Boolean = false,
    horizontalPager: PagerState,
    values: PaddingValues,
    filterType: AddSongToPlaylistSearchUiFilterType,
    onAction: (AddSongToPlaylistUiAction.OnSearchFilterTypeChange) -> Unit,
    content: @Composable (index: Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(ThemModeChanger.getGradiantBackground())
            )
            .fillMaxSize()
            .padding(values)
    ) {
        if (isExpanded.not() && horizontalPager.currentPage == horizontalPager.pageCount.dec()) {
            Spacer(Modifier.height(MaterialTheme.dimens.small3))

            AddSongToPlaylistSearchFilterChips(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(tween(400))
                    .padding(horizontal = MaterialTheme.dimens.medium1),
                filterType = filterType,
                onAction = onAction
            )
        }

        HorizontalPager(
            state = horizontalPager,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f)
                .animateContentSize(tween(400))
        ) { pageIndex ->
            Card(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.medium1)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                content = { content(pageIndex) }
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PagerIndicator(horizontalPager)
        }
    }
}