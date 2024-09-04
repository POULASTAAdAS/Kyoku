package com.poulastaa.play.presentation.add_new_artist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReq
import com.poulastaa.play.presentation.add_new_artist.AddArtistUiArtist

@Composable
fun AddNewArtistArtistCard(
    modifier: Modifier = Modifier,
    header: String,
    artist: AddArtistUiArtist
) {
    val primaryColor = MaterialTheme.colorScheme.primary.copy(.7f)

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        border = if (artist.isSelected) BorderStroke(
            width = 1.3.dp,
            color = primaryColor
        ) else null
    ) {
        Box(Modifier.fillMaxSize()) {
            this@Card.AnimatedVisibility(
                artist.isSelected,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = MaterialTheme.dimens.medium1,
                        end = MaterialTheme.dimens.medium1
                    )
            ) {
                Canvas(modifier = Modifier) {
                    drawCircle(
                        color = primaryColor,
                        radius = 20f
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.small1)
                    .align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.75f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier.aspectRatio(1f),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        SubcomposeAsyncImage(
                            model = imageReq(
                                header = header,
                                url = artist.coverImage
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            error = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = UserIcon,
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(
                                            color = MaterialTheme.colorScheme.onBackground.copy(.7f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(MaterialTheme.dimens.small3)
                                    )
                                }
                            },
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        strokeWidth = 1.5.dp,
                                        strokeCap = StrokeCap.Round,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .size(40.dp)
                                    )
                                }
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = artist.name,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (artist.isSelected) primaryColor else MaterialTheme.colorScheme.onBackground
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(MaterialTheme.dimens.medium1),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
        ) {
            AddNewArtistArtistCard(
                modifier = Modifier.aspectRatio(1f),
                header = "",
                artist = AddArtistUiArtist(
                    name = "That cool artist",
                )
            )

            AddNewArtistArtistCard(
                modifier = Modifier.aspectRatio(1f),
                header = "",
                artist = AddArtistUiArtist(
                    name = "That cool artist",
                    isSelected = true
                )
            )

            AddNewArtistArtistCard(
                modifier = Modifier.aspectRatio(1f),
                header = "",
                artist = AddArtistUiArtist(
                    name = "That cool artist",
                )
            )
        }
    }
}