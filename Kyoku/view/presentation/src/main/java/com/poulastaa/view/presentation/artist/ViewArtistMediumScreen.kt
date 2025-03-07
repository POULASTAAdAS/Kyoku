package com.poulastaa.view.presentation.artist

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.view.presentation.artist.components.ViewArtistMediumLoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ViewArtistMediumScreen(
    scroll: TopAppBarScrollBehavior,
    state: ViewArtistUiState,
    onAction: (ViewArtistUiAction) -> Unit,
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {

        }
    ) { paddingValues ->
        AnimatedContent(targetState = state.loadingType) {
            when (it) {
                LoadingType.LOADING -> ViewArtistMediumLoadingScreen(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        )
                        .fillMaxSize()
                )

                LoadingType.ERROR -> Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        )
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(MaterialTheme.dimens.medium1),
                ) {

                }

                LoadingType.CONTENT -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(
                            brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                        ),
                ) {

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=800dp,height=1280dp,dpi=480",
)
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ViewArtistMediumScreen(
                scroll = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                state = PREV_DATA,
                onAction = {},
                navigateBack = {}
            )
        }
    }
}