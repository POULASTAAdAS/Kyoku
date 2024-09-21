package com.poulastaa.play.presentation.add_new_album.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.components.ShimmerSongCard
import com.poulastaa.core.presentation.designsystem.components.shimmerEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun AddNewAlbumLoadingAnimation(
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = MaterialTheme.dimens.medium1)
            .verticalScroll(rememberScrollState()),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        Row {
            repeat(3) {
                Card(
                    modifier = Modifier
                        .width(60.dp)
                        .height(24.dp),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.dimens.medium1))
            }
        }

        Spacer(Modifier.height(MaterialTheme.dimens.medium1))

        repeat(12) {
            ShimmerSongCard(modifier = Modifier.height(80.dp))

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        AddNewAlbumLoadingAnimation(
            PaddingValues(
                top = MaterialTheme.dimens.medium1,
                bottom = MaterialTheme.dimens.medium1
            )
        )
    }
}