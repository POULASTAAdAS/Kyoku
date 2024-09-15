package com.poulastaa.play.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.CancelIcon
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.model.ViewUiSong
import com.poulastaa.play.presentation.view.ViewUiData
import com.poulastaa.play.presentation.view.ViewUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEditScreen(
    modifier: Modifier = Modifier,
    data: ViewUiData,
    onEvent: (ViewUiEvent) -> Unit,
    navigateBack: () -> Unit
) {
    val scroll = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scroll,
                title = {
                    Text(
                        text = data.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.inversePrimary
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        icon = CancelIcon,
                        onClick = navigateBack
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.inversePrimary
                        ),
                    )
                )
                .padding(paddingValues),
            contentPadding = PaddingValues(MaterialTheme.dimens.medium1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                FilledTonalButton(
                    onClick = {},
                    elevation = ButtonDefaults.filledTonalButtonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 3.dp
                    ),
                    modifier = Modifier.fillMaxWidth(.45f)
                ) {
                    Text(text = stringResource(R.string.explore))
                }
            }

            item {
                Spacer(Modifier.height(MaterialTheme.dimens.large1))
            }


        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        ViewEditScreen(
            data = ViewUiData(
                name = "Playlist",
                listOfSong = (1..10).map {
                    ViewUiSong(
                        id = it.toLong(),
                        name = "Song $it",
                        artist = "Artist $it"
                    )
                }
            ),
            onEvent = {}
        ) { }
    }
}