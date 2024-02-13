package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.ui.UiPlaylist
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomOkButton
import com.poulastaa.kyoku.presentation.screen.auth.common.CustomTextFiled
import com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist.components.SongView

@Composable
fun SpotifyPlaylistScreenContent(
    paddingValues: PaddingValues,
    uiPlaylist: List<UiPlaylist>,
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
    onSkipClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 15.dp,
                end = 15.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TopPart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            link = link,
            onValueChange = onValueChange,
            supportingText = supportingText,
            isError = isError,
            isFirstPlaylist = isFirstPlaylist,
            isLoading = isLoading,
            onAddClick = onAddClick,
            onContinueClick = onContinueClick
        )

        MidPart(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp)
                .weight(7f),
            uiPlaylist = uiPlaylist,
            isCookie = isCookie,
            headerValue = headerValue,
            onPlaylistClick = onPlaylistClick
        )


        Spacer(modifier = Modifier.height(16.dp))

        EndPart(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.8f),
            onClick = onSkipClick
        )
    }
}

@Composable
fun TopPart(
    modifier: Modifier,
    link: String,
    onValueChange: (String) -> Unit,
    supportingText: String,
    isError: Boolean,
    isFirstPlaylist: Boolean,
    isLoading: Boolean,
    onAddClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextFiled(
            value = link,
            onValueChange = onValueChange,
            onDone = {},
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            supportingText = supportingText,
            label = "Past Link",
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.link),
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onAddClick.invoke() }
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomOkButton(
                text = if (isFirstPlaylist) "Add" else "Add another",
                modifier = Modifier.fillMaxWidth(.4f),
                loading = isLoading,
                shape = RoundedCornerShape(8.dp),
                onClick = onAddClick
            )

            CustomOkButton(
                text = "Continue",
                modifier = Modifier.fillMaxWidth(.5f),
                loading = false,
                shape = RoundedCornerShape(8.dp),
                onClick = onContinueClick
            )
        }
    }
}

@Composable
fun MidPart(
    modifier: Modifier,
    uiPlaylist: List<UiPlaylist>,
    isCookie: Boolean,
    headerValue: String,
    onPlaylistClick: (name: String) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp)
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
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        )
                        .padding(8.dp),
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
                        SongView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            imageUrl = song.coverImage,
                            title = song.title,
                            artist = song.artist,
                            isCookie = isCookie,
                            headerValue = headerValue,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EndPart(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "skip",
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    SpotifyPlaylistScreenContent(
        paddingValues = PaddingValues(15.dp),
        link = "",
        uiPlaylist = emptyList(),
        onValueChange = {},
        supportingText = "",
        headerValue = "",
        isError = false,
        isLoading = false,
        isCookie = true,
        isFirstPlaylist = true,
        onAddClick = { /*TODO*/ },
        onPlaylistClick = {},
        onSkipClick = {}) {

    }
}