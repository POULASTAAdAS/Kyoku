package com.poulastaa.setup.presentation.set_genre.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.setup.presentation.set_genre.GenreUiEvent
import com.poulastaa.setup.presentation.set_genre.GenreUiState
import com.poulastaa.setup.presentation.set_genre.model.UiGenre
import kotlin.random.Random

@Composable
fun GenreCompact(
    paddingValues: PaddingValues,
    state: GenreUiState,
    onEvent: (GenreUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimens.medium1)
    ) {
        AnimatedVisibility(visible = state.isToastVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.dimens.small2)
                    .padding(MaterialTheme.dimens.medium1),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.less_genre_selected),
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        GenreListContent(
            grid = 3,
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
                data = data
            )
        ) {

        }
    }
}