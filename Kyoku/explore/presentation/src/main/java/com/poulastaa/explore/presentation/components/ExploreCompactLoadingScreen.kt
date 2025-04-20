package com.poulastaa.explore.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.dimens
import com.poulastaa.core.presentation.ui.components.AppLoadingSearchTopBar

@Composable
internal fun ExploreCompactLoadingScreen(
    modifier: Modifier = Modifier,
    title: String,
    navigateBack: () -> Unit,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        AppLoadingSearchTopBar(
            Modifier
                .fillMaxWidth()
                .height(56.dp),
            title,
            navigateBack
        )

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            repeat(3) {
                Card(
                    modifier = Modifier
                        .height(35.dp)
                        .width(80.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

        repeat(2) {
            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect(MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))

            repeat(6) {
                LoadingSongCard(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )

                Spacer(Modifier.height(MaterialTheme.dimens.small2))
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface {
            ExploreCompactLoadingScreen(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            ThemModeChanger.getGradiantBackground()
                        )
                    )
                    .padding(MaterialTheme.dimens.medium1),
                title = "that cool artist"
            ) {}
        }
    }
}