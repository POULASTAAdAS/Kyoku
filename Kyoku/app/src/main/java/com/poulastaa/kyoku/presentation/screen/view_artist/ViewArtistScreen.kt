package com.poulastaa.kyoku.presentation.screen.view_artist

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.ui.theme.dimens

@Composable
fun ViewArtistScreen(
    id: Long,
    viewModel: ViewArtistViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    navigate: (UiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = id) {
        viewModel.getArtist(id)
    }

    LaunchedEffect(key1 = viewModel.state.isErr) {
        if (viewModel.state.isErr) navigateBack.invoke()
    }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navigate.invoke(event)
                }

                is UiEvent.NavigateWithData -> {
                    navigate.invoke(event)
                }

                is  UiEvent.Play ->{
                    // todo
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Scaffold {
        if (viewModel.state.isLoading)
            ViewArtistLoading(it)
        else if (viewModel.state.noArtist)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding(),
                        start = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background
                        ),
                        onClick = navigateBack,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large1))

                Text(
                    text = "We don't have data of the artist on some of the songs.",
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Sorry for the Inconvenience.",
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    textAlign = TextAlign.Center
                )
            }
        else
            ViewArtistContent(
                paddingValues = it,
                isCookie = viewModel.state.isCookie,
                headerValue = viewModel.state.headerValue,
                data = viewModel.state.artist,
                onClick = viewModel::onEvent,
                navigateBack = navigateBack
            )
    }
}

