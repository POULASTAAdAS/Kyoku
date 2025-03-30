package com.poulastaa.explore.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun DummySearch(
    modifier: Modifier = Modifier,
    isSearchOpen: Boolean,
    onToggleSearch: () -> Unit,
) {
    AnimatedVisibility(
        isSearchOpen.not(),
        enter = fadeIn(tween(400)) + slideInVertically(tween(400)),
        exit = fadeOut(tween(400)) + slideOutVertically(tween(400))
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            onClick = {
                onToggleSearch()
            },
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = MaterialTheme.dimens.small1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = SearchIcon,
                    contentDescription = null
                )

                Spacer(Modifier.width(MaterialTheme.dimens.small1))

                Text(
                    text = stringResource(R.string.search),
                )
            }
        }
    }
}