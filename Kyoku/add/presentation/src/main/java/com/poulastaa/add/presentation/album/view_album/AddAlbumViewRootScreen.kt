package com.poulastaa.add.presentation.album.view_album

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.add.presentation.album.UiAlbum
import com.poulastaa.add.presentation.playlist.components.LoadingSongCard
import com.poulastaa.core.presentation.designsystem.CacheImageReq
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.model.UiDetailedPrevSong
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.ArrowDownIcon
import com.poulastaa.core.presentation.designsystem.ui.SongIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppBasicTopBar
import com.poulastaa.core.presentation.ui.components.AppErrorScreen

@Composable
internal fun AddAlbumViewRootScreen(
    album: UiAlbum,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    val viewmodel = hiltViewModel<AddAlbumViewViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()
    LaunchedEffect(album) {
        viewmodel.init(album)
    }

    ObserveAsEvent(viewmodel.uiEvent) {
        when (it) {
            is AddAlbumViewUiEvent.EmitToast -> Toast.makeText(
                context,
                it.message.asString(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    AddAlbumViewScreen(
        state = state,
        navigateBack = navigateBack
    )
}

@Composable
private fun AddAlbumViewScreen(
    state: AddAlbumViewUiState,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppBasicTopBar(
                title = state.album.name,
                navigationIcon = ArrowDownIcon,
                navigateBack = navigateBack
            )
        }
    ) {
        when (state.loadingType) {
            LoadingType.Loading -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1)
            ) {
                repeat(10) {
                    LoadingSongCard(
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        false
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                }
            }

            is LoadingType.Error -> AppErrorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(it)
                    .padding(MaterialTheme.dimens.medium1),
                error = state.loadingType,
                navigateBack = navigateBack
            )

            LoadingType.Content -> LazyColumn(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                items(state.songs) { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier.aspectRatio(1f),
                            shape = MaterialTheme.shapes.extraSmall,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            SubcomposeAsyncImage(
                                model = CacheImageReq.imageReq(
                                    song.poster,
                                    LocalContext.current
                                ),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds,
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.fillMaxSize(.4f),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.background
                                        )
                                    }
                                },
                                error = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = SongIcon,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(.7f),
                                            tint = MaterialTheme.colorScheme.background
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(.85f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = song.title,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary,
                            )

                            song.artists?.let {
                                Text(
                                    text = it,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            } ?: song.releaseYear?.let {
                                Text(
                                    text = "${stringResource(R.string.release_year)}: $it",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            AddAlbumViewScreen(
                state = AddAlbumViewUiState(
                    loadingType = LoadingType.Content,
                    songs = (1..10).map {
                        UiDetailedPrevSong(
                            title = "That Cool Song",
                            artists = "That Cool Artist",
                        )
                    }
                )
            ) { }
        }
    }
}