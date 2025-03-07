package com.poulastaa.view.presentation.artist.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens

@Composable
internal fun ViewArtistMediumLoadingScreen(modifier: Modifier) {
    var cardHeight by remember { mutableStateOf(0.dp) }
    var controllerHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.45f)
                    .onSizeChanged {
                        with(density) {
                            cardHeight = it.height.toDp()
                        }
                    },
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(.4f)
                            .fillMaxHeight(.6f),
                        shape = MaterialTheme.shapes.small,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = cardHeight)
                    .padding(top = (controllerHeight / 2).dp)
                    .padding(top = MaterialTheme.dimens.small3)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MaterialTheme.dimens.medium1)
            ) {
                LoadingSongs(10)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = cardHeight)
                    .onSizeChanged {
                        controllerHeight = (it.height / 2)
                    }
                    .offset {
                        IntOffset(y = -controllerHeight, x = 0)
                    }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.primaryContainer,
                                Color.Transparent,
                            )
                        )
                    )
            ) {
                ControlLoadingContent()
            }

            ViewArtistLoadingTopBar()
        }
    }
}


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
            ViewArtistMediumLoadingScreen(
                Modifier
                    .background(
                        brush = Brush.verticalGradient(colors = ThemModeChanger.getGradiantBackground())
                    )
                    .fillMaxSize()
            )
        }
    }
}