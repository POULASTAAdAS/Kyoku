package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.setup.spotify_playlist.SpotifyUiPlaylist
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomTextFiled
import com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.components.SpotifyPlaylistSongCard
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun SpotifyPlaylistScreenContent(
    paddingValues: PaddingValues,
    uiPlaylist: List<SpotifyUiPlaylist>,
    isCookie: Boolean,
    headerValue: String,
    link: String,
    onValueChange: (String) -> Unit,
    supportingText: String,
    isError: Boolean,
    isLoading: Boolean,
    isFirstPlaylist: Boolean,
    onPlaylistClick: (name: String) -> Unit,
    onAddClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 4),
            verticalArrangement = Arrangement.Center
        ) {
            CustomTextFiled(
                value = link,
                onValueChange = onValueChange,
                onDone = onAddClick,
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                supportingText = supportingText,
                shape = MaterialTheme.shapes.small,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.link),
                        contentDescription = null
                    )
                },
                label = "Past Link"
            )

            if (LocalConfiguration.current.screenHeightDp > 659)
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilledIconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .fillMaxWidth(1f / 3)
                        .heightIn(min = MaterialTheme.dimens.large2),
                    shape = MaterialTheme.shapes.small
                ) {
                    if (isLoading)
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    else
                        Text(
                            text = if (isFirstPlaylist) "Add" else "Add Another",
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            softWrap = false
                        )
                }

                
                FilledIconButton(
                    onClick = onContinueClick,
                    modifier = Modifier
                        .fillMaxWidth(1f / 2)
                        .heightIn(min = MaterialTheme.dimens.large2),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Continue",
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(MaterialTheme.dimens.small3)
            ) {
                items(
                    items = uiPlaylist,
                    key = {
                        it.name
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(
                                onClick = { onPlaylistClick.invoke(it.name) },
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null
                            )
                            .padding(MaterialTheme.dimens.small3),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            modifier = Modifier.weight(7f)
                        )

                        Icon(
                            modifier = Modifier.weight(1f),
                            imageVector = if (it.isExpanded) Icons.Rounded.KeyboardArrowUp
                            else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }

                    it.songs.forEach { song ->
                        AnimatedVisibility(visible = it.isExpanded) {
                            SpotifyPlaylistSongCard(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                imageUrl = song.coverImage,
                                title = song.title,
                                artist = song.artist,
                                isCookie = isCookie,
                                headerValue = headerValue
                            )
                        }
                    }
                }
            }
        }
    }
}