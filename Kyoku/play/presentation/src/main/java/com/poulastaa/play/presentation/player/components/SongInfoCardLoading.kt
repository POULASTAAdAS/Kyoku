package com.poulastaa.play.presentation.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
fun SongInfoCardLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .height(30.dp),
                shape = MaterialTheme.shapes.extraSmall,
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

        Spacer(Modifier.height(MaterialTheme.dimens.small3))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            shape = MaterialTheme.shapes.extraSmall,
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

        Spacer(Modifier.height(MaterialTheme.dimens.small3))

        repeat(5) {
            Row {
                Card(
                    modifier = Modifier.size(70.dp),
                    shape = MaterialTheme.shapes.extraSmall,
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

                Spacer(Modifier.width(MaterialTheme.dimens.small3))

                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(.5f)
                            .height(24.dp),
                        shape = MaterialTheme.shapes.extraSmall,
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

                    Spacer(Modifier.height(MaterialTheme.dimens.small2))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(.3f)
                            .height(20.dp),
                        shape = MaterialTheme.shapes.extraSmall,
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


            Spacer(Modifier.height(MaterialTheme.dimens.small3))
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            SongInfoCardLoading(
                Modifier.padding(MaterialTheme.dimens.medium1)
            )
        }
    }
}