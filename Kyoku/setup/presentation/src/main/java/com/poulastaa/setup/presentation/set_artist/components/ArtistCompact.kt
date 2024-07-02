package com.poulastaa.setup.presentation.set_artist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.components.LessSelectedToast
import com.poulastaa.setup.presentation.components.NoInternetToast
import com.poulastaa.setup.presentation.set_artist.ArtistUiEvent
import com.poulastaa.setup.presentation.set_artist.ArtistUiState
import com.poulastaa.setup.presentation.set_artist.model.UiArtist
import kotlin.random.Random

@Composable
fun ArtistCompact(
    paddingValues: PaddingValues,
    state: ArtistUiState,
    grid: Int = 3,
    onEvent: (ArtistUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimens.medium1)
    ) {
        LessSelectedToast(
            visible = state.isToastVisible,
            text = stringResource(id = R.string.less_artist_selected)
        )
        NoInternetToast(visible = state.isInternetErr)

        ArtistContentList(
            grid = grid,
            header = state.header,
            data = state.data,
            onClick = {
                onEvent(ArtistUiEvent.OnArtistClick(it))
            }
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 840,
    heightDp = 540
)
@Composable
private fun Preview() {
    AppThem {
        val data = (1..40).map {
            UiArtist(
                id = it.toLong(),
                name = "Artist $it",
                isSelected = Random.nextBoolean()
            )
        }

        ArtistCompact(
            paddingValues = PaddingValues(),
            state = ArtistUiState(
                data = data,
                isToastVisible = true,
                isInternetErr = true
            )
        ) {

        }
    }
}