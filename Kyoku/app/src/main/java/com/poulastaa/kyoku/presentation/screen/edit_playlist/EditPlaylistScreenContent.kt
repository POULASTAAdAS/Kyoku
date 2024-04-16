package com.poulastaa.kyoku.presentation.screen.edit_playlist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poulastaa.kyoku.R
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiPlaylist
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiState
import com.poulastaa.kyoku.data.model.screens.edit_playlist.UiFav
import com.poulastaa.kyoku.presentation.screen.home_root.home.component.CustomImageView
import com.poulastaa.kyoku.ui.theme.TestThem
import com.poulastaa.kyoku.ui.theme.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPlaylistScreenContent(
    state: EditPlaylistUiState,
    isDarkThem: Boolean = isSystemInDarkTheme(),
    focusManager: FocusManager = LocalFocusManager.current,
    navigateBack: () -> Unit,
    newPlaylistClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onPlaylistClick: (id: Long) -> Unit,
    onFavClick: () -> Unit,
    onDoneClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = !state.isSearchEnable,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 300
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(
                                    end = 56.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Edit Playlist",
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.isSearchEnable,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        ) + slideInVertically(
                            animationSpec = tween(
                                durationMillis = 500
                            ),
                            initialOffsetY = { -it / 2 }
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        ) + slideOutVertically(
                            animationSpec = tween(
                                durationMillis = 500
                            ),
                            targetOffsetY = { -it / 2 }
                        )
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = state.searchText,
                            onValueChange = onSearchTextChange,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null
                                )
                            },
                            maxLines = 1,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                                focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                ),
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                                    .5f
                                )
                            ),
                            placeholder = {
                                Text(text = "search playlist")
                            },
                            textStyle = TextStyle(
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        if (state.isSearchEnable)
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        else
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                    }
                }
            )
        },
        floatingActionButton = {
            OutlinedButton(
                modifier = Modifier
                    .height(42.dp),
                onClick = onDoneClick,
                shape = MaterialTheme.shapes.small,
                enabled = !state.isMakingApiCall,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (state.isMakingApiCall)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                else
                    Text(
                        text = "Done",
                        fontWeight = FontWeight.Medium
                    )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                ),
            contentPadding = PaddingValues(
                bottom = MaterialTheme.dimens.medium1,
                start = MaterialTheme.dimens.medium1,
                end = MaterialTheme.dimens.medium1
            )
        ) {
            item {
                AnimatedVisibility(
                    visible = !state.isSearchEnable,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    ) + slideInVertically(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    ) + slideOutVertically(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium2),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NewPlaylistButton {
                            newPlaylistClick.invoke()
                        }

                        DummySearch {
                            onSearchClick.invoke()
                        }

                        Spacer(modifier = Modifier)

                        FavouriteCard(lib = state.fav) {
                            onFavClick.invoke()
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
            }

            items(state.playlist.size) { index ->
                PlaylistCard(
                    data = state.playlist[index],
                    isDarkThem = isDarkThem,
                    isCookie = state.isCookie,
                    headerValue = state.headerValue
                ) {
                    onPlaylistClick.invoke(state.playlist[index].id)
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
    }
}


@Composable
private fun NewPlaylistButton(
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text(
            text = "New Playlist",
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}


@Composable
private fun DummySearch(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.extraSmall)
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(.2f)
            )
            .clickable {
                onClick.invoke()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small1),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            Icon(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(.7f)
            )

            Text(
                text = "Search Playlist",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(.7f)
            )
        }
    }
}

@Composable
private fun PlaylistImage(
    urls: List<String>,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(.2f)
            .clip(MaterialTheme.shapes.extraSmall)
    ) {
        if (urls.size < 4)
            CustomImageView(
                isDarkThem = isDarkThem,
                isCookie = isCookie,
                headerValue = headerValue,
                url = urls[0]
            )
        else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            ) {
                CustomImageView(
                    modifier = Modifier.fillMaxWidth(.5f),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = headerValue,
                    url = urls[0]
                )

                CustomImageView(
                    modifier = Modifier.fillMaxSize(),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = headerValue,
                    url = urls[1]
                )
            }

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                CustomImageView(
                    modifier = Modifier.fillMaxWidth(.5f),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = headerValue,
                    url = urls[2]
                )

                CustomImageView(
                    modifier = Modifier.fillMaxSize(),
                    isDarkThem = isDarkThem,
                    isCookie = isCookie,
                    headerValue = headerValue,
                    url = urls[3]
                )
            }
        }
    }
}

@Composable
fun PlaylistCard(
    data: EditPlaylistUiPlaylist,
    isDarkThem: Boolean,
    isCookie: Boolean,
    headerValue: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        PlaylistImage(
            urls = data.urls,
            isDarkThem = isDarkThem,
            isCookie = isCookie,
            headerValue = headerValue
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = data.name,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${data.totalSongs} songs",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 1.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                onClick = onClick,
                modifier = Modifier.size(20.dp),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = if (data.isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (data.isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            ) {
                if (data.isSelected)
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                    )
            }
        }
    }
}


@Composable
fun FavouriteCard(
    lib: UiFav,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.2f)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.favourite),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.small3)
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        Column(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Favourite",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${lib.totalSongs} songs",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 1.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                onClick = onClick,
                modifier = Modifier.size(20.dp),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = if (lib.isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (lib.isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            ) {
                if (lib.isSelected)
                    Icon(
                        modifier = Modifier.padding(4.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                    )
            }
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun Preview() {
    TestThem {
        var state by remember {
            mutableStateOf(
                EditPlaylistUiState(
                    playlist = listOf(
                        EditPlaylistUiPlaylist(
                            urls = listOf("", "", "", ""),
                            name = "Playlist1"
                        ), EditPlaylistUiPlaylist(
                            urls = listOf(""),
                            name = "Playlist2",
                            isSelected = true
                        ), EditPlaylistUiPlaylist(
                            urls = listOf("", "", "", ""),
                            name = "Playlist3"
                        ), EditPlaylistUiPlaylist(
                            urls = listOf("", "", "", ""),
                            name = "Playlist4"
                        ), EditPlaylistUiPlaylist(
                            urls = listOf("", "", "", ""),
                            name = "Playlist5",
                            isSelected = true
                        ), EditPlaylistUiPlaylist(
                            urls = listOf("", "", "", ""),
                            name = "Playlist6"
                        ), EditPlaylistUiPlaylist(
                            urls = listOf(""),
                            name = "Playlist7",
                            isSelected = true
                        )
                    ),
                    isSearchEnable = false,
                    isMakingApiCall = true
                )
            )
        }

        EditPlaylistScreenContent(
            state = state,
            onSearchTextChange = {},
            navigateBack = {
                if (state.isSearchEnable)
                    state = state.copy(
                        isSearchEnable = false
                    )
            },
            onDoneClick = {},
            newPlaylistClick = {},
            onSearchClick = {
                state = state.copy(
                    isSearchEnable = true
                )
            },
            onFavClick = {},
            onPlaylistClick = {}
        )
    }
}