package com.poulastaa.board.presentation.import_playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.poulastaa.board.presentation.R
import com.poulastaa.core.presentation.designsystem.UiPrevPlaylistSong
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.DropDownArrowIcon
import com.poulastaa.core.presentation.ui.LinkIcon
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait
import com.poulastaa.core.presentation.ui.dimens
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImportPlaylistVerticalScreen(
    modifier: Modifier = Modifier,
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.import_your_spotify_playlist),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.tertiary,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1)
                .padding(paddingValues)
                .then(modifier)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.link.value,
                    onValueChange = {
                        onAction(ImportPlaylistUiAction.OnLinkChange(it))
                    },
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.weight(1f),
                    isError = state.link.isErr,
                    supportingText = {
                        Text(text = state.link.errText.asString())
                    },
                    label = {
                        Text(text = stringResource(R.string.link_label))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = LinkIcon,
                            contentDescription = stringResource(R.string.link_label)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Uri
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onAction(ImportPlaylistUiAction.OnImportClick)
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        cursorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,

                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,

                        focusedSupportingTextColor = MaterialTheme.colorScheme.error,
                        unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,

                        errorTextColor = MaterialTheme.colorScheme.error,
                        errorLabelColor = MaterialTheme.colorScheme.error,
                        errorCursorColor = MaterialTheme.colorScheme.error,
                        errorBorderColor = MaterialTheme.colorScheme.error,
                        errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        errorPlaceholderColor = MaterialTheme.colorScheme.error,
                        errorLeadingIconColor = MaterialTheme.colorScheme.error,
                        errorTrailingIconColor = MaterialTheme.colorScheme.error,

                        selectionColors = TextSelectionColors(
                            handleColor = MaterialTheme.colorScheme.secondary,
                            backgroundColor = Color.Transparent
                        )
                    )
                )

                Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                Button(
                    onClick = {
                        onAction(ImportPlaylistUiAction.OnImportClick)
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.import_button),
                            modifier = Modifier.alpha(if (state.isMakingApiCall) 0f else 1f)
                        )

                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.alpha(if (state.isMakingApiCall) 1f else 0f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.3.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(bottom = MaterialTheme.dimens.medium1),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                items(state.data) { playlist ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = if (playlist.isExpanded) MaterialTheme.shapes.medium
                        else MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Card(
                            shape = MaterialTheme.shapes.medium.copy(
                                bottomEnd = CircleShape.bottomEnd,
                                bottomStart = CircleShape.bottomStart,
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(MaterialTheme.dimens.small3)
                                    .padding(horizontal = MaterialTheme.dimens.small2),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = playlist.title,
                                    modifier = Modifier.weight(1f),
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Icon(
                                    imageVector = DropDownArrowIcon,
                                    contentDescription = stringResource(R.string.drop_down),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable(
                                            onClick = {
                                                onAction(
                                                    ImportPlaylistUiAction.OnItemStateToggle(
                                                        id = playlist.id
                                                    )
                                                )
                                            }
                                        )
                                        .rotate(if (playlist.isExpanded) 180f else 0f),
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }

                        AnimatedVisibility(
                            playlist.isExpanded,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Spacer(Modifier.height(MaterialTheme.dimens.small2))

                                playlist.songs.forEach { song ->
                                    Card(
                                        modifier = Modifier
                                            .heightIn(max = 80.dp)
                                            .fillMaxWidth()
                                            .padding(horizontal = MaterialTheme.dimens.small3),
                                        shape = MaterialTheme.shapes.extraSmall,
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        elevation = CardDefaults.cardElevation(
                                            defaultElevation = 3.dp
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                Modifier
                                                    .fillMaxHeight()
                                                    .clip(MaterialTheme.shapes.extraSmall)
                                                    .aspectRatio(1f)
                                                    .background(MaterialTheme.colorScheme.primary)
                                            )

                                            Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                                            Column {
                                                Text(
                                                    text = song.title,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                                    color = MaterialTheme.colorScheme.onBackground
                                                )

                                                Text(
                                                    text = song.artist,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                                )
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                                }

                                Spacer(Modifier.height(MaterialTheme.dimens.small2))
                            }
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.medium1))
                }
            }
        }
    }
}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        ImportPlaylistVerticalScreen(
            state = ImportPlaylistUiState(
                data = (1..3).map { playlistId ->
                    UiPreviewPlaylist(
                        id = playlistId.toLong(),
                        title = "Playlist $playlistId",
                        songs = (1..5).map { songId ->
                            UiPrevPlaylistSong(
                                id = songId.toLong(),
                                title = "Song $songId",
                                artist = "Artist $songId",
                            )
                        },
                        isExpanded = Random.nextBoolean()
                    )
                }
            )
        ) { }
    }
}