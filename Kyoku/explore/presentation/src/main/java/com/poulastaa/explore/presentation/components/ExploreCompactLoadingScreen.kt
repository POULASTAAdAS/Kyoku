package com.poulastaa.explore.presentation.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.ThemModeChanger
import com.poulastaa.core.presentation.designsystem.shimmerEffect
import com.poulastaa.core.presentation.designsystem.ui.AppThem
import com.poulastaa.core.presentation.designsystem.ui.CloseIcon
import com.poulastaa.core.presentation.designsystem.ui.SearchIcon
import com.poulastaa.core.presentation.designsystem.ui.dimens

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

        ExploreLoadingTopBar(
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
                LoadingSongCard()

                Spacer(Modifier.height(MaterialTheme.dimens.small2))
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))
    }
}

@Composable
internal fun ExploreLoadingTopBar(
    modifier: Modifier,
    title: String,
    navigateBack: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = navigateBack
        ) {
            Icon(
                imageVector = CloseIcon,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(.8f)
            )
        }

        Spacer(Modifier.width(MaterialTheme.dimens.small3))

        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            shape = CircleShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = MaterialTheme.dimens.medium1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(.8f),
                    text = "${stringResource(R.string.search)} ${title.lowercase()}",
                    color = MaterialTheme.colorScheme.primary.copy(.8f),
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = navigateBack
                ) {
                    Icon(
                        imageVector = SearchIcon,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(.8f)
                    )
                }
            }
        }
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