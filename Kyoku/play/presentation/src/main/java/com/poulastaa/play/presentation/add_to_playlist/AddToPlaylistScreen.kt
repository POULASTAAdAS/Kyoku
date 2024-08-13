package com.poulastaa.play.presentation.add_to_playlist

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.ArrowBackIcon
import com.poulastaa.core.presentation.designsystem.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.CheckIcon
import com.poulastaa.core.presentation.designsystem.FavouriteIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.SearchIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.root_drawer.library.components.ImageGrid
import kotlin.random.Random

@Composable
fun AddToPlaylistRootScreen(
    viewModel: AddToPlaylistViewModel = hiltViewModel(),
    songId:Long,
    navigateBack: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(songId)
    }

    AddToPlaylistScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddToPlaylistScreen(
    state: AddToPlaylistUiState,
    onEvent: (AddToPlaylistUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current

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
                        Text(
                            text = stringResource(id = R.string.edit_playlist),
                            fontWeight = FontWeight.SemiBold
                        )
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
                            value = state.query,
                            onValueChange = { },
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
                                Text(text = stringResource(id = R.string.search_playlist))
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.isSearchEnable) onEvent(AddToPlaylistUiEvent.CancelSearch)
                            else navigateBack()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(.8f),
                            contentColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Icon(
                            imageVector = if (state.isSearchEnable) ArrowBackIcon
                            else ArrowDownIcon,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            OutlinedButton(
                modifier = Modifier
                    .height(42.dp),
                onClick = { },
                enabled = !state.isMakingApiCall,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    disabledContentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(.2f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alpha(if (state.isMakingApiCall) 0f else 1f),
                    )

                    CircularProgressIndicator(
                        strokeWidth = 1.5.dp,
                        modifier = Modifier
                            .size(24.dp)
                            .alpha(if (state.isMakingApiCall) 1f else 0f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(paddingValues),
            contentPadding = PaddingValues(
                bottom = MaterialTheme.dimens.large2 + MaterialTheme.dimens.large1,
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
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NewPlaylistButton {

                        }

                        DummySearch {
                            onEvent(AddToPlaylistUiEvent.EnableSearch)
                        }

                        FavouriteCard(
                            modifier = Modifier.height(80.dp),
                            fev = state.favouriteData
                        ) {
                            onEvent(AddToPlaylistUiEvent.OnFevToggle)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large2))
            }

            items(state.playlistData.size) { index ->
                PlaylistCard(
                    modifier = Modifier.height(80.dp),
                    header = state.header,
                    data = state.playlistData[index],
                    onClick = {
                        onEvent(AddToPlaylistUiEvent.OnPlaylistClick(state.playlistData[index].playlist.id))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
            }
        }
    }

    if (state.isSearchEnable) BackHandler {
        onEvent(AddToPlaylistUiEvent.CancelSearch)
    }
}

@Composable
private fun DummySearch(
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small1)
        ) {
            Icon(
                modifier = Modifier.padding(start = MaterialTheme.dimens.small1),
                imageVector = SearchIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(.7f)
            )

            Text(
                text = stringResource(id = R.string.search_playlist),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(.7f)
            )
        }
    }

}

@Composable
private fun NewPlaylistButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(.9f),
            contentColor = MaterialTheme.colorScheme.background
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        )
    ) {
        Text(
            text = stringResource(id = R.string.new_playlist),
            fontWeight = FontWeight.Medium,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@Composable
private fun FavouriteCard(
    modifier: Modifier = Modifier,
    fev: UiFavouriteData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable { onClick() }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isSystemInDarkTheme()) listOf(
                            MaterialTheme.colorScheme.secondary.copy(.8f),
                            MaterialTheme.colorScheme.secondaryContainer
                        ) else listOf(
                            MaterialTheme.colorScheme.secondary.copy(.8f),
                            MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = FavouriteIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.small3),
                tint = MaterialTheme.colorScheme.surfaceContainer.copy(.85f),
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        TextColumn(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .fillMaxHeight(),
            heading = stringResource(id = R.string.favourite),
            songsCount = fev.totalSongs
        )

        CustomCheckBox(selected = fev.selectStatus.new) {
            onClick()
        }
    }
}

@Composable
private fun PlaylistCard(
    modifier: Modifier = Modifier,
    header: String,
    data: UiPlaylistData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable { onClick() }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ImageGrid(
            modifier = Modifier.aspectRatio(1f),
            header = header,
            urls = data.playlist.urls
        )

        Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))

        TextColumn(
            modifier = Modifier
                .fillMaxWidth(.85f)
                .fillMaxHeight(),
            heading = data.playlist.name,
            songsCount = data.totalSongs
        )

        CustomCheckBox(selected = data.selectStatus.new) {
            onClick()
        }
    }
}

@Composable
private fun CustomCheckBox(
    selected: Boolean,
    onClick: () -> Unit
) {
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
                containerColor = if (selected) MaterialTheme.colorScheme.primary.copy(.9f) else Color.Transparent,
                contentColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary.copy(.9f)
                else MaterialTheme.colorScheme.onBackground
            )
        ) {
            if (selected) Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = CheckIcon,
                contentDescription = null,
            )
        }
    }
}


@Composable
private fun TextColumn(
    modifier: Modifier = Modifier,
    heading: String,
    songsCount: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = heading,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "$songsCount songs",
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            maxLines = 1
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PreviewD() {
    val data = AddToPlaylistUiState(
        playlistData = (1..14).map {
            UiPlaylistData(
                playlist = UiPrevPlaylist(
                    name = "Playlist $it"
                ),
                selectStatus = UiSelectStatus(
                    new = Random.nextBoolean()
                )
            )
        },
        favouriteData = UiFavouriteData(
            selectStatus = UiSelectStatus(
                new = true
            )
        )
    )

    AppThem {
        AddToPlaylistScreen(
            state = data,
            onEvent = {}) {

        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewS() {
    val data = AddToPlaylistUiState(
        playlistData = (1..4).map {
            UiPlaylistData(
                playlist = UiPrevPlaylist(
                    name = "Playlist $it"
                ),
                selectStatus = UiSelectStatus(
                    new = Random.nextBoolean()
                )
            )
        },
        isSearchEnable = true
    )

    AppThem {
        AddToPlaylistScreen(
            state = data,
            onEvent = {}) {

        }
    }
}

@Preview
@Composable
private fun PreviewL() {
    val data = AddToPlaylistUiState(
        playlistData = (1..4).map {
            UiPlaylistData(
                playlist = UiPrevPlaylist(
                    name = "Playlist $it"
                ),
                selectStatus = UiSelectStatus(
                    new = Random.nextBoolean()
                )
            )
        },
    )

    AppThem {
        AddToPlaylistScreen(
            state = data,
            onEvent = {}) {

        }
    }
}