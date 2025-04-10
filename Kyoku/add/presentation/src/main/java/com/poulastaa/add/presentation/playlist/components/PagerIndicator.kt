package com.poulastaa.add.presentation.playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun PagerIndicator(
    pagerState: PagerState,
) {
    repeat(pagerState.pageCount) {
        val color = if (it == pagerState.currentPage)
            MaterialTheme.colorScheme.primary.copy(.7f)
        else MaterialTheme.colorScheme.onBackground.copy(.3f)

        Box(
            modifier = Modifier
                .padding(MaterialTheme.dimens.small2)
                .clip(CircleShape)
                .background(color = color)
                .size(12.dp)
        )
    }
}