package com.poulastaa.play.presentation.song_artist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.SongArtistCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongArtistsBottomSheet(
    songId: Long,
    sheetState: SheetState,
    viewModel: SongArtistsViewModel = hiltViewModel(),
    navigateToArtistScreen: (artistId: Long) -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(songId) {
        viewModel.init(songId)
    }

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SongArtistsUiAction.EmitToast -> Toast.makeText(
                context,
                event.message.asString(context),
                Toast.LENGTH_LONG
            ).show()

            is SongArtistsUiAction.NavigateToArtist -> navigateToArtistScreen(event.artistId)
        }
    }

    SongArtistsScreen(
        sheetState = sheetState,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongArtistsScreen(
    sheetState: SheetState,
    state: SongArtistsUiState,
    onEvent: (SongArtistsUiEvent) -> Unit,
    navigateBack: () -> Unit,
) {

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
                if (state.artist.isEmpty()) item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.dimens.medium1)
                            .padding(bottom = MaterialTheme.dimens.medium1),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.nothing_to_show),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                } else items(state.artist) { artist ->
                    SongArtistCard(
                        modifier = Modifier
                            .height(80.dp)
                            .clickable {
                                onEvent(SongArtistsUiEvent.OnArtistClick(artist.id))
                            },
                        header = state.header,
                        artist = artist,
                    )

                    Spacer(Modifier.height(MaterialTheme.dimens.small3 + MaterialTheme.dimens.small1))
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


@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun Preview() {
    var loadingState by remember { mutableStateOf(DataLoadingState.LOADING) }

    LaunchedEffect(Unit) {
        loadingState = DataLoadingState.LOADED
    }

    AppThem {
        SongArtistsScreen(
            sheetState = rememberModalBottomSheetState(),
            state = SongArtistsUiState(
                artist = (1..10).map {
                    SongArtistUiArtist(
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