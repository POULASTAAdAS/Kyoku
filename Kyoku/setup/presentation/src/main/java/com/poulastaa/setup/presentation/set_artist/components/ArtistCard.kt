package com.poulastaa.setup.presentation.set_artist.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.UserIcon
import com.poulastaa.core.presentation.designsystem.dimens
import com.poulastaa.core.presentation.ui.imageReq
import com.poulastaa.setup.presentation.set_artist.model.UiArtist

@Composable
fun ArtistCard(
    modifier: Modifier = Modifier,
    header: String,
    artist: UiArtist,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(.75f)
                .align(Alignment.CenterHorizontally)
                .then(
                    if (artist.isSelected) Modifier.border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    ) else Modifier
                ),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = imageReq(
                        header = header,
                        url = artist.coverImageUrl
                    ),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary.copy(.7f),
                                strokeWidth = 1.5.dp,
                                strokeCap = StrokeCap.Round,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = UserIcon,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    color = MaterialTheme.colorScheme.onBackground.copy(.5f)
                                )
                            )
                        }
                    }
                )
            }
        }


        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = artist.name,
            color = if (artist.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontWeight = if (artist.isSelected) FontWeight.SemiBold else FontWeight.Normal,
            overflow = TextOverflow.Ellipsis,
            textDecoration = if (artist.isSelected) TextDecoration.Underline else null
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview
@Composable
private fun Preview() {
    AppThem {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            ArtistCard(
                modifier = Modifier
                    .aspectRatio(1f),
                header = "",
                artist = UiArtist(
                    name = "Artist",
                    isSelected = true
                )
            )
        }
    }
}