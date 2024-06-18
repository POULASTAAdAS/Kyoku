package com.poulastaa.auth.presentation.intro

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@Composable
fun IntroRootScreen(
    viewModel: IntroViewModel = hiltViewModel(),
    navigate: (NavigationScreen) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(IntroUiEvent.OnGoogleLogInClick)
    }

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
}

@Composable
private fun IntroScreen(
    state: IntroUiState,
    onEvent: (IntroUiEvent) -> Unit,
) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
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