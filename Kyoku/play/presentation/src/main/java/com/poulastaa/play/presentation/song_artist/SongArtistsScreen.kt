package com.poulastaa.play.presentation.song_artist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReqUser
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.domain.DataLoadingState

@Composable
fun SongArtistsRootScreen(
    songId: Long,
    viewModel: SongArtistsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    LaunchedEffect(songId) {
        viewModel.init(songId)
    }

    SongArtistsScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongArtistsScreen(
    state: SongArtistsUiState,
    onEvent: (SongArtistsUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = navigateBack,
        sheetState = sheetState,
        properties = properties,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.small3,
            topEnd = MaterialTheme.dimens.small3
        )
    ) {
        when (state.loadingState) {
            DataLoadingState.LOADING -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1)
                    .padding(bottom = MaterialTheme.dimens.medium1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }

            DataLoadingState.LOADED -> LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(MaterialTheme.dimens.medium1)
            ) {
                items(state.artist) { artist ->
                    Artist(
                        modifier = Modifier.clickable {
                            onEvent(SongArtistsUiEvent.OnArtistClick(artist.id))
                        },
                        artist = artist,
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small3))
                }
            }

            DataLoadingState.ERROR -> Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.dimens.medium1)
                    .padding(bottom = MaterialTheme.dimens.medium1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.error_something_went_wrong),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Composable
private fun Artist(
    modifier: Modifier = Modifier,
    artist: UiArtist,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(1f),
            shape = MaterialTheme.shapes.extraSmall,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 7.dp
            )
        ) {
            AsyncImage(
                model = imageReqUser(
                    header = "",
                    url = artist.coverImageUrl
                ),
                modifier = Modifier
                    .aspectRatio(1f),
                contentDescription = null,
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.medium1))

        Text(
            text = artist.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    var loadingState by remember { mutableStateOf(DataLoadingState.LOADING) }

    LaunchedEffect(Unit) {
        loadingState = DataLoadingState.LOADED
    }

    AppThem {
        SongArtistsScreen(
            state = SongArtistsUiState(
                artist = (1..10).map {
                    UiArtist(
                        id = it.toLong(),
                        name = "Artist $it"
                    )
                },
                loadingState = loadingState
            ),
            onEvent = {},
            navigateBack = {}
        )
    }
}