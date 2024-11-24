package com.poulastaa.play.presentation.add_new_artist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.components.shimmerEffect
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
fun AddNewArtistLoadingAnimation(
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

        Spacer(Modifier.height(MaterialTheme.dimens.medium3))

        Row {
            repeat(2) {
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

        Spacer(Modifier.height(MaterialTheme.dimens.medium3))

        repeat(7) {
            ShimmerArtistCard()

            Spacer(Modifier.height(MaterialTheme.dimens.medium1))
        }
    }
}

@Composable
private fun ShimmerArtistCard() {
    Row(
        modifier = Modifier
            .height(110.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) {
            Card(
                modifier = Modifier.aspectRatio(1f),
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
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        AddNewArtistLoadingAnimation(PaddingValues())
    }
}