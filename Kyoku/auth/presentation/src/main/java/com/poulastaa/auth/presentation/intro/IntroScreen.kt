package com.poulastaa.auth.presentation.intro

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.auth.presentation.intro.components.IntroCompactScreen
import com.poulastaa.auth.presentation.intro.components.IntroExpandedScreen
import com.poulastaa.auth.presentation.intro.components.IntroMediumScreen
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.components.ScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun IntroRootScreen(
    viewModel: IntroViewModel = hiltViewModel(),
    navigate: (ScreenEnum) -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
        when (event) {
            is IntroUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is IntroUiAction.OnSuccess -> navigate(event.screen)
        }
    }

    IntroScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun IntroScreen(
    state: IntroUiState,
    onEvent: (IntroUiEvent) -> Unit,
) {
    ScreenWrapper(
        compactContent = {
            IntroCompactScreen(
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            IntroMediumScreen(
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            IntroExpandedScreen(
                state = state,
                onEvent = onEvent
            )
        }
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        IntroScreen(
            state = IntroUiState()
        ) {

        }
    }
}