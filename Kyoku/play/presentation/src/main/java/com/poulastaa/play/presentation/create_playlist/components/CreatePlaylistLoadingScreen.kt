package com.poulastaa.play.presentation.create_playlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.components.AppBackButton
import com.poulastaa.core.presentation.designsystem.components.shimmerEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun CreatePlaylistLoadingScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AppBackButton {
                navigateBack()
            }

            Spacer(Modifier.weight(.7f))

            Card(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth(.4f)
                    .align(Alignment.CenterVertically),
                shape = MaterialTheme.shapes.extraSmall,
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .shimmerEffect()
                )
            }

            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.medium1),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            shape = MaterialTheme.shapes.small
        ) {
            repeat(7) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(MaterialTheme.dimens.small2)
                        .padding(horizontal = MaterialTheme.dimens.small2)
                ) {
                    Box(
                        Modifier
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .shimmerEffect()
                    )


                    Spacer(Modifier.width(MaterialTheme.dimens.medium1))

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(.6f)
                                .height(24.dp)
                                .clip(MaterialTheme.shapes.extraSmall)
                                .shimmerEffect()
                        )

                        Spacer(Modifier.height(MaterialTheme.dimens.small2))

                        Box(
                            Modifier
                                .fillMaxWidth(.6f)
                                .height(16.dp)
                                .clip(MaterialTheme.shapes.extraSmall)
                                .shimmerEffect()
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Box(
                        Modifier
                            .clip(CircleShape)
                            .size(20.dp)
                            .shimmerEffect()
                    )
                }
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.small3))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                        .size(10.dp)
                        .shimmerEffect()
                )
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.small3))
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        CreatePlaylistLoadingScreen() {}
    }
}