package com.poulastaa.setup.presentation.set_genre.components

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
import com.poulastaa.setup.presentation.set_genre.GenreUiEvent
import com.poulastaa.setup.presentation.set_genre.GenreUiState
import com.poulastaa.setup.presentation.set_genre.model.UiGenre
import kotlin.random.Random

@Composable
fun GenreCompact(
    paddingValues: PaddingValues,
    state: GenreUiState,
    grid: Int = 3,
    onEvent: (GenreUiEvent) -> Unit,
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
            text = stringResource(id = R.string.less_genre_selected)
        )

        NoInternetToast(visible = state.isInternetErr)

        GenreListContent(
            grid = grid,
            data = state.data,
            onClick = {
                onEvent(GenreUiEvent.OnGenreClick(it))
            }
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        val data = (1..30).map {
            UiGenre(
                id = it,
                name = "Genre $it",
                isSelected = Random.nextBoolean()
            )
        }

        GenreCompact(
            paddingValues = PaddingValues(),
            state = GenreUiState(
                data = data,
                isInternetErr = true
            ),
        ) {

        }
    }
}