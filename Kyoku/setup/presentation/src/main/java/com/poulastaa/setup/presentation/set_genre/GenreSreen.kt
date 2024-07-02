package com.poulastaa.setup.presentation.set_genre

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppTopAppbar
import com.poulastaa.core.presentation.designsystem.components.SetupScreenWrapper
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.setup.presentation.set_genre.components.GenreCompact

@Composable
fun GenreRootScreen(
    viewModel: SetGenreViewModel = hiltViewModel(),
    navigateToPicArtist: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is GenreUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    it.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            GenreUiAction.NavigateToSetArtist -> navigateToPicArtist()
        }
    }

    GenreScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun GenreScreen(
    state: GenreUiState,
    onEvent: (GenreUiEvent) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity = context)

    SetupScreenWrapper(
        windowSizeClass = windowSize,
        topBar = {
            AppTopAppbar(
                isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded,
                text = stringResource(id = R.string.set_genre_title)
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = state.canMakeApiCall) {
                val temp = remember {
                    state.canMakeApiCall
                }

                if (temp) Button(
                    modifier = Modifier.padding(
                        end = MaterialTheme.dimens.medium1,
                        bottom = MaterialTheme.dimens.medium3
                    ),
                    onClick = {
                        onEvent(GenreUiEvent.OnContinueClick)
                    },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.alpha(if (state.isMakingApiCall) 1f else 0f),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = stringResource(id = R.string.continue_text),
                            letterSpacing = 2.sp,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        },
        compactContent = {
            GenreCompact(
                paddingValues = it,
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            GenreCompact(
                grid = 4,
                paddingValues = it,
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            GenreCompact(
                grid = 5,
                paddingValues = it,
                state = state,
                onEvent = onEvent
            )
        }
    )
}